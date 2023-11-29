package icekubit.dao;

import icekubit.entity.User;
import icekubit.exception.UserAlreadyExistException;
import icekubit.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDao {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public int save(User user) {
        try (Session session = sessionFactory.openSession()) {
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
        try (Session session = sessionFactory.openSession()) {
            user = session.get(User.class, id);
        }
        return Optional.of(user);
    }

    public Optional<User> getUserByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM User " +
                    "WHERE Lower(login) = Lower(:login) ";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("login", username);

            List<User> resultList = query.getResultList();
            return resultList.stream().findFirst();
        }
    }
}
