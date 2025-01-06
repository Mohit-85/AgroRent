package com.example.forfarmer.Class;
public class Notification {

    private String notificationId;
    private String bookingId;
    private String message;
    private Long timestamp;
    private String title;
    String phone;

    public Notification() {}

    public Notification(String notificationId, String bookingId, String title,String message , Long timestamp , String phone) {
        this.notificationId = notificationId;
        this.bookingId = bookingId;
        this.message = message;
        this.timestamp = timestamp;
        this.title=title;
        this.phone=phone;
    }




    // Getters and setters
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
