package icekubit.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
