/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.entities.GlobalUnittypeReports;
import com.rastrack.rastrackservices.entities.LastVehicleLocation;
import com.rastrack.rastrackservices.entities.LocalizationAlert;
import com.rastrack.rastrackservices.entities.Tbvehicle;
import com.rastrack.rastrackservices.entities.VehicleLocation;
import com.rastrack.rastrackservices.log.LogHelper;
import com.rastrack.rastrackservices.persistence.IPersistence;
import com.rastrack.rastrackservices.persistence.Persistence;
import static com.rastrack.rastrackservices.processing.FTPProcessingThread.addThread;
import static com.rastrack.rastrackservices.processing.FTPProcessingThread.getCount;
import static com.rastrack.rastrackservices.processing.FTPProcessingThread.removeThread;
import static com.rastrack.rastrackservices.processing.FTPProcessingThread.setCount;
import com.rastrack.rastrackservices.util.JsonTbtravelHandler;
import com.rastrack.rastrackservices.util.MessageLog;
import java.io.File;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author usuario
 */
public class DBProcessingThread extends Thread {

    private static List<GlobalUnittypeReports> reports;

    private static int count = 0;
    private final Logger LOGGER = LogHelper.getLogger(DBProcessingThread.class.getName());

    private static int threadsNumber = 0;

    private boolean parameterized;
    private int servId;

    public DBProcessingThread(String name, boolean parameterized, int servId) {
        super(name);
        this.parameterized = parameterized;
        this.servId = servId;
        addThread();
    }

    @Override
    public void run() {
        GlobalUnittypeReports report = null;
        while ((report = getGlobalUnittypeReports()) != null) {
            MessageLog lg = new MessageLog();
            IPersistence persistence = null;
            try {
                persistence = new Persistence();
                LOGGER.debug("It starts to process report " + report.getUnitId());
                Tbvehicle vehicle = persistence.fetchVehicleByAliasOrPlate(report.getUnitId(), servId, parameterized);
                if (vehicle != null && report.getJsonData() != null && !report.getJsonData().isEmpty()) {
                    lg.setObjecToProcces("Report for " + report.getUnitId());
                    LOGGER.debug("It found a vehicle with plate " + vehicle.getVhcPlaca());
                    report.setVehicle(vehicle);
                    JSONParser jsonParser = new JSONParser();
                    JSONObject jsonData = null;
                    try {
                        jsonData = (JSONObject) jsonParser.parse(report.getJsonData());
                        LOGGER.debug("It parsed vehicle Jsondata ");
                        if (jsonData != null && jsonData.containsKey("lat") && jsonData.containsKey("lng")) {
                            VehicleLocation location = createVehicleLocation(jsonData, report);
                            LOGGER.debug("It creates a new location for the report ");
                            persistence.createVehicleLocation(location);
                            LOGGER.debug("New location was created in Data Base ");
                            if (report.getReaded() == false) {
                                report.setReaded(true);
                                persistence.updateReport(report);
                                lg.addResultMessage("Report was updated with readed variable to true");
                                LOGGER.debug("Report " + report.getUnitId() + " was updated with readed variable to true");
                            }
                            LastVehicleLocation lastVehicleLocation = new LastVehicleLocation(vehicle.getVhcPlaca(), location.getId());
                            persistence.insertOrUpdateLastVehicleLocation(lastVehicleLocation);
                            lg.addResultMessage("LastVehicleLocation was created in Database");
                            LOGGER.debug("LastVehicleLocation was created in Database, vehicle " + lastVehicleLocation.getVehicleId());
                            vehicle.setLastLocationJson(JsonTbtravelHandler.createJsonFromObject(location).toJSONString());
                            if (location.getEventCode() != null && location.getEventCode().equals(11) && !location.isPanicReported()) {
                                LocalizationAlert newAlert = EventHelper.createNewAlert(vehicle);
                                persistence.createAlert(newAlert);
                                lg.addResultMessage("New Alert was created in Database, alert id" + newAlert.getId());
                                LOGGER.debug("New Alert LocalizationAlert was created in Database, alert id" + newAlert.getId() + " for vehicle " + vehicle.getVhcPlaca());
                            }
                            lg.setState("OK");
                        }
                    } catch (ParseException ex) {
                        lg.addResultMessage("It couldn't parse vehicle Jsondata, continue with the next report .... ");
                        lg.setState("FAILED");
                        lg.setException(ex);
                        LOGGER.debug("It couldn't parse vehicle Jsondata, continue with the next report .... ");
                        LOGGER.debug(ex);
                    }
                } else {
                    lg.addResultMessage("It couldn't find a vehicle with report ");
                    lg.setState("FAILED");
                    LOGGER.debug("It couldn't find a vehicle with report " + report.getUnitId());
                }
                LOGGER.debug("It continues with the next report ...");
            } catch (Throwable ex) {
                lg.addResultMessage("It couldn't find a vehicle with report ");
                lg.setState("FAILED");
                lg.setException(ex);
                LOGGER.debug("There was a exception when it tries to process the reports " + ex.getMessage());
                LOGGER.debug(ex);
            } finally {
                persistence = null;
            }
            LOGGER.info(lg);
        }
        removeThread();
        if (threadsNumber <= 0) {
            LOGGER.info("All of Threads for DB process have ended");
        }
    }

