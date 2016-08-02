package com.kokabi.p.azmonbaz.EventBuss;

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
}