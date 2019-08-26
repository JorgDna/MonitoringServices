/**
 * AXURE Proyecto: AXURE Módulo: Autor: Jorge Espinosa
 * <analista_seis@rastrack.com.co> Fecha: 21/05/2013 Descripción:
 */
package com.rastrack.rastrackservices.persistence;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface ISqlManagement {

    public int create(String sql, Map<Integer, Object> params) throws SQLException, ClassNotFoundException, IOException;

    public void update(String sql, Map<Integer, Object> params) throws SQLException, ClassNotFoundException, IOException;

    public List<Map> select(String sql, Map<Integer, Object> params) throws SQLException, ClassNotFoundException, IOException;

    public List<Map> executeSPResultSet(String sql, Map<String, Object> params) throws ClassNotFoundException, SQLException, IOException;

    public void executeSPNamed(String sql, Map<String, Object> params) throws ClassNotFoundException, SQLException, IOException;

    public void executeSPIndexed(String sql, Map<Integer, Object> params) throws ClassNotFoundException, SQLException, IOException;
}
