package com.example.forfarmer.Class;

public class Booking {

    private String bookingId;
    private String machineId;
    private String userName;
    private String userContact;
    private String startDate;
    private String endDate;
    private String status;
    private String machineName;
    private String userEmal;

    // Default constructor required for Firebase
    public Booking() {}

    public Booking(String bookingId, String machineId, String userName, String userContact,
                   String startDate, String endDate, String status, String machineName , String userEmal) {
        this.bookingId = bookingId;
        this.machineId = machineId;
        this.userName = userName;
        this.userContact = userContact;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.machineName = machineName;
        this.userEmal = userEmal;
    }

    // Getters and setters for all fields
    public String getUserEmal() {
        return userEmal;
    }
    public void setUserEmal(String userEmal) {
        this.userEmal = userEmal;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }
}
