package com.kokabi.p.azmonbaz.Objects;

import android.support.annotation.NonNull;

/**
 * Created by P.Kokabi on 6/29/16.
 */
public class CategoryObj implements Comparable<CategoryObj> {

    int idCat, backImage, idParent;
    String catName, resIcon, categoryOrder, backColor, textColor;

    public int getIdCat() {
        return idCat;
    }

    public int getBackImage() {
        return backImage;
    }

    public String getResIcon() {
        return resIcon;
    }

    public int getIdParent() {
        return idParent;
    }

    public String getCategoryOrder() {
        return categoryOrder;
    }

    public String getCatName() {
        return catName;
    }

    public String getBackColor() {
        return backColor;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    @Override
    public int compareTo(@NonNull CategoryObj c) {
        return this.getCategoryOrder().compareTo(c.getCategoryOrder());
    }

    @Override
    public String toString() {
        return "CategoryObj{" +
                "idCat=" + idCat +
                ", backImage=" + backImage +
                ", idParent=" + idParent +
                ", catName='" + catName + '\'' +
                ", resIcon='" + resIcon + '\'' +
                ", categoryOrder='" + categoryOrder + '\'' +
                ", backColor='" + backColor + '\'' +
                ", textColor='" + textColor + '\'' +
                '}';
    }
}
