package com.kokabi.p.azmonbaz.Objects;

import android.support.annotation.NonNull;

/**
 * Created by P.Kokabi on 7/8/16.
 */
public class HistoryObj implements Comparable<HistoryObj> {

    int idHistory, idTest, answeredQuestion, incorrectQuestion, unAnsweredQuestion;
    String testName, testPercentage, testTime, updateTime;

    public HistoryObj(int idTest, String testName, String testTime, String testPercentage, int answeredQuestion,
                      int incorrectQuestion, int unAnsweredQuestion, String updateTime) {
        this.idTest = idTest;
        this.testName = testName;
        this.testTime = testTime;
        this.testPercentage = testPercentage;
        this.answeredQuestion = answeredQuestion;
        this.incorrectQuestion = incorrectQuestion;
        this.unAnsweredQuestion = unAnsweredQuestion;
        this.updateTime = updateTime;
    }

    public HistoryObj(int idHistory, int idTest, String testName, String testTime, String testPercentage, int answeredQuestion,
                      int incorrectQuestion, int unAnsweredQuestion, String updateTime) {
        this.idHistory = idHistory;
        this.idTest = idTest;
        this.testName = testName;
        this.testTime = testTime;
        this.testPercentage = testPercentage;
        this.answeredQuestion = answeredQuestion;
        this.incorrectQuestion = incorrectQuestion;
        this.unAnsweredQuestion = unAnsweredQuestion;
        this.updateTime = updateTime;
    }

    public int getIdHistory() {
        return idHistory;
    }

    public void setIdHistory(int idHistory) {
        this.idHistory = idHistory;
    }

    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    public int getAnsweredQuestion() {
        return answeredQuestion;
    }

    public void setAnsweredQuestion(int answeredQuestion) {
        this.answeredQuestion = answeredQuestion;
    }

    public int getIncorrectQuestion() {
        return incorrectQuestion;
    }

    public void setIncorrectQuestion(int incorrectQuestion) {
        this.incorrectQuestion = incorrectQuestion;
    }

    public int getUnAnsweredQuestion() {
        return unAnsweredQuestion;
    }

    public void setUnAnsweredQuestion(int unAnsweredQuestion) {
        this.unAnsweredQuestion = unAnsweredQuestion;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getTestPercentage() {
        return testPercentage;
    }

    public void setTestPercentage(String testPercentage) {
        this.testPercentage = testPercentage;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int compareTo(@NonNull HistoryObj h) {
        return this.getTestTime().compareTo(h.getTestTime());
    }
}
