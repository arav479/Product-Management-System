package pms;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;
import javafx.scene.layout.VBox;

import java.util.Locale;

/** Searchable product selector shared by product actions. */
public final class ProductPicker extends VBox {
    private final TextField searchField = new TextField();
    private final ComboBox<Product> productComboBox = new ComboBox<>();
    private FilteredList<Product> filteredProducts;

    public ProductPicker() {
        setSpacing(6);
        searchField.setPromptText("Filter by product ID or name");
        productComboBox.setPromptText("Choose a product");
        productComboBox.setMaxWidth(Double.MAX_VALUE);
        productComboBox.setItems(filteredProducts);
        productComboBox.setConverter(new StringConverter<>() {
            @Override public String toString(Product product) {
                return product == null ? "" : product.getProductId() + " – " + product.getProductName();
            }
            @Override public Product fromString(String value) { return null; }
        });
        searchField.textProperty().addListener((observable, oldValue, value) -> filter(value));
        getChildren().addAll(searchField, productComboBox);
    }

    public void setProducts(ObservableList<Product> source) {
        filteredProducts = new FilteredList<>(source);
        productComboBox.setItems(filteredProducts);
        filter(searchField.getText());
    }

    private void filter(String value) {
        String query = value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
        filteredProducts.setPredicate(product -> query.isEmpty()
                || product.getProductId().toLowerCase(Locale.ROOT).contains(query)
                || product.getProductName().toLowerCase(Locale.ROOT).contains(query));
    }

    public ObjectProperty<Product> selectedProductProperty() {
        return productComboBox.valueProperty();
    }

    public Product getSelectedProduct() { return productComboBox.getValue(); }

    public void setSelectedProduct(Product product) { productComboBox.setValue(product); }

    public void clearSelection() {
        productComboBox.getSelectionModel().clearSelection();
        searchField.clear();
    }
}
