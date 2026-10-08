package com.pms;


public class Product {

    public int productId;
    public String productName;
    public String description;
    public int supplierId;
    public int categoryId;
    public int quantity;
    public int reorderLevel;
    public double price;
    public String categoryName;   // display only, filled by JOIN in operation.findAll()
    public String supplierName;   // display only, filled by JOIN in operation.findAll()

    public Product(int productId, String productName, String description,
                   int supplierId, int categoryId, int quantity, int reorderLevel,double price) {

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

    public double getPrice() {
        return price;
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

    public void setPrice(double price) {
        this.price = price;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
