package server.entity;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static java.lang.System.out;
import static java.time.LocalDateTime.now;


public class HibernateTest {
    private static EntityManagerFactory entityManagerFactory;
    @BeforeAll
    protected static void setUp(){
        entityManagerFactory = Persistence.createEntityManagerFactory("server.entity");

    }
    @Test
    public void test1() {
        SessionFactory sessionFactory = (SessionFactory) entityManagerFactory;
        sessionFactory.inTransaction(session -> {
            var event = Event.builder().date(now()).title("yay?").build();
            session.persist(event);

            var event1 = Event.builder().date(now()).title("nay?").build();
            session.persist(event1);
        });
    }

    @Test
    public void test2() {
        SessionFactory sessionFactory = (SessionFactory)  entityManagerFactory;
        sessionFactory.inTransaction(session -> {
            session.createSelectionQuery("from Event", Event.class)
                    .getResultList()
                    .forEach(event ->  out.println(event.toString()));
        });
    }

}
