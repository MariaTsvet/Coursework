package nntc.tsvetkova.myapp;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;

public class MainAdminController {

    @FXML
    private TableView<Order> tableView;
    @FXML
    private TableColumn<Order, Integer> idColumn;
    @FXML
    public TableColumn<Order, String> serviceColumn;
    @FXML
    public TableColumn<Order, String> masterColumn;
    @FXML
    public TableColumn<Order, String> customerColumn;
    @FXML
    public TableColumn<Order, String> timeColumn;
    @FXML
    public TableColumn<Order, String> dateColumn;

    @FXML
    public TextField fieldID;

    @FXML
    public Button btnEdit;

    @FXML
    public Button btnDelete;

    private Boolean disableEditOrDeleteBtnsFlag = true;

    private DatabaseManager primaryDatabaseManager;

    public MainAdminController(DatabaseManager databaseManager) {
        this.primaryDatabaseManager = databaseManager;
    }

    private boolean showConfirmationDialog(String message) {
        // Создаем диалог подтверждения
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение действия");
        alert.setHeaderText(message);
        // alert.setContentText("Все несохраненные данные будут потеряны.");

        // Ожидание ответа пользователя
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    @FXML
    public void updateTable() {
        ObservableList<Order> data = primaryDatabaseManager.orderFetchData();
        idColumn.setCellValueFactory(new PropertyValueFactory<Order, Integer>("id"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("date"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("time"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("customer"));
        serviceColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("service"));
        masterColumn.setCellValueFactory(new PropertyValueFactory<Order, String>("master"));
        tableView.setItems(data);
        //fieldID.clear();
    }

    @FXML
    public void addRow() {
        // открываем форму для нового заказа
        try {
            showOrderWindow(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fieldID.clear();
    }

    @FXML
    public void editRow() {
        try {
            showOrderWindow(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        fieldID.clear();
    }

    @FXML
    public void deleteRow() {
        if (showConfirmationDialog(String.format("Действительно удалить заказ №%s", fieldID.getText()))) {
            primaryDatabaseManager.orderDeleteData(Integer.parseInt(fieldID.getText()));
            updateTable();
            fieldID.clear();
        }
    }

    @FXML
    private void onRowClick(MouseEvent event) {
        if (event.getClickCount() == 1) {  // Обработка одиночного клика
            Order selectedOrder = tableView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                fieldID.setText(String.format("%d", selectedOrder.getId()));
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
            disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty());
            btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
            btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
        });

    }




    public void showCustomersWindow(ActionEvent actionEvent) throws IOException {
        // Загружаем FXML файл для окна справки
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("customers-view.fxml"));

        VBox infoContent = fxmlLoader.load(); // Загружаем содержимое окна справки

        CustomersController controller = fxmlLoader.getController();
        controller.setPrimaryDatabaseManager(primaryDatabaseManager);

        // Создаем диалоговое окно
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Управление покупателями");

        dialog.getDialogPane().setContent(infoContent); // Добавляем содержимое в диалоговое окно

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        // Обработчик закрытия окна
        stage.setOnCloseRequest(event -> {
            System.out.println("Закрытие окна с покупателями...");
            dialog.close(); // Закрыть диалог
        });

        // Показываем диалог в модальном режиме
        dialog.showAndWait();
    }



    public void showOrderWindow(boolean modeEdit) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("order-view.fxml"));

        VBox infoContent = fxmlLoader.load();

        OrderController controller = fxmlLoader.getController();
        controller.setPrimaryDatabaseManager(primaryDatabaseManager);


        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Управление записями");

        dialog.getDialogPane().setContent(infoContent);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        stage.setOnCloseRequest(event -> {
            System.out.println("Закрытие окна с покупателями...");
            dialog.close();
        });

        dialog.showAndWait();
    }




    public void showLoginWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login-view.fxml"));
        VBox infoContent = fxmlLoader.load();

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Настройки доступа к СУБД");
        dialog.getDialogPane().setContent(infoContent);
        Stage stageLogin = (Stage) dialog.getDialogPane().getScene().getWindow();
        stageLogin.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        stageLogin.setOnCloseRequest(event -> {
            System.out.println("Закрытие окна настроек доступа к СУБД...");
            dialog.close(); // Закрыть диалог
        });

        dialog.showAndWait();
    }


//    public void onDateSelected(ActionEvent event) {
//        System.out.println("Date selected");
//        System.out.println(datePic.getValue());
//    }
}
