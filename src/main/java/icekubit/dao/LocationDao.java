package icekubit.dao;

import icekubit.entity.Location;
import icekubit.exception.UnauthorizedActionException;
import icekubit.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class LocationDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public void save(Location location) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(location);
            transaction.commit();
        }
    }


    public List<Location> findLocationsByUserId(int userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("select l from Location l " +
                    "join l.user where l.user.id = :userId", Location.class)
                    .setParameter("userId", userId)
                    .list();
        }
    }

    public void delete(int userId, int locationId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            int numberOfDeletedLocations = session.createQuery("DELETE FROM Location l "
                    + "WHERE l.user.id = :userId "
                    + "AND l.id = :locationId")
                    .setParameter("userId", userId)
                    .setParameter("locationId", locationId)
                    .executeUpdate();
            if (numberOfDeletedLocations != 1) {
                throw new UnauthorizedActionException();
            }
            transaction.commit();
        }
    }
}
