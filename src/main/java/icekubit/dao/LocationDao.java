package icekubit.dao;

import icekubit.entity.Location;
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
        return sessionFactory.openSession().createQuery("select l from Location l " +
                "join l.user where l.user.id = :userId", Location.class)
                .setParameter("userId", userId)
                .list();
    }

    public void delete(int userId, int locationId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = "DELETE FROM Location l "
                    + "WHERE l.user.id = :userId "
                    + "AND l.id = :locationId";
            Query query = session.createQuery(hql);
            query.setParameter("userId", userId);
            query.setParameter("locationId", locationId);
            query.executeUpdate();
            transaction.commit();
        }
    }
}
