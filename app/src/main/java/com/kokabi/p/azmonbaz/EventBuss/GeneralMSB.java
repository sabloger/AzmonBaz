package com.kokabi.p.azmonbaz.EventBuss;

/**
 * Created by Payam on 10/3/15.
 */
public class GeneralMSB {

    String message;
    int id;

    public GeneralMSB(String message) {
        this.message = message;
    }

    public GeneralMSB(String message, int id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

}