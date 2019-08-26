/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.configuration.DBServiceConfiguration;
import com.rastrack.rastrackservices.entities.Geofence;
import com.rastrack.rastrackservices.entities.GeofenceUnits;
import com.rastrack.rastrackservices.entities.GlobalUnittypeReports;
import com.rastrack.rastrackservices.entities.LastVehicleLocation;
import com.rastrack.rastrackservices.entities.LocalizationRule;
import com.rastrack.rastrackservices.entities.RuleOptions;
import com.rastrack.rastrackservices.entities.Tbcoordinates;
import com.rastrack.rastrackservices.entities.Tbvehicle;
import com.rastrack.rastrackservices.entities.VehicleLocation;
import com.rastrack.rastrackservices.log.LogHelper;
import com.rastrack.rastrackservices.persistence.IPersistence;
import com.rastrack.rastrackservices.persistence.Persistence;
import com.rastrack.rastrackservices.util.GeofenceUtil;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author usuario
 */
public class BDSearchAndHandleAlerts implements IProcessing {

    private final DBServiceConfiguration config;

    private final Logger LOGGER = LogHelper.getLogger(BDSearchAndHandleAlerts.class.getName());

    public BDSearchAndHandleAlerts(DBServiceConfiguration config) {
        this.config = config;
    }

    @Override
    public void procces() throws SQLException, ClassNotFoundException, IOException {

        LOGGER.info("It starts to process In Data Base");
        IPersistence persistence = null;
        try {
            persistence = new Persistence();
            // algoritmo de creación de alertas
            List<LocalizationRule> rulesToProcess = persistence.fetchRulesByClient();
            for (LocalizationRule rule : rulesToProcess) {
                List<Tbvehicle> vehiclesWithRules = persistence.fetchVehiclesByLocalization(rule.getId());
                rule.setOptionsList(persistence.fetchRuleOptionsByLocalization(rule.getId()));
                rule.setActionsList(persistence.fetchRuleActionsByLocalization(rule.getId()));

                for (Tbvehicle vehicleToProcess : vehiclesWithRules) {
                    LastVehicleLocation lastVehicleLocation = persistence.fetchLastVehicleLocationFromVehicleId(vehicleToProcess.getVhcPlaca());
                    VehicleLocation vehicleLocation = persistence.selectVehicleLocationById(lastVehicleLocation.getLastLocation());
                    vehicleToProcess.setLastVehicleLocation(vehicleLocation);
                    if (vehicleToProcess.getLastVehicleLocation() == null
                            || vehicleToProcess.getLastVehicleLocation().getLatitude() == null
                            || vehicleToProcess.getLastVehicleLocation().getLongitude() == null
                            || !GeofenceUtil.is_valid_gps_coordinate(vehicleToProcess.getLastVehicleLocation().getLatitude(), vehicleToProcess.getLastVehicleLocation().getLongitude())) {
                        break;
                    }
                    List<Geofence> geofenceList = persistence.fetchGeofencesByLocalization(rule.getId());
                    for (Geofence geofenceToProcess : geofenceList) {
                        geofenceToProcess.setCoordinatesList(persistence.fetchCoordinatesByGeofence(geofenceToProcess.getId()));
                        boolean insideZone = GeofenceUtil.coordinate_is_inside_polygon(vehicleToProcess.getLastVehicleLocation().getLatitude(), vehicleToProcess.getLastVehicleLocation().getLongitude(), geofenceToProcess.getCoordinatesList());
                        GeofenceUnits geofenceUnit = persistence.selectGeofenceUnitsBy(geofenceToProcess.getId(), vehicleToProcess.getVhcPlaca());
                        for (RuleOptions optionToEval : rule.getOptionsList()) {

                            switch (optionToEval.getRuleType()) {
                                case 1: { // Entrada a Geocerca
                                    if (insideZone && geofenceUnit == null) {
                                        GeofenceUnits geofenceUnitNew = new GeofenceUnits(geofenceToProcess.getId(), vehicleToProcess.getVhcPlaca());
                                        persistence.createGeofenceUnit(geofenceUnitNew);
                                        //handleRuleActions(optionToEval.getRuleType(), vehicleToProcess, rule.getActionsList(), geofenceToProcess.getName());
                                    }
                                    break;
                                }
                                case 2: { // Salida de Geocerca
                                    if (!insideZone && geofenceUnit != null) {
                                        persistence.removeGeofenceUnit(geofenceUnit);
                                        //handleRuleActions(optionToEval.getRuleType(), vehicleToProcess, rule.getActionsList(), geofenceToProcess.getName());
                                    }
                                    break;
                                }
                                case 3: { // Exceso de Velocidad
                                    if (insideZone && geofenceUnit != null
                                            && !geofenceUnit.getSpeeding()) {
                                        if (vehicleToProcess.getLastVehicleLocation().getSpeed() == null) {
                                            vehicleToProcess.getLastVehicleLocation().setSpeed(0.0);
                                        }
                                        if (optionToEval.getSpeedlimit() < vehicleToProcess.getLastVehicleLocation().getSpeed()) {
                                            geofenceUnit.setSpeeding(true);
                                            persistence.updateGeofenceUnit(geofenceUnit);
                                            // handleRuleActions(optionToEval.getRuleType(), vehicleToProcess, rule.getActionsList(), geofenceToProcess.getName());
                                        }
                                    } else if (geofenceUnit != null
                                            && geofenceUnit.getSpeeding()
                                            && (vehicleToProcess.getLastVehicleLocation().getSpeed() == null || optionToEval.getSpeedlimit() >= vehicleToProcess.getLastVehicleLocation().getSpeed())) {
                                        geofenceUnit.setSpeeding(false);
                                        persistence.updateGeofenceUnit(geofenceUnit);                                        
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            LOGGER.debug("It searched reports to process ...");

        } catch (IOException | ClassNotFoundException | SQLException ex) {
            LOGGER.error("There was a exception when it tries to process the reports " + ex.getMessage());
            LOGGER.catching(ex);
        } finally {
            persistence = null;
        }
        LOGGER.info("it ended to process ");
    }
}
