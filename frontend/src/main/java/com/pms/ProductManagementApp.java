package com.pms;

import com.pms.ui.ProductListPage;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ProductManagementApp extends Application {

    private final ObservableList<Object> products =
            FXCollections.observableArrayList();

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

        if (pageName.equals(
                "All Products")) {

            ProductListPage productListPage =
                    new ProductListPage();

            root.setCenter(
                    productListPage
            );

        } else {

            VBox content =
                    new VBox(20);

            content.setPadding(
                    new Insets(30)
            );

            Label label =
                    new Label(pageName);

            label.setStyle(
                    "-fx-font-size: 24px;" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #eaf6ff;"
            );

            content.getChildren()
                    .add(label);

            root.setCenter(
                    content
            );
        }
    }

    // ================= SHARED DATA =================

    public ObservableList<Object> getProducts() {

        return products;
    }

    public void refresh() {

        /*
         * Backend connection will be added
         * when P1's backend is connected.
         */
    }

    // ================= UPDATE =================

    public void openUpdate(
            Object product) {

        pageTitle.setText(
                "Update Product"
        );

        pageSubtitle.setText(
                "Modify existing product details"
        );

        VBox content =
                new VBox(20);

        content.setPadding(
                new Insets(30)
        );

        Label label =
                new Label("Update Product");

        label.setStyle(
                "-fx-font-size: 24px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #eaf6ff;"
        );

        content.getChildren()
                .add(label);

        root.setCenter(
                content
        );
    }

    // ================= MAIN =================

    public static void main(
            String[] args) {

        launch(args);
    }
}