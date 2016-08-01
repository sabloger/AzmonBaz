package com.kokabi.p.azmonbaz.Objects;

/**
 * Created by P.kokabi on 7/19/2016.
 */
public class TestObj {

    int id, k, state, l;
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

    public int getKey() {
        return k;
    }

    public String getQuestionImage() {
        return q;
    }

    public String getAnswerImage() {
        return a;
    }

    public String getTestName() {
        return testName;
    }

    public int getLevel() {
        return l;
    }

    @Override
    public String toString() {
        return "TestObj{" +
                "id=" + id +
                ", k=" + k +
                ", state=" + state +
                ", l=" + l +
                ", q='" + q + '\'' +
                ", a='" + a + '\'' +
                ", testName='" + testName + '\'' +
                '}';
    }
}
