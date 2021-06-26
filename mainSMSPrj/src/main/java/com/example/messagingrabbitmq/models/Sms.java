package com.example.messagingrabbitmq.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sms {

    private String number;
    private String text;

    private static final Logger log = LoggerFactory.getLogger(Sms.class);
    
    public static enum STATUS {
        SMS_NEW,
        SMS_QUEUED,
        SMS_SEND_SUCCESS,
        SMS_SEND_FAILED
    };

    private STATUS smsStatus;
    

    public Sms() {
    }

    public Sms(String number, String text, STATUS smsStatus) {
        this.number = number;
        this.text = text;
        this.smsStatus = smsStatus;
    }

    public String getNumber() {
        return this.number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public STATUS getSmsStatus() {
        return this.smsStatus;
    }

    public void setSmsStatus(STATUS smsStatus) {
        log.info("Sms status changed to " + smsStatus);
        this.smsStatus = smsStatus;
    }

    @Override
    public String toString() {
        return "{" +
            " number='" + getNumber() + "'" +
            ", text='" + getText() + "'" +
            ", smsStatus='" + getSmsStatus() + "'" +
            "}";
    }


}
