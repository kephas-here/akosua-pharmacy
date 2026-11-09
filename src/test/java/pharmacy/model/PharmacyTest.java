package pharmacy.model;

import pharmacy.enums.CustomerKind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PharmacyTest {
    private Pharmacy pharmacy;
    private Medication paracetamol;
    private Medication amoxicillin;
    private Customer kofi;
    private Customer ama;

    @BeforeEach
    void setUp() {
        pharmacy = new Pharmacy("Akosua's Pharmacy");
        paracetamol = new OtcMedication("Paracetamol 500mg", 12.50, 100);
        amoxicillin = new PrescriptionMedication("Amoxicillin 250mg", 30.00, 40);
        pharmacy.addMedication(paracetamol);
        pharmacy.addMedication(amoxicillin);
        kofi = new Customer("Kofi Mensah", CustomerKind.WALK_IN);
        ama = new Customer("Ama Owusu", CustomerKind.REGISTERED);
        pharmacy.addCustomer(kofi);
        pharmacy.addCustomer(ama);
    }

    @Test
    void dispensingMoreThanInStockIsRefused() {
        pharmacy.dispense(kofi, amoxicillin, 100, "Dr. Boateng");

        assertEquals(40, amoxicillin.getStock());
        assertTrue(pharmacy.getTransactions().isEmpty());
        assertTrue(kofi.getHistory().isEmpty());
    }

    @Test
    void dispensingExactlyWhatIsInStockLeavesZero() {
        pharmacy.dispense(kofi, amoxicillin, 40, "Dr. Boateng");

        assertEquals(0, amoxicillin.getStock());
        assertEquals(1, pharmacy.getTransactions().size());
    }

    @Test
    void prescriptionWithoutPrescriberIsRefused() {
        pharmacy.dispense(kofi, amoxicillin, 1, null);

        assertEquals(40, amoxicillin.getStock());
        assertTrue(pharmacy.getTransactions().isEmpty());
    }

    @Test
    void prescriptionWithBlankPrescriberIsRefused() {
        pharmacy.dispense(kofi, amoxicillin, 1, "  ");

        assertEquals(40, amoxicillin.getStock());
        assertTrue(pharmacy.getTransactions().isEmpty());
    }

    @Test
    void otcSaleNeedsNoPrescriber() {
        pharmacy.dispense(kofi, paracetamol, 2, null);

        assertEquals(98, paracetamol.getStock());
        assertEquals(25.0, pharmacy.getTransactions().get(0).getTotal());
        assertNull(pharmacy.getTransactions().get(0).getPrescriber());
    }

    @Test
    void registeredPatientsGetFivePercentOff() {
        pharmacy.dispense(ama, amoxicillin, 1, "Dr. Boateng");

        assertEquals(28.5, pharmacy.getTransactions().get(0).getTotal());
    }

    @Test
    void prescriptionSaleRecordsThePrescriber() {
        pharmacy.dispense(ama, amoxicillin, 1, "Dr. Boateng");

        assertEquals("Dr. Boateng", pharmacy.getTransactions().get(0).getPrescriber());
        assertEquals("Got 1 x Amoxicillin 250mg", ama.getHistory().get(0));
    }

    @Test
    void restockAddsStockAndIsRecorded() {
        pharmacy.restock(paracetamol, 50);

        assertEquals(150, paracetamol.getStock());
        assertEquals(1, pharmacy.getTransactions().size());
    }

    @Test
    void transactionsCannotBeChangedFromOutside() {
        pharmacy.dispense(kofi, paracetamol, 1, null);

        assertThrows(UnsupportedOperationException.class, () -> pharmacy.getTransactions().clear());
        assertEquals(1, pharmacy.getTransactions().size());
    }
}
