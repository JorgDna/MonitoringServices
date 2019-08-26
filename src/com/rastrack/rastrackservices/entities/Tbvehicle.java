/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.entities;

import com.rastrack.rastrackservices.util.JsonTbtravelHandler;
import java.util.Date;

/**
 *
 * @author usuario
 */
public class Tbvehicle {

    private static final long serialVersionUID = 1L;

    private String vhcPlaca;

    private String vhcWebAlias;

    private String vehicleName;

    private Date vhcStateDate;

    private String vhcLinea;

    private String vhcModelo;

    private String vhcMarca;

    private String vhcColor;

//    private String vhcTypeBodywork;
//    
//    private String vhcNumEngine;
//   
//    private String vhcNumChasis;
//  
//    private String vhcCapacity;
//    
//    private String vhcCubrim;
//  
//    private String vhcObservations;
//   
//    private String vhcOutEngine;
// 
//    private String vhcLinePhone;
//    
//    private String vhcTraylerId;
//   
//    private String vhcTraylerModel;
//    
//    private String vhcTraylerMark;
//    
//    private String vhcPrNumdoc;
//   
//    private String vhcPrName;
//    
//    private String vhcPrAddress;
//    
//    private String vhcPrTelephone;
//   
//    private String vhcPrTelephonemovil;
//   
//    private String vhcConfigvisual;
//    
//    private Date usaDate;
//   
//    private Integer vhcMaster;
//  
//    private Integer vhcNumrep;
//   
//    private Integer vhcBlockEngine;
////    @Size(max = 5000)
////    @Column(name = "LAST_LOCATION_JSON")
////    private String lastLocationJson;
//  
//    private boolean dms;
//   
//    private String timezone;
//    
    private long lastMessageId;
//   
//    private String mmsi;
// 
//    private String flag;
//   
//    private String vehicleType;
//   
//    private byte[] picture;
//   
//    private String imei;
//   
//    private String sim;
//    
//    private String apn;
//   
    private boolean createTextFile;
//   
//    private boolean createdFileForLastLocation;
//   
//    private boolean convertSpeedToKnots;

    private String unitSubtype;

    private Integer cliId;
    //  private TbaUserAdmin usaUsername;
    // private List<Tbtravel> tbtravelList;
    // private List<TbaUserAdmin> tbUserList;
    // private List<Tbjourney> tbjourneyList;
    // private TbvehicleType tyvId;
    // private List<TbjourneyHist> tbjourneyHistList;
    // private Tbserver provider;
    private String lastLocationJson;
//    
    private String lastLocation;
//  
//    private Date locationDate;
//  
//    private String associatedUser;
//   
//    private String currentOperatorStatus;
//   
//   // private Flags transFlag;
//   
    private VehicleLocation lastVehicleLocation;
//   
//    private int destinations;

    public Tbvehicle() {
    }

    public Tbvehicle(String vhcPlaca) {
        this.vhcPlaca = vhcPlaca.toUpperCase();
    }

    public Tbvehicle(String vhcPlaca, String unitSubtype, boolean createTextFile, Integer cliId) {
        this.vhcPlaca = vhcPlaca.toUpperCase();
        this.unitSubtype = unitSubtype;
        this.createTextFile = createTextFile;
        this.cliId = cliId;
    }

    public String getVhcPlaca() {
        return vhcPlaca;
    }

    public void setVhcPlaca(String vhcPlaca) {
        this.vhcPlaca = vhcPlaca.toUpperCase();
    }

    public String getVhcWebAlias() {
        return vhcWebAlias;
    }

    public void setVhcWebAlias(String vhcWebAlias) {
        this.vhcWebAlias = vhcWebAlias;
    }

    public Date getVhcStateDate() {
        return vhcStateDate;
    }

    public void setVhcStateDate(Date vhcStateDate) {
        this.vhcStateDate = vhcStateDate;
    }

    public String getVhcLinea() {
        return vhcLinea;
    }

    public void setVhcLinea(String vhcLinea) {
        this.vhcLinea = vhcLinea.toUpperCase();
    }

    public String getVhcModelo() {
        return vhcModelo;
    }

    public void setVhcModelo(String vhcModelo) {
        this.vhcModelo = vhcModelo.toUpperCase();
    }

