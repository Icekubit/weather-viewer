package icekubit.dao;

import icekubit.entity.User;
import icekubit.exception.UserAlreadyExistException;
import icekubit.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.Optional;

public class UserDao {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public void save(User user) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            throw new UserAlreadyExistException(e);
        }
    }

    public Optional<User> findById(int id) {
        User user;
        try (Session session = sessionFactory.openSession()) {
            user = session.get(User.class, id);
        }
        return Optional.of(user);
    }

    public Optional<User> findByLogin(String username) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select u from User u " +
                            "where lower(u.login) = lower(:login)", User.class)
                    .setParameter("login", username)
                    .uniqueResultOptional();
        }
    }

    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from User where id > 0")
                    .executeUpdate();
            transaction.commit();
        }
    }
}
