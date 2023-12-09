package icekubit.dao;

import icekubit.entity.Location;
import icekubit.entity.User;
import icekubit.util.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LocationDao {
    private final SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
    public int save(Location location) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.persist(location);
            transaction.commit();
        }
        return location.getId();
    }


    public List<Location> getLocationsForThisUser(User user) {
        List<Location> locations = new ArrayList<>();
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            session.update(user);
            locations = user.getUserLocations();
            Hibernate.initialize(locations);
            transaction.commit();
        }
        return locations;
    }

    public void deleteLocation(User user, int locationId) {
        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            String hql = "DELETE FROM Location l "
                    + "WHERE l.user.id = :userId "
                    + "AND l.id = :locationId";
            Query query = session.createQuery(hql);
            query.setParameter("userId", user.getId());
            query.setParameter("locationId", locationId);
            query.executeUpdate();
            transaction.commit();
        }
    }

    public Optional<Location> findLocation(User user, BigDecimal latitude, BigDecimal longitude) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Location l "
                    + "WHERE l.user.id = :userId "
                    + "AND l.latitude = :latitude "
                    + "AND l.longitude = :longitude";
            Query query = session.createQuery(hql);
            query.setParameter("userId", user.getId());
            query.setParameter("latitude", latitude);
            query.setParameter("longitude", longitude);
            List<Location> resultList = query.getResultList();
            return resultList.stream().findFirst();
        }
    }
}
