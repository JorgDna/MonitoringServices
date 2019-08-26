/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.entities;

import java.util.Date;

/**
 *
 * @author usuario
 */
public class GlobalUnittypeReports {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String unitId;

    private boolean readed;

    private Date reportDateGmt;

    private String jsonData;

    private Tbvehicle vehicle;

    public GlobalUnittypeReports() {
    }

    public GlobalUnittypeReports(Integer id) {
        this.id = id;
    }

    public GlobalUnittypeReports(Integer id, String unitId, boolean readed, Date reportDateGmt, String jsonData) {
        this.id = id;
        this.unitId = unitId;
        this.readed = readed;
        this.reportDateGmt = reportDateGmt;
        this.jsonData = jsonData;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public boolean getReaded() {
        return readed;
    }

    public void setReaded(boolean readed) {
        this.readed = readed;
    }

    public Date getReportDateGmt() {
        return reportDateGmt;
    }

    public void setReportDateGmt(Date reportDateGmt) {
        this.reportDateGmt = reportDateGmt;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    /**
     * @return the vehicle
     */
    public Tbvehicle getVehicle() {
        return vehicle;
    }

    /**
     * @param vehicle the vehicle to set
     */
    public void setVehicle(Tbvehicle vehicle) {
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
        if (!(object instanceof GlobalUnittypeReports)) {
            return false;
        }
        GlobalUnittypeReports other = (GlobalUnittypeReports) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.rastrack.entitiesCRT.GlobalUnittypeReports[ id=" + id + " ]";
    }

}
