package model;

import exception.InsufficientStockException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MedicationTest {

    @Test
    void stockCannotGoBelowZero() {
        Medication amoxicillin = new PrescriptionMedication("Amoxicillin 250mg", 30.00, 5);

        assertThrows(InsufficientStockException.class, () -> amoxicillin.deductStock(6));
        assertEquals(5, amoxicillin.getStock());
    }

    @Test
    void quantityMustBePositive() {
        Medication paracetamol = new OtcMedication("Paracetamol 500mg", 12.50, 10);

        assertThrows(IllegalArgumentException.class, () -> paracetamol.addStock(0));
        assertThrows(IllegalArgumentException.class, () -> paracetamol.addStock(-5));
        assertThrows(IllegalArgumentException.class, () -> paracetamol.deductStock(-1));
        assertEquals(10, paracetamol.getStock());
    }

    @Test
    void cannotCreateMedicationWithBadValues() {
        assertThrows(IllegalArgumentException.class, () -> new OtcMedication("", 12.50, 10));
        assertThrows(IllegalArgumentException.class, () -> new OtcMedication("Paracetamol", 0, 10));
        assertThrows(IllegalArgumentException.class, () -> new OtcMedication("Paracetamol", 12.50, -1));
    }
}
