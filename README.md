# Akosua's Pharmacy — Stock and Dispensing Tracker

A rebuild of the pharmacy's stock and dispensing tracker. It does exactly what the original did, except that stock can no longer go below zero.

- Diagnosis and design sketch: [DIAGNOSIS.md](DIAGNOSIS.md)
- Output of the original program: [expected-output-before.txt](expected-output-before.txt)
- Output of the rebuild: [actual-output-after.txt](actual-output-after.txt)

## Running it

You need JDK 21. From the project folder, in any terminal:

```bash
javac -d target/classes -sourcepath src/main/java src/main/java/pharmacy/PharmacyTracker.java
java -cp target/classes pharmacy.PharmacyTracker
```

Or with Maven: `mvn compile`, then the same `java` line.

To compare with the original, check the output against [expected-output-before.txt](expected-output-before.txt). Only three lines differ, all from the one sale that asks for more Amoxicillin than is in stock. It is now refused, stock stays at 39 instead of -61, and three transactions are recorded instead of four. Section 5 of the diagnosis has the details.

## Tests (improvement beyond the brief)

The original had no tests, so the only way to know a change hadn't broken something was to run the program and read the output by eye. That's how a change to one kind of medicine quietly broke another, and why Akosua stopped trusting the tool.

I added JUnit tests for the rules the business depends on:

- Stock can't go below zero, and a sale that asks for too much is refused with nothing recorded.
- A prescription medicine needs a prescriber, and a blank one doesn't count.
- An OTC sale needs no prescriber.
- Registered patients get 5% off.
- A prescription sale records its prescriber and the customer's history.
- Quantities, prices and names are checked, so bad values are rejected.
- The transaction list can't be changed from outside.
- A refused sale is kept in the refusals list.

Anyone who changes the code next, like adding controlled substances, can run these and know straight away whether the existing rules still hold.

To run them, use `mvn test`, or open the project in IntelliJ and run the tests under `src/test/java`.

## Project layout

```
src/main/java/pharmacy/
  PharmacyTracker.java          the same morning of sales as the original
  model/                        Pharmacy, Medication and its subclasses, Customer, Transaction
  enums/                        Category, CustomerKind, TransactionType
  exception/                    DispenseException and the reasons a sale can be refused
src/test/java/pharmacy/model/
  PharmacyTest.java             dispensing, refusals, discount, prescriber, read-only transactions
  MedicationTest.java           stock and value checks on a single medicine
```

## Design notes

### Each kind of medicine owns its own rule

`Medication` is abstract, and `OtcMedication` and `PrescriptionMedication` each override `verifyDispensable(prescriber)`. `Pharmacy.dispense` has one path for every medicine and just asks the medicine whether it can be sold. The medicine also writes its own "(prescribed by …)" note through `prescriberNote`, so `Pharmacy` never checks which kind of medicine it has.

I picked this over keeping a category enum and a `switch` in `Pharmacy`. With a `switch`, every new kind of medicine means editing `dispense` again, which is the same method that was edited when a change to one kind of medicine broke another. With subclasses, a new kind is a new class and the existing ones aren't touched.

The category is never stored as a field. Each subclass returns its own `Category` from `getCategory()`, so a medicine and its category can't disagree, and the report still prints `OTC` and `PRESCRIPTION` exactly as before.

### Stock can only change in one place

`stock` is private, and the only ways to change it are `addStock` and `deductStock`. Both reject a quantity of 0 or less, and `deductStock` refuses to take more than is there. They are package-private, so only `Pharmacy` can call them. This is the rule the business said must never be broken, so it lives inside `Medication` where nothing can get around it, not in `dispense` where any other code could still skip it.

### A refused sale leaves no trace

When a sale can't go ahead, the medicine throws an unchecked exception: `MissingPrescriberException` or `InsufficientStockException`, both extending `DispenseException`. `Pharmacy` catches the one parent type and prints its message. Everything that records the sale (history, transaction, receipt line) comes after the checks, so a refused sale records nothing. The refusal itself is kept in `getRefusals()`, so the pharmacy can see afterwards what was turned away and why, not just in what was printed.

I chose exceptions over returning `true`/`false`, because a return value is easy to ignore and the sale would go through anyway. I made them unchecked because `Pharmacy` handles them in one place, and checked exceptions would force every caller to deal with them.

### Nothing can be put in a bad state from outside

- All fields are private. Constructors and setters reject bad values (blank names, a price of 0 or less, negative opening stock, a missing customer kind), and constructors go through the same setters so each check lives in one place.
- Setters are `final` so a subclass can't skip the check.
- `Transaction` has no setters at all. It's the record of a sale, so it can't be edited afterwards. It also stores the prescriber for every prescription sale, since the original only printed it and then lost it.
- `getTransactions()`, `getHistory()` and the other list getters return read-only copies, so outside code can't add to or clear the audit trail.
- Enums replace the raw strings (`"OTC"`, `"WALK_IN"`, `"DISPENSE"`), so a typo is a compile error instead of a silent wrong path.

### Kept the same on purpose

Money is still a `double`, messages keep the original wording, and the id counters are unchanged. Changing any of these would change the output, and the point of this rebuild is to behave the same.

### Adding controlled substances

Controlled substances are a third kind of medicine with stricter rules, and every dispense has to record the prescriber. With this structure:

1. Add `CONTROLLED` to `Category`.
2. Add a `ControlledMedication` class that extends `PrescriptionMedication`, calls the parent's check and adds the stricter rule in `verifyDispensable`, and returns `CONTROLLED` from `getCategory()`.

That's it. It inherits the "(prescribed by …)" note, and every dispense already stores the prescriber on the `Transaction`. `Pharmacy`, `OtcMedication`, `PrescriptionMedication`, `Transaction`, the stock rules and the report don't change.

In the original, the same change would have meant a third `else if` branch in `dispense`, copying the price, discount, stock and print code a third time, and adding more string comparisons. Every one of those edits sits in the same method as the two medicine types that already work, so it's easy to break them.
