package pharmacy.model;

import pharmacy.enums.CustomerKind;
import pharmacy.enums.TransactionType;
import pharmacy.exception.DispenseException;

import java.util.ArrayList;
import java.util.List;

public class Pharmacy {
    private static final double REGISTERED_DISCOUNT = 0.95;

    private String name;
    private final List<Medication> medications = new ArrayList<>();
    private final List<Customer> customers = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();
    private final List<String> refusals = new ArrayList<>();

    public Pharmacy(String name) {
        setName(name);
    }

    public String getName() {
        return name;
    }

    public final void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        this.name = name;
    }

    public List<Medication> getMedications() {
        return List.copyOf(medications);
    }

    public List<Customer> getCustomers() {
        return List.copyOf(customers);
    }

    public void addMedication(Medication medication) {
        if (medication == null) {
            throw new IllegalArgumentException("Medication is required");
        }
        medications.add(medication);
    }

    public void addCustomer(Customer customer) {
        if (customer == null) {
            throw new IllegalArgumentException("Customer is required");
        }
        customers.add(customer);
    }

    public void dispense(Customer customer, Medication medication, int qty, String prescriber) {
        try {
            medication.verifyDispensable(prescriber);
            double total = medication.getPrice() * qty;
            if (customer.getKind() == CustomerKind.REGISTERED) {
                total = total * REGISTERED_DISCOUNT;
            }
            medication.deductStock(qty);
            customer.addHistory(medication.getName(), qty);
            String recordedPrescriber = prescriber == null || prescriber.isBlank() ? null : prescriber;
            transactions.add(new Transaction(customer.getName(), medication.getName(), qty, total,
                    TransactionType.DISPENSE, recordedPrescriber));
            System.out.println("Dispensed " + qty + " x " + medication.getName() + " to " + customer.getName()
                    + medication.prescriberNote(prescriber) + " for GHS " + total);
        } catch (DispenseException e) {
            refusals.add(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public void restock(Medication medication, int qty) {
        medication.addStock(qty);
        transactions.add(new Transaction("(supplier)", medication.getName(), qty, 0, TransactionType.RESTOCK, null));
        System.out.println("Restocked " + qty + " x " + medication.getName());
    }

    public List<Transaction> getTransactions() {
        return List.copyOf(transactions);
    }

    public List<String> getRefusals() {
        return List.copyOf(refusals);
    }

    public void printReport() {
        System.out.println();
        System.out.println("=== " + name + " - end of day ===");
        System.out.println("Stock levels:");
        for (Medication medication : medications) {
            System.out.println("  " + medication.getName() + " (" + medication.getCategory() + "): " + medication.getStock() + " in stock");
        }
        System.out.println("Transactions today: " + transactions.size());
    }
}
