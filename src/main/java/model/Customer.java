package model;

import enums.CustomerKind;

import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String name;
    private CustomerKind kind;
    private String phone;
    private final int id;
    private final List<String> history = new ArrayList<>();
    static int counter = 1;

    public Customer(String name, CustomerKind kind) {
        this.id = counter;
        counter++;
        setName(name);
        setKind(kind);
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

    public CustomerKind getKind() {
        return kind;
    }

    public final void setKind(CustomerKind kind) {
        if (kind == null) {
            throw new IllegalArgumentException("Customer kind is required");
        }
        this.kind = kind;
    }

    public String getPhone() {
        return phone;
    }

    public final void setPhone(String phone) {
        if (phone != null && phone.isBlank()) {
            throw new IllegalArgumentException("Phone cannot be blank");
        }
        this.phone = phone;
    }

    public List<String> getHistory() {
        return List.copyOf(history);
    }

    void addHistory(String medName, int qty) {
        history.add("Got " + qty + " x " + medName);
    }
}
