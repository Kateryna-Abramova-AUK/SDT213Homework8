package com.example.homework8sdt213;

public class CTExam {
    public static final String SCHEDULED = "Scheduled";
    public static final String COMPLETED = "Completed";

    private String patientId;
    private String examDate;
    private String status;

    public CTExam(String patientId, String examDate, String status) {
        this.patientId = patientId;
        this.examDate = examDate;
        this.status = status;
    }

    /** Marks the exam as scheduled (done by the receptionist). */
    public void schedule() { this.status = SCHEDULED; }

    /** Marks the exam as completed (done when CT results are saved). */
    public void complete() { this.status = COMPLETED; }

    public String getPatientId() { return patientId; }
    public String getExamDate() { return examDate; }
    public String getStatus() { return status; }
}
