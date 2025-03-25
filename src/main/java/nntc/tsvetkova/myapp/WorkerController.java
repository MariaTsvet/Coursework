package nntc.tsvetkova.myapp;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

public class WorkerController {
    @FXML
    private TableView<Worker> tableView;
    @FXML
    private TableColumn<Worker, Integer> idColumn;
    @FXML
    private TableColumn<Worker, String> nameColumn;
    @FXML
    private TableColumn<Worker, String> emailColumn;

    @FXML
    public TextField fieldID;

    @FXML
    public TextField fieldName;

    @FXML
    public TextField fieldEmail;

    @FXML
    public Button btnEdit;

    @FXML
    public Button btnDelete;

    private Boolean disableEditOrDeleteBtnsFlag = true;


    // private Stage primaryStage; // Ссылка на главное окно
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
        ObservableList<Worker> data = primaryDatabaseManager.workerFetchData();
        idColumn.setCellValueFactory(new PropertyValueFactory<Worker, Integer>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<Worker, String>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<Worker, String>("email"));
        tableView.setItems(data);
    }

    @FXML
    public void addRow() {
        primaryDatabaseManager.workerInsertData(fieldName.getText(), fieldEmail.getText().trim());
        updateTable();
        fieldID.clear();
        fieldName.clear();
        fieldEmail.clear();
    }

    @FXML
    public void editRow() {
        primaryDatabaseManager.workerUpdateData(Integer.parseInt(fieldID.getText()), fieldName.getText(), fieldEmail.getText().trim());
        updateTable();
        fieldID.clear();
        fieldName.clear();
        fieldEmail.clear();
    }

    @FXML
    public void deleteRow() {
        if (showConfirmationDialog(String.format("Действительно удалить запись %s с ID=%s и имейлом %s?", fieldName.getText(), fieldID.getText(), fieldEmail.getText()))) {
            primaryDatabaseManager.workerDeleteData(Integer.parseInt(fieldID.getText()));
            updateTable();
            fieldID.clear();
            fieldName.clear();
            fieldEmail.clear();
        }
    }

    // Обработчик для клика по строкам в TableView
    @FXML
    private void onRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {  // Обработка одиночного клика
            Worker selectedWorker = tableView.getSelectionModel().getSelectedItem();
            if (selectedWorker != null) {
                fieldID.setText(String.format("%d", selectedWorker.getId()));
                fieldName.setText(selectedWorker.getName());
                fieldEmail.setText(String.format("%s", selectedWorker.getEmail()));
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

        fieldEmail.textProperty().addListener((observable, oldValue, newValue) -> {
            // Если поле не пустое, активируем кнопку, иначе деактивируем
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldID.getText().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

    }
}
