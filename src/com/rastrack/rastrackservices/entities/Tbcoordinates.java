/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.entities;

import java.util.List;

/**
 *
 * @author usuario
 */
public class Tbcoordinates {

    private Integer cotId;

    private Double latitude;

    private Double longitude;

    private Integer radio;

    private Integer geofence;
    
    /*private List<TbcontrolPoint> tbcontrolPointList;
    
    private List<TbdistributionCenter> tbdistributionCenterList;*/
    private Integer subGroup;

    public Tbcoordinates() {
        this.radio = 40;
    }

    public Tbcoordinates(Integer cotId) {
        this.cotId = cotId;
    }

    public Tbcoordinates(Integer cotId, Double latitude, Double longitude, Integer geofence) {
        this.cotId = cotId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radio = 80;
        this.geofence = geofence;
    }

    public Tbcoordinates(Integer cotId, Double latitude, Double longitude, Integer geofence, Integer subgroup) {
        this.cotId = cotId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.radio = 80;
        this.geofence = geofence;
        this.subGroup = subgroup;
    }

    public Integer getCotId() {
        return cotId;
    }

    public void setCotId(Integer cotId) {
        this.cotId = cotId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Integer getRadio() {
        return radio;
    }

    public void setRadio(Integer radio) {
        if (radio < 40) {
            radio = 40;
        }
        this.radio = radio;
    }

    /* public List<TbcontrolPoint> getTbcontrolPointList() {
        return tbcontrolPointList;
    }

    public void setTbcontrolPointList(List<TbcontrolPoint> tbcontrolPointList) {
        this.tbcontrolPointList = tbcontrolPointList;
    }

    public List<TbdistributionCenter> getTbdistributionCenterList() {
        return tbdistributionCenterList;
    }

    public void setTbdistributionCenterList(List<TbdistributionCenter> tbdistributionCenterList) {
        this.tbdistributionCenterList = tbdistributionCenterList;
    }

    public Geofence getGeofence() {
        return geofence;
    }

    public void setGeofence(Geofence geofence) {
        this.geofence = geofence;
    }*/
    public Integer getSubGroup() {
        if (subGroup == null) {
            subGroup = 0;
        }
        return subGroup;
    }

    public void setSubGroup(Integer subGroup) {
        this.subGroup = subGroup;
    }

}
