package com.kokabi.p.azmonbaz.Objects;

/**
 * Created by Payam on 10/5/15.
 */
public class MainActivityNavObj {

    private String title;
    private int resIcon;
    boolean isLine = false;

    public MainActivityNavObj(String title, int resIcon) {
        this.title = title;
        this.resIcon = resIcon;
    }

    public MainActivityNavObj(boolean isLine) {
        this.isLine = isLine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getResIcon() {
        return resIcon;
    }

    public boolean isLine() {
        return isLine;
    }

}