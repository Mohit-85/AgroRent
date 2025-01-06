package com.example.forfarmer.Class;

public class Machine {
    private String machineId;
    private String machineName;
    private String price;
    private String location;
    private String imageUrl;
    private String ownerId;
    private String description;
    private String fcmToken;

    public Machine() {}


    public Machine(String machineId, String machineName, String price, String location , String imageUrl , String ownerId, String description , String fcmToken) {
        this.machineId = machineId;
        this.machineName = machineName;
        this.price = price;
        this.location = location;
        this.imageUrl = imageUrl;
        this.ownerId = ownerId;
        this.description=description;
        this.fcmToken=fcmToken;

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setO(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getO() {
        return ownerId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMachineId() {
        return machineId;
    }

    public String getMachineName() {
        return machineName;
    }

    public String getPrice() {
        return price;
    }

    public String getLocation() {
        return location;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
    // Default constructor required for Firebase
