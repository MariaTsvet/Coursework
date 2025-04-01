package nntc.tsvetkova.myapp;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.util.Optional;

public class ServiceController {
    @FXML
    private TableView<Service> tableView;
    @FXML
    private TableColumn<Service, Integer> idColumn;
    @FXML
    private TableColumn<Service, String> nameColumn;
    @FXML
    private TableColumn<Service, Float> priceColumn;

    @FXML
    public TextField fieldID;
    @FXML
    public TextField fieldName;
    @FXML
    public TextField fieldPrice;

    @FXML
    public Button btnEdit;
    @FXML
    public Button btnDelete;

    private Boolean disableEditOrDeleteBtnsFlag = true;

    private Stage primaryStage;
    private DatabaseManager primaryDatabaseManager;

    public void setPrimaryDatabaseManager(DatabaseManager dm) {
        this.primaryDatabaseManager = dm;
    }

    private boolean showConfirmationDialog(String message) {
        // Создаем диалог подтверждения
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение действия");
        alert.setHeaderText(message);
        alert.setContentText("Все несохраненные данные будут потеряны.");

        // Ожидание ответа пользователя
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    public void updateTable() {
        ObservableList<Service> data = primaryDatabaseManager.serviceFetchData();
        idColumn.setCellValueFactory(new PropertyValueFactory<Service, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Service, String>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<Service, Float>("price"));
        tableView.setItems(data);
    }

    @FXML
    public void addRow() {
        primaryDatabaseManager.serviceInsertData(fieldName.getText(), Float.parseFloat(fieldPrice.getText().trim()));
        updateTable();
        fieldID.clear();
        fieldName.clear();
        fieldPrice.clear();
    }

    @FXML
    public void editRow() {
        primaryDatabaseManager.serviceUpdateData(Integer.parseInt(fieldID.getText()), fieldName.getText(), Float.parseFloat(fieldPrice.getText().trim()));
        updateTable();
        fieldID.clear();
        fieldName.clear();
        fieldPrice.clear();
    }

    @FXML
    public void deleteRow() {
        if (showConfirmationDialog(String.format("Действительно удалить запись %s с ID=%s и имейлом %s?", fieldName.getText(), fieldID.getText(), Float.parseFloat(fieldPrice.getText().trim())))) {
            primaryDatabaseManager.serviceDeleteData(Integer.parseInt(fieldID.getText()));
            updateTable();
            fieldID.clear();
            fieldName.clear();
            fieldPrice.clear();
        }
    }

    // Обработчик для клика по строкам в TableView
    @FXML
    private void onRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {  // Обработка одиночного клика
            Service selectedService = tableView.getSelectionModel().getSelectedItem();
            if (selectedService != null) {
                fieldID.setText(String.format("%d", selectedService.getId()));
                fieldName.setText(selectedService.getName());
                fieldPrice.setText(String.format("%s", selectedService.getPrice()));
            }
        }
    }

    // Запустить updateTable() сразу после отрисовки fxml-разметки
    @FXML
    private void initialize() {
        Platform.runLater(() -> {
            System.out.println("TableView был отрисован.");
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
            updateTable();
        });

        // Добавляем слушатель на изменение текста в TextField
        fieldID.textProperty().addListener((observable, oldValue, newValue) -> {
            // Если поле не пустое, активируем кнопку, иначе деактивируем
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldName.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

        fieldName.textProperty().addListener((observable, oldValue, newValue) -> {
            // Если поле не пустое, активируем кнопку, иначе деактивируем
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldID.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

        fieldPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            // Если поле не пустое, активируем кнопку, иначе деактивируем
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldID.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

    }
}
