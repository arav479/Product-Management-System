package com.pms;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 * TEMPORARY STUB of the backend data class, so the UI can be built before the
 * real backend is ready. It follows the shared contract in
 * IMPLEMENTATION_PLAN.md, but talks to no database: the data below is
 * hard-coded (a sample of database/insert.sql) and lives in memory, so every
 * change is lost when the app closes.
 *
 * Delete this file once P1's backend jar provides the real com.pms.operation,
 * otherwise this copy hides the real one and the app keeps showing fake data.
 *
 * The find methods return java.util.List, exactly as in the contract. A page
 * that needs an observable list wraps the result itself:
 *     FXCollections.observableArrayList(operation.findAll())
 *
 * Difference from the real class: delete() removes the product from memory.
 * Hard or soft delete is still P1's decision; the UI sees the same thing
 * either way.
 */
public class operation {

    private static final List<Category> CATEGORIES = new ArrayList<>();
    private static final List<Supplier> SUPPLIERS = new ArrayList<>();
    private static final List<Product> PRODUCTS = new ArrayList<>();

    // insert.sql seeds products 1 to 30, so new ones start after that
    private static int nextProductId = 31;

    static {
        System.err.println("[STUB] com.pms.operation is the temporary in-memory stub. No database is used.");

        CATEGORIES.add(new Category(1, "Electronics"));
        CATEGORIES.add(new Category(2, "Home Appliances"));
        CATEGORIES.add(new Category(3, "Stationery"));
        CATEGORIES.add(new Category(4, "Groceries"));
        CATEGORIES.add(new Category(5, "Furniture"));
        CATEGORIES.add(new Category(6, "Personal Care"));

        SUPPLIERS.add(new Supplier(1, "TechWorld Distributors"));
        SUPPLIERS.add(new Supplier(2, "HomeNeeds Traders"));
        SUPPLIERS.add(new Supplier(3, "PaperPlus Supplies"));
        SUPPLIERS.add(new Supplier(4, "FreshMart Wholesale"));
        SUPPLIERS.add(new Supplier(5, "WoodCraft Furnishings"));
        SUPPLIERS.add(new Supplier(6, "CareWell Distributors"));

        //   id  name                      description                                  sup cat qty reorder price
        seed(1,  "Wireless Mouse",         "2.4GHz optical wireless mouse",             1, 1, 120, 20,   599.00);
        seed(2,  "Mechanical Keyboard",    "RGB backlit mechanical keyboard, blue switches", 1, 1, 35, 10, 2499.00);
        seed(3,  "USB-C Charger 65W",      "Fast charger for laptops and phones",       1, 1,   4, 10,  1799.00); // low stock
        seed(5,  "24-inch LED Monitor",    "Full HD IPS monitor with HDMI",             1, 1,  12,  5,  9499.00);
        seed(7,  "Electric Kettle 1.5L",   "Stainless steel auto shut-off kettle",      2, 2,  40, 10,  1299.00);
        seed(9,  "Steam Iron",             "1600W steam iron with non-stick soleplate", 2, 2,   3,  5,  1499.00); // low stock
        seed(13, "A4 Paper Ream",          "500 sheets, 75 GSM",                        3, 3, 300, 50,   289.00);
        seed(16, "Stapler",                "Heavy-duty stapler with 1000 pins",         3, 3,   8, 15,   175.00); // low stock
        seed(18, "Basmati Rice 5kg",       "Premium long-grain basmati rice",           4, 4,  80, 20,   649.00);
        seed(22, "Green Tea (100 bags)",   "Natural green tea bags",                    4, 4,  45, 10,   399.00);
        seed(23, "Ergonomic Office Chair", "Mesh back chair with lumbar support",       5, 5,  14,  5,  7499.00);
        seed(25, "Bookshelf 5-Tier",       "Open bookshelf, walnut finish",             5, 5,   2,  3,  4299.00); // low stock
        seed(27, "Herbal Shampoo 340ml",   "Paraben-free herbal shampoo",               6, 6,  95, 20,   245.00);
        seed(30, "Electric Trimmer",       "Rechargeable beard trimmer",                6, 6,  22,  5,  1349.00);
    }

    private static void seed(int id, String name, String description,
                             int supplierId, int categoryId, int quantity, int reorderLevel, double price) {
        PRODUCTS.add(new Product(id, name, description, supplierId, categoryId, quantity, reorderLevel, price));
    }

