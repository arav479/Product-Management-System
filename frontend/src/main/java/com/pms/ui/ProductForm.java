package com.pms.ui;

import java.math.BigDecimal;

import com.pms.Category;
import com.pms.Product;
import com.pms.Supplier;

import javafx.collections.ObservableList;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * The product form shared by the Add and Update pages.
 *
 * It has no ID field: the database generates the ID for a new product, and
 * for an existing one the form remembers the ID given to setProduct().
 */
public class ProductForm extends GridPane {

    private static final String DEFAULT_REORDER_LEVEL = "5";

    // Limits from the PRODUCT table in schema.sql
    private static final int MAX_NAME_LENGTH = 100;
    private static final int MAX_DESCRIPTION_LENGTH = 500;
    private static final BigDecimal MAX_PRICE = new BigDecimal("99999999.99");   // NUMBER(10,2)

    private final TextField nameField = textField("Enter product name");
    private final TextArea descriptionArea = new TextArea();
    private final ComboBox<Category> categoryBox;
    private final ComboBox<Supplier> supplierBox;
    private final TextField priceField = textField("Enter price");
    private final TextField quantityField = textField("Enter quantity");
    private final TextField reorderLevelField = textField("Enter reorder level");

    // ID of the product being edited; 0 while the form holds a new product
    private int productId;

    public ProductForm(ObservableList<Category> categories, ObservableList<Supplier> suppliers) {
        categoryBox = comboBox(categories, "Select category");
        supplierBox = comboBox(suppliers, "Select supplier");

        descriptionArea.setPromptText("Enter description (optional)");
        descriptionArea.setPrefRowCount(3);
        descriptionArea.setWrapText(true);

        reorderLevelField.setText(DEFAULT_REORDER_LEVEL);

        getStyleClass().add("form-grid");
        getColumnConstraints().addAll(labelColumn(), fieldColumn(), labelColumn(), fieldColumn());

        Label descriptionLabel = formLabel("Description:");
        GridPane.setValignment(descriptionLabel, VPos.TOP);

        addRow(0, formLabel("Product Name:"), nameField, formLabel("Category:"), categoryBox);
        addRow(1, formLabel("Price:"), priceField, formLabel("Supplier:"), supplierBox);
        addRow(2, formLabel("Quantity:"), quantityField, formLabel("Reorder Level:"), reorderLevelField);
        add(descriptionLabel, 0, 3);
        add(descriptionArea, 1, 3, 3, 1);   // spans the remaining three columns
    }

    /** Fills the form with the product and selects its category and supplier by ID. */
    public void setProduct(Product p) {
        if (p == null) {
            clear();
            return;
        }
        productId = p.getProductId();
        nameField.setText(p.getProductName());
        descriptionArea.setText(p.getDescription());
        categoryBox.setValue(findCategory(p.getCategoryId()));
        supplierBox.setValue(findSupplier(p.getSupplierId()));
        priceField.setText(BigDecimal.valueOf(p.getPrice()).stripTrailingZeros().toPlainString());
        quantityField.setText(String.valueOf(p.getQuantity()));
        reorderLevelField.setText(String.valueOf(p.getReorderLevel()));
    }

    /**
     * Validates the form and returns its contents as a Product.
     * Shows an error and returns null if something is wrong.
     */
    public Product readProduct() {
        String name = text(nameField);
        String description = text(descriptionArea);
        Category category = categoryBox.getValue();
        Supplier supplier = supplierBox.getValue();

        if (name.isEmpty()) {
            return invalid(nameField, "Product name is required.");
        }
        if (name.length() > MAX_NAME_LENGTH) {
            return invalid(nameField, "Product name can be at most " + MAX_NAME_LENGTH
                    + " characters (it has " + name.length() + ").");
        }
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            return invalid(descriptionArea, "Description can be at most " + MAX_DESCRIPTION_LENGTH
                    + " characters (it has " + description.length() + ").");
        }
        if (category == null) {
            return invalid(categoryBox, "Select a category.");
        }
        if (supplier == null) {
            return invalid(supplierBox, "Select a supplier.");
        }

