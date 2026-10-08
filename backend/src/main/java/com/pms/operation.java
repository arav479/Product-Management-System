package com.pms;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class operation {
    Product product;
    public operation(Product product) {
        this.product = product;
    }
    public void add() throws SQLException {
        String sql = "INSERT INTO PRODUCT (PRODUCT_NAME, DESCRIPTION, CATEGORY_ID, SUPPLIER_ID, "
                + "PRICE, QUANTITY, REORDER_LEVEL) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, product.productName);
            ps.setString(2, product.description);
            ps.setInt(3, product.categoryId);
            ps.setInt(4, product.supplierId);
            ps.setDouble(5, product.price);
            ps.setInt(6, product.quantity);
            ps.setInt(7, product.reorderLevel);

            ps.executeUpdate();
            System.out.println("Product added");
        }
        catch (SQLException e) {
            throw new SQLException("No product added: " + e.getMessage(), e);
        }
    }
    public void update() throws SQLException {
        String sql = "UPDATE PRODUCT SET "
                + "PRODUCT_NAME = ?, DESCRIPTION = ?, SUPPLIER_ID = ?, "
                + "CATEGORY_ID = ?, QUANTITY = ?, REORDER_LEVEL = ?, PRICE = ? "
                + "WHERE PRODUCT_ID = ?";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, product.productName);
            ps.setString(2, product.description);
            ps.setInt(3, product.supplierId);
            ps.setInt(4, product.categoryId);
            ps.setInt(5, product.quantity);
            ps.setInt(6, product.reorderLevel);
            ps.setDouble(7, product.price);
            ps.setInt(8, product.productId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No product with ID " + product.productId);
            }
        }
    }

    public void delete() throws SQLException {
        // Soft delete: the row stays in the table, findAll() just stops returning it
        String sql = "UPDATE PRODUCT SET STATUS = ? WHERE PRODUCT_ID = ?";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "INACTIVE");
            ps.setInt(2, product.productId);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                throw new SQLException("No product with ID " + product.productId);
            }
        }
    }
    // Searches by name (partial, case-insensitive) and prints the matches
    public void search() {
        String sql = "SELECT PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, SUPPLIER_ID, CATEGORY_ID, "
                + "QUANTITY, REORDER_LEVEL, PRICE "
                + "FROM PRODUCT WHERE UPPER(PRODUCT_NAME) LIKE UPPER(?) "
                + "ORDER BY PRODUCT_ID";

        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + product.productName + "%");

            try (ResultSet rs = ps.executeQuery()) {
                boolean found = false;
                while (rs.next()) {
                    found = true;
                    System.out.println(rs.getInt("PRODUCT_ID") + " | "
                            + rs.getString("PRODUCT_NAME") + " | "
                            + rs.getString("DESCRIPTION") + " | supplier "
                            + rs.getInt("SUPPLIER_ID") + " | category "
                            + rs.getInt("CATEGORY_ID") + " | qty "
                            + rs.getInt("QUANTITY") + " | reorder "
                            + rs.getInt("REORDER_LEVEL") + " | price "
                            + rs.getDouble("PRICE"));
                }
                if (!found) System.out.println("No products found");
            }

        } catch (SQLException e) {
            System.out.println("Search failed: " + e.getMessage());
        }
    }
    // All active products with their category and supplier names, ordered by ID
    public static List<Product> findAll() throws SQLException {
        String sql = "SELECT P.PRODUCT_ID, P.PRODUCT_NAME, P.DESCRIPTION, P.SUPPLIER_ID, P.CATEGORY_ID, "
                + "P.QUANTITY, P.REORDER_LEVEL, P.PRICE, C.CATEGORY_NAME, S.SUPPLIER_NAME "
                + "FROM PRODUCT P "
                + "JOIN CATEGORY C ON P.CATEGORY_ID = C.CATEGORY_ID "
                + "JOIN SUPPLIER S ON P.SUPPLIER_ID = S.SUPPLIER_ID "
                + "WHERE P.STATUS = 'ACTIVE' "
                + "ORDER BY P.PRODUCT_ID";

        List<Product> products = new ArrayList<>();
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Product p = new Product(
                        rs.getInt("PRODUCT_ID"),
                        rs.getString("PRODUCT_NAME"),
                        rs.getString("DESCRIPTION"),
                        rs.getInt("SUPPLIER_ID"),
                        rs.getInt("CATEGORY_ID"),
                        rs.getInt("QUANTITY"),
                        rs.getInt("REORDER_LEVEL"),
                        rs.getDouble("PRICE"));
                p.setCategoryName(rs.getString("CATEGORY_NAME"));
                p.setSupplierName(rs.getString("SUPPLIER_NAME"));
                products.add(p);
            }
        }
        return products;
    }

    public static List<Category> findAllCategories() throws SQLException {
        String sql = "SELECT CATEGORY_ID, CATEGORY_NAME, DESCRIPTION FROM CATEGORY ORDER BY CATEGORY_NAME";

        List<Category> categories = new ArrayList<>();
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                categories.add(new Category(
                        rs.getInt("CATEGORY_ID"),
                        rs.getString("CATEGORY_NAME"),
                        rs.getString("DESCRIPTION")));
            }
        }
        return categories;
    }

    public static List<Supplier> findAllSuppliers() throws SQLException {
        String sql = "SELECT SUPPLIER_ID, SUPPLIER_NAME, PHONE, EMAIL, ADDRESS FROM SUPPLIER ORDER BY SUPPLIER_NAME";

        List<Supplier> suppliers = new ArrayList<>();
        try (Connection con = DatabaseConnector.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("SUPPLIER_ID"),
                        rs.getString("SUPPLIER_NAME"),
                        rs.getString("PHONE"),
                        rs.getString("EMAIL"),
                        rs.getString("ADDRESS")));
            }
        }
        return suppliers;
    }
}
