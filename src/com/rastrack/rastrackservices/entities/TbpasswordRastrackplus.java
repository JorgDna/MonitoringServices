/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.entities;

/**
 *
 * @author usuario
 */
public class TbpasswordRastrackplus {

    private static final long serialVersionUID = 1L;

    private Integer urpId;

    private String uspUser;

    private String uspPassword;

    private Integer cliId;

    private Integer provider;

    private String lastMessageId;

    private boolean status;

    public TbpasswordRastrackplus() {
    }

    public TbpasswordRastrackplus(Integer urpId) {
        this.urpId = urpId;
    }

    public TbpasswordRastrackplus(Integer urpId, Integer cliId, String uspUser, String uspPassword, Integer provider, String lastMessageId, boolean status) {
        this.urpId = urpId;
        this.uspUser = uspUser;
        this.uspPassword = uspPassword;
        this.cliId = cliId;
        this.lastMessageId = lastMessageId;
        this.status = status;
        this.provider = provider;
    }

    public Integer getUrpId() {
        return urpId;
    }

    public void setUrpId(Integer urpId) {
        this.urpId = urpId;
    }

    public String getUspUser() {
        return uspUser;
    }

    public void setUspUser(String uspUser) {
        this.uspUser = uspUser;
    }

    public String getUspPassword() {
        return uspPassword;
    }

    public void setUspPassword(String uspPassword) {
        this.uspPassword = uspPassword;
    }

    public Integer getCliId() {
        return cliId;
    }

    public void setCliId(Integer cliId) {
        this.cliId = cliId;
    }

    public Integer getProvider() {
        return provider;
    }

    public void setProvider(Integer provider) {
        this.provider = provider;
    }

    public String getLastMessageId() {
        return lastMessageId;
    }

    public void setLastMessageId(String lastMessageId) {
        this.lastMessageId = lastMessageId;
    }

    public int getLastMessageIdInNumber() {
        if (this.lastMessageId != null) {
            try {
                return Integer.parseInt(this.lastMessageId);
            } catch (Exception ex) {
            }
        }
        return 0;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

}
