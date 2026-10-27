package com.example.homework8sdt213;

public class CTScanResult {
    private String patientId;
    private String totalScore;
    private String lm;
    private String lad;
    private String lcx;
    private String rca;
    private String pda;

    public CTScanResult(String patientId, String totalScore, String lm, String lad, String lcx, String rca, String pda) {
        this.patientId = patientId;
        this.totalScore = totalScore;
        this.lm = lm;
        this.lad = lad;
        this.lcx = lcx;
        this.rca = rca;
        this.pda = pda;
    }

    public String getPatientId() { return patientId; }
    public String getTotalScore() { return totalScore; }
    public String getLm() { return lm; }
    public String getLad() { return lad; }
    public String getLcx() { return lcx; }
    public String getRca() { return rca; }
    public String getPda() { return pda; }

    public boolean isAvailable() {
        String[] scores = {totalScore, lm, lad, lcx, rca, pda};
        for (String s : scores) {
            if (s == null || s.isEmpty()) return false;
        }
        return true;
    }
}