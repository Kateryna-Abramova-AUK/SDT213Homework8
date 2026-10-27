package com.example.homework8sdt213;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class HeartHealthSystem {

    public static String generateUniquePatientId() {
        Random random = new Random();
        String id;
        do {
            int num = 10000 + random.nextInt(90000);
            id = String.valueOf(num);
        } while (new File(id + "_PatientInfo.txt").exists());
        return id;
    }

    /**
     * A patient ID is valid when it has exactly 5 digits and a patient
     * with this ID was registered (its _PatientInfo.txt file exists).
     */
    public static boolean validatePatientID(String patientId) {
        return patientId != null
                && patientId.matches("\\d{5}")
                && new File(patientId + "_PatientInfo.txt").exists();
    }

    /** Saves the patient and the scheduled CT exam to <ID>_PatientInfo.txt. */
    public static void savePatientInfo(Patient patient, CTExam exam) throws IOException {
        String fileName = patient.getId() + "_PatientInfo.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("ID: " + patient.getId());
            writer.newLine();
            writer.write("First Name: " + patient.getFirstName());
            writer.newLine();
            writer.write("Last Name: " + patient.getLastName());
            writer.newLine();
            writer.write("Email: " + patient.getEmail());
            writer.newLine();
            writer.write("Phone: " + patient.getPhone());
            writer.newLine();
            writer.write("Health History: " + patient.getHealthHistory());
            writer.newLine();
            writer.write("Insurance ID: " + patient.getInsuranceId());
            writer.newLine();
            writer.write("Exam Date: " + exam.getExamDate());
            writer.newLine();
            writer.write("Exam Status: " + exam.getStatus());
            writer.newLine();
        }
    }

    public static Patient loadPatientInfo(String patientId) throws IOException {
        File file = new File(patientId + "_PatientInfo.txt");
        if (!file.exists()) {
            return null;
        }

        String id = "", firstName = "", lastName = "", email = "", phone = "", history = "", insurance = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("ID: ")) id = line.substring(4).trim();
                else if (line.startsWith("First Name: ")) firstName = line.substring(12).trim();
                else if (line.startsWith("Last Name: ")) lastName = line.substring(11).trim();
                else if (line.startsWith("Email: ")) email = line.substring(7).trim();
                else if (line.startsWith("Phone: ")) phone = line.substring(7).trim();
                else if (line.startsWith("Health History: ")) history = line.substring(16).trim();
                else if (line.startsWith("Insurance ID: ")) insurance = line.substring(14).trim();
            }
        }
        return new Patient(id, firstName, lastName, email, phone, history, insurance);
    }

    /** Reads the CT exam (date and status) from <ID>_PatientInfo.txt. */
    public static CTExam loadCTExam(String patientId) throws IOException {
        File file = new File(patientId + "_PatientInfo.txt");
        if (!file.exists()) {
            return null;
        }

        String examDate = "", status = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Exam Date: ")) examDate = line.substring(11).trim();
                else if (line.startsWith("Exam Status: ")) status = line.substring(13).trim();
            }
        }
        return new CTExam(patientId, examDate, status);
    }

    public static void saveCTResults(CTScanResult result) throws IOException {
        String fileName = result.getPatientId() + "CTResults.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Patient ID: " + result.getPatientId());
            writer.newLine();
            writer.write("Total CAC Score: " + result.getTotalScore());
            writer.newLine();
            writer.write("LM: " + result.getLm());
            writer.newLine();
            writer.write("LAD: " + result.getLad());
            writer.newLine();
            writer.write("LCX: " + result.getLcx());
            writer.newLine();
            writer.write("RCA: " + result.getRca());
            writer.newLine();
            writer.write("PDA: " + result.getPda());
            writer.newLine();
        }

        // The scan is done, so the patient's exam status becomes "Completed"
        Patient patient = loadPatientInfo(result.getPatientId());
        CTExam exam = loadCTExam(result.getPatientId());
        if (patient != null && exam != null) {
            exam.complete();
            savePatientInfo(patient, exam);
        }
    }

    public static CTScanResult loadCTResults(String patientId) throws IOException {
        File file = new File(patientId + "CTResults.txt");
        if (!file.exists()) {
            return null;
        }

        String pId = "", total = "", lm = "", lad = "", lcx = "", rca = "", pda = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Patient ID: ")) pId = line.substring(12).trim();
                else if (line.startsWith("Total CAC Score: ")) total = line.substring(17).trim();
                else if (line.startsWith("LM: ")) lm = line.substring(4).trim();
                else if (line.startsWith("LAD: ")) lad = line.substring(5).trim();
                else if (line.startsWith("LCX: ")) lcx = line.substring(5).trim();
                else if (line.startsWith("RCA: ")) rca = line.substring(5).trim();
                else if (line.startsWith("PDA: ")) pda = line.substring(5).trim();
            }
        }
        return new CTScanResult(pId, total, lm, lad, lcx, rca, pda);
    }
}