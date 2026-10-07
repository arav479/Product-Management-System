package pms;

/**
 * Frontend Product model used by the JavaFX TableView and form.
 *
 * This class stores product data in memory for the GUI demonstration.
 * The backend team can later reuse the same field names in their own
 * Product model or map JDBC ResultSet rows into this class.
 */
public class Product {

    private String productId;
    private String productName;
    private String category;
    private double price;
    private int quantity;

    public Product() {
    }

    public Product(String productId, String productName, String category, double price, int quantity) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
