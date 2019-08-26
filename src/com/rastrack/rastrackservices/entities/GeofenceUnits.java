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
public class GeofenceUnits {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private boolean speeding;

    private Integer geofence;

    private String vehicle;

    public GeofenceUnits() {
    }

    public GeofenceUnits(Integer id) {
        this.id = id;
    }

    public GeofenceUnits(Integer id, boolean speeding) {
        this.id = id;
        this.speeding = speeding;
    }   
  
    public GeofenceUnits(Integer id, Integer geofence, String vehicle, boolean speeding) {
        this.id= id;
        this.geofence = geofence;
        this.vehicle = vehicle;
        this.speeding = speeding;
    }
    
    public GeofenceUnits(Integer geofence, String vehicle) {        
        this.geofence = geofence;
        this.vehicle = vehicle;  
        this.speeding = false;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean getSpeeding() {
        return speeding;
    }

    public void setSpeeding(boolean speeding) {
        this.speeding = speeding;
    }

    public Integer getGeofence() {
        return geofence;
    }

    public void setGeofence(Integer geofence) {
        this.geofence = geofence;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GeofenceUnits)) {
            return false;
        }
        GeofenceUnits other = (GeofenceUnits) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.rastrack.entitiesCRT.GeofenceUnits[ id=" + id + " ]";
    }

}
