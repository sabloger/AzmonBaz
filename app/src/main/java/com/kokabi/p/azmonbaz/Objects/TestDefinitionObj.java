package com.kokabi.p.azmonbaz.Objects;

import java.util.ArrayList;

/**
 * Created by P.Kokabi on 6/29/16.
 */
public class TestDefinitionObj {

    int idTest, questionNo, percentage;
    String level;
    ArrayList<String> questionImages = new ArrayList<>();
    ArrayList<String> answerImages = new ArrayList<>();
    ArrayList<Integer> keys = new ArrayList<>();

    public TestDefinitionObj(int idTest, int questionNo, ArrayList<String> questionImages, ArrayList<String> answerImages, ArrayList<Integer> keys, int percentage, String level) {
        this.idTest = idTest;
        this.questionNo = questionNo;
        this.questionImages = questionImages;
        this.answerImages = answerImages;
        this.keys = keys;
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

    public ArrayList<String> getQuestionImages() {
        return questionImages;
    }

    public void setQuestionImages(ArrayList<String> questionImages) {
        this.questionImages = questionImages;
    }

    public ArrayList<String> getAnswerImages() {
        return answerImages;
    }

    public void setAnswerImages(ArrayList<String> answerImages) {
        this.answerImages = answerImages;
    }

    public ArrayList<Integer> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<Integer> keys) {
        this.keys = keys;
    }
}
