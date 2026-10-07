# Product Management System

A Java 17 and JavaFX desktop demo for managing a small in-memory product list. It currently supports adding, editing, searching, and safely confirming product deletion. Data resets to the sample products whenever the application is restarted; Oracle/database persistence is not connected in this checkout.

## Prerequisites

- JDK 17
- Apache Maven 3.8 or newer

## Run

From the repository root:

```powershell
mvn clean javafx:run
```

On Windows, if Maven is not on `PATH`, run the same command using your Maven installation's `mvn.cmd`.

## Delete a product

Use the **Delete Product** card to filter products by ID or name, select a result, and review its ID, name, category, price, and quantity. The delete button stays disabled until a product is selected. The confirmation dialog names the product and ID; cancel leaves the list unchanged. After confirming, the product is removed from the in-memory list and the picker is cleared.

The searchable `ProductPicker` is a reusable JavaFX control whose `selectedProductProperty()` exposes its current selection.

## Manual check list

- [ ] Start the app and confirm the five sample products appear.
- [ ] Filter the delete picker by part of a product name and by product ID.
- [ ] Select a product and verify its details; confirm the delete button enables.
- [ ] Cancel the confirmation and verify the product remains in the table.
- [ ] Confirm deletion and verify the product disappears from both the table and picker.
- [ ] Clear the regular search and verify the remaining products return.

## Current scope

The current repository is the initial single-screen JavaFX frontend. The Oracle backend, database setup scripts, multi-page shell, category/supplier IDs, description, and reorder level described in the larger implementation plan are not present yet. Consequently, this version cannot show those database fields, persist changes, or run database operations in background JavaFX tasks. Add those once the backend and shared model are integrated.
