package com.kokabi.p.azmonbaz.Objects;

import android.support.annotation.NonNull;

/**
 * Created by P.Kokabi on 6/29/16.
 */
public class CategoryObj implements Comparable<CategoryObj> {

    int idCat, backImage, idParent;
    String catName, resIcon,categoryOrder, backColor, textColor;

    public CategoryObj(int idCat, String catName, String backColor, String textColor, int backImage, String resIcon, String categoryOrder, int idParent) {
        this.idCat = idCat;
        this.catName = catName;
        this.backColor = backColor;
        this.textColor = textColor;
        this.backImage = backImage;
        this.resIcon = resIcon;
        this.categoryOrder = categoryOrder;
        this.idParent = idParent;
    }

    public int getIdCat() {
        return idCat;
    }

    public void setIdCat(int idCat) {
        this.idCat = idCat;
    }

    public int getBackImage() {
        return backImage;
    }

    public void setBackImage(int backImage) {
        this.backImage = backImage;
    }

    public String getResIcon() {
        return resIcon;
    }

    public void setResIcon(String resIcon) {
        this.resIcon = resIcon;
    }

    public int getIdParent() {
        return idParent;
    }

    public void setIdParent(int idParent) {
        this.idParent = idParent;
    }

    public String getCategoryOrder() {
        return categoryOrder;
    }

    public void setCategoryOrder(String categoryOrder) {
        this.categoryOrder = categoryOrder;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getBackColor() {
        return backColor;
    }

    public void setBackColor(String backColor) {
        this.backColor = backColor;
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
}
