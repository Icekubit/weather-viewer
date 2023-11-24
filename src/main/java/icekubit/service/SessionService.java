package icekubit.service;

import icekubit.dao.UserSessionDao;
import icekubit.dao.UserDao;
import icekubit.entity.UserSession;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public class SessionService {
    private static SessionService instance;
    private SessionService() {}
    public static SessionService getInstance() {
        if (instance == null) {
            instance = new SessionService();
        }
        return instance;
    }




}
