package nntc.tsvetkova.myapp;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

public class AppointmentController {
        @FXML
        private TableView<Appointment> tableView;
        @FXML
        private TableColumn<Appointment, Integer> idAppointment;
        @FXML
        private TableColumn<Appointment, String> serviceName;
        @FXML
        private TableColumn<Appointment, String> clientnameColumn;
        @FXML
        private TableColumn<Appointment, String> masternameColumn;
        @FXML
        private TableColumn<Appointment, String> dateColumn;
        @FXML
        private TableColumn<Appointment, String> timeColumn;


        @FXML
        public TextField fieldAppointmentId;
        @FXML
        public TextField fieldClient;
        @FXML
        public TextField fieldService;
        @FXML
        public TextField fieldMasterName;
        @FXML
        public TextField fieldDate;
        @FXML
        public TextField fieldTime;

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
            ObservableList<Appointment> data = primaryDatabaseManager.appointmentFetchData();
            idAppointment.setCellValueFactory(new PropertyValueFactory<Appointment, Integer>("id"));
            serviceName.setCellValueFactory(new PropertyValueFactory<Appointment, String>("service"));
            clientnameColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("client"));
            masternameColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("master"));
            dateColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("date"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<Appointment, String>("time"));
            tableView.setItems(data);
        }

        @FXML
        public void addRow() {
            primaryDatabaseManager.appointmentInsertData(
                    fieldService.getText(),
                    fieldClient.getText(),
                    fieldMasterName.getText(),
                    fieldDate.getText(),
                    fieldTime.getText());
            updateTable();
            fieldAppointmentId.clear();
            fieldClient.clear();
            fieldService.clear();
            fieldMasterName.clear();
            fieldDate.clear();
            fieldTime.clear();
        }

        @FXML
        public void editRow() {
            primaryDatabaseManager.appointmentUpdateData(fieldAppointmentId.getText(), fieldService.getText(), fieldClient.getText(), fieldMasterName.getText(), fieldDate.getText(), Integer.valueOf(fieldTime.getText()));
            updateTable();
            fieldAppointmentId.clear();
            fieldClient.clear();
            fieldService.clear();
            fieldMasterName.clear();
            fieldDate.clear();
            fieldTime.clear();
        }

        @FXML
        public void deleteRow() {
            if (showConfirmationDialog(String.format("Действительно удалить запись %s с ID=%s и имейлом %s?", fieldService.getText(), fieldClient.getText(), fieldMasterName.getText(), fieldDate.getText(), fieldTime.getText()))) {
                primaryDatabaseManager.appointmentDeleteData(Integer.parseInt(fieldAppointmentId.getText()));
                updateTable();
                fieldAppointmentId.clear();
                fieldClient.clear();
                fieldService.clear();
                fieldMasterName.clear();
                fieldDate.clear();
                fieldTime.clear();
            }
        }

        // Обработчик для клика по строкам в TableView
        @FXML
        private void onRowClick(MouseEvent event) {
            if (event.getClickCount() == 1) {  // Обработка одиночного клика
                Appointment selectedAppointment = tableView.getSelectionModel().getSelectedItem();
                if (selectedAppointment != null) {
                    fieldAppointmentId.setText(String.format("%d", selectedAppointment.getId()));
                   // fieldService.setText(selectedAppointment.getService());
                    fieldService.setText(String.format("%s", selectedAppointment.getService()));
                    fieldClient.setText(String.format("%s", selectedAppointment.getClient()));
                    fieldMasterName.setText(String.format("%s", selectedAppointment.getWorker()));
                    fieldDate.setText(String.format("%s", selectedAppointment.getDate()));
                    fieldTime.setText(String.format("%s", selectedAppointment.getTime()));
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

            fieldAppointmentId.textProperty().addListener((observable, oldValue, newValue) -> {
                // Если поле не пустое, активируем кнопку, иначе деактивируем
                disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldService.getText().isEmpty());
                btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
                btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
            });

            fieldService.textProperty().addListener((observable, oldValue, newValue) -> {
                // Если поле не пустое, активируем кнопку, иначе деактивируем
                disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldAppointmentId.getText().isEmpty());
                btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
                btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
            });

            fieldClient.textProperty().addListener((observable, oldValue, newValue) -> {
                // Если поле не пустое, активируем кнопку, иначе деактивируем
                disableEditOrDeleteBtnsFlag = (newValue.trim().isEmpty() && fieldAppointmentId.getText().isEmpty());
                btnEdit.setDisable(disableEditOrDeleteBtnsFlag);
                btnDelete.setDisable(disableEditOrDeleteBtnsFlag);
            });

            //FIX ME: Добавь Listener для всех полей

        }
    }


