package icekubit.util;

import icekubit.entity.Location;
import icekubit.entity.Session;
import icekubit.entity.User;
import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {
    @Getter
    private final static SessionFactory sessionFactory;
    static {
        sessionFactory = new Configuration()
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Location.class)
                .addAnnotatedClass(Session.class)
                .configure("hibernate.cfg.xml")
                .buildSessionFactory();
    }
}
