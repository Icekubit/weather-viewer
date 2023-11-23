package icekubit.dao;

import icekubit.entity.User;
import icekubit.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.Optional;
import java.util.UUID;

public class SessionDao {
    private static SessionDao instance;
    private SessionDao() {}
    public static SessionDao getInstance() {
        if (instance == null) {
            instance = new SessionDao();
        }
        return instance;
    }

    public String save(icekubit.entity.Session userSession) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(userSession);
            transaction.commit();
        }
        return userSession.getId().toString();
    }

    public String createSession(User user) {
        icekubit.entity.Session userSession = new icekubit.entity.Session();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            userSession.setUser(user);
            session.persist(userSession);
            transaction.commit();
        }
        return userSession.getId().toString();
    }

    public Optional<icekubit.entity.Session> findById(UUID id) {
        icekubit.entity.Session userSession;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            userSession = session.get(icekubit.entity.Session.class, id);
        }
        return userSession == null ? Optional.empty() : Optional.of(userSession);
    }


}
