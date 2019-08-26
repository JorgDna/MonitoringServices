/*                                             
 *              
 *Módulo:
 *Autor: Jorge Espinosa 
 *Fecha: 06/04/2019
 *Descripción: 
 *
 */
package com.rastrack.rastrackservices.persistence;

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
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Persistence implements IPersistence {

    protected ISqlManagement sql;

    public Persistence() {
        sql = new SqlManagment();
    }

    @Override
    public Tbvehicle fetchVehicleByAliasOrPlate(String vehicleId, int servId, boolean parameterized) throws SQLException, ClassNotFoundException, IOException {
        Tbvehicle vehicle = null;
        String query;
        if (parameterized) {
            query = "select t.VHC_PLACA, t.UNIT_SUBTYPE, t.CREATE_TEXT_FILE, t.CLI_ID, t.LAST_MESSAGE_ID from tbvehicle t where (t.VHC_WEB_ALIAS = ? or t.VHC_PLACA = ?) and t.SRV_ID = ? and VHC_PARAMETERIZED = true";
        } else {
            query = "select t.VHC_PLACA, t.UNIT_SUBTYPE, t.CREATE_TEXT_FILE, t.CLI_ID, t.LAST_MESSAGE_ID from tbvehicle t where (t.VHC_WEB_ALIAS = ? or t.VHC_PLACA = ?) and t.SRV_ID = ?";
        }
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, vehicleId);
        params.put(2, vehicleId);
        params.put(3, servId);
        List<Map> list = sql.select(query, params);
        if (!list.isEmpty()) {
            Map map = list.get(0);
            vehicle = new Tbvehicle((String) map.get("VHC_PLACA"), (String) map.get("UNIT_SUBTYPE"), (boolean) map.get("CREATE_TEXT_FILE"), (Integer) map.get("CLI_ID"));
            vehicle.setLastMessageId((Long) map.get("LAST_MESSAGE_ID"));
        }
        return vehicle;
    }

    @Override
    public void createReport(GlobalUnittypeReports report) throws SQLException, ClassNotFoundException, IOException {
        String updateSensor = "Insert into global_unittype_reports (UNIT_ID, READED, REPORT_DATE_GMT, JSON_DATA) values (?,?,?,?);";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, report.getUnitId());
        params.put(2, report.getReaded());
        params.put(3, report.getReportDateGmt());
        params.put(4, report.getJsonData());
        report.setId(sql.create(updateSensor, params));
    }

    @Override
    public void createVehicleLocation(VehicleLocation vehicleLocation) throws SQLException, ClassNotFoundException, IOException {
        String sqlsentence = "Insert into vehicle_location (VEHICLE_ID, AUX_DATA, LATITUDE, LONGITUDE, LOCATION, HEADING, SPEED, ALTITUDE, SERVER_DATE_GMT, GPS_DATE_GMT, EVENT_CODE, EVENT, UNIT_ID, UNIT_TYPE, DISTANCE) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, vehicleLocation.getVehicleId().getVhcPlaca());
        params.put(2, vehicleLocation.getAuxData());
        params.put(3, vehicleLocation.getLatitude());
        params.put(4, vehicleLocation.getLongitude());
        params.put(5, vehicleLocation.getLocation());
        params.put(6, vehicleLocation.getHeading());
        params.put(7, vehicleLocation.getSpeed());
        params.put(8, vehicleLocation.getAltitude());
        params.put(9, vehicleLocation.getServerDateGmt());
        params.put(10, vehicleLocation.getGpsDateGmt());
        params.put(11, vehicleLocation.getEventCode());
        params.put(12, vehicleLocation.getEvent());
        params.put(13, vehicleLocation.getUnitId());
        params.put(14, vehicleLocation.getUnitType());
        params.put(15, vehicleLocation.getDistance());
        vehicleLocation.setId(sql.create(sqlsentence, params));
    }

    @Override
    public VehicleLocation selectVehicleLocationById(Object id) throws SQLException, ClassNotFoundException, IOException {
        String sqlsentence = "select ID, VEHICLE_ID, AUX_DATA, LATITUDE, LONGITUDE, LOCATION, HEADING, SPEED, ALTITUDE, SERVER_DATE_GMT, GPS_DATE_GMT, EVENT_CODE, EVENT, UNIT_ID, UNIT_TYPE, DISTANCE from vehicle_location where ID = ?;";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, id);
        List<Map> list = sql.select(sqlsentence, params);
        VehicleLocation vehiclelocation = null;
        if (!list.isEmpty()) {
            Map map = list.get(0);
            vehiclelocation = new VehicleLocation((int) map.get("ID"));
            vehiclelocation.setVehicleId(new Tbvehicle((String) map.get("VEHICLE_ID")));
            vehiclelocation.setAuxData((String) map.get("AUX_DATA"));
            vehiclelocation.setLatitude((Double) map.get("LATITUDE"));
            vehiclelocation.setLongitude((Double) map.get("LONGITUDE"));
            vehiclelocation.setLocation((String) map.get("LOCATION"));
            vehiclelocation.setHeading((Integer) map.get("HEADING"));
            vehiclelocation.setSpeed((Double) map.get("SPEED"));
            vehiclelocation.setAltitude((Integer) map.get("ALTITUDE"));
            vehiclelocation.setServerDateGmt((Date) map.get("SERVER_DATE_GMT"));
            vehiclelocation.setGpsDateGmt((Date) map.get("GPS_DATE_GMT"));
            vehiclelocation.setEventCode((Integer) map.get("EVENT_CODE"));
            vehiclelocation.setEvent((String) map.get("EVENT"));
            vehiclelocation.setUnitId((String) map.get("UNIT_ID"));
            vehiclelocation.setUnitType((String) map.get("UNIT_TYPE"));
            vehiclelocation.setDistance((Double) map.get("DISTANCE"));
        }
        return vehiclelocation;
    }

    @Override
    public List<GlobalUnittypeReports> getGlobalUnittypeReports() throws SQLException, ClassNotFoundException, IOException {
        List<GlobalUnittypeReports> reports = new ArrayList<>();
        String query = "select t.ID, t.UNIT_ID, t.READED, t.REPORT_DATE_GMT, t.JSON_DATA from global_unittype_reports t where t.READED = false order by t.UNIT_ID, t.REPORT_DATE_GMT";
        List<Map> list = sql.select(query, null);
        for (Map map : list) {
            reports.add(new GlobalUnittypeReports((int) map.get("ID"), (String) map.get("UNIT_ID"), (boolean) map.get("READED"), (Date) map.get("REPORT_DATE_GMT"), (String) map.get("JSON_DATA")));
        }
        return reports;
    }

    @Override
    public List<Tbvehicle> getTbvehiclesByServenClient(int serverId, int clientId, boolean parameterized) throws SQLException, ClassNotFoundException, IOException {
        List<Tbvehicle> vehicles = new ArrayList<>();
        String query;
        if (parameterized) {
            query = "select t.VHC_PLACA, t.UNIT_SUBTYPE, t.CREATE_TEXT_FILE, t.CLI_ID, t.LAST_MESSAGE_ID  from tbvehicle t where t.SRV_ID = ? and t.CLI_ID = ? and t.VHC_PARAMETERIZED = true";
        } else {
            query = "select t.VHC_PLACA, t.UNIT_SUBTYPE, t.CREATE_TEXT_FILE, t.CLI_ID, t.LAST_MESSAGE_ID  from tbvehicle t where t.SRV_ID = ? and t.CLI_ID = ?";
        }
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, serverId);
        params.put(2, clientId);
        List<Map> list = sql.select(query, params);
        for (Map map : list) {
            Tbvehicle vehicle = new Tbvehicle((String) map.get("VHC_PLACA"), (String) map.get("UNIT_SUBTYPE"), (boolean) map.get("CREATE_TEXT_FILE"), (Integer) map.get("CLI_ID"));
            vehicle.setLastMessageId((Long) map.get("LAST_MESSAGE_ID"));
            vehicles.add(vehicle);
        }
        return vehicles;
    }

    @Override
    public List<TbpasswordRastrackplus> getPasswords(int provider) throws SQLException, ClassNotFoundException, IOException {
        List<TbpasswordRastrackplus> passwords = new ArrayList<>();
        String query = "select t.URP_ID, t.CLI_ID, t.USP_USER, t.USP_PASSWORD, t.PROVIDER_ID, t.LAST_MESSAGE_ID, t.STATUS from tbpassword_rastrackplus t where t.PROVIDER_ID = ? and  t.STATUS = true order by t.URP_ID asc";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, provider);
        List<Map> list = sql.select(query, params);
        for (Map map : list) {
            passwords.add(new TbpasswordRastrackplus((int) map.get("URP_ID"), (int) map.get("CLI_ID"), (String) map.get("USP_USER"), (String) map.get("USP_PASSWORD"), (int) map.get("PROVIDER_ID"), (String) map.get("LAST_MESSAGE_ID"), (boolean) map.get("STATUS")));
        }
        return passwords;
    }

    @Override
    public String selectGoogleKeybyClient(Object clientId) throws SQLException, ClassNotFoundException, IOException {
        String query = "SELECT k.api_key FROM tbclients c, maps_api_keys k where c.map_api_key = k.id and c.cli_id = ?;";
        String key = "";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, clientId);
        List<Map> list = sql.select(query, params);
        if (list != null && !list.isEmpty()) {
            Map map = list.get(0);
            key = (String) map.get("api_key");
        }
        return key;
    }

    @Override
    public void updateReport(GlobalUnittypeReports report) throws SQLException, ClassNotFoundException, IOException {
        String updateSensor = "update global_unittype_reports set READED = ? Where ID = ?";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, report.getReaded());
        params.put(2, report.getId());
        sql.update(updateSensor, params);
    }

    @Override
    public void updateCredentials(TbpasswordRastrackplus credential) throws SQLException, ClassNotFoundException, IOException {
        String updateSensor = "update tbpassword_rastrackplus set LAST_MESSAGE_ID = ? Where URP_ID = ?";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, credential.getLastMessageId());
        params.put(2, credential.getUrpId());
        sql.update(updateSensor, params);
    }

    @Override
    public void insertOrUpdateLastVehicleLocation(LastVehicleLocation lastVehicleLocation) throws SQLException, ClassNotFoundException, IOException {
        LastVehicleLocation lastVehicleLocationTemp = fetchLastVehicleLocationFromVehicleId(lastVehicleLocation.getVehicleId());
        if (lastVehicleLocationTemp == null) {
            createLastVehicleLocation(lastVehicleLocation);
        } else {
            updateLastVehicleLocation(lastVehicleLocation);
        }
    }

    @Override
    public LastVehicleLocation fetchLastVehicleLocationFromVehicleId(String vehicleId) throws SQLException, ClassNotFoundException, IOException {
        LastVehicleLocation lastVehicleLocation = null;
        String query = "select t.VEHICLE_ID, t.LAST_LOCATION from last_vehicle_location t where t.VEHICLE_ID = ?";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, vehicleId);
        List<Map> list = sql.select(query, params);
        if (!list.isEmpty()) {
            Map map = list.get(0);
            lastVehicleLocation = new LastVehicleLocation((String) map.get("VEHICLE_ID"), (int) map.get("LAST_LOCATION"));
        }
        return lastVehicleLocation;
    }

    @Override
    public void createLastVehicleLocation(LastVehicleLocation lastVehicleLocation) throws SQLException, ClassNotFoundException, IOException {
        String updateSensor = "Insert into last_vehicle_location (VEHICLE_ID, LAST_LOCATION) values (?,?);";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, lastVehicleLocation.getVehicleId());
        params.put(2, lastVehicleLocation.getLastLocation());
        sql.create(updateSensor, params);
    }

    @Override
    public void updateLastVehicleLocation(LastVehicleLocation lastVehicleLocation) throws SQLException, ClassNotFoundException, IOException {
        String updateSensor = "update last_vehicle_location set LAST_LOCATION = ? Where VEHICLE_ID = ?";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, lastVehicleLocation.getLastLocation());
        params.put(2, lastVehicleLocation.getVehicleId());
        sql.update(updateSensor, params);
    }

    @Override
    public void createAlert(LocalizationAlert alert) throws SQLException, ClassNotFoundException, IOException {
        String createAlert = "Insert into localization_alert (CLIENT, REPORT_DATE, RULE_TYPE, VEHICLE, MESSAGE) values (?,?,?,?,?);";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, alert.getClient());
        params.put(2, alert.getReportDate());
        params.put(3, alert.getRuleType());
        params.put(4, alert.getVehicle());
        params.put(5, alert.getMessage());
        alert.setId(sql.create(createAlert, params));
    }

    @Override
    public void updateLastMessageIdVehicle(Tbvehicle vehicle) throws SQLException, ClassNotFoundException, IOException {
        String query = "UPDATE tbvehicle SET LAST_MESSAGE_ID = ? WHERE VHC_PLACA = ?;";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, vehicle.getLastMessageId());
        params.put(2, vehicle.getVhcPlaca());
        sql.update(query, params);
    }

    @Override
    public List<LocalizationRule> fetchRulesByClient() throws SQLException, ClassNotFoundException, IOException {
        String query = "SELECT t.ID, t.NAME, t.STATUS, t.ENGINE_ON, t.ENGINE_OFF, t.CLIENT FROM localization_rule t where t.status = true";
        List<Map> list = sql.select(query, null);
        List<LocalizationRule> results = new ArrayList<>();
        for (Map map : list) {
            results.add(new LocalizationRule((int) map.get("ID"), (String) map.get("NAME"), (boolean) map.get("STATUS"), (boolean) map.get("ENGINE_ON"), (boolean) map.get("ENGINE_OFF"), (int) map.get("CLIENT")));
        }
        return results;
    }

    @Override
    public List<Geofence> fetchGeofencesByLocalization(Integer localization_id) throws SQLException, ClassNotFoundException, IOException {
        String query = "SELECT t.ID, t.NAME, t.COLOR, t.CLIENT, t.ACTIVE FROM geofence t, geofence_rule r where r.LOCALIZATION_RULE_ID = ? and t.STATUS = true";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, localization_id);
        List<Map> list = sql.select(query, params);
        List<Geofence> results = new ArrayList<>();
        for (Map map : list) {
            results.add(new Geofence((int) map.get("ID"), (String) map.get("NAME"), (String) map.get("COLOR"), (boolean) map.get("ACTIVE"), (int) map.get("CLIENT")));
        }
        return results;
    }

    @Override
    public List<Tbvehicle> fetchVehiclesByLocalization(Integer localization_id) throws SQLException, ClassNotFoundException, IOException {
        String query = "select t.VHC_PLACA, t.UNIT_SUBTYPE, t.CREATE_TEXT_FILE, t.CLI_ID, t.LAST_MESSAGE_ID from tbvehicle t, vehicles_rule v where v.localization_rule_id = ?";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, localization_id);
        List<Map> list = sql.select(query, params);
        List<Tbvehicle> vehicles = new ArrayList<>();
        for (Map map : list) {
            Tbvehicle vehicle = new Tbvehicle((String) map.get("VHC_PLACA"), (String) map.get("UNIT_SUBTYPE"), (boolean) map.get("CREATE_TEXT_FILE"), (Integer) map.get("CLI_ID"));
            vehicle.setLastMessageId((Long) map.get("LAST_MESSAGE_ID"));
            vehicles.add(vehicle);
        }
        return vehicles;
    }

    @Override
    public List<RuleOptions> fetchRuleOptionsByLocalization(Integer localization_id) throws SQLException, ClassNotFoundException, IOException {
        String query = "select r.ID, r.RULE_TYPE, r.SPEEDLIMIT, r.SPEED_MEASURE_UNIT from rule_options r, localization_rule_options l where l.localization_rule_id = ?";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, localization_id);
        List<Map> list = sql.select(query, params);
        List<RuleOptions> ruleOptions = new ArrayList<>();
        for (Map map : list) {
            RuleOptions ruleOption = new RuleOptions((int) map.get("ID"), (int) map.get("RULE_TYPE"), (double) map.get("SPEEDLIMIT"), (String) map.get("SPEED_MEASURE_UNIT"));
            ruleOptions.add(ruleOption);
        }
        return ruleOptions;
    }

    @Override
    public List<RuleActions> fetchRuleActionsByLocalization(Integer localization_id) throws SQLException, ClassNotFoundException, IOException {
        String query = "select r.ID, r.ACTION_TYPE, r.EMAILS, r.WEB_MESSAGE from rule_actions r, localization_rule_actions l where l.localization_rule_id = ?";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, localization_id);
        List<Map> list = sql.select(query, params);
        List<RuleActions> ruleActions = new ArrayList<>();
        for (Map map : list) {
            RuleActions ruleAction = new RuleActions((int) map.get("ID"), (int) map.get("ACTION_TYPE"), (String) map.get("EMAILS"), (String) map.get("WEB_MESSAGE"));
            ruleActions.add(ruleAction);
        }
        return ruleActions;
    }

    @Override
    public List<Tbcoordinates> fetchCoordinatesByGeofence(Integer geofence_id) throws SQLException, ClassNotFoundException, IOException {
        String query = "select t.COT_ID, t.LATITUDE, t.LONGITUDE, t.RADIO, t.SUB_GROUP, t.GEOFENCE_ID from tbcoordinates t where t.GEOFENCE_ID = ?";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, geofence_id);
        List<Map> list = sql.select(query, params);
        List<Tbcoordinates> coordinates = new ArrayList<>();
        for (Map map : list) {
            Tbcoordinates coordinate = new Tbcoordinates((int) map.get("COT_ID"), (Double) map.get("LATITUDE"), (Double) map.get("LONGITUDE"), (Integer) map.get("GEOFENCE_ID"), (Integer) map.get("SUB_GROUP"));
            coordinates.add(coordinate);
        }
        return coordinates;
    }

    @Override
    public GeofenceUnits selectGeofenceUnitsBy(Integer geofenceUnit, String vhcPlaca) throws SQLException, ClassNotFoundException, IOException {
        GeofenceUnits geofenceUnits = null;
        String query = "select u.ID, u.GEOFENCE_ID, u.UNIT, u.VEHICLE, u.SPEEDING from geofence_units u where u.GEOFENCE_ID = ? and u.VEHICLE = ?";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, geofenceUnit);
        params.put(2, vhcPlaca);
        List<Map> list = sql.select(query, params);
        if (!list.isEmpty()) {
            Map map = list.get(0);
            geofenceUnits = new GeofenceUnits((int) map.get("ID"), (int) map.get("GEOFENCE_ID"), (String) map.get("VEHICLE"), (boolean) map.get("SPEEDING"));
        }
        return geofenceUnits;
    }

    @Override
    public void createGeofenceUnit(GeofenceUnits geofenceUnits) throws SQLException, ClassNotFoundException, IOException {
        String query = "Insert into geofence_units (GEOFENCE_ID, VEHICLE, SPEEDING) values (?,?,?);";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, geofenceUnits.getGeofence());
        params.put(2, geofenceUnits.getVehicle());
        params.put(3, geofenceUnits.getSpeeding());
        geofenceUnits.setId(sql.create(query, params));
    }

    @Override
    public void removeGeofenceUnit(GeofenceUnits geofenceUnits) throws SQLException, ClassNotFoundException, IOException {
        String query = "delete from geofence_units where GEOFENCE_ID = ? and VEHICLE = ?;";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, geofenceUnits.getGeofence());
        params.put(2, geofenceUnits.getVehicle());
        sql.update(query, params);
    }

    @Override
    public void updateGeofenceUnit(GeofenceUnits geofenceUnits) throws SQLException, ClassNotFoundException, IOException {
        String query = "update geofence_units set SPEEDING = ? where GEOFENCE_ID = ? and VEHICLE = ?;";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, geofenceUnits.getSpeeding());
        params.put(2, geofenceUnits.getGeofence());
        params.put(3, geofenceUnits.getVehicle());
        sql.update(query, params);
    }

    /*   @Override
    public long findLastMessageId(Combination combination) throws SQLException, ClassNotFoundException, IOException {
        String query = "SELECT max(mes_id) as lastMessageId FROM " + combination.getTableName() + " WHERE dea_number = ?;";
        Map<Integer, Object> params = new HashMap<>();
        params.put(1, combination.getActivationCode());
        List<Map> list = sql.select(query, params);
        long lastMessageId = 0L;
        if (!list.isEmpty()) {
            Map map = list.get(0);
            lastMessageId = map.get("lastMessageId") != null ? (long) map.get("lastMessageId") : 0L;
        }
        return lastMessageId + 1L;
    }

    @Override
    public void callSPCombination(Combination combination, Map<String, Object> object) throws SQLException, ClassNotFoundException, IOException {
        String storeProcedure = "{call SPINSERT" + combination.getTableName();
        Map<String, Object> params = new HashMap<>();
        params.put("DEA_NUMBER", combination.getActivationCode());
        params.put("DATE_REP", new Timestamp(GregorianCalendar.getInstance().getTime().getTime()));
        params.put("MES_ID", combination.getLastMessageId());
        params.put("DATE_AUDIT", new Timestamp(GregorianCalendar.getInstance().getTime().getTime()));
        for (String key : object.keySet()) {
            params.put(key, object.get(key));
        }
        sql.executeSPNamed(storeProcedure, params);
    }*/
    @Override
    public void close() throws IOException {
        sql = null;
    }

}
