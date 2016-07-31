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

    public TestsTitleObj(int idTest, String testName, boolean hasNegativePoint, int time) {
        this.idTest = idTest;
        this.testName = testName;
        this.hasNegativePoint = hasNegativePoint;
        this.time = time;
    }

    /*Getters*/
    public TestsTitleObj(int idTest) {
        this.idTest = idTest;
    }

    public int getIdCat() {
        return idCat;
    }

    public int getIdTest() {
        return idTest;
    }

    public int getQuestionCount() {
        return questionCount;
    }

    public String getTestOrder() {
        return testOrder;
    }

    public String getTestName() {
        return testName;
    }

    public String getDescription() {
        return description;
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

    /*Setters*/
    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    @Override
    public int compareTo(@NonNull TestsTitleObj testsTitleObj) {
        return this.getTestOrder().compareTo(testsTitleObj.getTestOrder());
    }

    @Override
    public String toString() {
        return "TestsTitleObj{" +
                "idCat=" + idCat +
                ", idTest=" + idTest +
                ", questionCount=" + questionCount +
                ", time=" + time +
                ", testName='" + testName + '\'' +
                ", description='" + description + '\'' +
                ", testOrder='" + testOrder + '\'' +
                ", hasNegativePoint=" + hasNegativePoint +
                '}';
    }
}
