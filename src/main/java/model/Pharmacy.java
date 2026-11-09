package model;

import enums.Category;
import enums.CustomerKind;
import enums.TransactionType;
import exception.DispenseException;

import java.util.ArrayList;
import java.util.List;

public class Pharmacy {
    private static final double REGISTERED_DISCOUNT = 0.95;

    public String name;
    public List<Medication> meds = new ArrayList<>();
    public List<Customer> customers = new ArrayList<>();
    private List<Transaction> transactions = new ArrayList<>();

    public Pharmacy(String name) {
        this.name = name;
    }

    public void addMedication(Medication m) {
        meds.add(m);
    }

    public void addCustomer(Customer c) {
        customers.add(c);
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
            transactions.add(new Transaction(customer.getName(), medication.getName(), qty, total,
                    TransactionType.DISPENSE));

            String prescribedBy = medication.getCategory() == Category.PRESCRIPTION
                    ? " (prescribed by " + prescriber + ")" : "";

            System.out.println("Dispensed " + qty + " x " + medication.getName() + " to " + customer.getName()
                    + prescribedBy + " for GHS " + total);
        } catch (DispenseException e) {
            System.out.println(e.getMessage());
        }
    }

    public void restock(Medication medication, int qty) {
        medication.addStock(qty);
        transactions.add(new Transaction("(supplier)", medication.getName(), qty, 0, TransactionType.RESTOCK));
        System.out.println("Restocked " + qty + " x " + medication.getName());
    }

    public List<Transaction> getTransactions() {
        return transactions; // hand the list back so callers can read it
    }

    public void printReport() {
        System.out.println();
        System.out.println("=== " + name + " - end of day ===");
        System.out.println("Stock levels:");
        for (Medication m : meds) {
            System.out.println("  " + m.getName() + " (" + m.getCategory() + "): " + m.getStock() + " in stock");
        }
        System.out.println("Transactions today: " + transactions.size());
    }
}
