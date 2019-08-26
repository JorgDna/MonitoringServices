/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.entities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author usuario
 */
public class Tbtravel {

    private Integer idTravel;

    private Integer hst;

    private Date dateTimeGps;

    private Date dateTimeServer;

    private Double latitude;

    private Double longitude;

    private Double speed;

    private String heading;

    private String location;

    private Integer eventIdUnit;

    private String eventUnit;

    private Integer idJourney;

    private Tbvehicle vhcPlaca;

    private String plate;

    private String unitType;

    DecimalFormat df = new DecimalFormat("0.####", new DecimalFormatSymbols(Locale.US));

    private boolean localizat;

    private Integer altitude;

    private Integer headingInt;

    public Tbtravel() {
    }

    public Tbtravel(Integer idTravel) {
        this.idTravel = idTravel;
    }

    public Tbtravel(Integer hst, Date dateTimeGps, Date dateTimeServer, Double latitude, Double longitude, Double speed, String heading, String location, Integer eventIdUnit, String eventUnit, String plate) {
        this.hst = hst;
        this.dateTimeGps = dateTimeGps;
        this.dateTimeServer = dateTimeServer;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.heading = heading;
        this.location = location;
        this.eventIdUnit = eventIdUnit;
        this.eventUnit = eventUnit;
        this.plate = plate;
    }

    public Integer getIdTravel() {
        return idTravel;
    }

    public void setIdTravel(Integer idTravel) {
        this.idTravel = idTravel;
    }

    public Integer getHst() {
        return hst;
    }

    public void setHst(Integer hst) {
        this.hst = hst;
    }

    public Date getDateTimeGps() {
        return dateTimeGps;
    }

    public void setDateTimeGps(Date dateTimeGps) {
        this.dateTimeGps = dateTimeGps;
    }

    public Date getDateTimeServer() {
        return dateTimeServer;
    }

    public void setDateTimeServer(Date dateTimeServer) {
        this.dateTimeServer = dateTimeServer;
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

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getEventIdUnit() {
        return eventIdUnit;
    }

    public void setEventIdUnit(Integer eventIdUnit) {
        this.eventIdUnit = eventIdUnit;
    }

    public String getEventUnit() {
        return eventUnit;
    }

    public void setEventUnit(String eventUnit) {
        this.eventUnit = eventUnit;
    }

    public Integer getIdJourney() {
        return idJourney;
    }

    public void setIdJourney(Integer idJourney) {
        this.idJourney = idJourney;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTravel != null ? idTravel.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tbtravel)) {
            return false;
        }
        Tbtravel other = (Tbtravel) object;
        if ((this.idTravel == null && other.idTravel != null) || (this.idTravel != null && !this.idTravel.equals(other.idTravel))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.rastrack.entitiesCRT.Tbtravel[ idTravel=" + idTravel + " ]";
    }

    public Tbvehicle getVhcPlaca() {
        return vhcPlaca;
    }

    public void setVhcPlaca(Tbvehicle vhcPlaca) {
        this.vhcPlaca = vhcPlaca;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    /* public String getLatitudeInDMS(boolean htmlValid) {
        String orientation = null;

        if (latitude > 0.0 && latitude <= 90.0) {
            orientation = "N";
        } else if (latitude < 0.0 && latitude >= -90.0) {
            orientation = "S";
        }

        if (orientation != null) {
            String result = calculateDMS(latitude, htmlValid) + "\" " + orientation;
            if (htmlValid) {
                return org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(result);
            } else {
                return result;
            }
        }
        return "";
    }*/
 /*  public String getLongitudeInDMS(boolean htmlValid) {
        String orientation = null;

        if (longitude > 0.0 && longitude <= 180.0) {
            orientation = "E";
        } else if (longitude < 0.0 && longitude >= -180.0) {
            orientation = "W";
        }

        if (orientation != null) {
            String result = calculateDMS(longitude, htmlValid) + "\" " + orientation;
            if (htmlValid) {
                return org.apache.commons.lang3.StringEscapeUtils.escapeHtml4(result);
            } else {
                return result;
            }
        }
        return "";
    }*/
    private String calculateDMS(Double coord, boolean htmlValid) {
        int latDegrees = coord.intValue();
        Double min = Math.abs(coord - latDegrees) * 60.0;
        int latMins = min.intValue();
        Double latSecs = Math.abs(min - latMins) * 60.0;

        return Math.abs(latDegrees) + "°" + latMins + (htmlValid ? "\\'" : "'") + df.format(latSecs);
    }

    public boolean isLocalizat() {
        return localizat;
    }

    public void setLocalizat(boolean localizat) {
        this.localizat = localizat;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public Integer getHeadingInt() {
        return headingInt;
    }

    public void setHeadingInt(Integer headingInt) {
        this.headingInt = headingInt;
    }
}
