package com.pms;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class operation {
    Product product;
    operation(Product product){
        this.product = product;
    }
    void add (){
        //insert sql statement
        String sql = "INSERT INTO PRODUCT VALUES ("
                + product.productId + ","
                + product.productName + ","
                + product.description + ", "
                + product.supplierId + ","
                + product.categoryId + ", "
                + product.quantity + ", "
                + product.reorderLevel + ", "
                + product.price + ")";
    }
    void update() {
        String sql = "UPDATE PRODUCT SET "
                + "PRODUCT_NAME = ?, DESCRIPTION = ?, SUPPLIER_ID = ?, "
                + "CATEGORY_ID = ?, QUANTITY = ?, REORDER_LEVEL = ?, PRICE = ?, "
                + "UPDATED_AT = CURRENT_TIMESTAMP "
                + "WHERE PRODUCT_ID = ?";

        try (Connection con = new DatabaseConnector().connection;
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
            System.out.println(rows > 0 ? "Product updated" : "No product with ID " + product.productId);

        } catch (SQLException e) {
            System.out.println("Update failed: " + e.getMessage());
        }
    }

    void delete() {
        String sql = "DELETE FROM PRODUCT WHERE PRODUCT_ID = ?";

        try (Connection con = new DatabaseConnector().connection;
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, product.productId);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "Product deleted" : "No product with ID " + product.productId);

        } catch (SQLException e) {
            System.out.println("Delete failed: " + e.getMessage());
        }
    }

    // Searches by name (partial, case-insensitive) and prints the matches
    void search() {
        String sql = "SELECT PRODUCT_ID, PRODUCT_NAME, DESCRIPTION, SUPPLIER_ID, CATEGORY_ID, "
                + "QUANTITY, REORDER_LEVEL, PRICE "
                + "FROM PRODUCT WHERE UPPER(PRODUCT_NAME) LIKE UPPER(?) "
                + "ORDER BY PRODUCT_ID";

        try (Connection con = new DatabaseConnector().connection;
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
   
    