/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.entities.LastVehicleLocation;
import com.rastrack.rastrackservices.entities.LocalizationAlert;
import com.rastrack.rastrackservices.entities.Tbtravel;
import com.rastrack.rastrackservices.entities.Tbvehicle;
import com.rastrack.rastrackservices.entities.VehicleLocation;
import com.rastrack.rastrackservices.persistence.IPersistence;
import com.rastrack.rastrackservices.util.GeofenceUtil;
import com.rastrack.rastrackservices.util.JsonTbtravelHandler;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author usuario
 */
public class WSProcessingResult {

    IPersistence persistence;

    public WSProcessingResult(IPersistence persistence) {
        this.persistence = persistence;
    }

    public void updateVehicleLocationWithTravelData(Tbtravel locationData) throws ClassNotFoundException, SQLException, IOException, Exception {
        Tbvehicle vehicle = locationData.getVhcPlaca();
        if (vehicle != null) {
            try {
                VehicleLocation location = new VehicleLocation();
                location.setLatitude(locationData.getLatitude());
                location.setLongitude(locationData.getLongitude());
                location.setLocation(locationData.getLocation());
                location.setHeading(locationData.getHeadingInt());
                location.setSpeed(locationData.getSpeed());
                location.setAltitude(locationData.getAltitude());
                location.setServerDateGmt(locationData.getDateTimeServer());
                location.setGpsDateGmt(locationData.getDateTimeGps());
                location.setEventCode(locationData.getEventIdUnit());
                location.setEvent(locationData.getEventUnit());
                location.setVehicleId(vehicle);
                location.setUnitId(locationData.getPlate());
                location.setPlate(vehicle.getVehicleName());
                location.setUnitType(locationData.getUnitType());
                location.setPanicReported(false);

                if (location.getSpeed() == null || location.getSpeed() <= 0.0 || locationData.isLocalizat()) {
                    location.setSpeed(fetchAverageSpeedFromLastPositionToRecent(location));
                }
                if (location.getSpeed() < 0.0) {
                    location.setSpeed(0.0);
                }

                LastVehicleLocation lastVehicleLocationTemp = persistence.fetchLastVehicleLocationFromVehicleId(locationData.getVhcPlaca().getVhcPlaca());
                if (lastVehicleLocationTemp != null) {
                    vehicle.setLastVehicleLocation(persistence.selectVehicleLocationById(lastVehicleLocationTemp.getLastLocation()));
                    Calendar time = Calendar.getInstance();
                    time.setTime(vehicle.getLastVehicleLocation().getGpsDateGmt());
                    time.add(Calendar.SECOND, 1);
                    vehicle.getLastVehicleLocation().setGpsDateGmt(time.getTime());
                }

                if (location.getVehicleId().getLastVehicleLocation() == null || location.getVehicleId().getLastVehicleLocation().getGpsDateGmt().before(location.getGpsDateGmt())) {
                    persistence.createVehicleLocation(location);
                    LastVehicleLocation lastVehicleLocation = new LastVehicleLocation(location.getVehicleId().getVhcPlaca(), location.getId());
                    persistence.insertOrUpdateLastVehicleLocation(lastVehicleLocation);
                    vehicle.setLastLocationJson(JsonTbtravelHandler.createJsonFromObject(location).toJSONString());
                    if (location.getEventCode() != null && location.getEventCode().equals(11) && !location.isPanicReported()) {
                        LocalizationAlert newAlert = EventHelper.createNewAlert(vehicle);
                        persistence.createAlert(newAlert);
                    }
                }
            } catch (SQLException | ClassNotFoundException | IOException ex) {
                throw ex;
            }
        }
    }

    public Double fetchAverageSpeedFromLastPositionToRecent(VehicleLocation location) throws SQLException, ClassNotFoundException, IOException, Exception {
        if (location != null && location.getVehicleId() != null) {
            LastVehicleLocation lastlocation = persistence.fetchLastVehicleLocationFromVehicleId(location.getVehicleId().getVhcPlaca());
            VehicleLocation vehicleLocation = null;
            if (lastlocation != null) {
                vehicleLocation = persistence.selectVehicleLocationById(lastlocation.getLastLocation());
                if (vehicleLocation != null) {
                    location.getVehicleId().setLastLocationJson(JsonTbtravelHandler.createJsonFromObject(vehicleLocation).toJSONString());
                    location.getVehicleId().setLastVehicleLocation(vehicleLocation);
                }
            }

            if (location.getVehicleId().getLastVehicleLocation() != null && location.getVehicleId().getLastVehicleLocation().validateLatitude() && location.getVehicleId().getLastVehicleLocation().validateLongitude()
                    && location.getVehicleId().getLastVehicleLocation().validateIfCoordinatesAreNotZero()
                    && location.validateLatitude() && location.validateLongitude() && location.validateIfCoordinatesAreNotZero()) {
                double distanceBetweenLastLocationAndCurrentLocation = GeofenceUtil.distanceBetweenTwoPoints(
                        location.getLatitude(), location.getVehicleId().getLastVehicleLocation().getLatitude(),
                        location.getLongitude(), location.getVehicleId().getLastVehicleLocation().getLongitude(),
                        location.getAltitude() != null ? location.getAltitude() : 0, location.getVehicleId().getLastVehicleLocation().getAltitude() != null ? location.getVehicleId().getLastVehicleLocation().getAltitude() : 0);
                long timeInSeconds = (location.getGpsDateGmt().getTime() - location.getVehicleId().getLastVehicleLocation().getGpsDateGmt().getTime()) / 1000;
                if (timeInSeconds != 0) {
                    double distance = distanceBetweenLastLocationAndCurrentLocation / timeInSeconds;
                    return distance * 3.6;
                }
            }
        }
        return location.getSpeed();
    }

    public void processResultsFromWidetech(Tbtravel travel) throws SQLException, ClassNotFoundException, IOException {
        if (travel == null) {
            return;
        }
        Tbvehicle vehicle = travel.getVhcPlaca();
        if (vehicle != null) {
            Date travelDate = travel.getDateTimeGps();
            if (travelDate == null) {
                travelDate = travel.getDateTimeServer();
            }
            if (travelDate != null && travelDate.getTime() > 946684800000L && travelDate.getTime() > vehicle.getLastMessageId()) { // year 2000
                vehicle.setLastMessageId(travelDate.getTime());
                persistence.updateLastMessageIdVehicle(vehicle);
            }
        }
    }
}