    private VehicleLocation createVehicleLocation(JSONObject jsonData, GlobalUnittypeReports report) {
        VehicleLocation location = new VehicleLocation();
        location.setLatitude((Double) jsonData.get("lat"));
        location.setLongitude((Double) jsonData.get("lng"));
        location.setLocation((String) jsonData.get("location"));
        location.setHeading(0);
        if (jsonData.containsKey("speed")) {
            try {
                location.setSpeed((double) jsonData.get("speed"));
            } catch (Exception e) {
            }
        }
        if (jsonData.containsKey("heading")) {
            try {
                location.setHeading((int) jsonData.get("heading"));
            } catch (Exception e) {
            }
        }
        location.setAltitude(0);
        location.setServerDateGmt(report.getReportDateGmt());
        location.setGpsDateGmt(report.getReportDateGmt());
        if (location.getGpsDateGmt() == null || location.getGpsDateGmt().getTime() < 946684800000L) {
            location.setGpsDateGmt(new Date());
        }
        try {
            location.setEventCode(jsonData.containsKey("eventCode") ? ((Long) jsonData.get("eventCode")).intValue() : 133);
        } catch (Exception e) {
            location.setEventCode(133);
        }
        location.setEvent("Localizacion");
        location.setVehicleId(report.getVehicle());
        location.setPlate(report.getUnitId());
        location.setUnitId(report.getUnitId());
        location.setUnitType("Global");
        if (location.getEventCode() != null && location.getEventCode().equals(11)) {
            location.setEvent("Panico");
        }
        location.setPanicReported(false);
        return location;
    }

    private synchronized static GlobalUnittypeReports getGlobalUnittypeReports() {
        if (!ThreadProcessing.isStop() && count < reports.size()) {
            return reports.get(count++);
        }
        return null;
    }

    public static void addThread() {
        ThreadProcessing.addThreadActive();
        threadsNumber++;
    }

    public static void removeThread() {
        ThreadProcessing.removeThreadActive();
        threadsNumber--;
    }

    /**
     * @return the reports
     */
    public static List<GlobalUnittypeReports> getReports() {
        return reports;
    }

    /**
     * @param aReports the reports to set
     */
    public static void setReports(List<GlobalUnittypeReports> aReports) {
        reports = aReports;
        count = 0;
    }

    /**
     * @return the count
     */
    public static int getCount() {
        return count;
    }

    /**
     * @param aCount the count to set
     */
    public static void setCount(int aCount) {
        count = aCount;
    }

    /**
     * @return the threadsNumber
     */
    public static int getThreadsNumber() {
        return threadsNumber;
    }

    /**
     * @param aThreadsNumber the threadsNumber to set
     */
    public static void setThreadsNumber(int aThreadsNumber) {
        threadsNumber = aThreadsNumber;
    }

}
