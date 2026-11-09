package pharmacy.model;

import pharmacy.enums.Category;
import pharmacy.exception.MissingPrescriberException;

public class PrescriptionMedication extends Medication {
    public PrescriptionMedication(String name, double price, int stock) {
        super(name, price, stock);
    }

    @Override
    void verifyDispensable(String prescriber) {
        if (prescriber == null || prescriber.isBlank()) {
            throw new MissingPrescriberException(getName());
        }
    }

    @Override
    public Category getCategory() {
        return Category.PRESCRIPTION;
    }
}
