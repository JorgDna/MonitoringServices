/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.util;

import com.rastrack.rastrackservices.entities.VehicleLocation;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author usuario
 */
public class JsonTbtravelHandler {
    
    private static final DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    private static final String ID = "id";
    private static final String PLATE = "plate";
    private static final String HST = "hst";
    private static final String DATE_TIME_GPS = "dateTimeGps";
    private static final String DATE_TIME_SERVER = "dateTimeServer";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String SPEED = "speed";
    private static final String HEADING = "heading";
    private static final String ALTITUDE = "altitude";
    private static final String DISTANCE = "distance";
    private static final String AUX_DATA = "auxData";
    private static final String LOCATION = "location";
    private static final String EVENT_ID = "eventId";
    private static final String EVENT = "event";
    private static final String PANIC_REPORTED = "panicReported";

//    public static JSONObject createJsonFromObject(Tbtravel entity) {
//        if (entity != null) {
//            JSONObject json = new JSONObject();
//            json.put(PLATE, entity.getPlate());
//            json.put(HST, entity.getHst());
//            json.put(DATE_TIME_GPS, entity.getDateTimeGps() != null ? dateFormat.format(entity.getDateTimeGps()) : null);
//            json.put(DATE_TIME_SERVER, entity.getDateTimeServer() != null ? dateFormat.format(entity.getDateTimeServer()) : null);
//            json.put(LATITUDE, entity.getLatitude());
//            json.put(LONGITUDE, entity.getLongitude());
//            json.put(SPEED, entity.getSpeed());
//            json.put(HEADING, entity.getHeading());
//            json.put(LOCATION, entity.getLocation());
//            json.put(EVENT_ID, entity.getEventIdUnit());
//            json.put(EVENT, entity.getEventUnit());
//            return json;
//        } else {
//            return null;
//        }
//    }
//
//    public static JSONObject createJsonFromObject(UserLocation entity) {
//        if (entity != null) {
//            JSONObject json = new JSONObject();
//            json.put(PLATE, entity.getPlate());
//            json.put(HST, null);
//            json.put(DATE_TIME_GPS, entity.getReportDate() != null ? dateFormat.format(entity.getReportDate()) : null);
//            json.put(DATE_TIME_SERVER, entity.getReportDate() != null ? dateFormat.format(entity.getReportDate()) : null);
//            json.put(LATITUDE, entity.getLatitude());
//            json.put(LONGITUDE, entity.getLongitude());
//            json.put(SPEED, entity.getSpeed());
//            json.put(HEADING, entity.getHeading());
//            json.put(LOCATION, entity.getLocation());
//            json.put(EVENT_ID, entity.getEventCode());
//            json.put(EVENT, null);
//            return json;
//        } else {
//            return null;
//        }
//    }

    public static JSONObject createJsonFromObject(VehicleLocation entity) {
        if (entity != null) {
            JSONObject json = new JSONObject();
            json.put(ID, entity.getId());
            json.put(PLATE, entity.getVehicleId() != null ? entity.getVehicleId() : null);
            json.put(HST, null);
            json.put(DATE_TIME_GPS, entity.getGpsDateGmt() != null ? dateFormat.format(entity.getGpsDateGmt()) : null);
            json.put(DATE_TIME_SERVER, entity.getServerDateGmt() != null ? dateFormat.format(entity.getServerDateGmt()) : null);
            json.put(LATITUDE, entity.getLatitude());
            json.put(LONGITUDE, entity.getLongitude());
            json.put(SPEED, entity.getSpeed());
            json.put(HEADING, entity.getHeading());
            json.put(DISTANCE, entity.getDistance());
            json.put(ALTITUDE, entity.getAltitude());
            json.put(AUX_DATA, entity.getAuxData());
            json.put(LOCATION, entity.getLocation());
            json.put(EVENT_ID, entity.getEventCode());
            json.put(EVENT, entity.getEvent());
            json.put(PANIC_REPORTED, entity.isPanicReported());
            return json;
        } else {
            return null;
        }
    }

//    public static String createJsonArrayFromConfigList(List<Tbtravel> configList) {
//        String result = null;
//
//        if (configList != null) {
//            JSONArray array = new JSONArray();
//            for (Tbtravel config : configList) {
//                JSONObject json = createJsonFromObject(config);
//                if (json != null) {
//                    array.add(json);
//                }
//            }
//            if (!array.isEmpty()) {
//                result = array.toJSONString();
//            }
//        }
//
//        return result;
//    }
//
//    public static Tbtravel createObjectFromJson(String jsonText) {
//        if (jsonText != null && !jsonText.equals("")) {
//            JSONParser parser = new JSONParser();
//            try {
//                JSONObject json = (JSONObject) parser.parse(jsonText);
//                return convertJSONObjectToModel(json);
//            } catch (ParseException ex) {
//                System.out.println(ex.getMessage());
//            }
//        }
//        return null;
//    }

