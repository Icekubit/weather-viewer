package icekubit;

import icekubit.dao.UserSessionDao;
import icekubit.service.UserSessionCollectorService;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        UserSessionCollectorService.getInstance().removeExpiredUserSessions();
    }
}