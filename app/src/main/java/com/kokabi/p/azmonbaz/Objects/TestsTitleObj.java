package com.kokabi.p.azmonbaz.Objects;

import android.support.annotation.NonNull;

/**
 * Created by P.Kokabi on 6/29/16.
 */
public class TestsTitleObj implements Comparable<TestsTitleObj> {

    int idCat, idTest, questionCount, time;
    String testName, description, testOrder;
    boolean hasNegativePoint;

    public TestsTitleObj() {
    }

    public TestsTitleObj(int idTest) {
        this.idTest = idTest;
    }

    public int getIdCat() {
        return idCat;
    }

    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }

    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }

    public String getTestOrder() {
        return testOrder;
    }

    public void setTestOrder(String testOrder) {
        this.testOrder = testOrder;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isHasNegativePoint() {
        return hasNegativePoint;
    }

    public void setHasNegativePoint(boolean hasNegativePoint) {
        this.hasNegativePoint = hasNegativePoint;
    }

    @Override
    public int compareTo(@NonNull TestsTitleObj testsTitleObj) {
        return this.getTestOrder().compareTo(testsTitleObj.getTestOrder());
    }
}
