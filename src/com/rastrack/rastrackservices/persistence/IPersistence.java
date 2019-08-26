/**
 * AXURE Proyecto: AXURE Módulo: Autor: Jorge Espinosa
 * <analista_seis@rastrack.com.co> Fecha: 22/05/2013 Descripción:
 */
package com.rastrack.rastrackservices.persistence;

//import com.rastrack.rastrackservices.entities.Combination;
import com.rastrack.rastrackservices.entities.Geofence;
import com.rastrack.rastrackservices.entities.GeofenceUnits;
import com.rastrack.rastrackservices.entities.GlobalUnittypeReports;
import com.rastrack.rastrackservices.entities.LastVehicleLocation;
import com.rastrack.rastrackservices.entities.LocalizationAlert;
import com.rastrack.rastrackservices.entities.LocalizationRule;
import com.rastrack.rastrackservices.entities.RuleActions;
import com.rastrack.rastrackservices.entities.RuleOptions;
import com.rastrack.rastrackservices.entities.Tbcoordinates;
import com.rastrack.rastrackservices.entities.TbpasswordRastrackplus;
import com.rastrack.rastrackservices.entities.Tbvehicle;
import com.rastrack.rastrackservices.entities.VehicleLocation;
import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IPersistence extends Closeable {

    public Tbvehicle fetchVehicleByAliasOrPlate(String vehicleId,  int servId, boolean parameterized) throws SQLException, ClassNotFoundException, IOException;

    public void createReport(GlobalUnittypeReports report) throws SQLException, ClassNotFoundException, IOException;

    public List<GlobalUnittypeReports> getGlobalUnittypeReports() throws SQLException, ClassNotFoundException, IOException;

    public void createVehicleLocation(VehicleLocation vehicleLocation) throws SQLException, ClassNotFoundException, IOException;

    public void insertOrUpdateLastVehicleLocation(LastVehicleLocation lastVehicleLocation) throws SQLException, ClassNotFoundException, IOException;

    public LastVehicleLocation fetchLastVehicleLocationFromVehicleId(String vehicleId) throws SQLException, ClassNotFoundException, IOException;

    public VehicleLocation selectVehicleLocationById(Object id) throws SQLException, ClassNotFoundException, IOException;

    public void createLastVehicleLocation(LastVehicleLocation lastVehicleLocation) throws SQLException, ClassNotFoundException, IOException;

    public void updateLastVehicleLocation(LastVehicleLocation lastVehicleLocation) throws SQLException, ClassNotFoundException, IOException;

    public void updateReport(GlobalUnittypeReports report) throws SQLException, ClassNotFoundException, IOException;

    public void createAlert(LocalizationAlert alert) throws SQLException, ClassNotFoundException, IOException;

    public List<TbpasswordRastrackplus> getPasswords(int provider) throws SQLException, ClassNotFoundException, IOException;

    public void updateLastMessageIdVehicle(Tbvehicle vehicle) throws SQLException, ClassNotFoundException, IOException;

    public String selectGoogleKeybyClient(Object clientId) throws SQLException, ClassNotFoundException, IOException;

    public void updateCredentials(TbpasswordRastrackplus credential) throws SQLException, ClassNotFoundException, IOException;

    public List<Tbvehicle> getTbvehiclesByServenClient(int serverId, int clientId, boolean parameterized) throws SQLException, ClassNotFoundException, IOException;

    public List<LocalizationRule> fetchRulesByClient() throws SQLException, ClassNotFoundException, IOException;

    public List<Geofence> fetchGeofencesByLocalization(Integer localization_id) throws SQLException, ClassNotFoundException, IOException;

    public List<Tbvehicle> fetchVehiclesByLocalization(Integer localization_id) throws SQLException, ClassNotFoundException, IOException;

    public List<RuleOptions> fetchRuleOptionsByLocalization(Integer localization_id) throws SQLException, ClassNotFoundException, IOException;

    public List<RuleActions> fetchRuleActionsByLocalization(Integer localization_id) throws SQLException, ClassNotFoundException, IOException;

    public List<Tbcoordinates> fetchCoordinatesByGeofence(Integer geofence_id) throws SQLException, ClassNotFoundException, IOException;

    public GeofenceUnits selectGeofenceUnitsBy(Integer geofenceUnit, String vhcPlaca) throws SQLException, ClassNotFoundException, IOException;

    public void createGeofenceUnit(GeofenceUnits geofenceUnits) throws SQLException, ClassNotFoundException, IOException;

    public void removeGeofenceUnit(GeofenceUnits geofenceUnits) throws SQLException, ClassNotFoundException, IOException;

    public void updateGeofenceUnit(GeofenceUnits geofenceUnits) throws SQLException, ClassNotFoundException, IOException;

    // public long findLastMessageId(Combination combination) throws SQLException, ClassNotFoundException, IOException;
    // public void callSPCombination(Combination combination, Map<String, Object> map) throws SQLException, ClassNotFoundException, IOException;
}
