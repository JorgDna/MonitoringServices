/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.entities;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author usuario
 */
public class VehicleLocation {

    private static final long serialVersionUID = 1L;

    private Integer id;

    private String auxData;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation

    private Double latitude;

    private Double longitude;

    private String location;

    private Integer heading;

    private Double speed;

    private Integer altitude;

    private Date serverDateGmt;

    private Date gpsDateGmt;

    private Integer eventCode;

    private String event;

    private String unitId;

    private String unitType;

    private Double distance;

    private Tbvehicle vehicleId;

    DecimalFormat df = new DecimalFormat("0.####", new DecimalFormatSymbols(Locale.US));

    private String plate;

    private Boolean panicReported;

    private String kmlDescription;

    public VehicleLocation() {
    }

    public VehicleLocation(Integer id) {
        this.id = id;
    }

    public VehicleLocation(Integer id, Date gpsDateGmt) {
        this.id = id;
        this.gpsDateGmt = gpsDateGmt;
    }

    public VehicleLocation(Integer id, Date dateTimeGps, Date dateTimeServer, Double latitude,
            Double longitude, Double speed, Integer heading, String location,
            Integer eventIdUnit, String eventUnit, String plate, Double distance, Integer altitude, String auxData, Boolean panicReported) {
        this.id = id;
        this.gpsDateGmt = dateTimeGps;
        this.serverDateGmt = dateTimeServer;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.heading = heading;
        this.location = location;
        this.eventCode = eventIdUnit;
        this.event = eventUnit;
        this.plate = plate;
        this.distance = distance;
        this.altitude = altitude;
        this.auxData = auxData;
        this.panicReported = panicReported;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuxData() {
        return auxData;
    }

    public void setAuxData(String auxData) {
        this.auxData = auxData;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location != null && location.length() >= 255) {
            this.location = location.substring(0, 255);
        } else {
            this.location = location;
        }
    }

    public Integer getHeading() {
        return heading;
    }

    public void setHeading(Integer heading) {
        this.heading = heading;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Double fetchSpeed(boolean convertToKnots) {
        if (this.speed != null && convertToKnots) {
            return this.speed * 0.539957;
        }
        return this.speed;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public Date getServerDateGmt() {
        return serverDateGmt;
    }

    public void setServerDateGmt(Date serverDateGmt) {
        this.serverDateGmt = serverDateGmt;
    }

    public Date getGpsDateGmt() {
        return gpsDateGmt;
    }

    public void setGpsDateGmt(Date gpsDateGmt) {
        this.gpsDateGmt = gpsDateGmt;
    }

    public Integer getEventCode() {
        return eventCode;
    }

    public void setEventCode(Integer eventCode) {
        this.eventCode = eventCode;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }

    public Tbvehicle getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Tbvehicle vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public Double getDistance() {
        return distance;
    }

    public void setDistance(Double distance) {
        this.distance = distance;
    }

    public Date getRealReportDate() {
        return this.gpsDateGmt.getTime() > 946684800000l ? this.gpsDateGmt : this.serverDateGmt;
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
 /*   public String getLongitudeInDMS(boolean htmlValid) {
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

    public String showLocationResume() {
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return (location != null && !location.isEmpty() ? location + ", " : "") + (latitude != null ? "lat: " + latitude + ", " : "") + (longitude != null ? "long: " + longitude + ", " : "") + (eventCode != null ? "event: " + eventCode + ", " : "") + (this.gpsDateGmt != null ? "date: " + format.format(this.gpsDateGmt) : "");
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
        if (!(object instanceof VehicleLocation)) {
            return false;
        }
        VehicleLocation other = (VehicleLocation) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.rastrack.entitiesCRT.VehicleLocation[ id=" + id + " ]";
    }

    public String getHeadingInString() {
        if (this.heading != null) {
            if (this.heading >= 338 || this.heading <= 22) {
                return "Norte";
            }
            if (isBetween(this.heading, 23, 67)) {
                return "Nor-Oriente";
            }
            if (isBetween(this.heading, 68, 112)) {
                return "Oriente";
            }
            if (isBetween(this.heading, 113, 157)) {
                return "Sur-Oriente";
            }
            if (isBetween(this.heading, 158, 202)) {
                return "Sur";
            }
            if (isBetween(this.heading, 203, 247)) {
                return "Sur-Occidente";
            }
            if (isBetween(this.heading, 248, 292)) {
                return "Occidente";
            }
            if (isBetween(this.heading, 293, 337)) {
                return "Nor-Occidente";
            }
        }
        return "Norte";
    }

    public String getHeadingInString2() {
        if (this.heading != null) {
            if (this.heading >= 338 || this.heading <= 22) {
                return "N";
            }
            if (isBetween(this.heading, 23, 67)) {
                return "NE";
            }
            if (isBetween(this.heading, 68, 112)) {
                return "E";
            }
            if (isBetween(this.heading, 113, 157)) {
                return "SE";
            }
            if (isBetween(this.heading, 158, 202)) {
                return "S";
            }
            if (isBetween(this.heading, 203, 247)) {
                return "SO";
            }
            if (isBetween(this.heading, 248, 292)) {
                return "O";
            }
            if (isBetween(this.heading, 293, 337)) {
                return "NO";
            }
        }
        return "N";
    }

    private boolean isBetween(double x, double lower, double upper) {
        return lower <= x && x <= upper;
    }

    public boolean validateLatitude() {
        return this.latitude != null && this.latitude >= -90.0 && this.latitude <= 90.0;
    }

    public boolean validateLongitude() {
        return this.longitude != null && this.longitude >= -180.0 && this.longitude <= 180.0;
    }

    public boolean validateIfCoordinatesAreNotZero() {
        return this.latitude != null && !this.latitude.equals(0.0) && this.longitude != null && !this.longitude.equals(0.0);
    }

    /**
     * Method to find distance between this coordinates and another point
     *
     * @param point point to calculate the distance from the present coordinates
     * @return distance in meters
     */
    /* public Double calculateDistanceFrom(SupportPoint point) {
        if (validateLatitude() && validateLongitude()
                && point != null && point.validateLatitude() && point.validateLongitude()) {
            return GeofenceUtil.distanceBetweenTwoPoints(this.latitude, point.getLatitude(), this.longitude, point.getLatitude(), this.altitude != null ? this.altitude : 0, 0);
        }
        return null;
    }*/
    public Boolean isPanicReported() {
        return panicReported != null ? panicReported : false;
    }

    public void setPanicReported(Boolean panicReported) {
        this.panicReported = panicReported;
    }

    /*  public int getLocationState(TbaUserAdmin user) {
        Date currentDate = new Date();
        if (user != null) {
            if (user.getUserType() != null && user.getUserType().equalsIgnoreCase("operator") && user.getTracingTime() > 0) {
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(gpsDateGmt);
                cal1.add(Calendar.MINUTE, user.getTracingTime() * 2);
                if (cal1.getTime().after(currentDate)) {
                    return 1;
                } else {
                    Calendar cal2 = Calendar.getInstance();
                    cal2.setTime(gpsDateGmt);
                    cal2.add(Calendar.MINUTE, user.getTracingTime() * 4);
                    if (cal2.getTime().after(currentDate)) {
                        return 2;
                    }
                }
            }
        }
        return 0;
    }*/
    public String getKmlDescription() {
        return kmlDescription;
    }

    public void setKmlDescription(String kmlDescription) {
        this.kmlDescription = kmlDescription;
    }
}
