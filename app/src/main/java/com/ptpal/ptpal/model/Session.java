package com.ptpal.ptpal.model;

public class Session
{
    private String patientEmail;
    private String exercise;
    private double duration;
    private int pronations;
    private int overExtensions;
    private int overExertions;
    private String createdDate;

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail) {
        this.patientEmail = patientEmail;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise) {
        this.exercise = exercise;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public int getPronations() {
        return pronations;
    }

    public void setPronations(int pronations) {
        this.pronations = pronations;
    }

    public int getOverExertions() {
        return overExertions;
    }

    public void setOverExertions(int overExertions) {
        this.overExertions = overExertions;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getOverExtensions() {
        return overExtensions;
    }

    public void setOverExtensions(int overExtensions) {
        this.overExtensions = overExtensions;
    }
}
