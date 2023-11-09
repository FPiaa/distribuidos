package server.repository;

import jakarta.persistence.Persistence;
import jakarta.persistence.RollbackException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import server.entity.User;
import server.exceptions.BadRequestException;
import server.exceptions.ResourceNotFoundException;
import server.exceptions.ServerResponseException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UserRepository implements Repository<User, Long> {
    private final SessionFactory sessionFactory;

    public UserRepository() {
        sessionFactory = (SessionFactory) Persistence.createEntityManagerFactory("server.entity");
    }

    public Optional<User> login(String email) {
        try (Session session = sessionFactory.openSession()) {
            return session.bySimpleNaturalId(User.class)
                    .loadOptional(email);
        }
    }

    @Override
    public Optional<User> find(Long id) {

        try (Session session = sessionFactory.openSession()) {
            var user = session.find(User.class, id);
            return Optional.ofNullable(user);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from User", User.class)
                    .getResultList();
        }
    }

    @Override
    public void create(User newInstance) throws BadRequestException {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                session.persist(newInstance);
                tx.commit();
            } catch (RollbackException ignored) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new BadRequestException("usuario com email " + newInstance.getEmail() + " ja existe");
            }
        }
    }

    @Override
    public void delete(User instance) {
        sessionFactory.inTransaction(session -> session.remove(instance));
    }

    @Override
    public void deleteById(Long id) {
        sessionFactory.inTransaction(session ->
            session.createMutationQuery("delete from User where id = :id")
                    .setParameter("id", id)
                    .executeUpdate()
        );
    }


    public long countAdmins() {
        try (var session = sessionFactory.openSession()) {
            return session.createSelectionQuery("select count(*) from User user where user.isAdmin = :admin", Long.class)
                    .setParameter("admin", true)
                    .uniqueResult();
        }
    }

    @Override
    public User update(Long id, User instance) throws ServerResponseException {
        try (Session session = sessionFactory.openSession()) {

            User user = session.byId(User.class)
                    .loadOptional(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Usuario com id: " + id + " não existe"));


            var userWithEmail = session.bySimpleNaturalId(User.class)
                    .loadOptional(instance.getEmail());

            if (userWithEmail.isPresent() && !Objects.equals(userWithEmail.get().getId(), instance.getId())) {
                throw new BadRequestException("usuario com email " + userWithEmail.get().getEmail() + " ja existe");
            }


            Transaction tx = null;
            try {
                tx = session.beginTransaction();
                user.update(instance);
                session.merge(user);
                tx.commit();
            } catch (RollbackException ignored) {
                if (tx != null) {
                    tx.rollback();
                }
                throw new BadRequestException("usuario com email " + user.getEmail() + " ja existe");
            }
            return user;
        }
    }
}
