package nntc.tsvetkova.myapp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.control.Dialog;
import java.io.IOException;
import java.util.Optional;
import java.util.prefs.Preferences;
import javafx.scene.image.Image;

public class DesktopController {

    @FXML
    public DatePicker datePic;

    private Stage primaryStage; // Ссылка на главное окно
    private DatabaseManager primaryDatabaseManager; // Ссылка на главное окно

    // Метод для инициализации Stage из Application
    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setPrimaryDatabaseManager(DatabaseManager dm) {
        this.primaryDatabaseManager = dm;
    }

    public void handleMenuClose(ActionEvent event) {

        System.out.println("Метод handleMenuClose вызван.");

        // Проверяем, инициализирован ли Stage
        if (primaryStage != null) {
            boolean shouldClose = showCloseConfirmationDialog();
            if (shouldClose) {
                System.out.println("Пользователь подтвердил закрытие. Окно будет закрыто.");
                // Получаем настройки
                Preferences prefs = Preferences.userNodeForPackage(DesktopApplication.class);
                prefs.putDouble("windowX", primaryStage.getX());
                prefs.putDouble("windowY", primaryStage.getY());
                prefs.putDouble("windowWidth", primaryStage.getWidth());
                prefs.putDouble("windowHeight", primaryStage.getHeight());

                if (primaryDatabaseManager != null) {
                    this.primaryDatabaseManager.disconnect();
                }

                primaryStage.close(); // Закрытие окна
            } else {
                System.out.println("Пользователь отменил закрытие.");
            }
        } else {
            System.err.println("Stage не был установлен!");
        }
    }

    private boolean showCloseConfirmationDialog() {
        // Создаем диалог подтверждения
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подтверждение закрытия");
        alert.setHeaderText("Вы уверены, что хотите выйти?");
        alert.setContentText("Все несохраненные данные будут потеряны.");

        Image icon = new Image(getClass().getResourceAsStream("/icon.png")); // Укажите путь к вашей иконке
        Stage alertStage = (Stage) alert.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(icon);

        // Ожидание ответа пользователя
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    public void showinfoWindow() throws IOException {
        // Загружаем FXML файл для окна справки
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("info-view.fxml"));
        VBox infoContent = fxmlLoader.load(); // Загружаем содержимое окна справки

        // Создаем диалоговое окно
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("О программе");
        
        dialog.getDialogPane().setContent(infoContent); // Добавляем содержимое в диалоговое окно

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        // Обработчик закрытия окна
        stage.setOnCloseRequest(event -> {
            System.out.println("Закрытие окна справки...");
            dialog.close(); // Закрыть диалог
        });

        // Показываем диалог в модальном режиме
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

    public void showProductsWindow(ActionEvent actionEvent) throws IOException {
            // Загружаем FXML файл для окна справки
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("products-view.fxml"));

            VBox infoContent = fxmlLoader.load(); // Загружаем содержимое окна справки

            ProductsController controller = fxmlLoader.getController();
            controller.setPrimaryDatabaseManager(primaryDatabaseManager);

            // Создаем диалоговое окно
            Dialog<Void> dialog = new Dialog<>();
            dialog.setTitle("Управление товарами");

            dialog.getDialogPane().setContent(infoContent); // Добавляем содержимое в диалоговое окно

            Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));


            // Обработчик закрытия окна
            stage.setOnCloseRequest(event -> {
                System.out.println("Закрытие окна с товарами...");
                dialog.close(); // Закрыть диалог
            });

            // Показываем диалог в модальном режиме
            dialog.showAndWait();
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

    public void showWorkersWindow(ActionEvent actionEvent) throws IOException {
        // Загружаем FXML файл для окна справки
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("workers.fxml"));

        VBox infoContent = fxmlLoader.load(); // Загружаем содержимое окна справки

        WorkerController controller = fxmlLoader.getController();
        controller.setPrimaryDatabaseManager(primaryDatabaseManager);

        // Создаем диалоговое окно
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Управление сотрудниками");

        dialog.getDialogPane().setContent(infoContent); // Добавляем содержимое в диалоговое окно

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        // Обработчик закрытия окна
        stage.setOnCloseRequest(event -> {
            System.out.println("Закрытие окна с сотрудниками...");
            dialog.close(); // Закрыть диалог
        });

        // Показываем диалог в модальном режиме
        dialog.showAndWait();
    }

    public void showAppointmentWindow(ActionEvent actionEvent) throws IOException {
        // Загружаем FXML файл для окна справки
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("appointment.fxml"));

        VBox infoContent = fxmlLoader.load(); // Загружаем содержимое окна справки

        AppointmentController controller = fxmlLoader.getController();
        controller.setPrimaryDatabaseManager(primaryDatabaseManager);

        // Создаем диалоговое окно
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Управление записями");

        dialog.getDialogPane().setContent(infoContent);

        Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));

        // Обработчик закрытия окна
        stage.setOnCloseRequest(event -> {
            System.out.println("Закрытие окна с покупателями...");
            dialog.close();
        });

        dialog.showAndWait();
    }

    public void onDateSelected(ActionEvent event) {
        System.out.println("Date selected");
        System.out.println(datePic.getValue());
    }
}