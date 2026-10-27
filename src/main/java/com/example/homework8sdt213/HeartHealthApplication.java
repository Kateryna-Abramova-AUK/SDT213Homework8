package com.example.homework8sdt213;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Kateryna
 * SDT 213 Introduction to Software Engineering
 * assignment is due by 11:59 pm on Tuesday, October 27, 2026
 * 2-3 hours
 * implementing the following functionalities of
 * the heart health imaging and monitoring system.
 * AI: was used to improve code structure, create styles, regex to validate emails
 */

public class HeartHealthApplication extends Application {

    // Blue button style used across all screens (matches the assignment mockups)
    private static final String BLUE_BUTTON =
            "-fx-background-color: #3b6bb0; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 25;";

    private Stage primaryStage;
    private Scene mainScene;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        primaryStage.setTitle("Heart Health Imaging and Recording System");

        buildMainView();
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    // Main View
    private void buildMainView() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        Label title = new Label("Welcome to Heart Health Imaging and Recording System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        Button btnIntake = new Button("Patient Intake");
        Button btnTech = new Button("CT Scan Tech View");
        Button btnPatient = new Button("Patient View");

        double btnWidth = 220;
        btnIntake.setPrefWidth(btnWidth);
        btnTech.setPrefWidth(btnWidth);
        btnPatient.setPrefWidth(btnWidth);
        for (Button b : new Button[]{btnIntake, btnTech, btnPatient}) {
            b.setPrefHeight(55);
            b.setStyle(BLUE_BUTTON);
        }

        btnIntake.setOnAction(e -> showPatientIntakeView());
        btnTech.setOnAction(e -> showTechView());
        btnPatient.setOnAction(e -> showPatientLookupDialog());

        root.getChildren().addAll(title, btnIntake, btnTech, btnPatient);
        mainScene = new Scene(root, 650, 480);
    }

