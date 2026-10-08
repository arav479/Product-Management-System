package com.pms.ui;

import com.pms.Product;
import com.pms.operation;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/** Delete screen backed by the shared observable product list. */
public class DeleteProductPage extends VBox {
    private final ObservableList<Product> products;
    private final ProductPicker picker;
    private final Button deleteButton = new Button("Delete Product");
    private final ProgressIndicator loading = new ProgressIndicator();
    private final Label status = new Label();
    private final Label[] values = new Label[8];

    public DeleteProductPage(ObservableList<Product> products) {
        super(10);
        this.products = products;
        this.picker = new ProductPicker(products);
        setPadding(new Insets(18));
        getChildren().addAll(new Label("Delete Product"), picker, buildDetails(), deleteButton, loading, status);
        deleteButton.setDisable(true);
        loading.setVisible(false);
        loading.setManaged(false);
        picker.selectedProductProperty().addListener((obs, oldProduct, product) -> {
            showDetails(product);
            deleteButton.setDisable(product == null);
            status.setText("");
        });
        deleteButton.setOnAction(event -> confirmDelete());
        loadProducts();
    }

    private GridPane buildDetails() {
        String[] names = {"ID", "Name", "Description", "Category", "Supplier", "Price", "Quantity", "Reorder level"};
        GridPane grid = new GridPane();
        grid.setHgap(12);
        grid.setVgap(7);
        for (int i = 0; i < names.length; i++) {
            values[i] = new Label("—");
            grid.addRow(i, new Label(names[i] + ":"), values[i]);
        }
        return grid;
    }

    private void showDetails(Product product) {
        String[] details = product == null ? new String[]{"—", "—", "—", "—", "—", "—", "—", "—"}
                : new String[]{String.valueOf(product.getProductId()), product.getProductName(),
                value(product.getDescription()), product.getCategoryName(), product.getSupplierName(),
                String.valueOf(product.getPrice()), String.valueOf(product.getQuantity()),
                String.valueOf(product.getReorderLevel())};
        for (int i = 0; i < values.length; i++) values[i].setText(details[i]);
    }

    private String value(String text) { return text == null || text.isBlank() ? "—" : text; }

    private void confirmDelete() {
        Product selected = picker.getSelectedProduct();
        if (selected == null) return;
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + selected.getProductName() + " (ID " + selected.getProductId() + ")?",
                ButtonType.YES, ButtonType.NO);
        confirmation.setHeaderText(null);
        if (confirmation.showAndWait().orElse(ButtonType.NO) != ButtonType.YES) return;

        setLoading(true);
        Task<java.util.List<Product>> task = new Task<>() {
            @Override protected java.util.List<Product> call() throws Exception {
                new operation(selected).delete();
                return operation.findAll();
            }
        };
        task.setOnSucceeded(event -> {
            products.setAll(task.getValue());
            picker.clearSelection();
            status.setText(selected.getProductName() + " deleted successfully.");
            setLoading(false);
        });
        task.setOnFailed(event -> {
            status.setText("Could not delete product: " + readableError(task.getException()));
            setLoading(false);
        });
        Thread worker = new Thread(task, "delete-product-task");
        worker.setDaemon(true);
        worker.start();
    }

    private void loadProducts() {
        setLoading(true);
        Task<java.util.List<Product>> task = new Task<>() {
            @Override protected java.util.List<Product> call() throws Exception {
                return operation.findAll();
            }
        };
        task.setOnSucceeded(event -> {
            products.setAll(task.getValue());
            status.setText(products.isEmpty() ? "No products found." : "");
            setLoading(false);
        });
        task.setOnFailed(event -> {
            status.setText("Could not load products: " + readableError(task.getException()));
            setLoading(false);
        });
        Thread worker = new Thread(task, "load-products-task");
        worker.setDaemon(true);
        worker.start();
    }

    private String readableError(Throwable error) {
        Throwable cause = error;
        while (cause.getCause() != null) cause = cause.getCause();
        String message = cause.getMessage();
        if (message == null || message.isBlank()) message = cause.getClass().getSimpleName();
        return message + " Check that Oracle XE is running and the database is configured.";
    }

    private void setLoading(boolean busy) {
        loading.setVisible(busy);
        loading.setManaged(busy);
        picker.setControlsDisabled(busy);
        deleteButton.setDisable(busy || picker.getSelectedProduct() == null);
    }
}
