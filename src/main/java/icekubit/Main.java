package icekubit;

import icekubit.dao.SessionDao;
import icekubit.dao.UserDao;
import icekubit.entity.Location;
import icekubit.entity.User;
import icekubit.service.AuthorisationService;
import icekubit.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.time.LocalDateTime;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        SessionDao.getInstance().findById(UUID.fromString("f3897ab8-216c-429f-92a2-3c3ce5f77068"));
    }
}