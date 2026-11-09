package exception;

public class MissingPrescriberException extends DispenseException {
    public MissingPrescriberException(String medName) {
        super("Cannot dispense " + medName + " without a prescriber");
    }
}
