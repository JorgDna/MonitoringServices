/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author usuario
 */
public class LocalizationRule {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String name;

    private boolean status;

    private boolean engineOn;

    private boolean engineOff;

    private List<Geofence> geofenceList;

    private List<Tbvehicle> vehiclesList;

    private List<RuleActions> actionsList;

    private List<RuleOptions> optionsList;

    private Integer client;

    public LocalizationRule() {
        if (geofenceList == null) {
            geofenceList = new ArrayList<>();
        }
        if (vehiclesList == null) {
            vehiclesList = new ArrayList<>();
        }
        if (actionsList == null) {
            actionsList = new ArrayList<>();
        }
        if (optionsList == null) {
            optionsList = new ArrayList<>();
        }
    }

    public LocalizationRule(Integer id) {
        this.id = id;
    }

    public LocalizationRule(Integer id, String name, boolean status, boolean engineOn, boolean engineOff, Integer client) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.engineOn = engineOn;
        this.engineOff = engineOff;
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getEngineOn() {
        return engineOn;
    }

    public void setEngineOn(boolean engineOn) {
        this.engineOn = engineOn;
    }

    public boolean getEngineOff() {
        return engineOff;
    }

    public void setEngineOff(boolean engineOff) {
        this.engineOff = engineOff;
    }

    public List<Geofence> getGeofenceList() {
        return geofenceList;
    }

    public void setGeofenceList(List<Geofence> geofenceList) {
        this.geofenceList = geofenceList;
    }

    public List<Tbvehicle> getVehiclesList() {
        return vehiclesList;
    }

    public void setVehiclesList(List<Tbvehicle> vehiclesList) {
        this.vehiclesList = vehiclesList;
    }

    public List<RuleActions> getActionsList() {
        return actionsList;
    }

    public void setActionsList(List<RuleActions> actionsList) {
        this.actionsList = actionsList;
    }

    public List<RuleOptions> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<RuleOptions> optionsList) {
        this.optionsList = optionsList;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
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
        if (!(object instanceof LocalizationRule)) {
            return false;
        }
        LocalizationRule other = (LocalizationRule) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.rastrack.entitiesCRT.LocalizationRule[ id=" + id + " ]";
    }

}
