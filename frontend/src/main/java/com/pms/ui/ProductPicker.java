package com.pms.ui;

import com.pms.Product;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Locale;

/** Searchable product selector shared by product pages. */
public class ProductPicker extends VBox {
    private final TextField searchField = new TextField();
    private final ListView<Product> results = new ListView<>();
    private final ObjectProperty<Product> selectedProduct = new SimpleObjectProperty<>();

    public ProductPicker(ObservableList<Product> products) {
        super(6);
        searchField.setPromptText("Search by product ID or name");
        FilteredList<Product> filtered = new FilteredList<>(products, product -> true);
        results.setItems(filtered);
        results.setCellFactory(list -> new ListCell<>() {
            @Override protected void updateItem(Product product, boolean empty) {
                super.updateItem(product, empty);
                setText(empty || product == null ? null
                        : product.getProductId() + " – " + product.getProductName());
            }
        });
        searchField.textProperty().addListener((obs, oldText, newText) -> {
            String query = newText == null ? "" : newText.trim().toLowerCase(Locale.ROOT);
            filtered.setPredicate(product -> query.isEmpty()
                    || String.valueOf(product.getProductId()).contains(query)
                    || product.getProductName().toLowerCase(Locale.ROOT).contains(query));
            results.getSelectionModel().clearSelection();
        });
        results.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldProduct, product) -> selectedProduct.set(product));
        getChildren().addAll(new Label("Select a product"), searchField, results);
        results.setPrefHeight(150);
    }

    public Product getSelectedProduct() { return selectedProduct.get(); }
    public ObjectProperty<Product> selectedProductProperty() { return selectedProduct; }

    public void clearSelection() {
        results.getSelectionModel().clearSelection();
        searchField.clear();
    }

    public void setControlsDisabled(boolean disabled) {
        searchField.setDisable(disabled);
        results.setDisable(disabled);
    }
}
