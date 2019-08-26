/*                                             
 *                            AXURE
 *Proyecto: AXURE
 *Módulo:
 *Autor: Jorge Espinosa <analista_seis@rastrack.com.co>
 *Fecha: 21/05/2013
 *Descripción: 
 *
 */
package com.rastrack.rastrackservices.persistence;

import com.rastrack.rastrackservices.configuration.AppConfiguration;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connection {

    public static java.sql.Connection getConnection(AppConfiguration app) throws ClassNotFoundException, SQLException {
        Class.forName(app.getStringProperty(AppConfiguration.DRIVER));
        return (java.sql.Connection) DriverManager.getConnection(app.getStringProperty(AppConfiguration.URL), app.getStringProperty(AppConfiguration.USER), app.getStringProperty(AppConfiguration.PASSWORD));
    }

    public static java.sql.Connection getConnection(String driver, String url, String user, String password) throws ClassNotFoundException, SQLException {
        Class.forName(driver);
        return (java.sql.Connection) DriverManager.getConnection(url, user, password);
    }
}
