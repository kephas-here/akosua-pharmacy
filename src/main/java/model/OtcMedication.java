package model;

import enums.Category;

public class OtcMedication extends Medication {
    public OtcMedication(String name, double price, int stock) {
        super(name, price, stock);
    }

    @Override
    void verifyDispensable(String prescriber) {
    }

    @Override
    public Category getCategory() {
        return Category.OTC;
    }
}