    public String getVhcMarca() {
        return vhcMarca;
    }

    public void setVhcMarca(String vhcMarca) {
        this.vhcMarca = vhcMarca.toUpperCase();
    }

    public String getVhcColor() {
        return vhcColor;
    }

    public void setVhcColor(String vhcColor) {
        this.vhcColor = vhcColor.toUpperCase();
    }

//    public String getVhcTypeBodywork() {
//        return vhcTypeBodywork;
//    }
//
//    public void setVhcTypeBodywork(String vhcTypeBodywork) {
//        this.vhcTypeBodywork = vhcTypeBodywork;
//    }
//
//    public String getVhcNumEngine() {
//        return vhcNumEngine;
//    }
//
//    public void setVhcNumEngine(String vhcNumEngine) {
//        this.vhcNumEngine = vhcNumEngine;
//    }
//
//    public String getVhcNumChasis() {
//        return vhcNumChasis;
//    }
//
//    public void setVhcNumChasis(String vhcNumChasis) {
//        this.vhcNumChasis = vhcNumChasis;
//    }
//
//    public String getVhcCapacity() {
//        return vhcCapacity;
//    }
//
//    public void setVhcCapacity(String vhcCapacity) {
//        this.vhcCapacity = vhcCapacity;
//    }
//
//    public String getVhcCubrim() {
//        return vhcCubrim;
//    }
//
//    public void setVhcCubrim(String vhcCubrim) {
//        this.vhcCubrim = vhcCubrim;
//    }
//
//    public String getVhcObservations() {
//        return vhcObservations;
//    }
//
//    public void setVhcObservations(String vhcObservations) {
//        this.vhcObservations = vhcObservations;
//    }
//
//    public String getVhcOutEngine() {
//        return vhcOutEngine;
//    }
//
//    public void setVhcOutEngine(String vhcOutEngine) {
//        this.vhcOutEngine = vhcOutEngine;
//    }
//
//    public String getVhcLinePhone() {
//        return vhcLinePhone;
//    }
//
//    public void setVhcLinePhone(String vhcLinePhone) {
//        this.vhcLinePhone = vhcLinePhone;
//    }
//
//    public String getVhcTraylerId() {
//        return vhcTraylerId;
//    }
//
//    public void setVhcTraylerId(String vhcTraylerId) {
//        this.vhcTraylerId = vhcTraylerId;
//    }
//
//    public String getVhcTraylerModel() {
//        return vhcTraylerModel;
//    }
//
//    public void setVhcTraylerModel(String vhcTraylerModel) {
//        this.vhcTraylerModel = vhcTraylerModel;
//    }
//
//    public String getVhcTraylerMark() {
//        return vhcTraylerMark;
//    }
//
//    public void setVhcTraylerMark(String vhcTraylerMark) {
//        this.vhcTraylerMark = vhcTraylerMark;
//    }
//
//    public String getVhcPrNumdoc() {
//        return vhcPrNumdoc;
//    }
//
//    public void setVhcPrNumdoc(String vhcPrNumdoc) {
//        this.vhcPrNumdoc = vhcPrNumdoc;
//    }
//
//    public String getVhcPrName() {
//        return vhcPrName;
//    }
//
//    public void setVhcPrName(String vhcPrName) {
//        this.vhcPrName = vhcPrName;
//    }
//
//    public String getVhcPrAddress() {
//        return vhcPrAddress;
//    }
//
//    public void setVhcPrAddress(String vhcPrAddress) {
//        this.vhcPrAddress = vhcPrAddress;
//    }
//
//    public String getVhcPrTelephone() {
//        return vhcPrTelephone;
//    }
//
//    public void setVhcPrTelephone(String vhcPrTelephone) {
//        this.vhcPrTelephone = vhcPrTelephone;
//    }
//
//    public String getVhcPrTelephonemovil() {
//        return vhcPrTelephonemovil;
//    }
//
//    public void setVhcPrTelephonemovil(String vhcPrTelephonemovil) {
//        this.vhcPrTelephonemovil = vhcPrTelephonemovil;
//    }
//
//    public String getVhcConfigvisual() {
//        return vhcConfigvisual;
//    }
//
//    public void setVhcConfigvisual(String vhcConfigvisual) {
//        this.vhcConfigvisual = vhcConfigvisual;
//    }
//
//    public Date getUsaDate() {
//        return usaDate;
//    }
//
//    public void setUsaDate(Date usaDate) {
//        this.usaDate = usaDate;
//    }
//
//    public Integer getVhcMaster() {
//        return vhcMaster;
//    }
//
//    public void setVhcMaster(Integer vhcMaster) {
//        this.vhcMaster = vhcMaster;
//    }
//
//    public Integer getVhcNumrep() {
//        return vhcNumrep;
//    }
//
//    public void setVhcNumrep(Integer vhcNumrep) {
//        this.vhcNumrep = vhcNumrep;
//    }
//
//    public Integer getVhcBlockEngine() {
//        return vhcBlockEngine;
//    }
//
//    public void setVhcBlockEngine(Integer vhcBlockEngine) {
//        this.vhcBlockEngine = vhcBlockEngine;
//    }
    public String getLastLocationJson() {
        return lastLocationJson;
    }