        BigDecimal price;
        try {
            price = new BigDecimal(text(priceField));
        } catch (NumberFormatException e) {
            return invalid(priceField, "Price must be a number, for example 1499.50.");
        }
        if (price.signum() <= 0) {
            return invalid(priceField, "Price must be greater than 0.");
        }
        if (price.stripTrailingZeros().scale() > 2) {
            return invalid(priceField, "Price can have at most 2 decimal places.");
        }
        if (price.compareTo(MAX_PRICE) > 0) {
            return invalid(priceField, "Price can be at most " + MAX_PRICE.toPlainString() + ".");
        }

        Integer quantity = wholeNumber(quantityField);
        if (quantity == null) {
            return invalid(quantityField, "Quantity must be a whole number, 0 or more.");
        }
        Integer reorderLevel = wholeNumber(reorderLevelField);
        if (reorderLevel == null) {
            return invalid(reorderLevelField, "Reorder level must be a whole number, 0 or more.");
        }

        Product p = new Product(productId, name, description,
                supplier.getSupplierId(), category.getCategoryId(),
                quantity, reorderLevel, price.doubleValue());
        p.setCategoryName(category.getCategoryName());
        p.setSupplierName(supplier.getSupplierName());
        return p;
    }

    /** Empties the form and puts the reorder level back to its default. */
    public void clear() {
        productId = 0;
        nameField.clear();
        descriptionArea.clear();
        // The button cells set up in comboBox() bring the prompt text back
        categoryBox.setValue(null);
        supplierBox.setValue(null);
        priceField.clear();
        quantityField.clear();
        reorderLevelField.setText(DEFAULT_REORDER_LEVEL);
    }

    // ----------------------------------------------------------- helpers

    private Category findCategory(int categoryId) {
        for (Category c : categoryBox.getItems()) {
            if (c.getCategoryId() == categoryId) {
                return c;
            }
        }
        return null;
    }

    private Supplier findSupplier(int supplierId) {
        for (Supplier s : supplierBox.getItems()) {
            if (s.getSupplierId() == supplierId) {
                return s;
            }
        }
        return null;
    }

    /** The field's text without surrounding spaces; never null. */
    private static String text(TextInputControl field) {
        String value = field.getText();
        return value == null ? "" : value.trim();
    }

    /** The field's value as a whole number of 0 or more, or null if it isn't one. */
    private static Integer wholeNumber(TextField field) {
        try {
            int value = Integer.parseInt(text(field));
            return value >= 0 ? value : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Shows the error, puts the cursor in the field that caused it and returns null. */
    private static Product invalid(Node field, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
        field.requestFocus();
        return null;
    }

    private static <T> ComboBox<T> comboBox(ObservableList<T> items, String prompt) {
        ComboBox<T> box = new ComboBox<>(items);
        box.setPromptText(prompt);
        box.setMaxWidth(Double.MAX_VALUE);
        // Keeps the prompt text visible after the selection is cleared
        box.setButtonCell(new ListCell<T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? box.getPromptText() : item.toString());
            }
        });
        return box;
    }

    // The helpers below mirror the ones in ProductManagementApp. P2 is moving
    // those into ui/UiUtil.java; switch to that class once it exists.

    private static Label formLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("form-label");
        return label;
    }

    private static TextField textField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    private static ColumnConstraints labelColumn() {
        ColumnConstraints c = new ColumnConstraints();
        c.setMinWidth(100);
        c.setPrefWidth(100);
        return c;
    }

    private static ColumnConstraints fieldColumn() {
        ColumnConstraints c = new ColumnConstraints();
        c.setHgrow(Priority.ALWAYS);
        c.setFillWidth(true);
        return c;
    }
}
