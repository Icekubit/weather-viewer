package icekubit.dao;

import icekubit.entity.User;
import icekubit.exception.UserAlreadyExistException;
import icekubit.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDao {
    private static UserDao instance;
    private UserDao() {}
    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    public int save(User user) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();
        } catch (ConstraintViolationException e) {
            throw new UserAlreadyExistException(e);
        }
        return user.getId();
    }

    public Optional<User> getUserById(int id) {
        User user = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            user = session.get(User.class, id);
        }
        return Optional.of(user);
    }

    public Optional<User> getUserByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM User " +
                    "WHERE Lower(login) = Lower(:login) ";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("login", username);

            List<User> resultList = query.getResultList();
            return resultList.stream().findFirst();
        }
    }
}
