package com.pms.ui;

import java.sql.SQLException;

import com.pms.Category;
import com.pms.Product;
import com.pms.Supplier;
import com.pms.operation;

import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The Update Product page: pick a product, edit it in the shared product
 * form, and save.
 *
 * The shell passes in its shared state: the product list for the picker, the
 * category and supplier lists for the dropdowns, and its refresh() (for
 * example this::refresh), which this page runs after a product is saved.
 *
 * The picker is a plain ComboBox for now. Replace it with P4's ProductPicker
 * once that is ready.
 */
public class UpdateProductPage extends VBox {

    private final ObservableList<Product> products;
    private final Runnable refresh;

    private final ComboBox<Product> picker;
    private final ProductForm form;
    private final Button saveButton;

    public UpdateProductPage(ObservableList<Product> products, ObservableList<Category> categories,
                             ObservableList<Supplier> suppliers, Runnable refresh) {
        this.products = products;
        this.refresh = refresh;

        picker = buildPicker();
        form = new ProductForm(categories, suppliers);
        saveButton = button("Save", "btn-update", this::save);

        VBox pickerCard = new VBox(picker);
        pickerCard.getStyleClass().add("card");

        HBox actions = new HBox(10, saveButton, button("Clear", "btn-neutral", this::clear));
        actions.getStyleClass().add("form-actions");

        VBox formCard = new VBox(form, actions);
        formCard.getStyleClass().add("card");

        setSpacing(8);
        getStyleClass().add("content");
        getChildren().addAll(sectionTitle("Select Product"), pickerCard,
                sectionTitle("Product Details"), formCard);

        // Picking a product fills the form; no selection empties and disables it
        picker.valueProperty().addListener((obs, old, p) -> showProduct(p));
        showProduct(null);
    }

    /**
     * Selects the product and loads it into the form. The list page calls this
     * on a double-click. This page cannot bring itself to the front, so the
     * shell must also switch to it (showPage).
     */
    public void openUpdate(Product p) {
        if (p == null) {
            select(null);
            return;
        }
        // Prefer the copy in the shared list, so the picker shows it as selected
        Product listed = findById(p.getProductId());
        select(listed != null ? listed : p);
    }

    private void save() {
        Product p = form.readProduct();   // shows its own error and returns null if invalid
        if (p == null) {
            return;
        }
        try {
            new operation(p).update();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "The product could not be updated.\n" + e.getMessage());
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Product updated.");
        refresh.run();
        // refresh() replaces the objects in the list, so pick the saved product again
        select(findById(p.getProductId()));
    }

    private void clear() {
        select(null);
    }

    private void select(Product p) {
        if (picker.getValue() == p) {
            showProduct(p);        // the picker's listener only runs when the value changes
        } else {
            picker.setValue(p);
        }
    }

    private void showProduct(Product p) {
        boolean none = p == null;
        if (none) {
            form.clear();
        } else {
            form.setProduct(p);
        }
        form.setDisable(none);
        saveButton.setDisable(none);
    }

    private Product findById(int productId) {
        for (Product p : products) {
            if (p.getProductId() == productId) {
                return p;
            }
        }
        return null;
    }

    private ComboBox<Product> buildPicker() {
        ComboBox<Product> box = new ComboBox<>(products);
        box.setPromptText("Select a product to update");
        box.setMaxWidth(Double.MAX_VALUE);
        // Product has no toString(), so the cells build the "ID - Name" text
        box.setCellFactory(list -> new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : describe(item));
            }
        });
        // Keeps the prompt text visible after the selection is cleared
        box.setButtonCell(new ListCell<Product>() {
            @Override
            protected void updateItem(Product item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? box.getPromptText() : describe(item));
            }
        });
        return box;
    }

    private static String describe(Product p) {
        return p.getProductId() + " - " + p.getProductName();
    }

    // The helpers below mirror the ones in ProductManagementApp. P2 is moving
    // those into ui/UiUtil.java; switch to that class once it exists.

    private static Label sectionTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-title");
        return label;
    }

    private static Button button(String text, String styleClass, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().addAll("btn", styleClass);
        button.setOnAction(e -> action.run());
        return button;
    }

    private static void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
