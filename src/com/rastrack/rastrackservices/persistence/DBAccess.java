package com.rastrack.rastrackservices.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Esta clase abstracta contiene los atributos que almacenan las credenciales de
 * conexión. También posee métodos especializados en levantar la conexión y en
 * eliminarla.
 *
 * @author Ricardo Betancourt <analista_tres@rastrack.com.co>
 * @version 1.0.0
 */
public abstract class DBAccess {

    protected final String url;
    protected final String driver;
    protected final String user;
    protected final String password;
    protected Connection connectionHandler = null;

    /**
     * Este constructor recibe como parámetros los datos necesarios para
     * realizar la conexión a una base de datos
     *
     * @param url Contiene la url de conexión a la base de datos
     * @param driver Nombre del driver que debe cargarse a la memoria
     * @param user Código del usuario de la base de datos
     * @param password Contraseña
     */
    public DBAccess(String url, String driver, String user, String password) {
        this.url = url;
        this.driver = driver;
        this.user = user;
        this.password = password;
    }

    /**
     * Este método se encarga de conectar con la base de datos. Si dicha
     * conexión falla, retornará un false.
     *
     * @throws Exception lanzada en caso de encontrar alguna novedad con la
     * conexión a la base de datos
     */
    public void openDBConnection() throws Exception {

        try {
            Class.forName(driver);
            connectionHandler = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            connectionHandler = null;
            throw e;
        }

    }

    /**
     * Método que valida si la conexión con la base de datos es válida
     *
     * @return true si la conexión es válida, false de lo contrario
     * @throws Exception lanzada en caso de que la conexión aún no haya sido
     * abierta
     */
    public boolean isConnectionOpen() throws Exception {
        boolean connectionStatus = connectionHandler.isValid(5);
        return connectionStatus;
    }

    /**
     * Método que libera la conexión actual y los recursos existentes
     */
    public void dispose() {
        try {
            if (connectionHandler != null & !connectionHandler.isClosed()) {
                connectionHandler.close();
            }
        } catch (Exception e) {
        } finally {
            connectionHandler = null;
        }
    }
}
