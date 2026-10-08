package com.pms.ui;

import com.pms.ProductManagementApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ProductListPage extends BorderPane {

    private final TableView<ProductRow> table =
            new TableView<>();

    private final ObservableList<ProductRow> products =
            FXCollections.observableArrayList();

    private final FilteredList<ProductRow> filteredProducts =
            new FilteredList<>(products, product -> true);

    private final TextField searchField =
            new TextField();

    private final ComboBox<String> categoryFilter =
            new ComboBox<>();

    public ProductListPage() {

        setPadding(new Insets(20));

        createSearchArea();

        createTable();

        table.setItems(filteredProducts);

        addLowStockHighlighting();

        loadProducts();
    }

    // ================= SEARCH + CATEGORY FILTER =================

    private void createSearchArea() {

        Label title =
                new Label("All Products");

        title.getStyleClass()
                .add("section-title");

        // SEARCH

        searchField.setPromptText(
                "Search by ID, name, category or supplier..."
        );

        searchField.setPrefWidth(400);

        searchField.textProperty().addListener(
                (observable, oldValue, newValue) ->
                        filterProducts()
        );

        // CATEGORY FILTER

        categoryFilter.setPromptText(
                "All Categories"
        );

        categoryFilter.setPrefWidth(180);

        categoryFilter.getItems().add(
                "All Categories"
        );

        categoryFilter.setValue(
                "All Categories"
        );

        categoryFilter.valueProperty().addListener(
                (observable, oldValue, newValue) ->
                        filterProducts()
        );

        // SEARCH AREA

        HBox searchBox =
                new HBox(15);

        searchBox.setPadding(
                new Insets(0, 0, 15, 0)
        );

        searchBox.getChildren().addAll(
                title,
                searchField,
                categoryFilter
        );

        VBox top =
                new VBox(10);

        top.getChildren().add(
                searchBox
        );

        setTop(top);
    }

    // ================= TABLE =================

    private void createTable() {

        // ID

        TableColumn<ProductRow, Integer> idColumn =
                new TableColumn<>("ID");

        idColumn.setCellValueFactory(
                new PropertyValueFactory<>("productId")
        );

        // NAME

        TableColumn<ProductRow, String> nameColumn =
                new TableColumn<>("Name");

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("productName")
        );

        // DESCRIPTION

        TableColumn<ProductRow, String> descriptionColumn =
                new TableColumn<>("Description");

        descriptionColumn.setCellValueFactory(
                new PropertyValueFactory<>("description")
        );

        // CATEGORY

        TableColumn<ProductRow, String> categoryColumn =
                new TableColumn<>("Category");

        categoryColumn.setCellValueFactory(
                new PropertyValueFactory<>("categoryName")
        );

        // SUPPLIER

        TableColumn<ProductRow, String> supplierColumn =
                new TableColumn<>("Supplier");

        supplierColumn.setCellValueFactory(
                new PropertyValueFactory<>("supplierName")
        );

        // PRICE

        TableColumn<ProductRow, Double> priceColumn =
                new TableColumn<>("Price");

        priceColumn.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );

        // QUANTITY

        TableColumn<ProductRow, Integer> quantityColumn =
                new TableColumn<>("Quantity");

        quantityColumn.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );

        // REORDER LEVEL

        TableColumn<ProductRow, Integer> reorderColumn =
                new TableColumn<>("Reorder Level");

        reorderColumn.setCellValueFactory(
                new PropertyValueFactory<>("reorderLevel")
        );

        // ADD ALL 8 COLUMNS

        table.getColumns().addAll(
                idColumn,
                nameColumn,
                descriptionColumn,
                categoryColumn,
                supplierColumn,
                priceColumn,
                quantityColumn,
                reorderColumn
        );

        // MAKE COLUMNS FILL TABLE WIDTH

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // EMPTY TABLE MESSAGE

        table.setPlaceholder(
                new Label("No products available")
        );

        // GIVE TABLE A VISIBLE HEIGHT

        table.setPrefHeight(500);

        // DOUBLE CLICK → UPDATE

        table.setOnMouseClicked(event -> {

            if (event.getClickCount() == 2
                    && !table.getSelectionModel()
                            .isEmpty()) {

                ProductRow selectedProduct =
                        table.getSelectionModel()
                                .getSelectedItem();

                if (selectedProduct != null) {

                    if (getScene() != null
                            && getScene().getWindow() != null) {

                        ProductManagementApp app =
                                (ProductManagementApp)
                                        getScene()
                                                .getWindow()
                                                .getUserData();

                        if (app != null) {

                            app.openUpdate(
                                    selectedProduct
                            );
                        }
                    }
                }
            }
        });

        setCenter(table);
    }

    // ================= LOW STOCK =================

    private void addLowStockHighlighting() {

        table.setRowFactory(tv -> {

            TableRow<ProductRow> row =
                    new TableRow<>();

            row.itemProperty().addListener(
                    (observable,
                     oldProduct,
                     newProduct) -> {

                        row.getStyleClass()
                                .remove("low-stock");

                        if (newProduct != null
                                && newProduct.getQuantity()
                                <= newProduct.getReorderLevel()) {

                            row.getStyleClass()
                                    .add("low-stock");
                        }
                    }
            );

            return row;
        });
    }

    // ================= SEARCH + CATEGORY =================

    private void filterProducts() {

        String search =
                searchField.getText();

        if (search == null) {
            search = "";
        }

        search =
                search.trim()
                        .toLowerCase();

        String selectedCategory =
                categoryFilter.getValue();

        if (selectedCategory == null) {
            selectedCategory =
                    "All Categories";
        }

        final String finalSearch =
                search;

        final String finalCategory =
                selectedCategory;

        filteredProducts.setPredicate(
                product -> {

                    boolean searchMatches;

                    if (finalSearch.isEmpty()) {

                        searchMatches = true;

                    } else {

                        searchMatches =
                                String.valueOf(
                                        product.getProductId()
                                )
                                .contains(finalSearch)

                                || product.getProductName()
                                .toLowerCase()
                                .contains(finalSearch)

                                || product.getDescription()
                                .toLowerCase()
                                .contains(finalSearch)

                                || product.getCategoryName()
                                .toLowerCase()
                                .contains(finalSearch)

                                || product.getSupplierName()
                                .toLowerCase()
                                .contains(finalSearch);
                    }

                    boolean categoryMatches =
                            finalCategory.equals(
                                    "All Categories"
                            )
                            || product.getCategoryName()
                                    .equalsIgnoreCase(
                                            finalCategory
                                    );

                    return searchMatches
                            && categoryMatches;
                }
        );
    }

    // ================= LOAD PRODUCTS =================

    private void loadProducts() {

        /*
         * Backend connection will be added
         * after P1 is completed.
         *
         * For now the table is intentionally empty.
         */
    }

    // ================= CATEGORY FILTER DATA =================

    private void updateCategoryFilter() {

        String currentCategory =
                categoryFilter.getValue();

        categoryFilter.getItems()
                .clear();

        categoryFilter.getItems()
                .add("All Categories");

        products.stream()
                .map(ProductRow::getCategoryName)
                .filter(category ->
                        category != null
                                && !category.isBlank()
                )
                .distinct()
                .sorted()
                .forEach(category ->
                        categoryFilter.getItems()
                                .add(category)
                );

        if (currentCategory != null
                && categoryFilter.getItems()
                        .contains(currentCategory)) {

            categoryFilter.setValue(
                    currentCategory
            );

        } else {

            categoryFilter.setValue(
                    "All Categories"
            );
        }
    }

    // ================= PRODUCT ROW =================

    public static class ProductRow {

        private final int productId;
        private final String productName;
        private final String description;
        private final String categoryName;
        private final String supplierName;
        private final double price;
        private final int quantity;
        private final int reorderLevel;

        public ProductRow(
                int productId,
                String productName,
                String description,
                String categoryName,
                String supplierName,
                double price,
                int quantity,
                int reorderLevel) {

            this.productId = productId;
            this.productName = productName;
            this.description = description;
            this.categoryName = categoryName;
            this.supplierName = supplierName;
            this.price = price;
            this.quantity = quantity;
            this.reorderLevel = reorderLevel;
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

        public String getCategoryName() {
            return categoryName;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public double getPrice() {
            return price;
        }

        public int getQuantity() {
            return quantity;
        }

        public int getReorderLevel() {
            return reorderLevel;
        }
    }
}