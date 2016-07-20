package com.kokabi.p.azmonbaz.EventBussObj;

/**
 * Created by Payam on 10/3/15.
 */
public class GeneralMSB {

    String message;

    public GeneralMSB(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}