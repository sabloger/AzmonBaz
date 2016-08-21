package com.kokabi.p.azmonbaz.Objects;

import java.util.ArrayList;

/**
 * Created by P.Kokabi on 6/29/16.
 */
public class TestDefinitionObj {

    int idTest = 0, questionNo = 0, percentage = 0;
    ArrayList<TestObj> questionInfo = new ArrayList<>();

    public TestDefinitionObj(int idTest, int questionNo, ArrayList<TestObj> questionInfo, int percentage) {
        this.idTest = idTest;
        this.questionNo = questionNo;
        this.questionInfo = questionInfo;
        this.percentage = percentage;
    }

    public int getIdTest() {
        return idTest;
    }

    public int getQuestionNo() {
        return questionNo;
    }

    public int getPercentage() {
        return percentage;
    }

    public ArrayList<TestObj> getQuestionInfo() {
        return questionInfo;
    }
}
