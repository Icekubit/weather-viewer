package icekubit;

import icekubit.dao.UserSessionDao;

import java.util.UUID;

public class Main {
    public static void main(String[] args) {

        UserSessionDao.getInstance().delete(UUID.fromString("47fa5136-16dd-4900-b2c2-ee9553b92917"));
    }
}