package server.repository;

import helper.json.JsonHelper;
import jakarta.persistence.Persistence;
import org.apache.age.jdbc.base.Agtype;
import org.apache.age.jdbc.base.AgtypeUtil;
import org.apache.age.jdbc.base.type.AgtypeMap;
import org.hibernate.SessionFactory;
import server.dto.PoiDTO;
import server.exceptions.ResourceNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class GraphRepository {
    private static GraphRepository graph = null;
    private static final String graphName = "'mapa'";

    private static SessionFactory sessionFactory = null;

    private void prepare_graph(boolean recreateGraph) {
        sessionFactory.inTransaction(session -> {
            session.createNativeQuery("CREATE EXTENSION IF NOT EXISTS age", Agtype.class).executeUpdate();
            session.createNativeQuery("LOAD 'age'", Agtype.class).executeUpdate();
            session.createNativeQuery("SET search_path = ag_catalog, \"$user\", public", Agtype.class).executeUpdate();
        });

        if (recreateGraph) {

            try (var session = sessionFactory.openSession()) {
                session.createNativeQuery("select drop_graph(%s)".formatted(graphName), Agtype.class)
                        .uniqueResultOptional();
            }catch (Exception ignored) {}

            try (var session = sessionFactory.openSession()) {
                session.createNativeQuery("select create_graph(%s)".formatted(graphName), Agtype.class)
                        .uniqueResultOptional();
            } catch (Exception ignored) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            }

            sessionFactory.inTransaction(session -> {
                session.createNativeQuery("DEALLOCATE ALL", Agtype.class).executeUpdate();

                // poi prepared statements

                //create_poi
                session.createNativeQuery("""
                    prepare create_poi(agtype) as
                    select * from cypher(%s, $$
                        CREATE (a\\:POI {id\\: \\$id, nome\\: \\$nome, posicao \\: \\$posicao, aviso \\: \\$aviso, acessivel \\: \\$acessivel })
                        return a $$, \\$1
                    )
                    as (a agtype);
                    """.formatted(graphName)
                                , Agtype.class)
                        .executeUpdate();

                // find_poi
                session.createNativeQuery("""
                    prepare find_poi(agtype) as
                    select * from cypher(%s, $$
                        MATCH (a\\:POI {id \\: \\$id})
                        RETURN a $$, \\$1
                    )
                    as (a agtype);
                    """.formatted(graphName)
                                , Agtype.class)
                        .executeUpdate();

                // update_poi
                session.createNativeQuery("""
                    prepare update_poi(agtype) as
                    select * from cypher(%s, $$
                        MATCH (a\\:POI {id \\: \\$id})
                        SET a.nome = \\$nome, a.posicao = \\$posicao, a.aviso = \\$aviso, a.acessivel = \\$acessivel
                        RETURN a $$, \\$1
                    )
                    as (a agtype);
                    """.formatted(graphName)
                                , Agtype.class)
                        .executeUpdate();


                // delete_poi
                session.createNativeQuery("""
                    prepare delete_poi(agtype) as
                    select * from cypher(%s, $$
                        MATCH (a\\:POI {id \\: \\$id})
                        DETACH DELETE a
                        RETURN a $$, \\$1
                    )
                    as (a agtype);
                    """.formatted(graphName)
                                , Agtype.class)
                        .executeUpdate();


                // segments prepare statement
                //create_segment
                session.createNativeQuery("""
                        prepare create_segment(agtype) as
                        select * from cypher(%s, $$
                        	match (a\\: Poi {id\\: \\$pdi_inicial}), (b\\: Poi {id\\: \\$pdi_final})
                        	create (a)-[s\\:CONNECTS {distancia\\: \\$distancia, descricao\\: \\$descricao, acessivel\\: \\$acessivel}]->(b),
                        	(b)-[r\\:CONNECTS {distancia\\: \\$distancia, descricao\\: \\$descricao, acessivel\\: \\$acessivel}]->(a)
                        	RETURN s, r
                        $$, \\$1) as (s agtype, r agtype);
                        """.formatted(graphName), Agtype.class).executeUpdate();

                // find_segment
                session.createNativeQuery("""
                        prepare find_segment(agtype) as
                        select * from cypher(%s, $$
                        	match (\\:Poi {id\\: \\$pdi_inicial})-[s\\:CONNECTS]->(\\:Poi {id\\: \\$pdi_final})
                        	return s
                        $$, \\$1) as (segment agtype);
                        """.formatted(graphName), Agtype.class).executeUpdate();


                // update_segment
                session.createNativeQuery("""
                        prepare update_segment(agtype) as
                        select * from cypher(%s, $$
                        	match (a\\: Poi)-[s\\:CONNECTS]->(b\\: Poi),(b\\: Poi)-[r\\:CONNECTS]->(a\\: Poi)
                        	where a.id = \\$pdi_inicial and b.id = \\$pdi_final
                        	set s.acessivel = \\$acessivel, s.descricao = \\$descricao, r.acessivel = \\$acessivel, r.descricao = \\$descricao
                        	return s, r
                        $$, \\$1) as (s agtype, r agtype);
                        """.formatted(graphName), Agtype.class).executeUpdate();


                // delete_segment
                session.createNativeQuery("""
                        prepare delete_segment(agtype) as\s
                        select * from cypher(%s, $$\s
                        	match (a\\: Poi)-[s\\:CONNECTS]->(b\\: Poi),(b\\: Poi)-[r\\:CONNECTS]->(a\\: Poi)
                        	where a.id = \\$pdi_inicial and b.id = \\$pdi_final
                        	delete s, r
                        	return s, r
                        $$, \\$1) as (s agtype, r agtype);
                        """.formatted(graphName), Agtype.class).executeUpdate();



                // FIND ROUTE
                session.createNativeQuery("""
                        prepare find_route(agtype) as
                        SELECT * FROM cypher(%s , $$
                            MATCH paths = (a\\:Poi {id\\: \\$pdi_inicial})-[\\:CONNECTS*1.. {acessivel\\: true}]->(b\\:Poi {id\\: \\$pdi_final})
                            WITH paths, relationships(paths) AS rels
                            UNWIND rels AS rel
                            WITH nodes(paths) AS nodes,
                                collect(rel) AS segments,
                                sum(rel.distancia) AS travelTime
                            RETURN nodes, segments, travelTime
                        $$, \\$1) AS (pois agtype, segments agtype, distancia agtype)
                        ORDER BY distancia asc limit 1;
                        """.formatted(graphName), Agtype.class).executeUpdate();

                // compute_distance
                session.createNativeQuery("""
                        prepare compute_distance(agtype) as
                        select * from cypher(%s, $$\s
                        	match (a\\: Poi {id\\: \\$pdi_inicial}), (b\\: Poi {id\\: \\$pdi_final})
                        	with sqrt((a.posicao.x - b.posicao.x) * (a.posicao.x - b.posicao.x) +
                        	(a.posicao.y - b.posicao.y) * (a.posicao.y - b.posicao.y) +
                        	(a.posicao.z - b.posicao.z) * (a.posicao.z - b.posicao.z)) as distance
                        	return distance
                        $$, \\$1) as (ret agtype);
                                                """.formatted(graphName), Agtype.class).executeUpdate();
            });
        }

    }

    private GraphRepository() {
        sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("server.entity");
        prepare_graph(true);
    }

    public static GraphRepository getInstance() {
        if (sessionFactory == null) {
            graph = new GraphRepository();
        }

        return graph;
    }

    public PoiDTO createPoi(PoiDTO poi) {
        try(var session = sessionFactory.openSession()) {
            Agtype ret = session.createNativeQuery("execute create_poi(' " + JsonHelper.toJson(poi) + "')", Agtype.class)
                    .uniqueResult();
            return convertAgtypeToPoi(ret);
        }
    }

    private PoiDTO convertAgtypeToPoi(Agtype value) {
        // biblioteca boa, não consegue converter direto
        AgtypeMap map = (AgtypeMap) AgtypeUtil.parse(value.getValue());
        return JsonHelper.fromJson(JsonHelper.toJson(map.getObject("properties")), PoiDTO.class);
    }

    public List<PoiDTO> findPois() {
        try(var session = sessionFactory.openSession()) {
            var ags = session.createNativeQuery("select * from cypher(" + graphName + ", $$ match (p\\:Poi) return p $$) as (pois agtype)"
                    , Agtype.class).list();

            return ags.stream().map(this::convertAgtypeToPoi).toList();
        }
    }

    public Optional<PoiDTO> findPoi(long id) {
        try(var session = sessionFactory.openSession())  {
            HashMap<String, Long> preguica = new HashMap<>();
            preguica.put("id", id);
            var ag = session.createNativeQuery("execute find_poi('" +
                    JsonHelper.toJson(preguica)+
                    "')",
                    Agtype.class)
                    .uniqueResultOptional();
            // já falei que amo monads?
            return ag.map(this::convertAgtypeToPoi);
        }
    }

    public PoiDTO updatePoi(PoiDTO newValue) throws ResourceNotFoundException {
       try(var session = sessionFactory.openSession())  {
           var exception = new ResourceNotFoundException("Não foi possível encontrar PDI com id: " + newValue.id());
           var poi = findPoi(newValue.id()).orElseThrow(() -> exception);
            poi = poi.update(newValue);
            var ag = session.createNativeQuery("execute update_poi('" + JsonHelper.toJson(poi) + "')", Agtype.class).uniqueResultOptional();
            // eu amo monads
            return ag.map(this::convertAgtypeToPoi).orElseThrow(() -> exception);
       }
    }


    public PoiDTO deletePoi(long id) throws ResourceNotFoundException {
        try(var session = sessionFactory.openSession()) {
            var exception = new ResourceNotFoundException("Não foi possível encontrar PDI com id: " + id);
            String s = "{ \"id\": " + id + "}')";
            var ag = session.createNativeQuery("execute delete_poi('" + s, Agtype.class).uniqueResultOptional();
            return ag.map(this::convertAgtypeToPoi).orElseThrow(() -> exception);

        }
    }

}
