package com.example.parkmate;

public class ParkingLot {
    private int availableSpots,totalSpots,occupiedSpots;
    private String parkingLotName, availableSpotsNos;
    public ParkingLot(String parkingLotName, int availableSpots, int occupiedSpots, int totalSpots, String availableSpotsNos) {
        this.availableSpots = availableSpots;
        this.occupiedSpots = occupiedSpots;
        this.parkingLotName = parkingLotName;
        this.totalSpots = totalSpots;
        this.availableSpotsNos = availableSpotsNos;
    }
    public int getAvailableSpots() {
        return availableSpots;
    }
    public void setAvailableSpots(int availableSpots) {
        this.availableSpots = availableSpots;
    }
    public int getOccupiedSpots() {
        return occupiedSpots;
    }
    public void setOccupiedSpots(int occupiedSpots) {
        this.occupiedSpots = occupiedSpots;
    }
    public int getTotalSpots() {
        return totalSpots;
    }
    public void setTotalSpots(int totalSpots) {
        this.totalSpots = totalSpots;
    }
    public String getAvailableSpotsNos() {
        return availableSpotsNos;
    }
    public void setAvailableSpotsNos(String availableSpotsNos) {this.availableSpotsNos = availableSpotsNos;}
}