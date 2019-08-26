/*                                             
 *                      
 *Proyecto: 
 *Módulo:
 *Autor: Jorge Espinosa 
 *Fecha: 28/03/2019
 *Descripción: 
 *
 */
package com.rastrack.rastrackservices.persistence;

import com.rastrack.rastrackservices.configuration.AppConfiguration;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlManagment implements ISqlManagement {

    @Override
    public int create(String sql, Map<Integer, Object> params) throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = (Connection) com.rastrack.rastrackservices.persistence.Connection.getConnection(AppConfiguration.getInstance())) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            setParams(prepareStatement, params);
            prepareStatement.executeUpdate();
            ResultSet rs = prepareStatement.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    @Override
    public void update(String sql, Map<Integer, Object> params) throws SQLException, ClassNotFoundException, IOException {
        try (Connection connection = (Connection) com.rastrack.rastrackservices.persistence.Connection.getConnection(AppConfiguration.getInstance())) {
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            setParams(prepareStatement, params);
            prepareStatement.executeUpdate();
        }
    }    
   
    @Override
    public List<Map> select(String sql, Map<Integer, Object> params) throws ClassNotFoundException, SQLException, IOException {
        try (Connection connection = (Connection) com.rastrack.rastrackservices.persistence.Connection.getConnection(AppConfiguration.getInstance())) {
            List<Map> list = new ArrayList();
            PreparedStatement prepareStatement = connection.prepareStatement(sql);
            setParams(prepareStatement, params);
            ResultSet rs = prepareStatement.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                Map map = new HashMap();
                int i = 1;
                while (i <= rsmd.getColumnCount()) {
                    map.put(rsmd.getColumnName(i), rs.getObject(i));
                    i++;
                }
                list.add(map);
            }
            return list;
        }
    }

    @Override
    public List<Map> executeSPResultSet(String sql, Map<String, Object> params) throws ClassNotFoundException, SQLException, IOException {
        try (Connection connection = (Connection) com.rastrack.rastrackservices.persistence.Connection.getConnection(AppConfiguration.getInstance())) {
            List<Map> list = new ArrayList<>();
            CallableStatement proc = connection.prepareCall(sql);
            setParams(proc, params);
            proc.execute();
            ResultSet rs = proc.getResultSet();
            while (rs == null) {
                proc.getMoreResults();
                rs = proc.getResultSet();
            }
            ResultSetMetaData rsmd = rs.getMetaData();
            while (rs.next()) {
                Map map = new HashMap();
                int i = 1;
                while (i <= rsmd.getColumnCount()) {
                    map.put(rsmd.getColumnName(i), rs.getObject(i));
                    i++;
                }
                list.add(map);
            }
            return list;
        }
    }

    @Override
    public void executeSPNamed(String sql, Map<String, Object> params) throws ClassNotFoundException, SQLException, IOException {
        try (Connection connection = (Connection) com.rastrack.rastrackservices.persistence.Connection.getConnection(AppConfiguration.getInstance())) {
            int count = countParameters(sql, connection);
            CallableStatement proc = connection.prepareCall(generateQuery(sql, count));
            setParams(proc, params);
            proc.executeUpdate();
        }
    }

    @Override
    public void executeSPIndexed(String sql, Map<Integer, Object> params) throws ClassNotFoundException, SQLException, IOException {
        try (Connection connection = (Connection) com.rastrack.rastrackservices.persistence.Connection.getConnection(AppConfiguration.getInstance())) {
            CallableStatement proc = connection.prepareCall(sql);
            setParams(proc, params);
            proc.executeUpdate();
        }
    }

    private void setParams(PreparedStatement proc, Map<Integer, Object> params) throws SQLException {
        if (params != null) {
            for (int key : params.keySet()) {
                Object ob = params.get(key);
                if (ob == null) {
                    proc.setNull(key, java.sql.Types.NULL);
                    continue;
                }
                if (ob instanceof String) {
                    proc.setString(key, (String) ob);
                    continue;
                }
                if (ob instanceof Integer) {
                    proc.setInt(key, (int) ob);
                    continue;
                }
                if (ob instanceof Float) {
                    proc.setFloat(key, (float) ob);
                    continue;
                }
                if (ob instanceof Timestamp) {
                    proc.setTimestamp(key, (Timestamp) ob);
                    continue;
                }
                if (ob instanceof Date) {
                    proc.setTimestamp(key, new java.sql.Timestamp(((Date) ob).getTime()));
                    continue;
                }
                if (ob instanceof Boolean) {
                    proc.setBoolean(key, (boolean) ob);
                    continue;
                }
                
                 if (ob instanceof Long) {
                    proc.setDouble(key, (long) ob);
                    continue;
                }

                if (ob instanceof Double) {
                    proc.setDouble(key, (Double) ob);
                    continue;
                }

            }
        }
    }

    private void setParams(CallableStatement proc, Map<String, Object> params) throws SQLException {
        if (params != null) {
            ParameterMetaData data = proc.getParameterMetaData();
            for (int i = 1; i <= data.getParameterCount(); i++) {
                proc.setNull(i, data.getParameterType(i));
            }
            for (String key : params.keySet()) {
                Object ob = params.get(key);
                if (ob instanceof String) {
                    proc.setString(key, (String) ob);
                    continue;
                }
                if (ob instanceof Long) {
                    proc.setLong(key, (long) ob);
                    continue;
                }
                if (ob instanceof Integer) {
                    proc.setInt(key, (int) ob);
                    continue;
                }
                if (ob instanceof Float) {
                    proc.setFloat(key, (float) ob);
                    continue;
                }
                if (ob instanceof Timestamp) {
                    proc.setTimestamp(key, (Timestamp) ob);
                    continue;
                }
                if (ob instanceof Date) {
                    proc.setDate(key, new java.sql.Date(((Date) ob).getTime()));
                    continue;
                }
                if (ob instanceof Boolean) {
                    proc.setBoolean(key, (boolean) ob);
                    continue;
                }
            }
        }
    }

    private String generateQuery(String callSp, int count) {
        StringBuilder query = new StringBuilder();
//        query.append("{ ");
        query.append(callSp);
        query.append(" ( ");
        for (int i = 0; i < count; i++) {
            query.append("?");
            if ((i + 1) < count) {
                query.append(",");
            }
        }
        query.append(")}");
        return query.toString();
    }

    private int countParameters(String sql, Connection con) throws SQLException {
        int count = 0;
        CallableStatement proc = null;
        try {
            proc = con.prepareCall(generateQuery(sql, 50));
            count = proc.getParameterMetaData().getParameterCount();
        } finally {
            if (proc != null) {
                proc.close();
            }
        }
        return count;
    }
}
