/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.util;

import java.util.Arrays;

/**
 *
 * @author usuario
 */
public class MessageLog {

    private String objecToProcces;

    private String state;

    private StringBuilder resultMessage;

    private Throwable exception;

    public MessageLog() {
    }

    public MessageLog(String objecToProcces, String state, StringBuilder resultMessage) {
        this.objecToProcces = objecToProcces;
        this.state = state;
        this.resultMessage = resultMessage;
    }

    public MessageLog(String objecToProcces, String state, StringBuilder resultMessage, Throwable exception) {
        this.objecToProcces = objecToProcces;
        this.state = state;
        this.resultMessage = resultMessage;
        this.exception = exception;
    }

    public void addResultMessage(String messege) {
        if (resultMessage == null) {
            resultMessage = new StringBuilder();
        }
        resultMessage.append(" ");
        resultMessage.append(messege);
    }

    @Override
    public String toString() {
        String build = "Object processed: " + objecToProcces + " | State: " + state + " | ResultMessage: " + resultMessage.toString() + "\n";
        if (exception != null) {
            build = build + " Exception: " + exception.getMessage() + "\n";
            build = build + Arrays.toString(exception.getStackTrace()).replace(",", "\n");
        }
        return build;
    }

    /**
     * @return the objecToProcces
     */
    public String getObjecToProcces() {
        return objecToProcces;
    }

    /**
     * @param objecToProcces the objecToProcces to set
     */
    public void setObjecToProcces(String objecToProcces) {
        this.objecToProcces = objecToProcces;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the resultMessage
     */
    public StringBuilder getResultMessage() {
        return resultMessage;
    }

    /**
     * @param resultMessage the resultMessage to set
     */
    public void setResultMessage(StringBuilder resultMessage) {
        this.resultMessage = resultMessage;
    }

    /**
     * @return the exception
     */
    public Throwable getException() {
        return exception;
    }

    /**
     * @param exception the exception to set
     */
    public void setException(Throwable exception) {
        this.exception = exception;
    }

}