    public void setLastLocationJson(String lastLocationJson) {
        this.lastLocationJson = lastLocationJson;
    }

    public Integer getCliId() {
        return cliId;
    }

    public void setCliId(Integer cliId) {
        this.cliId = cliId;
    }

    /*
    public TbaUserAdmin getUsaUsername() {
        return usaUsername;
    }

    public void setUsaUsername(TbaUserAdmin usaUsername) {
        this.usaUsername = usaUsername;
    }*/
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (vhcPlaca != null ? vhcPlaca.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {

        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tbvehicle)) {
            return false;
        }
        Tbvehicle other = (Tbvehicle) object;
        if ((this.vhcPlaca == null && other.vhcPlaca != null) || (this.vhcPlaca != null && !this.vhcPlaca.equals(other.vhcPlaca))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return vhcPlaca;
    }

    private String validateString(String string, int maxSize) {
        return string != null && string.length() > maxSize ? string.substring(0, maxSize) : string;
    }

    /*  public List<Tbtravel> getTbtravelList() {
        return tbtravelList;
    }

    public void setTbtravelList(List<Tbtravel> tbtravelList) {
        this.tbtravelList = tbtravelList;
    }

    /**
     * @return the tbUserList
     */
 /* public List<TbaUserAdmin> getTbUserList() {
        return tbUserList;
    }

    /**
     * @param tbUserList the tbUserList to set
     */
 /*  public void setTbUserList(List<TbaUserAdmin> tbUserList) {
        this.tbUserList = tbUserList;
    }

    public List<Tbjourney> getTbjourneyList() {
        return tbjourneyList;
    }

    public void setTbjourneyList(List<Tbjourney> tbjourneyList) {
        this.tbjourneyList = tbjourneyList;
    }

    public TbvehicleType getTyvId() {
        return tyvId;
    }

    public void setTyvId(TbvehicleType tyvId) {
        this.tyvId = tyvId;
    }

    public Tbserver getProvider() {
        return provider;
    }

    public void setProvider(Tbserver provider) {
        this.provider = provider;
    }

    public List<TbjourneyHist> getTbjourneyHistList() {
        return tbjourneyHistList;
    }

    public void setTbjourneyHistList(List<TbjourneyHist> tbjourneyHistList) {
        this.tbjourneyHistList = tbjourneyHistList;
    }*/
    public String getLastLocation() {
        return lastLocation;
    }
//

    public void setLastLocation(String lastLocation) {
        this.lastLocation = lastLocation;
    }
//
//    public Date getLocationDate() {
//        return locationDate;
//    }
//
//    public void setLocationDate(Date locationDate) {
//        this.locationDate = locationDate;
//    }
//
//    public String getAssociatedUser() {
//        return associatedUser;
//    }
//
//    public void setAssociatedUser(String associatedUser) {
//        this.associatedUser = associatedUser;
//    }
//
//    public String getCurrentOperatorStatus() {
//        return currentOperatorStatus;
//    }
//
//    public void setCurrentOperatorStatus(String currentOperatorStatus) {
//        this.currentOperatorStatus = currentOperatorStatus;
//    }
//
//    public boolean isDms() {
//        return dms;
//    }
//
//    public void setDms(boolean dms) {
//        this.dms = dms;
//    }
//
//    public String getTimezone() {
//        return timezone != null ? timezone : "GMT-05:00";
//    }
//
//    public void setTimezone(String timezone) {
//        this.timezone = timezone != null ? (timezone.length() < 9 ? timezone : timezone.substring(0, 9)) : "GMT-05:00";
//    }
//

