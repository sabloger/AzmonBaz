package com.kokabi.p.azmonbaz.EventBuss;

import com.kokabi.p.azmonbaz.Objects.TestsTitleObj;

/**
 * Created by Payam on 10/3/15.
 */
public class GeneralMSB {

    String message, breadCrumb, testName;
    int id;
    TestsTitleObj testsTitleObj = new TestsTitleObj();

    public GeneralMSB(String message) {
        this.message = message;
    }

    public GeneralMSB(String message, int id) {
        this.message = message;
        this.id = id;
    }

    public GeneralMSB(String message, TestsTitleObj testsTitleObj) {
        this.message = message;
        this.testsTitleObj = testsTitleObj;
    }

    public GeneralMSB(String message, TestsTitleObj testsTitleObj, String breadCrumb) {
        this.message = message;
        this.testsTitleObj = testsTitleObj;
        this.breadCrumb = breadCrumb;
    }

    public GeneralMSB(String message, int id, String breadCrumb, String testName) {
        this.message = message;
        this.id = id;
        this.breadCrumb = breadCrumb;
        this.testName = testName;
    }

    public String getMessage() {
        return message;
    }

    public int getId() {
        return id;
    }

    public TestsTitleObj getTestsTitleObj() {
        return testsTitleObj;
    }

    public String getBreadCrumb() {
        return breadCrumb;
    }

    public String getTestName() {
        return testName;
    }
}