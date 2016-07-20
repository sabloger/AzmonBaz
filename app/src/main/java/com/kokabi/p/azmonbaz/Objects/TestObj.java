package com.kokabi.p.azmonbaz.Objects;

/**
 * Created by P.kokabi on 7/19/2016.
 */
public class TestObj {

    int idQuestion, key;
    String questionImage, answerImage, testName;

    public TestObj() {
    }

    public TestObj(int idQuestion, String questionImage, String answerImage, int key) {
        this.idQuestion = idQuestion;
        this.questionImage = questionImage;
        this.answerImage = answerImage;
        this.key = key;
    }

    public TestObj(String testName, int idQuestion, String questionImage, String answerImage, int key) {
        this.testName = testName;
        this.idQuestion = idQuestion;
        this.questionImage = questionImage;
        this.answerImage = answerImage;
        this.key = key;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(int idQuestion) {
        this.idQuestion = idQuestion;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getQuestionImage() {
        return questionImage;
    }

    public void setQuestionImage(String questionImage) {
        this.questionImage = questionImage;
    }

    public String getAnswerImage() {
        return answerImage;
    }

    public void setAnswerImage(String answerImage) {
        this.answerImage = answerImage;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }
}
