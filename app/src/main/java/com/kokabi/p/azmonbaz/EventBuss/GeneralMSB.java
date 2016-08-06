package com.kokabi.p.azmonbaz.EventBuss;

import com.kokabi.p.azmonbaz.Objects.HistoryObj;
import com.kokabi.p.azmonbaz.Objects.TestObj;
import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;

/**
 * Created by Payam on 10/3/15.
 */
public class GeneralMSB {

    String message;
    int position;
    TestObj testItem;
    HistoryObj historyObj;
    TestsTitleObj testsTitleObj;

    public GeneralMSB(String message) {
        this.message = message;
    }

    public GeneralMSB(String message, TestObj testItem, int position) {
        this.message = message;
        this.testItem = testItem;
        this.position = position;
    }

    public GeneralMSB(String message, HistoryObj historyObj, int position) {
        this.message = message;
        this.historyObj = historyObj;
        this.position = position;
    }

    public GeneralMSB(String message, TestsTitleObj testsTitleObj, int position) {
        this.message = message;
        this.testsTitleObj = testsTitleObj;
        this.position = position;
    }

    public String getMessage() {
        return message;
    }

    public TestObj getTestItem() {
        return testItem;
    }

    public HistoryObj getHistoryObj() {
        return historyObj;
    }

    public TestsTitleObj getTestsTitleObj() {
        return testsTitleObj;
    }

    public int getPosition() {
        return position;
    }

}