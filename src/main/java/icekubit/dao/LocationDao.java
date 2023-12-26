package icekubit.dao;

import icekubit.entity.Location;
import icekubit.exception.UnauthorizedActionException;
import icekubit.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class LocationDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public void save(Location location) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(location);
            transaction.commit();
        }
    }

    public Optional<Location> findLocationById(int locationId) {
        try (Session session = sessionFactory.openSession()) {

            return session
                    .createQuery("select l from Location l where l.id = :locationId", Location.class)
                    .setParameter("locationId", locationId)
                    .uniqueResultOptional();
        }
    }


    public List<Location> findLocationsByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select l from Location l " +
                    "join l.user where l.user.id = :userId " +
                    "order by l.id", Location.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public void delete(int locationId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session
                    .createQuery("delete from Location l where l.id = :locationId")
                    .setParameter("locationId", locationId)
                    .executeUpdate();
            transaction.commit();
        }
    }
}
