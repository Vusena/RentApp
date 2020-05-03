package com.mysasse.rentapp.data.models;

public class House {

    private int id;

    private String name;
    private double costPerRoom;
    private String location;
    private int roomsCount;
    private int occupiedRoomsCount;
    private int userId;
    private byte[] image;
    private String createdAt;

    public House(String name, double costPerRoom, String location, int roomsCount, int occupiedRoomsCount, int userId) {
        this.name = name;
        this.costPerRoom = costPerRoom;
        this.location = location;
        this.roomsCount = roomsCount;
        this.occupiedRoomsCount = occupiedRoomsCount;
        this.userId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public double getCostPerRoom() {
        return costPerRoom;
    }

    public void setCostPerRoom(double costPerRoom) {
        this.costPerRoom = costPerRoom;
    }

    public int getRoomsCount() {
        return roomsCount;
    }

    public void setRoomsCount(int roomsCount) {
        this.roomsCount = roomsCount;
    }

    public int getOccupiedRoomsCount() {
        return occupiedRoomsCount;
    }

    public void setOccupiedRoomsCount(int occupiedRoomsCount) {
        this.occupiedRoomsCount = occupiedRoomsCount;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
