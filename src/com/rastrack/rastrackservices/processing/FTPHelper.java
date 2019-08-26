/*                          
 *Proyecto: 
 *Módulo:
 *Autor: Jorge Espinosa <analista_seis@rastrack.com.co>
 *Fecha: 23/05/2013
 *Descripción: 
 *
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.entities.GlobalUnittypeReports;
import com.rastrack.rastrackservices.entities.Tbvehicle;
import com.rastrack.rastrackservices.log.LogHelper;
import com.rastrack.rastrackservices.persistence.IPersistence;
import com.rastrack.rastrackservices.persistence.Persistence;
import com.rastrack.rastrackservices.util.MessageLog;
import com.rastrack.rastrackservices.util.Utils;
import java.io.IOException;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;
import org.json.simple.JSONObject;

public class FTPHelper {
    
    private static final Logger LOGGER = LogHelper.getLogger(FTPHelper.class.getName());
    
    public static List<GlobalUnittypeReports> decodeXMLContent(String content, int servId, boolean parameterized) throws Exception {
        LOGGER.debug("it starts to decodeXMLContent to decoding each FTP file");
        Document jdomDocument = null;
        SAXBuilder jdomBuilder = new SAXBuilder();
        jdomDocument = jdomBuilder.build(new StringReader(content));
        Map<String, String> elements = new HashMap<>();
        List<GlobalUnittypeReports> listReports = new ArrayList<>();
        if (jdomDocument != null) {
            LOGGER.debug("JSON Object is with content, it starts to process to");
            Element stuMessages = jdomDocument.getRootElement();
            elements.put("messageID", stuMessages.getAttributeValue("messageID"));
            elements.put("timeStamp", stuMessages.getAttributeValue("timeStamp"));
            List<Element> stuMessageList = stuMessages.getChildren("stuMessage");
            for (Element stuMessage : stuMessageList) {
                MessageLog lg = new MessageLog();
                Element payload = stuMessage.getChild("payload");
                elements.put("encoding", payload.getAttributeValue("encoding"));
                elements.put("length", payload.getAttributeValue("length"));
                elements.put("source", payload.getAttributeValue("source"));
                elements.put("payloadHex", payload.getContent(0).getValue());
                Element gps = stuMessage.getChild("gps");
                elements.put("gps", gps.getContent(0).getValue());
                Element unixTime = stuMessage.getChild("unixTime");
                elements.put("unixTime", unixTime.getContent(0).getValue());
                Element esn = stuMessage.getChild("esn");
                String vehicleId = esn.getContent(0).getValue();
                lg.setObjecToProcces(vehicleId);
                Tbvehicle vehicle = null;
                try (IPersistence persistence = new Persistence()) {
                    LOGGER.debug("searching the vehicle from data base " + vehicleId);
                    vehicle = persistence.fetchVehicleByAliasOrPlate(vehicleId, servId, parameterized);
                }
                if (vehicle == null) {
                    LOGGER.debug("Vehicle " + vehicleId + " NOT FOUND during the xml archive reading or NOT PARAMETERIZED ");
                    lg.addResultMessage("Vehicle " + vehicleId + " NOT FOUND during the xml archive reading or NOT PARAMETERIZED ");
                    lg.setState("FAILED");
                    LOGGER.info(lg);                    
                    continue;
                }
                if (vehicle.getUnitSubtype() == null || vehicle.getUnitSubtype().equalsIgnoreCase("Smartone Standard")) {
                    LOGGER.debug("Type of Vehicile is Smartone Standard");
                    GlobalUnittypeReports globalReport = processSmartoneStandard(vehicle, elements);
                    if (globalReport != null) {
                        listReports.add(globalReport);
                        lg.addResultMessage("it was proccessed ");
                    }
                } else if (vehicle.getUnitSubtype().equalsIgnoreCase("Smartone Solar")) {
                    LOGGER.debug("Type of Vehicile is Smartone Solar");
                    GlobalUnittypeReports globalReport = processSmartoneSolar(vehicle, elements);
                    if (globalReport != null) {
                        listReports.add(globalReport);
                        lg.addResultMessage("it was proccessed ");
                    }
                }
                lg.setState("OK");
                LOGGER.info(lg);
            }
        } else {
            LOGGER.debug("JSON Object was null, it couldn't process to");
        }
        LOGGER.debug("It ends to decodeXMLContent to decoding the FTP file");
        return listReports;
    }
    
    public static GlobalUnittypeReports processSmartoneStandard(Tbvehicle vehicle, Map<String, String> elements) {
        LOGGER.debug("It starts to process the plate " + vehicle.getVhcPlaca() + " ans it's type of " + vehicle.getUnitSubtype());
        String payloadHex = elements.get("payloadHex");
        if (payloadHex != null && payloadHex.length() == 20) {
            payloadHex = payloadHex.replaceAll("0x", "");
            String byte0 = payloadHex.substring(0, 2);
            String byte0Binary = Utils.hexToBinary(byte0);
            
            String latitudeHex = payloadHex.substring(2, 8);
            String longitudeHex = payloadHex.substring(8, 14);
            String byte7 = payloadHex.substring(14, 16);
            //String byte8 = payloadHex.substring(16, 18);

            Double lat = Utils.processStandardLatitudeFromHexValue(latitudeHex);
            Double lng = Utils.processStandardLongitudeFromHexValue(longitudeHex);
            double speed = Utils.decodeHexToDouble(byte7);
            
            if (lat != null && lng != null && Utils.isNormalStandardMessage(byte0Binary)) {
                GlobalUnittypeReports report = new GlobalUnittypeReports();
                JSONObject jsonData = new JSONObject();
                jsonData.put("lat", lat);
                jsonData.put("lng", lng);
//                    jsonData.put("location", fetchLocationThroughMapService(lat, lng));
                jsonData.put("location", null);
                jsonData.put("speed", speed);
                jsonData.put("messageID", elements.get("messageID"));
                jsonData.put("timeStamp", elements.get("timeStamp"));
                jsonData.put("raw", payloadHex);
                jsonData.put("encoding", elements.get("encoding"));
                jsonData.put("length", elements.get("length"));
                jsonData.put("source", elements.get("source"));
                jsonData.put("gps", elements.get("gps"));
                jsonData.put("unixTime", elements.get("unixTime"));
                jsonData.put("esn", vehicle.getVhcPlaca());
                jsonData.put("eventCode", Utils.isPanicMessage(byte0Binary) ? 11 : 133);
                
                report.setUnitId(vehicle.getVhcPlaca());
                report.setReaded(false);
                report.setReportDateGmt(new Date(Long.parseLong(elements.get("unixTime")) * 1000));
                report.setJsonData(jsonData.toJSONString());
                try {
                    IPersistence persistence = new Persistence();
                    persistence.createReport(report);
                    LOGGER.debug("The report with plate " + vehicle.getVhcPlaca() + " was persisted into database");
                    report.setVehicle(vehicle);
                    LOGGER.debug("It ended to generate the report for the plate " + vehicle.getVhcPlaca() + " and returned");
                    return report;
                } catch (SQLException | ClassNotFoundException | IOException ex) {
                    LOGGER.debug("There was an exception while tried to persit into data base " + ex.getMessage());
                    LOGGER.debug(ex);
                }
            } else {
                LOGGER.debug("The report didn't have a correct latitude or a longitude or a normal standar message, plate: " + vehicle.getVhcPlaca());
                return null;
            }
        }
        LOGGER.debug("The report didn't have a payload, plate: " + vehicle.getVhcPlaca());
        return null;
    }
    
    public static GlobalUnittypeReports processSmartoneSolar(Tbvehicle vehicle, Map<String, String> elements) {
        LOGGER.debug("it starts to process the plate " + vehicle.getVhcPlaca() + " ans it's type of " + vehicle.getUnitSubtype());
        String payloadHex = elements.get("payloadHex");
        if (payloadHex != null && payloadHex.length() == 20) {
            payloadHex = payloadHex.replaceAll("0x", "");
            String byte0 = payloadHex.substring(0, 2);
            String byte0Binary = Utils.hexToBinary(byte0);
            String motion = byte0Binary.substring(1, 2); // "0" es vehículo detenido y "1" es vehículo en movimiento
            String messageType = byte0Binary.substring(6, 8); // "00" es un mensaje válido normal

            String latitudeHex = payloadHex.substring(2, 8);
            String longitudeHex = payloadHex.substring(8, 14);
            String byte7 = payloadHex.substring(14, 16);
            String byte8 = payloadHex.substring(16, 18);
            
            Double lat = Utils.processSolarLatitudeFromHexValue(latitudeHex);
            Double lng = Utils.processSolarLongitudeFromHexValue(longitudeHex);
            int heading = 0;
            try {
                heading = Utils.evaluateHeadingForSolarUnits(Utils.hexToBinary(byte7).substring(5, 8));
            } catch (Exception e) {
            }
            double speed = 0;
            if (motion != null && motion.equals("1")) {
                speed = Utils.decodeHexToDouble(byte8);
            }
            
            if (lat != null && lng != null && messageType != null && messageType.equals("00")) {
                GlobalUnittypeReports report = new GlobalUnittypeReports();
                JSONObject jsonData = new JSONObject();
                jsonData.put("lat", lat);
                jsonData.put("lng", lng);
//                    jsonData.put("location", fetchLocationThroughMapService(lat, lng));
                jsonData.put("location", null);
                jsonData.put("heading", heading);
                jsonData.put("speed", speed);
                jsonData.put("messageID", elements.get("messageID"));
                jsonData.put("timeStamp", elements.get("timeStamp"));
                jsonData.put("raw", payloadHex);
                jsonData.put("encoding", elements.get("encoding"));
                jsonData.put("length", elements.get("length"));
                jsonData.put("source", elements.get("source"));
                jsonData.put("gps", elements.get("gps"));
                jsonData.put("unixTime", elements.get("unixTime"));
                jsonData.put("esn", elements.get("vehicleId"));
                jsonData.put("eventCode", 133);
                
                report.setUnitId(vehicle.getVhcPlaca());
                report.setReaded(false);
                report.setReportDateGmt(new Date(Long.parseLong(elements.get("unixTime")) * 1000));
                report.setJsonData(jsonData.toJSONString());
                try {
                    IPersistence persistence = new Persistence();
                    persistence.createReport(report);
                    LOGGER.debug("The report with plate " + vehicle.getVhcPlaca() + " is persisted into database");
                    
                    report.setVehicle(vehicle);
                    LOGGER.debug("It ended to generate the report for the plate " + vehicle.getVhcPlaca() + " and returned");
                    return report;
                } catch (SQLException | ClassNotFoundException | IOException ex) {
                    LOGGER.debug("There was an expceción while it tried to persit into data base " + ex.getMessage());
                    LOGGER.debug(ex);
                }
            }
        }
        LOGGER.debug("The report is null, plate: " + vehicle.getVhcPlaca());
        return null;
    }
}
