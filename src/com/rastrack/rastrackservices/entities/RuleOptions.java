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
public class RuleOptions {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private int ruleType;

    private double speedlimit;

    private String speedMeasureUnit;

    public RuleOptions() {
    }

    public RuleOptions(Integer id) {
        this.id = id;
    }

    public RuleOptions(Integer id, int ruleType, double speedlimit, String speedMeasureUnit) {
        this.id = id;
        this.ruleType = ruleType;
        this.speedlimit = speedlimit;
        this.speedMeasureUnit = speedMeasureUnit;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getRuleType() {
        return ruleType;
    }

    public void setRuleType(int ruleType) {
        this.ruleType = ruleType;
    }

    public double getSpeedlimit() {
        return speedlimit;
    }

    public void setSpeedlimit(double speedlimit) {
        this.speedlimit = speedlimit;
    }

    public String getSpeedMeasureUnit() {
        return speedMeasureUnit;
    }

    public void setSpeedMeasureUnit(String speedMeasureUnit) {
        this.speedMeasureUnit = speedMeasureUnit;
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
        if (!(object instanceof RuleOptions)) {
            return false;
        }
        RuleOptions other = (RuleOptions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.rastrack.entitiesCRT.RuleOptions[ id=" + id + " ]";
    }
}
