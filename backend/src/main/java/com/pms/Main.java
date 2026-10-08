package com.pms;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

// Manual check of every operation method against the local database
public class Main {

    public static void main(String[] args) throws SQLException {
        System.out.println("=== Products ===");
        printProducts(operation.findAll());

        System.out.println("\n=== Categories ===");
        List<Category> categories = operation.findAllCategories();
        for (Category c : categories) {
            System.out.println(c.getCategoryId() + " | " + c.getCategoryName());
        }

        System.out.println("\n=== Suppliers ===");
        List<Supplier> suppliers = operation.findAllSuppliers();
        for (Supplier s : suppliers) {
            System.out.println(s.getSupplierId() + " | " + s.getSupplierName());
        }

        int categoryId = categories.get(0).getCategoryId();
        int supplierId = suppliers.get(0).getSupplierId();

        // The apostrophe checks that the PreparedStatement handles quotes
        System.out.println("\n=== Add Kid's Chair ===");
        Product chair = new Product(0, "Kid's Chair", "Small wooden chair",
                supplierId, categoryId, 10, 3, 49.99);
        new operation(chair).add();
        Product added = findByName("Kid's Chair");
        System.out.println(added == null ? "FAIL: not found after add" : "OK, ID " + added.getProductId());
        if (added == null) return;

        System.out.println("\n=== Update Kid's Chair ===");
        added.setQuantity(25);
        added.setPrice(59.99);
        new operation(added).update();
        Product updated = findByName("Kid's Chair");
        System.out.println(updated.getQuantity() == 25 && updated.getPrice() == 59.99
                ? "OK, qty " + updated.getQuantity() + ", price " + updated.getPrice()
                : "FAIL: values not updated");

        System.out.println("\n=== Delete Kid's Chair ===");
        new operation(added).delete();
        System.out.println(findByName("Kid's Chair") == null
                ? "OK, no longer listed"
                : "FAIL: still listed");

        System.out.println("\n=== Negative price (should be rejected) ===");
        Product bad = new Product(0, "Bad Price Test", null,
                supplierId, categoryId, 1, 0, -5);
        try {
            new operation(bad).add();
            System.out.println("FAIL: negative price was accepted");
        } catch (SQLException e) {
            System.out.println("OK, rejected: " + e.getMessage());
        }

        // delete() only marks the row INACTIVE, so remove the test row for good
        removeTestProduct("Kid's Chair");
    }

    private static void removeTestProduct(String name) throws SQLException {
        String sql = "DELETE FROM PRODUCT WHERE PRODUCT_NAME = ?";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name);
            System.out.println("\nCleanup: removed " + ps.executeUpdate() + " test row(s)");
        }
    }

    private static Product findByName(String name) throws SQLException {
        Product match = null;
        for (Product p : operation.findAll()) {
            if (p.getProductName().equals(name)) {
                match = p;   // keep the last one, which is the newest ID
            }
        }
        return match;
    }

    // Prints the products as a table: one header row, then one aligned row per product
    private static void printProducts(List<Product> products) {
        String format = "%-4s | %-25s | %-16s | %-24s | %5s | %7s | %10s%n";
        System.out.printf(format, "ID", "Name", "Category", "Supplier", "Qty", "Reorder", "Price");
        System.out.println("-".repeat(108));

        for (Product p : products) {
            System.out.printf(format,
                    p.getProductId(),
                    p.getProductName(),
                    p.getCategoryName(),
                    p.getSupplierName(),
                    p.getQuantity(),
                    p.getReorderLevel(),
                    String.format("%.2f", p.getPrice()));
        }
        System.out.println(products.size() + " product(s)");
    }
}
