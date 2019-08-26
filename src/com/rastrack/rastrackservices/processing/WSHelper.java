/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.entities.TbpasswordRastrackplus;
import com.rastrack.rastrackservices.entities.Tbtravel;
import com.rastrack.rastrackservices.log.LogHelper;
import com.rastrack.rastrackservices.util.Utils;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import java.io.StringReader;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.naming.AuthenticationException;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author usuario
 */
public class WSHelper {

    public Integer RASTRACK_PLUS = 1;

    private static final Logger LOGGER = LogHelper.getLogger(FTPHelper.class.getName());

    public final String ROOT_TAG = "space";
    public final String MAIN_RESPONSE_TAG = "Response";
    public final String STATUS_TAG = "Status";
    public final String STATUS_CODE_TAG = "code";
    public final String STATUS_CODE_OK = "100";
    public final String STATUS_CODE_NO_LOGIN = "101";
    public final String STATUS_CODE_TIMEOUT = "109";
    public final String STATUS_CODE_NOT_FOUND = "0";
    public final String STATUS_DESCRIPTION_TAG = "description";
    public final String PLATE_TAG = "Plate";
    public final String PLATE_HSD_TAG = "hst";
    public final String PLATE_HSD_DATEGPS_TAG = "DateTimeGPS";
    public final String PLATE_HSD_DATESERVER_TAG = "DateTimeServer";
    public final String PLATE_HSD_LATITUDE_TAG = "Latitude";
    public final String PLATE_HSD_LONGITUDE_TAG = "Longitude";
    public final String PLATE_HSD_SPEED_TAG = "Speed";
    public final String PLATE_HSD_HEADING_TAG = "Heading";
    public final String PLATE_HSD_LOCATION_TAG = "Location";
    public final String PLATE_HSD_EVENT_ID_TAG = "EventID";
    public final String PLATE_HSD_EVENT_TAG = "Event";
    public final String DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";
    public final String DATE_FORMAT_DITRANSA = "yyyy-MM-dd";
    public final String TIME_FORMAT_DITRANSA = "HH:mm:ss";

    private DateFormat ditransaDateParser;
    private DateFormat ditransaTimeParser;
    private Client client;

