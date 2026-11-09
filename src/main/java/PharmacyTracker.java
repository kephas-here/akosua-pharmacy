import enums.CustomerKind;
import model.Customer;
import model.Medication;
import model.OtcMedication;
import model.Pharmacy;
import model.PrescriptionMedication;

public class PharmacyTracker {

    public static void main(String[] args) {
        Pharmacy p = new Pharmacy("Akosua's Pharmacy");

        Medication paracetamol = new OtcMedication("Paracetamol 500mg", 12.50, 100);
        Medication amoxicillin = new PrescriptionMedication("Amoxicillin 250mg", 30.00, 40);
        p.addMedication(paracetamol);
        p.addMedication(amoxicillin);

        Customer kofi = new Customer("Kofi Mensah", CustomerKind.WALK_IN);
        Customer ama = new Customer("Ama Owusu", CustomerKind.REGISTERED);
        ama.setPhone("0244 000 000"); // registered patients have contact details on file
        p.addCustomer(kofi);
        p.addCustomer(ama);

        // a normal morning
        p.dispense(kofi, paracetamol, 2, null);
        p.dispense(ama, amoxicillin, 1, "Dr. Boateng"); // prescription-only
        p.restock(paracetamol, 50);

        // ... and the sort of thing that keeps Akosua up at night:
        p.dispense(kofi, amoxicillin, 100, "Dr. Boateng"); // far more than we have in stock

        p.printReport();
    }
}
