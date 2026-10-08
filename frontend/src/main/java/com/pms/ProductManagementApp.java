package com.pms;

import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductManagementApp extends Application {

    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private FilteredList<Product> filteredProducts;

    private TextField idField;
    private TextField nameField;
    private TextField priceField;
    private TextField quantityField;
    private TextField searchField;
    private ComboBox<String> categoryBox;
    private TableView<Product> table;

    @Override
    public void start(Stage stage) {
        loadSampleData();
        filteredProducts = new FilteredList<>(products, p -> true);

        VBox content = new VBox(8,
                sectionTitle("Product Details"), buildFormCard(),
                sectionTitle("Search Products"), buildSearchCard(),
                sectionTitle("Available Products"), buildTable());
        content.getStyleClass().add("content");
        VBox.setVgrow(table, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.getStyleClass().add("root-pane");
        root.setTop(buildHeader());
        root.setCenter(content);

        Scene scene = new Scene(root, 920, 680);
        scene.getStylesheets().add(
                getClass().getResource("/css/styles.css").toExternalForm());

        stage.setTitle("Product Management System");
        stage.setMinWidth(760);
        stage.setMinHeight(560);
        stage.setScene(scene);
        stage.show();
    }

    // ---------------------------------------------------------------- UI

    private VBox buildHeader() {
        Label title = new Label("Product Management System");
        title.getStyleClass().add("header-title");

        Label subtitle = new Label("Manage your products efficiently");
        subtitle.getStyleClass().add("header-subtitle");

        VBox header = new VBox(4, title, subtitle);
        header.setAlignment(Pos.CENTER);
        header.getStyleClass().add("header");
        return header;
    }

    private VBox buildFormCard() {
        idField = textField("Enter product ID");
        nameField = textField("Enter product name");
        priceField = textField("Enter price");
        quantityField = textField("Enter quantity");

        categoryBox = new ComboBox<>(FXCollections.observableArrayList(
                "Computer", "Accessory", "Electronics", "Audio", "Mobile", "Other"));
        categoryBox.setPromptText("Select category");
        categoryBox.setMaxWidth(Double.MAX_VALUE);
        // Keeps the prompt text visible after the selection is cleared
        categoryBox.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? categoryBox.getPromptText() : item);
            }
        });

        GridPane grid = new GridPane();
        grid.getStyleClass().add("form-grid");
        grid.getColumnConstraints().addAll(
                labelColumn(), fieldColumn(), labelColumn(), fieldColumn());

        grid.addRow(0, formLabel("Product ID:"), idField, formLabel("Product Name:"), nameField);
        grid.addRow(1, formLabel("Category:"), categoryBox, formLabel("Price:"), priceField);
        grid.addRow(2, formLabel("Quantity:"), quantityField);

        HBox actions = new HBox(10,
                button("Add Product", "btn-add", this::addProduct),
                button("Update Product", "btn-update", this::updateProduct),
                button("Delete Product", "btn-delete", this::deleteProduct),
                button("Clear", "btn-neutral", this::clearForm));
        actions.getStyleClass().add("form-actions");

        VBox card = new VBox(grid, actions);
        card.getStyleClass().add("card");
        return card;
    }

    private HBox buildSearchCard() {
        searchField = textField("Search by Product ID or Product Name");
        searchField.setOnAction(e -> applySearch());   // Enter key searches
        HBox.setHgrow(searchField, Priority.ALWAYS);

        HBox card = new HBox(10,
                searchField,
                button("Search", "btn-search", this::applySearch),
                button("Clear Search", "btn-neutral", () -> {
                    searchField.clear();
                    applySearch();
                }));
        card.setAlignment(Pos.CENTER_LEFT);
        card.getStyleClass().add("card");
        return card;
    }

    private TableView<Product> buildTable() {
        table = new TableView<>();

        SortedList<Product> sorted = new SortedList<>(filteredProducts);
        sorted.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted);

        TableColumn<Product, String> idCol = new TableColumn<>("Product ID");
        idCol.setCellValueFactory(c -> c.getValue().idProperty());

        TableColumn<Product, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory(c -> c.getValue().nameProperty());

        TableColumn<Product, String> categoryCol = new TableColumn<>("Category");
        categoryCol.setCellValueFactory(c -> c.getValue().categoryProperty());

        TableColumn<Product, Number> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c -> c.getValue().priceProperty());

        TableColumn<Product, Number> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(c -> c.getValue().quantityProperty());

        table.getColumns().addAll(List.of(idCol, nameCol, categoryCol, priceCol, qtyCol));
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        table.setPlaceholder(new Label("No products match your search."));

        // Clicking a row loads it into the form for editing
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, p) -> {
            if (p != null) fillForm(p);
        });
        return table;
    }

    // ----------------------------------------------------------- actions

    private void addProduct() {
        Product p = readForm();
        if (p == null) return;

        if (findById(p.getId()) != null) {
            showAlert(Alert.AlertType.ERROR, "Product ID " + p.getId() + " already exists.");
            return;
        }
        products.add(p);            // TODO: productDao.insert(p);
        clearForm();
        showAlert(Alert.AlertType.INFORMATION, "Product added.");
    }

    private void updateProduct() {
        Product p = readForm();
        if (p == null) return;

        Product existing = findById(p.getId());
        if (existing == null) {
            showAlert(Alert.AlertType.ERROR, "No product with ID " + p.getId() + ".");
            return;
        }
        existing.setName(p.getName());
        existing.setCategory(p.getCategory());
        existing.setPrice(p.getPrice());
        existing.setQuantity(p.getQuantity());   // TODO: productDao.update(existing);
        clearForm();
        showAlert(Alert.AlertType.INFORMATION, "Product updated.");
    }

    private void deleteProduct() {
        String id = idField.getText().trim();
        if (id.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Enter a product ID or select a row to delete.");
            return;
        }
        Product existing = findById(id);
        if (existing == null) {
            showAlert(Alert.AlertType.ERROR, "No product with ID " + id + ".");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + existing.getName() + " (" + existing.getId() + ")?",
                ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText(null);
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            products.remove(existing);   // TODO: productDao.delete(id);
            clearForm();
        }
    }

    private void applySearch() {
        String q = searchField.getText().trim().toLowerCase();
        filteredProducts.setPredicate(p -> q.isEmpty()
                || p.getId().toLowerCase().contains(q)
                || p.getName().toLowerCase().contains(q));
    }

    private void clearForm() {
        idField.clear();
        nameField.clear();
        priceField.clear();
        quantityField.clear();
        categoryBox.setValue(null);
        table.getSelectionModel().clearSelection();
    }

    // ----------------------------------------------------------- helpers

    /** Validates the form; returns null and shows an error if invalid. */
    private Product readForm() {
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String category = categoryBox.getValue();
        String priceText = priceField.getText().trim();
        String qtyText = quantityField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || category == null
                || priceText.isEmpty() || qtyText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Fill in all fields before saving.");
            return null;
        }

        double price;
        int quantity;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Price must be a number, for example 1499.0.");
            return null;
        }
        try {
            quantity = Integer.parseInt(qtyText);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Quantity must be a whole number.");
            return null;
        }
        if (price < 0 || quantity < 0) {
            showAlert(Alert.AlertType.ERROR, "Price and quantity can't be negative.");
            return null;
        }
        return new Product(id, name, category, price, quantity);
    }

    private void fillForm(Product p) {
        idField.setText(p.getId());
        nameField.setText(p.getName());
        categoryBox.setValue(p.getCategory());
        priceField.setText(String.valueOf(p.getPrice()));
        quantityField.setText(String.valueOf(p.getQuantity()));
    }

    private Product findById(String id) {
        return products.stream()
                .filter(p -> p.getId().equalsIgnoreCase(id))
                .findFirst()
                .orElse(null);
    }

    private void loadSampleData() {
        // TODO: replace with productDao.findAll() once JDBC is wired in
        products.addAll(
                new Product("P101", "Laptop", "Computer", 54999.0, 12),
                new Product("P102", "Keyboard", "Accessory", 1499.0, 40),
                new Product("P103", "Mouse", "Accessory", 799.0, 55),
                new Product("P104", "Monitor", "Electronics", 12999.0, 18),
                new Product("P105", "Headphones", "Audio", 2499.0, 30));
    }

    private Label sectionTitle(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("section-title");
        return label;
    }

    private Label formLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("form-label");
        return label;
    }

    private TextField textField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        return field;
    }

    private Button button(String text, String styleClass, Runnable action) {
        Button button = new Button(text);
        button.getStyleClass().addAll("btn", styleClass);
        button.setOnAction(e -> action.run());
        return button;
    }

    private ColumnConstraints labelColumn() {
        ColumnConstraints c = new ColumnConstraints();
        c.setMinWidth(100);
        c.setPrefWidth(100);
        return c;
    }

    private ColumnConstraints fieldColumn() {
        ColumnConstraints c = new ColumnConstraints();
        c.setHgrow(Priority.ALWAYS);
        c.setFillWidth(true);
        return c;
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
