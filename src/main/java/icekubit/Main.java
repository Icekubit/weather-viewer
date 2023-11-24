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

        SessionDao.getInstance().delete(UUID.fromString("47fa5136-16dd-4900-b2c2-ee9553b92917"));
    }
}