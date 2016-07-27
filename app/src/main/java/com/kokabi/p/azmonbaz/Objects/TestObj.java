package com.kokabi.p.azmonbaz.Objects;

/**
 * Created by P.kokabi on 7/19/2016.
 */
public class TestObj {

    int id, k, state, level;
    String q, a, testName;

    public TestObj() {
    }

    public TestObj(String testName, int idQuestion, String questionImage, String answerImage, int key) {
        this.testName = testName;
        this.id = idQuestion;
        this.q = questionImage;
        this.a = answerImage;
        this.k = key;
    }

    public int getIdQuestion() {
        return id;
    }

    public void setIdQuestion(int idQuestion) {
        this.id = idQuestion;
    }

    public int getKey() {
        return k;
    }

    public void setKey(int key) {
        this.k = key;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getQuestionImage() {
        return q;
    }

    public void setQuestionImage(String questionImage) {
        this.q = questionImage;
    }

    public String getAnswerImage() {
        return a;
    }

    public void setAnswerImage(String answerImage) {
        this.a = answerImage;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
