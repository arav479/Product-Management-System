package pms;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Frontend controller for the Product Management System UI.
 *
 * All methods below currently work with a temporary ObservableList.
 * The backend team can later replace the list operations with JDBC/DAO
 * calls without changing the FXML layout.
 */
public class ProductController {

    @FXML
    private TextField productIdField;
    @FXML
    private TextField productNameField;
    @FXML
    private ComboBox<String> categoryComboBox;
    @FXML
    private TextField priceField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField searchField;

    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, String> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, String> categoryColumn;
    @FXML
    private TableColumn<Product, Double> priceColumn;
    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    /**
     * Temporary in-memory product list used only for the frontend demo.
     *
     * TODO: Replace temporary ObservableList operation
     * with ProductDAO/JDBC implementation by backend team.
     */
    private final ObservableList<Product> productList = FXCollections.observableArrayList();
    private FilteredList<Product> filteredProducts;

    @FXML
    private void initialize() {
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Computer",
                "Accessory",
                "Electronics",
                "Audio"
        ));

        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadProducts();

        filteredProducts = new FilteredList<>(productList, product -> true);
        productTable.setItems(filteredProducts);

        productTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, selectedProduct) -> {
                    if (selectedProduct != null) {
                        fillForm(selectedProduct);
                    }
                }
        );
    }

    /**
     * Loads sample products so the TableView is not empty at startup.
     *
     * TODO: Replace temporary ObservableList operation
     * with ProductDAO/JDBC implementation by backend team.
     * Example later: productList.setAll(productDAO.getAllProducts());
     */
    private void loadProducts() {
        productList.clear();
        productList.addAll(
                new Product("P101", "Laptop", "Computer", 54999.00, 12),
                new Product("P102", "Keyboard", "Accessory", 1499.00, 40),
                new Product("P103", "Mouse", "Accessory", 799.00, 55),
                new Product("P104", "Monitor", "Electronics", 12999.00, 18),
                new Product("P105", "Headphones", "Audio", 2499.00, 30)
        );
    }

    @FXML
    private void addProduct() {
        if (!validateForm()) {
            return;
        }

        String productId = productIdField.getText().trim();
        if (findProductById(productId) != null) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Product ID already exists. Please use a different ID.");
            return;
        }

        Product product = new Product(
                productId,
                productNameField.getText().trim(),
                categoryComboBox.getValue(),
                Double.parseDouble(priceField.getText().trim()),
                Integer.parseInt(quantityField.getText().trim())
        );

        // TODO: Replace temporary ObservableList operation
        // with ProductDAO/JDBC implementation by backend team.
        // Example later: productDAO.insertProduct(product);
        productList.add(product);

        productTable.refresh();
        clearFields();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully.");
    }

    @FXML
    private void updateProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a product from the table to update.");
            return;
        }

        if (!validateForm()) {
            return;
        }

        String newProductId = productIdField.getText().trim();
        Product existingProduct = findProductById(newProductId);
        if (existingProduct != null && existingProduct != selectedProduct) {
            showAlert(Alert.AlertType.ERROR, "Validation Error",
                    "Another product already uses this Product ID.");
            return;
        }

        selectedProduct.setProductId(newProductId);
        selectedProduct.setProductName(productNameField.getText().trim());
        selectedProduct.setCategory(categoryComboBox.getValue());
        selectedProduct.setPrice(Double.parseDouble(priceField.getText().trim()));
        selectedProduct.setQuantity(Integer.parseInt(quantityField.getText().trim()));

        // TODO: Replace temporary ObservableList operation
        // with ProductDAO/JDBC implementation by backend team.
        // Example later: productDAO.updateProduct(selectedProduct);

        productTable.refresh();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Product updated successfully.");
    }

    @FXML
    private void deleteProduct() {
        Product selectedProduct = productTable.getSelectionModel().getSelectedItem();
        if (selectedProduct == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection",
                    "Please select a product from the table to delete.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText("Delete this product?");
        confirmation.setContentText("Product: " + selectedProduct.getProductName()
                + " (ID: " + selectedProduct.getProductId() + ")");

        if (confirmation.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
            return;
        }

        // TODO: Replace temporary ObservableList operation
        // with ProductDAO/JDBC implementation by backend team.
        // Example later: productDAO.deleteProduct(selectedProduct.getProductId());
        productList.remove(selectedProduct);

        productTable.refresh();
        clearFields();
        showAlert(Alert.AlertType.INFORMATION, "Success", "Product deleted successfully.");
    }

    @FXML
    private void searchProduct() {
        String keyword = searchField.getText() == null ? "" : searchField.getText().trim().toLowerCase();

        // TODO: Replace temporary ObservableList operation
        // with ProductDAO/JDBC implementation by backend team.
        // Example later: productList.setAll(productDAO.searchProducts(keyword));
        filteredProducts.setPredicate(product -> {
            if (keyword.isEmpty()) {
                return true;
            }
            return product.getProductId().toLowerCase().contains(keyword)
                    || product.getProductName().toLowerCase().contains(keyword);
        });
    }

    @FXML
    private void clearSearch() {
        searchField.clear();
        filteredProducts.setPredicate(product -> true);
        productTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void clearFields() {
        productIdField.clear();
        productNameField.clear();
        categoryComboBox.getSelectionModel().clearSelection();
        priceField.clear();
        quantityField.clear();
        productTable.getSelectionModel().clearSelection();
    }

    private void fillForm(Product product) {
        productIdField.setText(product.getProductId());
        productNameField.setText(product.getProductName());
        categoryComboBox.setValue(product.getCategory());
        priceField.setText(String.valueOf(product.getPrice()));
        quantityField.setText(String.valueOf(product.getQuantity()));
    }

    private Product findProductById(String productId) {
        for (Product product : productList) {
            if (product.getProductId().equalsIgnoreCase(productId)) {
                return product;
            }
        }
        return null;
    }

    private boolean validateForm() {
        if (isBlank(productIdField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Product ID cannot be empty.");
            return false;
        }
        if (isBlank(productNameField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Product Name cannot be empty.");
            return false;
        }
        if (categoryComboBox.getValue() == null || categoryComboBox.getValue().isBlank()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Category cannot be empty.");
            return false;
        }
        if (isBlank(priceField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Price cannot be empty.");
            return false;
        }
        if (isBlank(quantityField.getText())) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity cannot be empty.");
            return false;
        }

        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price < 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Price cannot be negative.");
                return false;
            }
        } catch (NumberFormatException exception) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Price should accept numeric input.");
            return false;
        }

        try {
            int quantity = Integer.parseInt(quantityField.getText().trim());
            if (quantity < 0) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity cannot be negative.");
                return false;
            }
        } catch (NumberFormatException exception) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Quantity should accept integer input.");
            return false;
        }

        return true;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
