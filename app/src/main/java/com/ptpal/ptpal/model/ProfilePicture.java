package com.ptpal.ptpal.model;

public class ProfilePicture
{
    private String patientEmail;
    private byte[] data;

    public String getPatientEmail() {
        return patientEmail;
    }
    public void setPatientEmail(String patientEmail)
    {
        this.patientEmail = patientEmail;
    }

    public byte[] getData() {
        return data;
    }
    public void setData(byte[] data)
    {
        this.data = data;
    }
}
