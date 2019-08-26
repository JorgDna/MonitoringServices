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
public class RuleActions {

    private static final long serialVersionUID = 1L;
    
    private Integer id;
    
    private int actionType;
    
    private String emails;
   
    private String webMessage;

    public RuleActions() {
    }

    public RuleActions(Integer id) {
        this.id = id;
    }

    public RuleActions(Integer id, int actionType, String emails, String webMessages) {
        this.id = id;
        this.actionType = actionType;
        this.emails = emails;
        this.webMessage = webMessages;        
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getActionType() {
        return actionType;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getWebMessage() {
        return webMessage;
    }

    public void setWebMessage(String webMessage) {
        this.webMessage = webMessage;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof RuleActions)) {
            return false;
        }
        RuleActions other = (RuleActions) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.rastrack.entitiesCRT.RuleActions[ id=" + id + " ]";
    }

}