    // ---------- Reading ----------

    // Every product, ordered by ID, with categoryName and supplierName filled in.
    // Returns copies, so editing a returned Product changes nothing until update() is called.
    public static synchronized List<Product> findAll() {
        List<Product> result = new ArrayList<>();
        for (Product p : PRODUCTS) {
            result.add(copyWithNames(p));
        }
        result.sort(Comparator.comparingInt(Product::getProductId));
        return result;
    }

    // Every category, ordered by name
    public static synchronized List<Category> findAllCategories() {
        List<Category> result = new ArrayList<>();
        for (Category c : CATEGORIES) {
            result.add(new Category(c.categoryId, c.categoryName));
        }
        result.sort(Comparator.comparing(Category::getCategoryName));
        return result;
    }

    // Every supplier, ordered by name
    public static synchronized List<Supplier> findAllSuppliers() {
        List<Supplier> result = new ArrayList<>();
        for (Supplier s : SUPPLIERS) {
            result.add(new Supplier(s.supplierId, s.supplierName));
        }
        result.sort(Comparator.comparing(Supplier::getSupplierName));
        return result;
    }

    // ---------- Writing ----------

    Product product;

    public operation(Product product) {
        this.product = product;
    }

    // Saves the product under a new ID. The productId on the given product is ignored,
    // like the real add(), where the database generates it.
    public void add() throws SQLException {
        synchronized (operation.class) {
            check(product);
            PRODUCTS.add(new Product(nextProductId++, product.productName, product.description,
                    product.supplierId, product.categoryId, product.quantity,
                    product.reorderLevel, product.price));
        }
    }

    // Overwrites the stored product that has the same productId
    public void update() throws SQLException {
        synchronized (operation.class) {
            check(product);
            Product stored = find(product.productId);
            stored.productName = product.productName;
            stored.description = product.description;
            stored.supplierId = product.supplierId;
            stored.categoryId = product.categoryId;
            stored.quantity = product.quantity;
            stored.reorderLevel = product.reorderLevel;
            stored.price = product.price;
        }
    }

    // Removes the stored product that has the same productId
    public void delete() throws SQLException {
        synchronized (operation.class) {
            PRODUCTS.remove(find(product.productId));
        }
    }

    // ---------- Helpers ----------

    private static Product find(int productId) throws SQLException {
        for (Product p : PRODUCTS) {
            if (p.productId == productId) {
                return p;
            }
        }
        throw new SQLException("No product with ID " + productId);
    }

    // The same rules the PRODUCT table enforces in schema.sql
    private static void check(Product p) throws SQLException {
        if (p == null) {
            throw new SQLException("No product was given.");
        }
        if (p.productName == null || p.productName.isBlank()) {
            throw new SQLException("Product name is required.");
        }
        if (p.productName.length() > 100) {
            throw new SQLException("Product name is longer than 100 characters.");
        }
        if (p.description != null && p.description.length() > 500) {
            throw new SQLException("Description is longer than 500 characters.");
        }
        if (categoryName(p.categoryId) == null) {
            throw new SQLException("Category ID " + p.categoryId + " does not exist.");
        }
        if (supplierName(p.supplierId) == null) {
            throw new SQLException("Supplier ID " + p.supplierId + " does not exist.");
        }
        if (p.price <= 0) {
            throw new SQLException("Price must be greater than 0.");
        }
        if (p.quantity < 0) {
            throw new SQLException("Quantity cannot be negative.");
        }
        if (p.reorderLevel < 0) {
            throw new SQLException("Reorder level cannot be negative.");
        }
    }

    private static Product copyWithNames(Product p) {
        Product copy = new Product(p.productId, p.productName, p.description,
                p.supplierId, p.categoryId, p.quantity, p.reorderLevel, p.price);
        copy.setCategoryName(categoryName(p.categoryId));
        copy.setSupplierName(supplierName(p.supplierId));
        return copy;
    }

    private static String categoryName(int categoryId) {
        for (Category c : CATEGORIES) {
            if (c.categoryId == categoryId) {
                return c.categoryName;
            }
        }
        return null;
    }

    private static String supplierName(int supplierId) {
        for (Supplier s : SUPPLIERS) {
            if (s.supplierId == supplierId) {
                return s.supplierName;
            }
        }
        return null;
    }
}