    public List<Tbtravel> retrieveAllLastLocationOfPlatesFromUser(String url) throws Throwable {
        initializeHandlers();
        List<Tbtravel> trackingList = new ArrayList<>();
        boolean executeCicle = true;
        DateFormat dateParser = new SimpleDateFormat(DATE_FORMAT);
        int retries = 1;
        while (executeCicle && retries <= 4) {
            executeCicle = false;

            WebResource resource = client.resource(url);
            String response = resource.type(MediaType.APPLICATION_XML_TYPE).get(String.class);

            response = Utils.cleanXMLResponse(response);

            Document doc = Utils.convertStringToXMLDocument(response);

            if (doc != null) {

                String statusCode = null;
                String statusDescription = "";

                NodeList statusNodeList = doc.getElementsByTagName(STATUS_TAG);
                Node statusNode = statusNodeList.item(0);

                if (statusNode.getNodeType() == Node.ELEMENT_NODE) {
                    statusCode = ((Element) statusNode).getElementsByTagName(STATUS_CODE_TAG).item(0).getTextContent();
                    statusDescription = ((Element) statusNode).getElementsByTagName(STATUS_DESCRIPTION_TAG).item(0).getTextContent();
                }

                if (statusCode == null || statusCode.isEmpty()) {
                    statusCode = "0";
                }

                switch (statusCode) {
                    case STATUS_CODE_NOT_FOUND: {
                        // Lanzar excepción que indique el error en la lectura del xml
                        throw new IllegalStateException("Error en lectura xml del archivo. No se encontró código de estado.");
                    }
                    case STATUS_CODE_OK: {
                        // Activar ejecución del método de recorrido xml y rellenado de los objetos contenedores

                        NodeList plateNodesList = doc.getElementsByTagName(PLATE_TAG);
                        Tbtravel travel;
                        for (int i = 0; i < plateNodesList.getLength(); i++) {
                            Element plateTag = (Element) plateNodesList.item(i);
                            travel = new Tbtravel();
                            travel.setPlate(plateTag.getAttribute("id"));
                            travel.setUnitType("Rastrack Plus");
                            try {
                                travel.setHst(Integer.parseInt(((Element) plateTag.getElementsByTagName(PLATE_HSD_TAG).item(0)).getAttribute("id")));
                            } catch (NumberFormatException numberFormatException) {
                            }
                            travel.setDateTimeGps(Utils.convertDateFromString(Utils.fetchTagValue(plateTag, PLATE_HSD_DATEGPS_TAG), dateParser));
                            if (travel.getDateTimeGps() != null && travel.getDateTimeGps().getTime() < 946684800000l) {
                                travel.setDateTimeGps(null);
                            }
                            travel.setDateTimeServer(Utils.convertDateFromString(Utils.fetchTagValue(plateTag, PLATE_HSD_DATESERVER_TAG), dateParser));
                            travel.setLatitude(Utils.convertDoubleFromString(Utils.fetchTagValue(plateTag, PLATE_HSD_LATITUDE_TAG)));
                            travel.setLongitude(Utils.convertDoubleFromString(Utils.fetchTagValue(plateTag, PLATE_HSD_LONGITUDE_TAG)));
                            travel.setSpeed(Utils.convertDoubleFromString(Utils.fetchTagValue(plateTag, PLATE_HSD_SPEED_TAG)));
                            travel.setHeading(Utils.transformHeadingFromSpanishToEnglishAbbreviations(Utils.fetchTagValue(plateTag, PLATE_HSD_HEADING_TAG)));
                            travel.setLocation(Utils.removeCDATAAttributeFromString(Utils.fetchTagValue(plateTag, PLATE_HSD_LOCATION_TAG)));
                            travel.setEventIdUnit(Utils.convertIntegerFromString(Utils.fetchTagValue(plateTag, PLATE_HSD_EVENT_ID_TAG)));
                            travel.setEventUnit(Utils.fetchTagValue(plateTag, PLATE_HSD_EVENT_TAG));
                            trackingList.add(travel);
                        }

                        break;
                    }
                    case STATUS_CODE_NO_LOGIN: {
                        // Lanzar excepción que indique error en las credenciales de autenticación
                        throw new AuthenticationException(statusDescription);
                    }
                    case STATUS_CODE_TIMEOUT: {
                        // hacer un sleep de 20 segundos y volver a consumir el servicio web
                        try {
                            Thread.sleep(15500);
                        } catch (InterruptedException interruptedException) {
                        }
                        executeCicle = true;
//                        System.out.println("Código de respuesta 109, límite de tiempo de consulta por usuario para web service rastrack plus HistoyDataLastLocationByUser. Reintentando por " + retries + " vez.");
                        retries++;
                        if (retries > 4) {
                            System.out.println("Código de respuesta 109, límite de tiempo de consulta por usuario para web service rastrack plus HistoyDataLastLocationByUser.");
                        }
                        break;
                    }
                }
            } else {
                throw new NullPointerException("No hay respuesta correcta del web service HistoyDataLastLocationByUser");
            }
        }

        return trackingList;
    }

    private synchronized void initializeHandlers() {
        if (ditransaDateParser == null) {
            ditransaDateParser = new SimpleDateFormat(DATE_FORMAT_DITRANSA);
        }
        if (ditransaTimeParser == null) {
            ditransaTimeParser = new SimpleDateFormat(TIME_FORMAT_DITRANSA);
        }
        if (client == null) {
            client = Client.create();
        }
    }

    public List<Tbtravel> loadLastLocationFromSkywaveInPlateList(String url, String googleURL, String key, TbpasswordRastrackplus credential) throws Throwable {
        List<Tbtravel> results = null;
        results = processResponseFromSkywaveCall(sendRestfulRequest(Utils.encodeURL(url), null, "GET"), googleURL, key, credential);
        return results;
    }

    public ClientResponse sendRestfulRequest(String url, String jsonRequest, String httpMethod) throws Throwable {
        initializeHandlers();

        WebResource resource = client.resource(url);

        switch (httpMethod.toUpperCase()) {
            case "GET": {
                return resource.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
            }
            case "POST": {
                return resource.type(MediaType.APPLICATION_JSON).post(ClientResponse.class, jsonRequest);
            }
        }

        return null;
    }

