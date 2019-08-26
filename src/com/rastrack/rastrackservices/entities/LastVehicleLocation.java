/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.entities;

/**
 *
 * @author usuario
 */
public class LastVehicleLocation {

    private static final long serialVersionUID = 1L;

    private String vehicleId;

    private int lastLocation;

    public LastVehicleLocation() {
    }

    public LastVehicleLocation(String vehicleId, int lastLocation) {
        this.vehicleId = validateString(vehicleId, 255);
        this.lastLocation = lastLocation;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = validateString(vehicleId, 255);
    }

    public int getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(int lastLocation) {
        this.lastLocation = lastLocation;
    }

    private String validateString(String string, int maxSize) {
        return string != null && string.length() > maxSize ? string.substring(0, maxSize) : string;
    }
}
