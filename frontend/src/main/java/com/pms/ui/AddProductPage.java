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
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * The Add Product page: the shared product form and a Save button.
 *
 * The shell passes in its shared state: the category and supplier lists for
 * the dropdowns, and its refresh() (for example this::refresh), which this
 * page runs after a product is saved so the product list reloads.
 */
public class AddProductPage extends VBox {

    private final ProductForm form;
    private final Runnable refresh;

    public AddProductPage(ObservableList<Category> categories, ObservableList<Supplier> suppliers,
                          Runnable refresh) {
        this.form = new ProductForm(categories, suppliers);
        this.refresh = refresh;

        HBox actions = new HBox(10, button("Save", "btn-add", this::save));
        actions.getStyleClass().add("form-actions");

        VBox card = new VBox(form, actions);
        card.getStyleClass().add("card");

        setSpacing(8);
        getStyleClass().add("content");
        getChildren().addAll(sectionTitle("Add Product"), card);
    }

    private void save() {
        Product p = form.readProduct();   // shows its own error and returns null if invalid
        if (p == null) {
            return;
        }
        try {
            new operation(p).add();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "The product could not be added.\n" + e.getMessage());
            return;
        }
        showAlert(Alert.AlertType.INFORMATION, "Product added.");
        form.clear();
        refresh.run();
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
