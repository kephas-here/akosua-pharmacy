package model;

import enums.TransactionType;

public class Transaction {
    private final int id;
    private final String customerName;
    private final String medName;
    private final int qty;
    private final double total;
    private final TransactionType type;
    static int counter = 1;

    public Transaction(String customerName, String medName, int qty, double total, TransactionType type) {
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be blank");
        }
        if (medName == null || medName.isBlank()) {
            throw new IllegalArgumentException("Medication name cannot be blank");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        if (type == null) {
            throw new IllegalArgumentException("Transaction type is required");
        }
        if (total < 0) {
            throw new IllegalArgumentException("Total cannot be negative");
        }
        if (type == TransactionType.DISPENSE && total == 0) {
            throw new IllegalArgumentException("A dispense must have a total greater than 0");
        }
        this.id = counter;
        counter++;
        this.customerName = customerName;
        this.medName = medName;
        this.qty = qty;
        this.total = total;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getMedName() {
        return medName;
    }

    public int getQty() {
        return qty;
    }

    public double getTotal() {
        return total;
    }

    public TransactionType getType() {
        return type;
    }
}
