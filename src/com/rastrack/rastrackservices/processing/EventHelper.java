/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.entities.LocalizationAlert;
import com.rastrack.rastrackservices.entities.Tbvehicle;
import java.util.Date;

/**
 *
 * @author usuario
 */
public class EventHelper {

    public static LocalizationAlert createNewAlert(Tbvehicle unit) {
        LocalizationAlert newAlert = new LocalizationAlert();
        newAlert.setClient(unit.getCliId());
        newAlert.setVehicle(unit.getVhcPlaca());
        newAlert.setReportDate(new Date());
        newAlert.setRuleType(4);
        newAlert.setMessage("Reporte de Panico: " + ("Unidad " + unit.getVehicleName() != null ? unit.getVehicleName() : unit.getVhcPlaca()));
        return newAlert;
    }
}
