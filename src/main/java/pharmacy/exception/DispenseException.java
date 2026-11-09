package pharmacy.exception;

public abstract class DispenseException extends RuntimeException {
    protected DispenseException(String message) {
        super(message);
    }
}