    // Patient Intake Form
    private void showPatientIntakeView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));

        Label header = new Label("Patient Intake Form");
        header.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        BorderPane.setAlignment(header, Pos.CENTER);
        root.setTop(header);

        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(12);
        grid.setAlignment(Pos.CENTER);

        TextField tfFirstName = new TextField();
        TextField tfLastName = new TextField();
        TextField tfEmail = new TextField();
        TextField tfPhone = new TextField();
        TextField tfHistory = new TextField();
        TextField tfInsurance = new TextField();
        DatePicker dpExamDate = new DatePicker(); // receptionist schedules the exam date

        grid.add(new Label("First Name:"), 0, 0);
        grid.add(tfFirstName, 1, 0);
        grid.add(new Label("Last Name:"), 0, 1);
        grid.add(tfLastName, 1, 1);
        grid.add(new Label("Email:"), 0, 2);
        grid.add(tfEmail, 1, 2);
        grid.add(new Label("Phone Number:"), 0, 3);
        grid.add(tfPhone, 1, 3);
        grid.add(new Label("Health History:"), 0, 4);
        grid.add(tfHistory, 1, 4);
        grid.add(new Label("Insurance ID:"), 0, 5);
        grid.add(tfInsurance, 1, 5);
        grid.add(new Label("Exam Date:"), 0, 6);
        grid.add(dpExamDate, 1, 6);

        root.setCenter(grid);

        HBox buttonBar = new HBox(15);
        buttonBar.setAlignment(Pos.CENTER_RIGHT);
        Button btnBack = new Button("Back");
        Button btnSave = new Button("Save");
        btnSave.setStyle("-fx-background-color: #3b6bb0; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 25;");

        btnBack.setOnAction(e -> primaryStage.setScene(mainScene));
        btnSave.setOnAction(e -> {
            // All fields are required
            if (tfFirstName.getText().trim().isEmpty() || tfLastName.getText().trim().isEmpty()
                    || tfEmail.getText().trim().isEmpty() || tfPhone.getText().trim().isEmpty()
                    || tfHistory.getText().trim().isEmpty() || tfInsurance.getText().trim().isEmpty()
                    || dpExamDate.getValue() == null) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields and select an exam date.");
                return;
            }
            if (!tfEmail.getText().trim().matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Please enter a valid email address.");
                return;
            }
            if (dpExamDate.getValue().isBefore(LocalDate.now())) {
                showAlert(Alert.AlertType.WARNING, "Validation Error", "Exam date cannot be in the past.");
                return;
            }

            String patientId = HeartHealthSystem.generateUniquePatientId();
            Patient patient = new Patient(
                    patientId,
                    tfFirstName.getText().trim(),
                    tfLastName.getText().trim(),
                    tfEmail.getText().trim(),
                    tfPhone.getText().trim(),
                    tfHistory.getText().trim(),
                    tfInsurance.getText().trim()
            );
            // The receptionist schedules the CT exam for this patient
            CTExam exam = new CTExam(patientId, dpExamDate.getValue().toString(), CTExam.SCHEDULED);

            try {
                HeartHealthSystem.savePatientInfo(patient, exam);
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "Patient intake saved successfully!\nGenerated Patient ID: " + patientId +
                                "\nExam date: " + exam.getExamDate() + " (" + exam.getStatus() + ")" +
                                "\nFile created: " + patientId + "_PatientInfo.txt");
                primaryStage.setScene(mainScene);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "File Error", "Could not save patient file: " + ex.getMessage());
            }
        });

        buttonBar.getChildren().addAll(btnBack, btnSave);
        root.setBottom(buttonBar);

        primaryStage.setScene(new Scene(root, 650, 480));
    }

    // CT Scan Tech View
    private void showTechView() {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));

        VBox content = new VBox(15);
        content.setAlignment(Pos.TOP_LEFT);

        GridPane topGrid = new GridPane();
        topGrid.setHgap(15);
        topGrid.setVgap(10);

        TextField tfPatientId = new TextField();
        TextField tfTotalScore = new TextField();
        tfPatientId.setPrefWidth(220);
        tfTotalScore.setPrefWidth(220);

        topGrid.add(new Label("Patient ID:"), 0, 0);
        topGrid.add(tfPatientId, 1, 0);
        topGrid.add(new Label("The total Agatston CAC score"), 0, 1);
        topGrid.add(tfTotalScore, 1, 1);

        Label vesselHeader = new Label("Vessel level Agatston CAC score");
        vesselHeader.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        GridPane vesselGrid = new GridPane();
        vesselGrid.setHgap(15);
        vesselGrid.setVgap(8);

        TextField tfLM = new TextField();
        TextField tfLAD = new TextField();
        TextField tfLCX = new TextField();
        TextField tfRCA = new TextField();
        TextField tfPDA = new TextField();

        vesselGrid.add(new Label("LM:"), 0, 0);
        vesselGrid.add(tfLM, 1, 0);
        vesselGrid.add(new Label("LAD:"), 0, 1);
        vesselGrid.add(tfLAD, 1, 1);
        vesselGrid.add(new Label("LCX:"), 0, 2);
        vesselGrid.add(tfLCX, 1, 2);
        vesselGrid.add(new Label("RCA:"), 0, 3);
        vesselGrid.add(tfRCA, 1, 3);
        vesselGrid.add(new Label("PDA:"), 0, 4);
        vesselGrid.add(tfPDA, 1, 4);

        content.getChildren().addAll(topGrid, vesselHeader, vesselGrid);
        root.setCenter(content);

        HBox bottomBar = new HBox(15);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        Button btnBack = new Button("Back");
        Button btnSave = new Button("Save");
        btnSave.setStyle("-fx-background-color: #3b6bb0; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 8 25;");

        btnBack.setOnAction(e -> primaryStage.setScene(mainScene));
        btnSave.setOnAction(e -> {
            String pId = tfPatientId.getText().trim();
            if (pId.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please enter Patient ID.");
                return;
            }

            try {
                if (!HeartHealthSystem.validatePatientID(pId)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "wrong patient ID");
                    return;
                }

                TextField[] scoreFields = {tfTotalScore, tfLM, tfLAD, tfLCX, tfRCA, tfPDA};
                for (TextField tf : scoreFields) {
                    if (!tf.getText().trim().matches("\\d+")) {
                        showAlert(Alert.AlertType.WARNING, "Input Error",
                                "All CAC scores must be whole numbers (0 or greater).");
                        return;
                    }
                }

                CTScanResult result = new CTScanResult(
                        pId,
                        tfTotalScore.getText().trim(),
                        tfLM.getText().trim(),
                        tfLAD.getText().trim(),
                        tfLCX.getText().trim(),
                        tfRCA.getText().trim(),
                        tfPDA.getText().trim()
                );

                HeartHealthSystem.saveCTResults(result);
                showAlert(Alert.AlertType.INFORMATION, "Success",
                        "CT Scan Results saved successfully to " + pId + "CTResults.txt");
                primaryStage.setScene(mainScene);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "File Error", "Could not save scan results: " + ex.getMessage());
            }
        });

        bottomBar.getChildren().addAll(btnBack, btnSave);
        root.setBottom(bottomBar);

        primaryStage.setScene(new Scene(root, 650, 480));
    }

    private void showPatientLookupDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Patient Verification");
        dialog.setHeaderText("View Imaging Results");
        dialog.setContentText("Enter your 5-digit Patient ID:");

        dialog.showAndWait().ifPresent(id -> {
            String patientId = id.trim();
            if (!HeartHealthSystem.validatePatientID(patientId)) {
                showAlert(Alert.AlertType.ERROR, "Error", "wrong patient ID");
                return;
            }

            try {
                Patient patient = HeartHealthSystem.loadPatientInfo(patientId);

                CTScanResult ctResult = HeartHealthSystem.loadCTResults(patientId);
                if (ctResult == null || !ctResult.isAvailable()) {
                    showAlert(Alert.AlertType.INFORMATION, "Information", "No data is available yet.");
                    return;
                }

                renderPatientResultsView(patient, ctResult);
            } catch (IOException ex) {
                showAlert(Alert.AlertType.ERROR, "Read Error", "Error reading records: " + ex.getMessage());
            }
        });
    }

    private void renderPatientResultsView(Patient patient, CTScanResult result) {
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(25));

        VBox content = new VBox(20);
        content.setAlignment(Pos.TOP_LEFT);

        Label greeting = new Label("Hello " + patient.getFullName());
        greeting.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        GridPane totalGrid = new GridPane();
        totalGrid.setHgap(15);
        TextField tfTotal = new TextField(result.getTotalScore());
        tfTotal.setEditable(false);
        tfTotal.setPrefWidth(220);
        totalGrid.add(new Label("The total Agatston CAC score"), 0, 0);
        totalGrid.add(tfTotal, 1, 0);

        GridPane vesselGrid = new GridPane();
        vesselGrid.setHgap(15);
        vesselGrid.setVgap(10);

        TextField tfLM = new TextField(result.getLm());
        TextField tfLAD = new TextField(result.getLad());
        TextField tfLCX = new TextField(result.getLcx());
        TextField tfRCA = new TextField(result.getRca());
        TextField tfPDA = new TextField(result.getPda());

        tfLM.setEditable(false);
        tfLAD.setEditable(false);
        tfLCX.setEditable(false);
        tfRCA.setEditable(false);
        tfPDA.setEditable(false);

        vesselGrid.add(new Label("LM:"), 0, 0);
        vesselGrid.add(tfLM, 1, 0);
        vesselGrid.add(new Label("LAD:"), 0, 1);
        vesselGrid.add(tfLAD, 1, 1);
        vesselGrid.add(new Label("LCX:"), 0, 2);
        vesselGrid.add(tfLCX, 1, 2);
        vesselGrid.add(new Label("RCA:"), 0, 3);
        vesselGrid.add(tfRCA, 1, 3);
        vesselGrid.add(new Label("PDA:"), 0, 4);
        vesselGrid.add(tfPDA, 1, 4);

        content.getChildren().addAll(greeting, totalGrid, vesselGrid);
        root.setCenter(content);

        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        Button btnBack = new Button("Back to Main");
        btnBack.setOnAction(e -> primaryStage.setScene(mainScene));
        bottomBar.getChildren().add(btnBack);
        root.setBottom(bottomBar);

        primaryStage.setScene(new Scene(root, 650, 480));
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}