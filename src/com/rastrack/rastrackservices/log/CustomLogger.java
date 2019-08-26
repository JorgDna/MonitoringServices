/*                                             
 *                            
 *Proyecto: RastrackServices
 *Módulo:
 *Autor: Jorge Espinosa 
 *Fecha: 27/03/2019
 *Descripción: 
 *
 */
package com.rastrack.rastrackservices.log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;

public class CustomLogger {

    private synchronized static void writeLog(String message) {
        Calendar date = Calendar.getInstance();
        String log = "log-" + date.get(Calendar.YEAR) + (date.get(Calendar.MONTH) + 1) + date.get(Calendar.DATE) + ".txt";
        File f = new File(log);
        FileWriter w = null;
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(CustomLogger.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            w = new FileWriter(log, true); //the true will append the new data
            w.write("Thread - " + Thread.currentThread().getName() + "  " + new Date() + ": " + message + "\r\n");//appends the string             
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(CustomLogger.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (w != null) {
                try {
                    w.close();
                } catch (IOException ex) {
                    java.util.logging.Logger.getLogger(CustomLogger.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    public synchronized static void writeMessage(String message) {
        writeLog(message);
    }

    public synchronized static void writeException(String message, Throwable ex) {
        StringBuilder msg = new StringBuilder();
        msg.append(message);
        msg.append(" Exception ");
        msg.append(ex.getMessage());
        msg.append("..  \n");
        for (StackTraceElement el : ex.getStackTrace()) {
            msg.append(el.getClassName());
            msg.append("..");
            msg.append(el.getMethodName());
            msg.append(el.getLineNumber());
        }
        writeLog(msg.toString());
    }
}
