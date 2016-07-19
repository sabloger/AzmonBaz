package com.kokabi.p.azmonbaz.Objects;

import java.util.ArrayList;

/**
 * Created by P.Kokabi on 6/29/16.
 */
public class TestDefinitionObj {

    int idTest, questionNo, percentage;
    String level;
    ArrayList<TestObj> questionInfo = new ArrayList<>();

    public TestDefinitionObj(int idTest, int questionNo, ArrayList<TestObj> questionInfo, int percentage, String level) {
        this.idTest = idTest;
        this.questionNo = questionNo;
        this.questionInfo = questionInfo;
        this.percentage = percentage;
        this.level = level;
    }

    public int getIdTest() {
        return idTest;
    }

    public void setIdTest(int idTest) {
        this.idTest = idTest;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public void setQuestionNo(int questionNo) {
        this.questionNo = questionNo;
    }

    public int getPercentage() {
        return percentage;
    }

    public void setPercentage(int percentage) {
        this.percentage = percentage;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public ArrayList<TestObj> getQuestionInfo() {
        return questionInfo;
    }

    public void setQuestionInfo(ArrayList<TestObj> questionInfo) {
        this.questionInfo = questionInfo;
    }
}
