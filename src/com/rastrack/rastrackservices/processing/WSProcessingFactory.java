/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.configuration.WSServiceConfiguration;

/**
 *
 * @author usuario
 */
public class WSProcessingFactory {

    public static IProcessing getWSThreadProcessing(WSServiceConfiguration configWs, int server) {
        switch (server) {
            case 1: {
                return new WSWidtechProcessing(configWs);
            }
            case 2: {
                return new WSSkyWaveProcessing(configWs);
            }
            default: {
                return new WSInReachProcessing(configWs);
            }

        }

    }

}
