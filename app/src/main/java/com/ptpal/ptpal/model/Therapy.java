package com.ptpal.ptpal.model;

public class Therapy
{
    private int id;
    private String therapistEmail;
    private String patientEmail;
    private String exercise;
    private int sessionsPerDay;
    private int sessionDuration;
    private int daysPerWeek;
    private int totalWeeks;

    public int getId() {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getTherapistEmail() {
        return therapistEmail;
    }

    public void setTherapistEmail(String therapist)
    {
        this.therapistEmail = therapist;
    }

    public String getPatientEmail() {
        return patientEmail;
    }

    public void setPatientEmail(String patientEmail)
    {
        this.patientEmail = patientEmail;
    }

    public String getExercise() {
        return exercise;
    }

    public void setExercise(String exercise)
    {
        this.exercise = exercise;
    }

    public int getSessionsPerDay() {
        return sessionsPerDay;
    }

    public void setSessionsPerDay(int sessionsPerDay)
    {
        this.sessionsPerDay = sessionsPerDay;
    }

    public int getSessionDuration() {
        return sessionDuration;
    }

    public void setSessionDuration(int sessionDuration)
    {
        this.sessionDuration = sessionDuration;
    }

    public int getDaysPerWeek() {
        return daysPerWeek;
    }

    public void setDaysPerWeek(int daysPerWeek)
    {
        this.daysPerWeek = daysPerWeek;
    }

    public int getTotalWeeks() {
        return totalWeeks;
    }

    public void setTotalWeeks(int totalWeeks)
    {
        this.totalWeeks = totalWeeks;
    }




}
