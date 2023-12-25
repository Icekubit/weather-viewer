package icekubit.dao;

import icekubit.entity.UserSession;
import icekubit.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class UserSessionDao {

    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public UUID save(UserSession userSession) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(userSession);
            transaction.commit();
        }
        return userSession.getId();
    }

    public Optional<UserSession> findById(UUID id) {
        UserSession userSession;
        try (Session session = sessionFactory.openSession()) {
            userSession = session.get(UserSession.class, id);
        }
        return Optional.ofNullable(userSession);
    }

    public void delete(UUID userSessionId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query
                    = session.createQuery("delete from UserSession where id = :sessionId");
            query.setParameter("sessionId", userSessionId);
            query.executeUpdate();
            transaction.commit();
        }
    }

    public void deleteByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query
                    = session.createQuery("delete from UserSession where user.id = :userId");
            query.setParameter("userId", userId);
            query.executeUpdate();
            transaction.commit();
        }
    }


    public void deleteExpiredUserSessions(LocalDateTime now) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query
                    = session.createQuery("delete from UserSession where expiresAt < :now");
            query.setParameter("now", now);
            query.executeUpdate();
            transaction.commit();
        }
    }


    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query
                    = session.createQuery("delete from UserSession where user.id > 0");
            query.executeUpdate();
            transaction.commit();
        }
    }
}
