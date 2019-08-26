/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author usuario
 */
public class Geofence {
    
    private static final long serialVersionUID = 1L;
   
    private Integer id;
    
    private String name;
    
    private String color;
    
    private boolean active;
   
    private Integer client;
   
    private List<Tbcoordinates> coordinatesList;
    
    private List<GeofenceUnits> geofenceUnitsList;
   
    public Geofence() {
        if (geofenceUnitsList == null) {
            geofenceUnitsList = new ArrayList<>();
        }      
    }

    public Geofence(Integer id) {
        this.id = id;
    }

    public Geofence(Integer id, String name, Integer client) {
        this.id = id;
        this.name = name;
        this.client = client;
    }
    
    public Geofence(Integer id, String name, String color, boolean active, Integer client) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.active = active;
        this.client = client;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public List<Tbcoordinates> getCoordinatesList() {
        return coordinatesList;
    }

    public void setCoordinatesList(List<Tbcoordinates> coordinatesList) {
        this.coordinatesList = coordinatesList;
    }

    public List<GeofenceUnits> getGeofenceUnitsList() {
        return geofenceUnitsList;
    }

    public void setGeofenceUnitsList(List<GeofenceUnits> geofenceUnitsList) {
        this.geofenceUnitsList = geofenceUnitsList;
    }
   
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

   /* @Transient
    public void loadUsersMap() {
        geofenceUnitsMap = new HashMap<>();
        if (geofenceUnitsList != null && !geofenceUnitsList.isEmpty()) {
            for (GeofenceUnits geofenceUnit : geofenceUnitsList) {
                if (geofenceUnit.getUser() != null) {
                    geofenceUnitsMap.put(geofenceUnit.getUser().getUsaUsername(), geofenceUnit);
                }
            }
        }
    }*/
    
    
 /*   public void loadVehiclesMap() {
        geofenceUnitsMap = new HashMap<>();
        if (geofenceUnitsList != null && !geofenceUnitsList.isEmpty()) {
            for (GeofenceUnits geofenceUnit : geofenceUnitsList) {
                if (geofenceUnit.getVehicle() != null) {
                    geofenceUnitsMap.put(geofenceUnit.getVehicle().getVhcPlaca(), geofenceUnit);
                }
            }
        }
    }*/

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Geofence)) {
            return false;
        }
        Geofence other = (Geofence) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.rastrack.entitiesCRT.Geofence[ id=" + id + " ]";
    }

    
}
