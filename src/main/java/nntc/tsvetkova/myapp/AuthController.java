package nntc.tsvetkova.myapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import java.util.prefs.Preferences;

public class AuthController {

    @FXML
    private TextField loginNameField;
    @FXML
    private TextField loginEmailField;
    @FXML
    private PasswordField loginPasswordField;
    @FXML
    private ComboBox<String> loginRoleComboBox;

    @FXML
    private TextField registerNameField;
    @FXML
    private TextField registerEmailField;
    @FXML
    private PasswordField registerPasswordField;
    @FXML
    private ComboBox<String> registerRoleComboBox;

    private DatabaseManager databaseManager;
    private DesktopController desktopController;

    private Stage primaryStage;
    private DatabaseManager primaryDatabaseManager;

    public void setPrimaryStage(Stage stage) {
        this.primaryStage = stage;
    }

    public void setPrimaryDatabaseManager(DatabaseManager dm) {
        this.primaryDatabaseManager = dm;
    }

    public void setDatabaseManager(DatabaseManager dm) {
        this.databaseManager = dm;
    }

    public void setDesktopController(DesktopController dc) {
        this.desktopController = dc;
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

    @FXML
    private void handleLogin() throws IOException {
        String name = loginNameField.getText().trim();
        String password = loginPasswordField.getText().trim();
        String email = loginEmailField.getText().trim();
        String role = loginRoleComboBox.getValue();

        User authenticatedUser = databaseManager.authenticateCustomer(name, email, password, role);

        if (authenticatedUser != null) {
            ((Stage) loginNameField.getScene().getWindow()).close(); // Закрыть окно аутентификации

            if ("admin".equals(authenticatedUser.getRole())) {
                desktopController.showMainAdminWindow();
            } else if ("client".equals(authenticatedUser.getRole())) {
                desktopController.showMainCustomerWindow();
            }
        } else {
            showAlert("Ошибка авторизации", "Неверные данные.");
        }
    }

    @FXML
    private void handleRegistration() {
        String name = registerNameField.getText();
        String email = registerEmailField.getText();
        String password = registerPasswordField.getText();
        String role = registerRoleComboBox.getValue();

        if (name.isEmpty() | email.isEmpty() | password.isEmpty() | Objects.isNull(role)) {
            showAlert("Ошибка", "Все поля должны быть заполнены.");
            return;
        }

        boolean isRegistered = databaseManager.registerUser(name, email, password, role);

        if (isRegistered) {
            showAlert("Успех", "Регистрация прошла успешно! Теперь вы можете войти.");
            registerNameField.clear();
            registerPasswordField.clear();
        } else {
            showAlert("Ошибка", "Пользователь с таким логином уже существует.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
