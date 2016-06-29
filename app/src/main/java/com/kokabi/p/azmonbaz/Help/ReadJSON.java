package com.kokabi.p.azmonbaz.Help;

import android.support.annotation.RawRes;

import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by P.Kokabi on 6/29/16.
 */
public class ReadJSON {

    public static String readRawResource(@RawRes int res) {
        return readStream(AppController.getCurrentContext().getResources().openRawResource(res));
    }

    private static String readStream(InputStream is) {
        // http://stackoverflow.com/a/5445161
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
