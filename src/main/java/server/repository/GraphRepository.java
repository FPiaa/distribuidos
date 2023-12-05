package server.repository;

import helper.json.JsonHelper;
import jakarta.persistence.Persistence;
import jakarta.persistence.Tuple;
import org.apache.age.jdbc.base.Agtype;
import org.apache.age.jdbc.base.AgtypeUtil;
import org.apache.age.jdbc.base.type.AgtypeMap;
import org.hibernate.SessionFactory;
import server.dto.PoiDTO;
import server.dto.SegmentDTO;
import server.exceptions.BadRequestException;
import server.exceptions.ResourceNotFoundException;
import server.exceptions.ServerResponseException;

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
                session.createNativeQuery("select drop_graph(%s, true)".formatted(graphName), Agtype.class)
                        .uniqueResultOptional();
            } catch (Exception ignored) {
                System.out.printf("Grafo %s nao existe%n", graphName);
            }

            try (var session = sessionFactory.openSession()) {
                session.createNativeQuery("select create_graph(%s)".formatted(graphName), Agtype.class)
                        .uniqueResultOptional();
            } catch (Exception ignored) {
                System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            }
        }

            try {
                sessionFactory.inTransaction(session -> {
                session.createNativeQuery("DEALLOCATE ALL", Agtype.class).executeUpdate();


                    // poi prepared statements

                    //create_poi
                    session.createNativeQuery("""
                                            prepare create_Poi(agtype) as
                                            select * from cypher(%s, $$
                                                CREATE (a\\:Poi {id\\: \\$id, nome\\: \\$nome, posicao \\: \\$posicao, aviso \\: \\$aviso, acessivel \\: \\$acessivel })
                                                return a $$, \\$1
                                            )
                                            as (a agtype);
                                            """.formatted(graphName)
                                    , Agtype.class)
                            .executeUpdate();

                    session.createNativeQuery("""
                            prepare find_Pois(agtype) as
                            select * from cypher(%s, $$
                                match(n\\:Poi)
                                return n
                            $$) as (Pois agtype);
                            """.formatted(graphName), Agtype.class).executeUpdate();

                    // find_Poi
                    session.createNativeQuery("""
                                            prepare find_Poi(agtype) as
                                            select * from cypher(%s, $$
                                                MATCH (a\\:Poi {id \\: \\$id})
                                                RETURN a $$, \\$1
                                            )
                                            as (a agtype);
                                            """.formatted(graphName)
                                    , Agtype.class)
                            .executeUpdate();

                    // update_Poi
                    session.createNativeQuery("""
                                            prepare update_Poi(agtype) as
                                            select * from cypher(%s, $$
                                                MATCH (a\\:Poi {id \\: \\$id})
                                                SET a.nome = \\$nome, a.posicao = \\$posicao, a.aviso = \\$aviso, a.acessivel = \\$acessivel
                                                RETURN a $$, \\$1
                                            )
                                            as (a agtype);
                                            """.formatted(graphName)
                                    , Agtype.class)
                            .executeUpdate();


                    // delete_Poi
                    session.createNativeQuery("""
                                            prepare delete_Poi(agtype) as
                                            select * from cypher(%s, $$
                                                MATCH (a\\:Poi {id \\: \\$id})
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
                            	match (a\\:Poi {id\\: \\$pdi_inicial}), (b\\:Poi {id\\: \\$pdi_final})
                            	create (a)-[s\\:CONNECTS {pdi_inicial\\: \\$pdi_inicial, pdi_final\\: \\$pdi_final, distancia\\: \\$distancia, aviso\\: \\$aviso, acessivel\\: \\$acessivel, distancia\\: \\$distancia}]->(b),
                            	(b)-[r\\:CONNECTS {pdi_inicial\\: \\$pdi_final, pdi_final\\: \\$pdi_inicial, distancia\\: \\$distancia, aviso\\: \\$aviso, acessivel\\: \\$acessivel}]->(a)
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
                    // find_segments
                    session.createNativeQuery("""
                            prepare find_segments(agtype) as
                            select * from cypher(%s, $$
                            	match (\\:Poi)-[s\\:CONNECTS]->(\\:Poi)
                            	return s
                            $$) as (segment agtype);
                            """.formatted(graphName), Agtype.class).executeUpdate();


                    // update_segment
                    session.createNativeQuery("""
                            prepare update_segment(agtype) as
                            select * from cypher(%s, $$
                            	match (a\\:Poi)-[s\\:CONNECTS]->(b\\:Poi),(b\\:Poi)-[r\\:CONNECTS]->(a\\:Poi)
                            	where a.id = \\$pdi_inicial and b.id = \\$pdi_final
                            	set s.acessivel = \\$acessivel, s.aviso = \\$aviso, r.acessivel = \\$acessivel, r.aviso = \\$aviso
                            	return s
                            $$, \\$1) as (s agtype);
                            """.formatted(graphName), Agtype.class).executeUpdate();


                    // delete_segment
                    session.createNativeQuery("""
                            prepare delete_segment(agtype) as\s
                            select * from cypher(%s, $$\s
                            	match (a\\:Poi)-[s\\:CONNECTS]->(b\\:Poi),(b\\:Poi)-[r\\:CONNECTS]->(a\\:Poi)
                            	where a.id = \\$pdi_inicial and b.id = \\$pdi_final
                            	delete s, r
                            	return s
                            $$, \\$1) as (s agtype);
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
                            $$, \\$1) AS (Pois agtype, segments agtype, distancia agtype)
                            ORDER BY distancia asc limit 1;
                            """.formatted(graphName), Agtype.class).executeUpdate();

                    // compute_distance
                    session.createNativeQuery("""
                            prepare compute_distance(agtype) as
                            select * from cypher(%s, $$\s
                            	match (a\\:Poi {id\\: \\$pdi_inicial}), (b\\:Poi {id\\: \\$pdi_final})
                            	with sqrt((a.posicao.x - b.posicao.x) * (a.posicao.x - b.posicao.x) +
                            	(a.posicao.y - b.posicao.y) * (a.posicao.y - b.posicao.y)) as distance
                            	return distance
                            $$, \\$1) as (ret agtype);
                                                    """.formatted(graphName), Agtype.class).executeUpdate();
                });
            }
            catch (Exception ignored) {
                // rollback acontece, significa que a queries ja existe
                // possivel ignorar sem probleasí
            }


    }

    private GraphRepository() {
        sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("server.graph");
        prepare_graph(true);
    }

    public static GraphRepository getInstance() {
        if (sessionFactory == null) {
            graph = new GraphRepository();
        }

        return graph;
    }

    public PoiDTO createPoi(PoiDTO poi) {
        try (var session = sessionFactory.openSession()) {
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
        try (var session = sessionFactory.openSession()) {
            var ags = session.createNativeQuery("execute find_pois('{}')", Agtype.class).list();

            return ags.stream().map(this::convertAgtypeToPoi).toList();
        }
    }

    public Optional<PoiDTO> findPoi(long id) {
        try (var session = sessionFactory.openSession()) {
            HashMap<String, Long> preguica = new HashMap<>();
            preguica.put("id", id);
            var ag = session.createNativeQuery("execute find_poi('" +
                                    JsonHelper.toJson(preguica) +
                                    "')",
                            Agtype.class)
                    .uniqueResultOptional();
            // já falei que amo monads?
            return ag.map(this::convertAgtypeToPoi);
        }
    }

    public PoiDTO updatePoi(PoiDTO newValue) throws ResourceNotFoundException {
        try (var session = sessionFactory.openSession()) {
            var exception = new ResourceNotFoundException("Não foi possível encontrar PDI com id: " + newValue.id());
            var poi = findPoi(newValue.id()).orElseThrow(() -> exception);
            poi = poi.update(newValue);
            var ag = session.createNativeQuery("execute update_poi('" + JsonHelper.toJson(poi) + "')", Agtype.class).uniqueResultOptional();
            // eu amo monads
            return ag.map(this::convertAgtypeToPoi).orElseThrow(() -> exception);
        }
    }


    public PoiDTO deletePoi(long id) throws ResourceNotFoundException {
        try (var session = sessionFactory.openSession()) {
            var exception = new ResourceNotFoundException("Não foi possível encontrar PDI com id: " + id);
            String s = "{ \"id\": " + id + "}')";
            var ag = session.createNativeQuery("execute delete_poi('" + s, Agtype.class).uniqueResultOptional();
            return ag.map(this::convertAgtypeToPoi).orElseThrow(() -> exception);

        }
    }


    private SegmentDTO convertAgtypeToSegment(Agtype value) {

        AgtypeMap map = (AgtypeMap) AgtypeUtil.parse(value.getValue());
        return JsonHelper.fromJson(JsonHelper.toJson(map.getObject("properties")), SegmentDTO.class);
    }

    public Optional<SegmentDTO> findSegment(long id1, long id2) {
        HashMap<String, Long> serialize = new HashMap<>();
        serialize.put("pdi_inicial", id1);
        serialize.put("pdi_final", id2);

        try (var session = sessionFactory.openSession()) {
            var ag = session.createNativeQuery("execute find_segment('" +
                                    JsonHelper.toJson(serialize) +
                                    "')",
                            Agtype.class)
                    .uniqueResultOptional();
            // já falei que amo monads?
            return ag.map(this::convertAgtypeToSegment);
        }

    }

    public List<SegmentDTO> findSegments() {
        try (var session = sessionFactory.openSession()) {
            var ags = session.createNativeQuery("execute find_segments('{}')", Agtype.class).list();
            // uma monad  um monoid na categoria dos endofunctors
            return ags.stream().map(this::convertAgtypeToSegment).toList();
        }
    }

    public SegmentDTO createSegment(SegmentDTO dto) throws ServerResponseException {
        var exception = new ResourceNotFoundException("Não foi possivel encontrar um dos pontos, " + dto.pdi_inicial() + " ou " + dto.pdi_final());
        HashMap<String, Object> serialize = new HashMap<>();
        serialize.put("pdi_inicial", dto.pdi_inicial());
        serialize.put("pdi_final", dto.pdi_final());
        serialize.put("acessivel", dto.acessivel());
        serialize.put("aviso", dto.aviso());
        try (var session = sessionFactory.openSession()) {
            var exists = findSegment(dto.pdi_inicial(), dto.pdi_final());
            if(exists.isPresent()) {
                throw new BadRequestException("Um segmento entre " + dto.pdi_inicial() + " e " + dto.pdi_final() + "já existe.");
            }


            var dist = session.createNativeQuery("execute compute_distance('" + JsonHelper.toJson(dto) + "')", Agtype.class)
                    .uniqueResultOptional()
                    .map(Agtype::getValue)
                    .map(Double::parseDouble)
                    .orElseThrow(() -> exception);

            serialize.put("distancia", dist);
            var ags = session.createNativeQuery("execute create_segment('" + JsonHelper.toJson(serialize) + "')", Tuple.class).uniqueResultOptional().orElseThrow(() -> exception);
            return dto.update(new SegmentDTO(null, null, dist, null, null));
            // uma monad  um monoid na categoria dos endofunctors

        }
    }

    public SegmentDTO updateSegment(SegmentDTO newSegment) throws ResourceNotFoundException {
        var exception = new ResourceNotFoundException("Não foi possivel encontrar um segmento entre o ponto " + newSegment.pdi_inicial() + " e o ponto " + newSegment.pdi_final());
        var current = findSegment(newSegment.pdi_inicial(), newSegment.pdi_final()).orElseThrow(() -> exception);
        var updated = current.update(newSegment);
        try (var session = sessionFactory.openSession()) {
            var ag = session.createNativeQuery("execute update_segment('" + JsonHelper.toJson(updated) + "')", Agtype.class)
                    .uniqueResultOptional();

            return ag.map(this::convertAgtypeToSegment).orElseThrow(() -> exception);
        }
    }

    public SegmentDTO deleteSegment(long id1, long id2) throws ResourceNotFoundException {
        HashMap<String, Long> serialize = new HashMap<>();
        serialize.put("pdi_inicial", id1);
        serialize.put("pdi_final", id2);
        var exception = new ResourceNotFoundException("Não foi possivel encontrar um segmento entre o ponto " + id1 + " e o ponto " + id2);

        try (var session = sessionFactory.openSession()) {
            return session.createNativeQuery("execute delete_segment('" + JsonHelper.toJson(serialize) + "')", Agtype.class)
                    .uniqueResultOptional()
                    .map(this::convertAgtypeToSegment)
                    .orElseThrow(() -> exception);

        }
    }
}
