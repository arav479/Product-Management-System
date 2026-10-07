package com.pms;

/*
 * TEMPORARY frontend copy of the backend Product model, extended as agreed in
 * the shared contract (IMPLEMENTATION_PLAN.md): the existing fields, plus
 * getReorderLevel()/setReorderLevel() and the display-only categoryName and
 * supplierName. Delete this file once P1's backend jar provides the updated
 * com.pms.Product, otherwise this copy hides the real one.
 */
public class Product {

    public int productId;
    public String productName;
    public String description;
    public int supplierId;
    public int categoryId;
    public int quantity;
    public int reorderLevel;
    public double price;

    // Display only: filled by the JOIN in findAll(), never written to the database
    public String categoryName;
    public String supplierName;

    public Product(int productId, String productName, String description,
                   int supplierId, int categoryId, int quantity, int reorderLevel, double price) {

        this.productId = productId;
        this.productName = productName;
        this.description = description;
        this.supplierId = supplierId;
        this.categoryId = categoryId;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.price = price;
    }

    public int getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getDescription() {
        return description;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public double getPrice() {
        return price;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
