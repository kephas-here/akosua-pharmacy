package pharmacy.exception;

public class InsufficientStockException extends DispenseException {
    public InsufficientStockException(String medName, int requested, int available) {
        super("Cannot dispense " + requested + " x " + medName
                + ": only " + available + " in stock");
    }
}
