package pharmacy;

import pharmacy.enums.CustomerKind;
import pharmacy.model.Customer;
import pharmacy.model.Medication;
import pharmacy.model.OtcMedication;
import pharmacy.model.Pharmacy;
import pharmacy.model.PrescriptionMedication;

public class PharmacyTracker {

    public static void main(String[] args) {
        Pharmacy pharmacy = new Pharmacy("Akosua's Pharmacy");

        Medication paracetamol = new OtcMedication("Paracetamol 500mg", 12.50, 100);
        Medication amoxicillin = new PrescriptionMedication("Amoxicillin 250mg", 30.00, 40);
        pharmacy.addMedication(paracetamol);
        pharmacy.addMedication(amoxicillin);

        Customer kofi = new Customer("Kofi Mensah", CustomerKind.WALK_IN);
        Customer ama = new Customer("Ama Owusu", CustomerKind.REGISTERED);
        ama.setPhone("0244 000 000"); // registered patients have contact details on file
        pharmacy.addCustomer(kofi);
        pharmacy.addCustomer(ama);

        // a normal morning
        pharmacy.dispense(kofi, paracetamol, 2, null);
        pharmacy.dispense(ama, amoxicillin, 1, "Dr. Boateng"); // prescription-only
        pharmacy.restock(paracetamol, 50);

        // ... and the sort of thing that keeps Akosua up at night:
        pharmacy.dispense(kofi, amoxicillin, 100, "Dr. Boateng"); // far more than we have in stock

        pharmacy.printReport();
    }
}
