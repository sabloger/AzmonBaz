package com.kokabi.p.azmonbaz.Objects;

import android.support.annotation.NonNull;

/**
 * Created by P.Kokabi on 7/8/16.
 */
public class HistoryObj implements Comparable<HistoryObj> {

    int idHistory, idTest, answeredQuestion, incorrectQuestion, unAnsweredQuestion;
    String testName, testPercentage, testTime, updateTime, answerList;

    public HistoryObj(int idTest, String testName, String testTime, String testPercentage, int answeredQuestion,
                      int incorrectQuestion, int unAnsweredQuestion, String updateTime,String answerList) {
        this.idTest = idTest;
        this.testName = testName;
        this.testTime = testTime;
        this.testPercentage = testPercentage;
        this.answeredQuestion = answeredQuestion;
        this.incorrectQuestion = incorrectQuestion;
        this.unAnsweredQuestion = unAnsweredQuestion;
        this.updateTime = updateTime;
        this.answerList = answerList;
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

    public int getIdTest() {
        return idTest;
    }

    public int getAnsweredQuestion() {
        return answeredQuestion;
    }

    public int getIncorrectQuestion() {
        return incorrectQuestion;
    }

    public int getUnAnsweredQuestion() {
        return unAnsweredQuestion;
    }

    public String getTestName() {
        return testName;
    }

    public String getTestPercentage() {
        return testPercentage;
    }

    public String getTestTime() {
        return testTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public String getAnswerList() {
        return answerList;
    }

    @Override
    public int compareTo(@NonNull HistoryObj h) {
        return this.getTestTime().compareTo(h.getTestTime());
    }

    @Override
    public String toString() {
        return "HistoryObj{" +
                "idHistory=" + idHistory +
                ", idTest=" + idTest +
                ", answeredQuestion=" + answeredQuestion +
                ", incorrectQuestion=" + incorrectQuestion +
                ", unAnsweredQuestion=" + unAnsweredQuestion +
                ", testName='" + testName + '\'' +
                ", testPercentage='" + testPercentage + '\'' +
                ", testTime='" + testTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                '}';
    }
}
