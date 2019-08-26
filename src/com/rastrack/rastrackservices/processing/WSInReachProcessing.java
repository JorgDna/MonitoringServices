/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.configuration.WSServiceConfiguration;
import com.rastrack.rastrackservices.entities.TbpasswordRastrackplus;
import com.rastrack.rastrackservices.entities.Tbtravel;
import com.rastrack.rastrackservices.entities.Tbvehicle;
import com.rastrack.rastrackservices.log.LogHelper;
import com.rastrack.rastrackservices.persistence.IPersistence;
import com.rastrack.rastrackservices.persistence.Persistence;
import com.rastrack.rastrackservices.util.MessageLog;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author usuario
 */
public class WSInReachProcessing implements IProcessing {

    private final WSServiceConfiguration config;

    private final Logger LOGGER = LogHelper.getLogger(WSWidtechProcessing.class.getName());

    public WSInReachProcessing(WSServiceConfiguration config) {
        this.config = config;
    }

    @Override
    public void procces() throws SQLException, ClassNotFoundException, IOException {
        LOGGER.info("It starts to process In Web Service");
        IPersistence persistence = null;
        try {
            persistence = new Persistence();
            List<TbpasswordRastrackplus> credentials = persistence.getPasswords(config.getId());
            if (credentials != null && !credentials.isEmpty()) {
                for (TbpasswordRastrackplus credential : credentials) {
                    try {
                        LOGGER.info("It starts to process with credential: " + credential.getUspUser());
                        List<Tbvehicle> vehiclesToFetchData = persistence.getTbvehiclesByServenClient(config.getId(), credential.getCliId(), config.isParameterized());
                        Set<Tbtravel> results = new TreeSet<>(new Comparator<Tbtravel>() {
                            @Override
                            public int compare(Tbtravel o1, Tbtravel o2) {
                                try {
                                    return o1.getDateTimeGps().compareTo(o2.getDateTimeGps());
                                } catch (Exception e) {
                                }
                                return 0;
                            }
                        });
                        if (vehiclesToFetchData != null && !vehiclesToFetchData.isEmpty()) {
                            Calendar lastDateCal = Calendar.getInstance();
                            lastDateCal.add(Calendar.MINUTE, -10);
                            for (Tbvehicle vehicle : vehiclesToFetchData) {
                                try {
                                    LOGGER.debug("Starting to process plate: " + vehicle.getVhcPlaca());
                                    //LastVehicleLocation lastVehicleLocation = persistence.fetchLastVehicleLocationFromVehicleId(vehicle.getVhcPlaca());
                                    //VehicleLocation vehicleLocation = persistence.selectVehicleLocationById(lastVehicleLocation.getLastLocation());
                                    String creden = Base64.encodeBase64String((credential.getUspUser() + ":" + credential.getUspPassword()).getBytes());
                                    StringBuilder url = null;
                                    Calendar calendar = Calendar.getInstance();
                                    Date endDate = calendar.getTime();
                                    calendar.add(Calendar.DAY_OF_MONTH, -30);
                                    Date initDate = calendar.getTime();
                                    if (vehicle.getLastVehicleLocation() != null && vehicle.getLastVehicleLocation().getGpsDateGmt().after(initDate)) {
                                        Calendar tmpCal = Calendar.getInstance();
                                        tmpCal.setTime(vehicle.getLastVehicleLocation().getGpsDateGmt());
                                        tmpCal.add(Calendar.MINUTE, 1);
                                        initDate = tmpCal.getTime();
                                    }
                                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
                                    url = new StringBuilder()
                                            .append(config.getUrl())
                                            .append("?").append("IMEIs=").append(URLEncoder.encode(vehicle.fetchUnitIdForServices(), "UTF-8"))
                                            .append("&").append("Start=").append(URLEncoder.encode(format.format(initDate), "UTF-8"))
                                            .append("&").append("End=").append(URLEncoder.encode(format.format(endDate), "UTF-8"));
                                    Tbtravel location = WSHelper.loadLocationHistoryFromInreachServiceInPlateList(url.toString(), creden);
                                    if (location != null) {
                                        location.setPlate(vehicle.fetchUnitIdForServices());
                                        location.setVhcPlaca(vehicle);
                                        results.add(location);
                                    }
                                    LOGGER.debug("Starting to process plate: " + vehicle.getVhcPlaca());
                                } catch (IOException | ClassNotFoundException | SQLException ex) {
                                    LOGGER.error("There was an exception trying to process the credentials for " + credential.getUspUser() + " ... " + ex.getMessage());
                                    LOGGER.catching(ex);
                                } catch (Throwable ex) {
                                    LOGGER.error("There was an exception trying to process the credentials for" + ex.getMessage());
                                    LOGGER.catching(ex);
                                }
                            }
                            LOGGER.debug("Starting to save In DB Locations");
                            WSProcessingResult wsResult = new WSProcessingResult(persistence);
                            if (results != null && !results.isEmpty()) {
                                for (Tbtravel travel : results) {
                                    try {
                                        wsResult.updateVehicleLocationWithTravelData(travel);
                                        LOGGER.info(new MessageLog(travel.getPlate(), "OK", new StringBuilder("The Vehicle Location was updated and processed.")));
                                    } catch (Throwable ex) {
                                        LOGGER.debug("There was an exception trying to process the location for " + travel.getPlate() + " ... " + ex.getMessage());
                                        LOGGER.debug(ex);
                                        LOGGER.info(new MessageLog(travel.getPlate(), "FAILED", new StringBuilder("There was an exception trying to process the location for " + travel.getPlate()), ex));
                                    }
                                }
                            }
                            if (results.isEmpty()) {
                                LOGGER.info("There were not result for vehicles to proces ");
                            }
                            LOGGER.debug("finished saving In DB Locations");
                        }
                    } catch (IOException | ClassNotFoundException | SQLException ex) {
                        LOGGER.error("There was an exception trying to process the credentials for " + credential.getUspUser() + " ... " + ex.getMessage());
                        LOGGER.catching(ex);
                    } catch (Throwable ex) {
                        LOGGER.error("There was an exception trying to process the credentials for" + ex.getMessage());
                        LOGGER.catching(ex);
                    }
                }
            }
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            LOGGER.error("There was an exception when it tries to process the reports " + ex.getMessage());
            LOGGER.catching(ex);
        } catch (Throwable ex) {
            LOGGER.error("There was a exception when it tries to process the reports " + ex.getMessage());
            LOGGER.catching(ex);
        } finally {
            persistence = null;
        }
        LOGGER.info("WS process ended");
    }
}
