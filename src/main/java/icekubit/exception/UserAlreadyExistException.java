package icekubit.exception;

public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(Throwable cause) {
        super(cause);
    }
    public UserAlreadyExistException(String message) {
        super(message);
    }
}
