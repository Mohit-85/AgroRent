package com.example.forfarmer;

public class Booking {
String bookingId;
String machineId;
String userName;
String userContact;
String startDate;
String endDate;
String status;

public Booking(){}

    public Booking(String bookingId, String machineId, String userName, String userContact, String startDate, String endDate, String status) {
        this.bookingId = bookingId;
        this.machineId = machineId;
        this.userName = userName;
        this.userContact = userContact;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setUserContact(String userContact) {
        this.userContact = userContact;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingId() {
        return bookingId;
    }

    public String getMachineId() {
        return machineId;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserContact() {
        return userContact;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStatus() {
        return status;
    }
}
