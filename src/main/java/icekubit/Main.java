package icekubit;

import icekubit.dao.UserDao;
import icekubit.entity.Location;
import icekubit.entity.User;
import icekubit.service.AuthorisationService;
import icekubit.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        AuthorisationService.getInstance().authoriseUser("buabodran", "12345");
    }
}