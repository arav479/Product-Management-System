package com.pms;

import com.pms.ui.AddProductPage;
import com.pms.ui.DeleteProductPage;
import com.pms.ui.ProductListPage;
import com.pms.ui.UpdateProductPage;

import java.util.List;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductManagementApp extends Application {

    // Shared with the Add/Update/Delete pages; refresh() reloads them from the database
    private final ObservableList<Product> products =
            FXCollections.observableArrayList();

    private final ObservableList<Category> categories =
            FXCollections.observableArrayList();

    private final ObservableList<Supplier> suppliers =
            FXCollections.observableArrayList();

    private Button updateButton;

    private UpdateProductPage updatePage;

    private final BorderPane root =
            new BorderPane();

    private final Label pageTitle =
            new Label("All Products");

    private final Label pageSubtitle =
            new Label("Manage and monitor your inventory");

    private final VBox sidebar =
            new VBox(10);

    private Button activeButton;

    @Override
    public void start(Stage stage) {

        root.setTop(createHeader());

        root.setLeft(createSidebar());

        Scene scene =
                new Scene(root, 1200, 700);

        scene.getStylesheets().add(
                getClass()
                        .getResource("/styles.css")
                        .toExternalForm()
        );

        stage.setTitle(
                "Product Management System"
        );

        stage.setScene(scene);

        // Allow ProductListPage to access this app
        stage.setUserData(this);

        stage.show();

        refresh();

        showInitialPage();
    }

    // ================= HEADER =================

    private VBox createHeader() {

        VBox header =
                new VBox(4);

        header.getStyleClass()
                .add("header");

        pageTitle.getStyleClass()
                .add("page-title");

        pageSubtitle.getStyleClass()
                .add("page-subtitle");

        header.getChildren().addAll(
                pageTitle,
                pageSubtitle
        );

        return header;
    }

    // ================= SIDEBAR =================

    private VBox createSidebar() {

        sidebar.setPadding(
                new Insets(25, 15, 25, 15)
        );

        sidebar.setAlignment(
                Pos.TOP_CENTER
        );

        sidebar.getStyleClass()
                .add("sidebar");

        // ---------- BRAND ----------

        Label logo =
                new Label("◈ PMS");

        logo.getStyleClass()
                .add("logo");

        Label brand =
                new Label("PRODUCT HUB");

        brand.getStyleClass()
                .add("brand-subtitle");

        VBox branding =
                new VBox(3);

        branding.setAlignment(
                Pos.CENTER_LEFT
        );

        branding.setPadding(
                new Insets(0, 10, 30, 10)
        );

        branding.getChildren().addAll(
                logo,
                brand
        );

        // ---------- NAVIGATION ----------

        Button allProducts =
                createNavButton(
                        "▣   All Products"
                );

        Button addProduct =
                createNavButton(
                        "＋   Add Product"
                );

        Button updateProduct =
                createNavButton(
                        "✎   Update Product"
                );

        updateButton = updateProduct;

        Button deleteProduct =
                createNavButton(
                        "×   Delete Product"
                );

        allProducts.setOnAction(
                event -> showPage(
                        "All Products",
                        "Manage and monitor your inventory",
                        allProducts
                )
        );

        addProduct.setOnAction(
                event -> showPage(
                        "Add Product",
                        "Create a new product entry",
                        addProduct
                )
        );

        updateProduct.setOnAction(
                event -> showPage(
                        "Update Product",
                        "Modify existing product details",
                        updateProduct
                )
        );

        deleteProduct.setOnAction(
                event -> showPage(
                        "Delete Product",
                        "Remove products from your inventory",
                        deleteProduct
                )
        );

        sidebar.getChildren().addAll(
                branding,
                allProducts,
                addProduct,
                updateProduct,
                deleteProduct
        );

        activeButton = allProducts;

        activeButton
                .getStyleClass()
                .add("nav-active");

        return sidebar;
    }

    // ================= NAV BUTTON =================

    private Button createNavButton(
            String text) {

        Button button =
                new Button(text);

        button.setMaxWidth(
                Double.MAX_VALUE
        );

        button.getStyleClass()
                .add("nav-button");

        return button;
    }

    // ================= INITIAL PAGE =================

    private void showInitialPage() {

        ProductListPage productListPage =
                new ProductListPage();

        root.setCenter(
                productListPage
        );
    }

    // ================= PAGE DISPLAY =================

    private void showPage(
            String pageName,
            String subtitle,
            Button selectedButton) {

        pageTitle.setText(
                pageName
        );

        pageSubtitle.setText(
                subtitle
        );

        if (activeButton != null) {

            activeButton
                    .getStyleClass()
                    .remove("nav-active");
        }

        selectedButton
                .getStyleClass()
                .add("nav-active");

        activeButton =
                selectedButton;

        switch (pageName) {

            case "Add Product":

                root.setCenter(
                        new AddProductPage(
                                categories,
                                suppliers,
                                this::refresh
                        )
                );
                break;

            case "Update Product":

                updatePage =
                        new UpdateProductPage(
                                products,
                                categories,
                                suppliers,
                                this::refresh
                        );

                root.setCenter(
                        updatePage
                );
                break;

            case "Delete Product":

                root.setCenter(
                        new DeleteProductPage(
                                products
                        )
                );
                break;

            default:

                root.setCenter(
                        new ProductListPage()
                );
        }
    }

    // ================= SHARED DATA =================

    public ObservableList<Product> getProducts() {

        return products;
    }

    /**
     * Reloads products, categories and suppliers from the database.
     * Runs off the JavaFX thread; the shared lists are updated when done.
     */
    public void refresh() {

        Task<Void> task =
                new Task<>() {

                    private List<Product> loadedProducts;
                    private List<Category> loadedCategories;
                    private List<Supplier> loadedSuppliers;

                    @Override
                    protected Void call() throws Exception {

                        loadedProducts = operation.findAll();
                        loadedCategories = operation.findAllCategories();
                        loadedSuppliers = operation.findAllSuppliers();
                        return null;
                    }

                    @Override
                    protected void succeeded() {

                        products.setAll(loadedProducts);
                        categories.setAll(loadedCategories);
                        suppliers.setAll(loadedSuppliers);
                    }
                };

        task.setOnFailed(event -> {

            Alert alert =
                    new Alert(
                            Alert.AlertType.ERROR,
                            "Could not load data from the database.\n"
                                    + task.getException().getMessage(),
                            ButtonType.OK
                    );

            alert.setHeaderText(null);

            alert.show();
        });

        Thread thread =
                new Thread(task);

        thread.setDaemon(true);

        thread.start();
    }

    // ================= UPDATE =================

    /** Called on a double-click in the product list. */
    public void openUpdate(
            ProductListPage.ProductRow row) {

        showPage(
                "Update Product",
                "Modify existing product details",
                updateButton
        );

        for (Product product : products) {

            if (product.getProductId() == row.getProductId()) {

                updatePage.openUpdate(product);
                return;
            }
        }
    }

    // ================= MAIN =================

    public static void main(
            String[] args) {

        launch(args);
    }
}