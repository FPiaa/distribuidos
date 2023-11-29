package server;

import commons.Position;
import helper.json.JsonHelper;
import jakarta.persistence.Persistence;
import org.apache.age.jdbc.base.Agtype;
import org.apache.age.jdbc.base.AgtypeUtil;
import org.apache.age.jdbc.base.type.AgtypeMap;
import org.hibernate.SessionFactory;
import server.dto.PoiDTO;

public class Age {
    public static void main(String[] args) {

        SessionFactory sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("server.entity");
        PoiDTO poi = new PoiDTO(1L, "ponto 1", new Position(0 ,0), "", true);
        PoiDTO poi2 = new PoiDTO(2L, "ponto 2", new Position(0 ,0), "", true);

        sessionFactory.inTransaction(session -> {

            session.createNativeQuery("CREATE EXTENSION IF NOT EXISTS age", Agtype.class).executeUpdate();
            session.createNativeQuery("LOAD 'age'", Agtype.class).executeUpdate();
            session.createNativeQuery("SET search_path = ag_catalog, \"$user\", public", Agtype.class).executeUpdate();
        });

        sessionFactory.inTransaction(session -> {
            session.createNativeQuery("select create_graph('teste')", Agtype.class).uniqueResult();
            session.createNativeQuery("""
                    prepare create_poi(agtype) as
                    select * from cypher('teste', $$
                        CREATE (a\\:POI {id\\: \\$id, nome\\: \\$nome, posicao \\: \\$posicao, aviso \\: \\$aviso, acessivel \\: \\$acessivel })
                        return a $$, \\$1
                    )
                    as (a agtype);
                    """
                            , Agtype.class)
                    .executeUpdate();
            session.createNativeQuery("""
                    prepare delete_poi(agtype) as
                    select * from cypher('teste', $$
                        MATCH (a\\:POI {id \\: \\$id})
                        DETACH DELETE a
                        RETURN a $$, \\$1
                    )
                    as (a agtype);
                    """
                            , Agtype.class)
                    .executeUpdate();
            session.createNativeQuery("""
                    prepare find_poi(agtype) as
                    select * from cypher('teste', $$
                        MATCH (a\\:POI {id \\: \\$id})
                        RETURN a $$, \\$1
                    )
                    as (a agtype);
                    """
                            , Agtype.class)
                    .executeUpdate();

            session.createNativeQuery("""
                    prepare update_poi(agtype) as
                    select * from cypher('teste', $$
                        MATCH (a\\:POI {id \\: \\$id})
                        SET a.nome = \\$nome, a.posicao = \\$posicao, a.aviso = \\$aviso, a.acessivel = \\$acessivel
                        RETURN a $$, \\$1
                    )
                    as (a agtype);
                    """
                            , Agtype.class)
                    .executeUpdate();

        });

        try(var session = sessionFactory.openSession()) {

            var query = "execute create_poi('" + JsonHelper.toJson(poi) + "');";

            var foo = session.createNativeQuery(query, Agtype.class).uniqueResult();
            query = "execute create_poi('" + JsonHelper.toJson(poi2) + "');";


            query = "execute find_poi('" + JsonHelper.toJson(poi) + "');";
            foo = session.createNativeQuery(query, Agtype.class).uniqueResult();

            var updated = new PoiDTO(1L, "novo nome", new Position(69, 69), null, false);
            query = "execute update_poi('" + JsonHelper.toJson(updated) + "');";
            foo = session.createNativeQuery(query, Agtype.class).uniqueResult();

            query = "execute find_poi('" + JsonHelper.toJson(poi) + "');";
            var p = session.createNativeQuery(query, Agtype.class).uniqueResult();
            AgtypeMap map = (AgtypeMap) AgtypeUtil.parse(p.getValue());
            PoiDTO dto = JsonHelper.fromJson(JsonHelper.toJson(map.getObject("properties")), PoiDTO.class);
            System.out.println();
        } catch (Exception e) {
            e.printStackTrace();
        }


        try(var session = sessionFactory.openSession()) {
            session.createNativeQuery("select drop_graph('teste', true)", Agtype.class).uniqueResult();
        }

    }

}