    public List<Tbtravel> processResponseFromSkywaveCall(ClientResponse response, String googleUrl, String key, TbpasswordRastrackplus credential) {
        List<Tbtravel> results = new ArrayList<>();
        DateFormat dateParser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            if (response != null && response.getStatus() == 200) {

                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(response.getEntity(String.class));

                if (json.containsKey("ErrorID")) {
                    Long errorId = (Long) json.get("ErrorID");
                    if (errorId != null && errorId == 0l) {
                        if (json.containsKey("NextStartID")) {

                            long lastMessageId = (Long) json.get("NextStartID");
                            if (lastMessageId > 0) {
                                credential.setLastMessageId(String.valueOf(lastMessageId));
                            }
                        }

                        if (json.containsKey("Messages")) {
                            JSONArray messagesArray = (JSONArray) json.get("Messages");
                            if (messagesArray != null && !messagesArray.isEmpty()) {
                                for (Object mes : messagesArray) {
                                    Tbtravel location = new Tbtravel();
                                    JSONObject message = (JSONObject) mes;
                                    location.setHst(((Long) message.get("ID")).intValue());
                                    location.setPlate((String) message.get("MobileID"));                                    
                                    location.setUnitType("Skywave");
                                    location.setDateTimeServer(Utils.convertDateFromString((String) message.get("MessageUTC"), dateParser));
                                    long sin = ((Long) message.get("SIN")).intValue();

                                    if ((sin == 20 || sin == 126) && message.containsKey("Payload")) {
                                        JSONObject payload = (JSONObject) message.get("Payload");
                                        JSONArray fieldsArray = (JSONArray) payload.get("Fields");
                                        if (fieldsArray != null && !fieldsArray.isEmpty()) {
                                            for (Object fi : fieldsArray) {
                                                JSONObject field = (JSONObject) fi;
                                                String name = (String) field.get("Name");
                                                switch (name.toLowerCase()) {
                                                    case "latitude": {
                                                        String lat = (String) field.get("Value");
                                                        if (lat != null && !lat.isEmpty()) {
                                                            try {
                                                                double latitude = Double.valueOf(lat);
                                                                latitude = latitude / (1000 * 60);
                                                                location.setLatitude(latitude);
                                                            } catch (Exception ex) {

                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case "longitude": {
                                                        String lng = (String) field.get("Value");
                                                        if (lng != null && !lng.isEmpty()) {
                                                            try {
                                                                double longitude = Double.valueOf(lng);
                                                                longitude = longitude / (1000 * 60);
                                                                location.setLongitude(longitude);
                                                            } catch (Exception ex) {

                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case "heading": {
                                                        String heading = (String) field.get("Value");
                                                        if (heading != null && !heading.isEmpty()) {
                                                            try {
                                                                location.setHeading(heading);
                                                            } catch (Exception ex) {

                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case "speed": {
                                                        String speed = (String) field.get("Value");
                                                        if (speed != null && !speed.isEmpty()) {
                                                            try {
                                                                location.setSpeed(Double.valueOf(speed));
                                                            } catch (Exception ex) {

                                                            }
                                                        }
                                                        break;
                                                    }
                                                    case "eventtime":
                                                    case "timestamp": {
                                                        long time = Long.valueOf((String) field.get("Value"));
                                                        Date date = new Date(time * 1000);
                                                        location.setDateTimeGps(date);
                                                        if (location.getDateTimeGps() != null && location.getDateTimeGps().getTime() < 946684800000l) {
                                                            location.setDateTimeGps(null);
                                                        }
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                    }

//                                    fetchLocationThroughtGeocodingAPI(location, googleUrl, key);
                                    results.add(location);
                                }
                            }
                        }
                    }
                }

            }
        } catch (Exception ex) {
            System.out.println("Exception in handling skywave response: " + ex.getMessage());
        }

        return results;
    }

    // desactivada la busqueda de geolocalización a través de las apis de google por temas de costos 
    public void fetchLocationThroughtGeocodingAPI(Tbtravel location, String googleURL, String key) {
//        if (location.getLatitude() != null && location.getLongitude() != null
//                && location.getLatitude() != 0.0 && location.getLongitude() != 0.0) {
//            String url = googleURL + "?latlng=" + location.getLatitude().toString() + "," + location.getLongitude().toString() + "&key=" + key;
//            try {
//                ClientResponse response = sendRestfulRequest(url, null, "GET");
//                String jsonResponseString = response.getEntity(String.class);
//                JSONParser parser = new JSONParser();
//                JSONObject json = (JSONObject) parser.parse(jsonResponseString);
//                if (json.containsKey("status") && ((String) json.get("status")).equals("OK")) {
//                    String formattedAddress = (String) ((JSONObject) ((JSONArray) json.get("results")).get(0)).get("formatted_address");
//                    location.setLocation(formattedAddress);
//                } else {
//                    if (json.containsKey("error_message")) {
//                        System.out.println("Error in Geocoding API request: " + url.substring(0, url.indexOf("&key")) + " --- Status: " + json.get("status") + " -- error_message: " + json.get("error_message"));
//                    }
//                }
//            } catch (Throwable ex) {
//                System.out.println("Unknown Error in Geocoding API request: " + ex.getMessage());
//            }
//        }
    }

    public static Tbtravel loadLocationHistoryFromInreachServiceInPlateList(String url, String credentials) throws Throwable {

        java.lang.System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
        ClientResponse response = Client.create().resource(url).header("Authorization", "Basic " + credentials).header("Accept", "application/json").get(ClientResponse.class);

        if (response != null && response.getStatus() == 200) {
            JSONParser jsonParser = new JSONParser();
            JSONObject responseObj = null;
            try {
                responseObj = (JSONObject) jsonParser.parse(response.getEntity(String.class));
            } catch (Exception ex) {
            }
            if (responseObj != null) {
                if (responseObj.containsKey("HistoryItems")) {
                    JSONArray jsonLocationList = (JSONArray) responseObj.get("HistoryItems");
                    if (jsonLocationList != null && !jsonLocationList.isEmpty()) {
                        for (Object jsonLocationObj : jsonLocationList) {
                            JSONObject jsonLocation = (JSONObject) jsonLocationObj;
                            Tbtravel location = new Tbtravel();
                            String timestamp = (String) jsonLocation.get("Timestamp");
                            try {
                                if (timestamp != null && !timestamp.isEmpty()) {
                                    timestamp = timestamp.replace("/Date(", "");
                                    timestamp = timestamp.replace(")/", "");
                                    Long time = Long.parseLong(timestamp);
                                    location.setDateTimeGps(new Date(time.longValue()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            location.setDateTimeServer(new Date());
                            JSONObject jsonCoords = (JSONObject) jsonLocation.get("Coordinate");
                            if (jsonCoords != null && jsonCoords.containsKey("Latitude") && jsonCoords.containsKey("Longitude")) {
                                try {
                                    location.setLatitude((double) jsonCoords.get("Latitude"));
                                } catch (Exception e) {
                                }
                                try {
                                    location.setLongitude((double) jsonCoords.get("Longitude"));
                                } catch (Exception e) {
                                }
                            } else {
                                continue;
                            }
                            try {
                                location.setAltitude((int) ((long) jsonLocation.get("Altitude")));
                            } catch (Exception e) {
                            }
                            try {
                                location.setHeading(Utils.transformHeadingFromGradesToSpanishAbbreviations((int) ((long) jsonLocation.get("Course"))));
                            } catch (Exception e) {
                            }
                            try {
                                location.setHeadingInt((int) ((long) jsonLocation.get("Course")));
                            } catch (Exception e) {
                            }
                            try {
                                location.setSpeed((double) ((long) jsonLocation.get("Speed")));
                            } catch (Exception e) {
                            }
                            try {
                                location.setEventUnit((String) jsonLocation.get("TextMessage"));
                                if (location.getEventUnit() == null || location.getEventUnit().isEmpty()) {
                                    location.setEventUnit("Localización");
                                }
                            } catch (Exception e) {
                            }
                            location.setEventIdUnit(133);
                            location.setUnitType("Inreach");

                            // validate if required fields are present
                            if (location.getDateTimeGps() != null && location.getLatitude() != null && location.getLongitude() != null) {
                                return location;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

}
