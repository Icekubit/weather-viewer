package icekubit;

import icekubit.entity.Location;
import icekubit.entity.User;
import icekubit.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        Session session = HibernateUtil.getSessionFactory().openSession();
        try (session) {
            Transaction transaction = session.beginTransaction();

            User user = session.get(User.class, 1);
            icekubit.entity.Session userSession = new icekubit.entity.Session();
            userSession.setUser(user);
            userSession.setExpiresAt(LocalDateTime.now().plusHours(1));

            session.persist(userSession);

            transaction.commit();
        }
    }
}