    public static VehicleLocation createVehicleLocationFromJson(String jsonText) {
        if (jsonText != null && !jsonText.equals("")) {
            JSONParser parser = new JSONParser();
            try {
                JSONObject json = (JSONObject) parser.parse(jsonText);
                return convertJSONObjectToVehicleLocationModel(json);
            } catch (ParseException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return null;
    }

//    private static Tbtravel convertJSONObjectToModel(JSONObject json) {
//        String plate = json.get(PLATE) != null ? (String) json.get(PLATE) : null;
//        Integer hst = json.get(HST) != null ? ((Long) json.get(HST)).intValue() : null;
//        Date dateTimeGps = null;
//        Date dateTimeServer = null;
//        try {
//            dateTimeGps = json.get(DATE_TIME_GPS) != null ? dateFormat.parse((String) json.get(DATE_TIME_GPS)) : null;
//            dateTimeServer = json.get(DATE_TIME_SERVER) != null ? dateFormat.parse((String) json.get(DATE_TIME_SERVER)) : null;
//        } catch (java.text.ParseException parseException) {
//        }
//        Double latitude = json.get(LATITUDE) != null ? (Double) json.get(LATITUDE) : null;
//        Double longitude = json.get(LONGITUDE) != null ? (Double) json.get(LONGITUDE) : null;
//        Double speed = json.get(SPEED) != null ? (Double) json.get(SPEED) : null;
//        String heading = "";
//        Object headingObj = json.get(HEADING);
//        if (headingObj instanceof String) {
//            heading = json.get(HEADING) != null ? (String) json.get(HEADING) : null;
//        } else if (headingObj instanceof Long) {
//            heading = json.get(HEADING) != null ? ((Long) json.get(HEADING)).toString() : null;
//        }
//        String location = json.get(LOCATION) != null ? (String) json.get(LOCATION) : null;
//        Integer eventId = json.get(EVENT_ID) != null ? ((Long) json.get(EVENT_ID)).intValue() : null;
//        String event = json.get(EVENT) != null ? (String) json.get(EVENT) : null;
//
//        return new Tbtravel(hst, dateTimeGps, dateTimeServer, latitude, longitude, speed, heading, location, eventId, event, plate);
//    }

    private static VehicleLocation convertJSONObjectToVehicleLocationModel(JSONObject json) {
        Integer id = json.containsKey(ID) && json.get(ID) != null ? (json.get(ID) instanceof Integer ? (Integer) json.get(ID) : ((json.get(ID) instanceof Long ? ((Long) json.get(ID)).intValue() : null))) : null;
        String plate = json.get(PLATE) != null ? (String) json.get(PLATE) : null;
        Integer hst = json.get(HST) != null ? ((Long) json.get(HST)).intValue() : null;
        Date dateTimeGps = null;
        Date dateTimeServer = null;
        try {
            dateTimeGps = json.get(DATE_TIME_GPS) != null ? dateFormat.parse((String) json.get(DATE_TIME_GPS)) : null;
            dateTimeServer = json.get(DATE_TIME_SERVER) != null ? dateFormat.parse((String) json.get(DATE_TIME_SERVER)) : null;
        } catch (java.text.ParseException parseException) {
        }
        Double latitude = json.get(LATITUDE) != null ? (Double) json.get(LATITUDE) : null;
        Double longitude = json.get(LONGITUDE) != null ? (Double) json.get(LONGITUDE) : null;
        Double speed = json.get(SPEED) != null ? (Double) json.get(SPEED) : null;
        Integer heading = null;
        Object headingObj = json.get(HEADING);
        if (headingObj instanceof String) {
            try {
                heading = json.get(HEADING) != null ? Integer.parseInt((String) json.get(HEADING)) : null;
            } catch (Exception e) {
            }
        } else if (headingObj instanceof Long) {
            heading = json.get(HEADING) != null ? ((Long) json.get(HEADING)).intValue() : null;
        }
        Double distance = json.get(DISTANCE) != null ? (Double) json.get(DISTANCE) : null;
        Integer altitude = json.get(ALTITUDE) != null ? ((Long) json.get(ALTITUDE)).intValue() : null;
        String auxData = json.get(AUX_DATA) != null ? (String) json.get(AUX_DATA) : null;
        String location = json.get(LOCATION) != null ? (String) json.get(LOCATION) : null;
        Integer eventId = json.get(EVENT_ID) != null ? ((Long) json.get(EVENT_ID)).intValue() : null;
        String event = json.get(EVENT) != null ? (String) json.get(EVENT) : null;
        Boolean panicReported = false;
        try {
            panicReported = json.containsKey(PANIC_REPORTED) && json.get(PANIC_REPORTED) != null ? (Boolean) json.get(PANIC_REPORTED) : false;
        } catch (Exception e) {
        }

        return new VehicleLocation(id, dateTimeGps, dateTimeServer, latitude, longitude, speed, heading, location, eventId, event, plate, distance, altitude, auxData, panicReported);
    }

//    public static List<Tbtravel> createObjectListFromJsonArray(String jsonText) {
//        if (jsonText != null && !jsonText.equals("")) {
//            JSONParser parser = new JSONParser();
//            List<Tbtravel> results = new ArrayList<>();
//            try {
//                JSONArray jsonArray = (JSONArray) parser.parse(jsonText);
//                for (Object json : jsonArray) {
//                    Tbtravel model = convertJSONObjectToModel((JSONObject) json);
//                    if (model != null) {
//                        results.add(model);
//                    }
//                }
//            } catch (ParseException ex) {
//
//            }
//            return results;
//        }
//        return null;
//    }
//    
}