    public long getLastMessageId() {
        return lastMessageId;
    }
//

    public void setLastMessageId(long lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    /* public String locationDateInCurrentTimezone(String timezone) {
        if (provider != null && provider.getSrvName().equals("Skywave")) {
            if (timezone == null || timezone.isEmpty()) {
                timezone = "GMT-05:00";
            }
            DateFormat localFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            localFormat.setTimeZone(TimeZone.getTimeZone(timezone));

            DateFormat utcFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            utcFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

            if (locationDate != null) {
                try {
                    Date date = utcFormat.parse(localFormat.format(locationDate));
                    return localFormat.format(date) + " " + timezone;
                } catch (ParseException parseException) {
                }
                return utcFormat.format(locationDate);
            }
        } else {
            DateFormat localFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
            if (locationDate != null) {
                return localFormat.format(locationDate);
            }
        }
        return "";
    }

    public String getMmsi() {
        return mmsi;
    }

    public void setMmsi(String mmsi) {
        this.mmsi = mmsi;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public Flags getTransFlag() {
        return transFlag;
    }

    public void setTransFlag(Flags transFlag) {
        if(transFlag != null && (this.flag == null || !this.flag.equalsIgnoreCase(transFlag.getFlagFilename()))) {
            this.flag = transFlag.getFlagFilename();
        }
        this.transFlag = transFlag;
    }*/
    public String getVehicleName() {
        return vehicleName != null && !vehicleName.isEmpty() ? vehicleName : vhcPlaca;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

//    public String getImei() {
//        return imei;
//    }
//
//    public void setImei(String imei) {
//        this.imei = imei;
//    }
//
//    public String getSim() {
//        return sim;
//    }
//
//    public void setSim(String sim) {
//        this.sim = sim;
//    }
//
//    public String getApn() {
//        return apn;
//    }
//
//    public void setApn(String apn) {
//        this.apn = apn;
//    }
    public VehicleLocation getLastVehicleLocation() {
        if (lastVehicleLocation == null && lastLocationJson != null) {
            try {
                lastVehicleLocation = JsonTbtravelHandler.createVehicleLocationFromJson(lastLocationJson);
                lastVehicleLocation.setVehicleId(this);
            } catch (Exception ex) {

            }
        }
        return lastVehicleLocation;
    }

    public void setLastVehicleLocation(VehicleLocation lastVehicleLocation) {
        this.lastVehicleLocation = lastVehicleLocation;
    }
//
//    public int getDestinations() {
//        return destinations;
//    }
//
//    public void setDestinations(int destinations) {
//        this.destinations = destinations;
//    }
//

    public boolean isCreateTextFile() {
        return createTextFile;
    }

    public void setCreateTextFile(boolean createTextFile) {
        this.createTextFile = createTextFile;
    }

//    public boolean isCreatedFileForLastLocation() {
//        return createdFileForLastLocation;
//    }
//
//    public void setCreatedFileForLastLocation(boolean createdFileForLastLocation) {
//        this.createdFileForLastLocation = createdFileForLastLocation;
//    }
//
//    public boolean isConvertSpeedToKnots() {
//        return convertSpeedToKnots;
//    }
//
//    public void setConvertSpeedToKnots(boolean convertSpeedToKnots) {
//        this.convertSpeedToKnots = convertSpeedToKnots;
//    }
    public String getUnitSubtype() {
        return unitSubtype;
    }

    public void setUnitSubtype(String unitSubtype) {
        this.unitSubtype = validateString(unitSubtype, 255);
    }

    public String fetchSubtype() {
        return this.unitSubtype != null ? getUnitSubtype() : "";
    }

    public String fetchUnitIdForServices() {
        return this.vhcWebAlias != null && !this.vhcWebAlias.isEmpty() ? this.vhcWebAlias : this.vhcPlaca;
    }
}
