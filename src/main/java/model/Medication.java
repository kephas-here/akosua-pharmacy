package model;

import enums.Category;

public abstract class Medication {
    private String name;
    private double price;
    private int stock;
    private final int id;
    static int counter = 1;

    public Medication(String name, double price, int stock) {
        if (stock < 0) {
            throw new IllegalArgumentException("Opening stock cannot be negative");
        }
        this.id = counter;
        counter++;
        setName(name);
        setPrice(price);
        this.stock = stock;
    }

    public int getId() {
        return id;
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

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public final void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        this.price = price;
    }

    void addStock(int qty) {
        requirePositive(qty);
        stock += qty;
    }

    void deductStock(int qty) {
        requirePositive(qty);
        stock -= qty;
    }

    private static void requirePositive(int qty) {
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
    }

    abstract void verifyDispensable(String prescriber);

    public abstract Category getCategory();
}
