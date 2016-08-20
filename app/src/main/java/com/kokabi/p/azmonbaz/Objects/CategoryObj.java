package com.kokabi.p.azmonbaz.Objects;

import android.support.annotation.NonNull;

/**
 * Created by P.Kokabi on 6/29/16.
 */
public class CategoryObj implements Comparable<CategoryObj> {

    int idCat, idParent;
    String catName, backImage, categoryOrder;

    public int getIdCat() {
        return idCat;
    }

    public int getIdParent() {
        return idParent;
    }

    public String getCatName() {
        return catName;
    }

    public String getBackImage() {
        return backImage;
    }

    public String getCategoryOrder() {
        return categoryOrder;
    }

    @Override
    public int compareTo(@NonNull CategoryObj c) {
        try {
            return this.categoryOrder.compareTo(c.getCategoryOrder());
        } catch (Exception e) {
            return Integer.parseInt(e.toString());
        }
    }
}
