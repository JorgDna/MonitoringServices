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
public class LocalizationAlert {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private Date reportDate;

    private int ruleType;

    private String message;

    private Integer client;

    private String vehicle;

    public LocalizationAlert() {
    }

    public LocalizationAlert(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public int getRuleType() {
        return ruleType;
    }

    public void setRuleType(int ruleType) {
        this.ruleType = ruleType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getRuleTypeName() {
        switch (ruleType) {
            case 1: {
                return "Entrada a Geocerca";
            }
            case 2: {
                return "Salida de Geocerca";
            }
            case 3: {
                return "Exceso de Velocidad";
            }
            case 4: {
                return "Panico";
            }
            default: {
                return "";
            }
        }
    }
}
