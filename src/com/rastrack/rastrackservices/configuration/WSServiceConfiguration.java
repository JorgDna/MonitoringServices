/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.configuration;

/**
 *
 * @author usuario
 */
public class WSServiceConfiguration {

    public static final String URL = "url";
    public static final String ID = "id";
    public static final String STOP = "stop";
    public static final String FREQUENCY = "frequency";
    public static final String NAME = "nameService";
    public static final String GOOGLE_URL = "googleUrl";
    public static final String THREADSNUMBER = "threadsnumber";

    private final String url;
    private final int id;
    private final boolean stop;
    private final int frequency;
    private final String nameService;
    private String googleURL;
    private int threadsNumber;
    private int millsToWait;
    private boolean parameterized;

    WSServiceConfiguration(String url, int id, boolean stop, int frequency, String nameService, int threadsNumber, int millsToWait) {
        this.url = url;
        this.id = id;
        this.stop = stop;
        this.frequency = frequency;
        this.nameService = nameService;
        this.threadsNumber = threadsNumber;
        this.millsToWait = millsToWait;
    }

    /**
     * @return the url
     */
    public String getUrl() {
        return url;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the stop
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * @return the frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * @return the nameService
     */
    public String getNameService() {
        return nameService;
    }

    /**
     * @return the googleURL
     */
    public String getGoogleURL() {
        return googleURL;
    }

    /**
     * @param googleURL the googleURL to set
     */
    void setGoogleURL(String googleURL) {
        this.googleURL = googleURL;
    }

    /**
     * @return the threadsNumber
     */
    public int getThreadsNumber() {
        return threadsNumber;
    }

    /**
     * @param threadsNumber the threadsNumber to set
     */
    public void setThreadsNumber(int threadsNumber) {
        this.threadsNumber = threadsNumber;
    }

    /**
     * @return the millsToWait
     */
    public int getMillsToWait() {
        return millsToWait;
    }

    /**
     * @param millsToWait the millsToWait to set
     */
    public void setMillsToWait(int millsToWait) {
        this.millsToWait = millsToWait;
    }

    /**
     * @return the parameterized
     */
    public boolean isParameterized() {
        return parameterized;
    }

    /**
     * @param parameterized the parameterized to set
     */
    public void setParameterized(boolean parameterized) {
        this.parameterized = parameterized;
    }

}
