package com.pms.ui;

import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public final class UiUtil {

    private UiUtil() {
    }

    public static void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static Button button(String text) {

        Button button = new Button(text);

        button.getStyleClass()
                .add("action-button");

        return button;
    }

    public static TextField textField(String prompt) {

        TextField field =
                new TextField();

        field.setPromptText(prompt);

        field.setPadding(
                new Insets(8)
        );

        return field;
    }

    public static Label formLabel(String text) {

        Label label =
                new Label(text);

        label.getStyleClass()
                .add("form-label");

        return label;
    }

    public static <S, T> void setColumnWidth(
            TableColumn<S, T> column,
            double width) {

        column.setPrefWidth(width);
        column.setMinWidth(width);
    }
}