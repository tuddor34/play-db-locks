package play.exception;

public class AlreadyRunningTaskRequestException extends RuntimeException {

    public AlreadyRunningTaskRequestException(String message) {
        super(message);
    }
}
