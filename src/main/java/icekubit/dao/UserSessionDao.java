package icekubit.dao;

import icekubit.entity.User;
import icekubit.entity.UserSession;
import icekubit.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;
import java.util.UUID;

public class UserSessionDao {
    private static UserSessionDao instance;
    private UserSessionDao() {}
    public static UserSessionDao getInstance() {
        if (instance == null) {
            instance = new UserSessionDao();
        }
        return instance;
    }

    public String save(UserSession userSession) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(userSession);
            transaction.commit();
        }
        return userSession.getId().toString();
    }

    public String createSession(User user) {
        UserSession userSession = new UserSession();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            userSession.setUser(user);
            session.persist(userSession);
            transaction.commit();
        }
        return userSession.getId().toString();
    }

    public Optional<UserSession> findById(UUID id) {
        UserSession userSession;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userSession = session.get(UserSession.class, id);
        }
        return userSession == null ? Optional.empty() : Optional.of(userSession);
    }

    public void delete(UUID userSessionId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query<UserSession> query
                    = session.createQuery("delete from UserSession where id = :sessionId", UserSession.class);
            query.setParameter("sessionId", userSessionId);
            query.executeUpdate();
            transaction.commit();
        }
    }


}
