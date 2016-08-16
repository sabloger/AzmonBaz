package com.kokabi.p.azmonbaz.Help;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 * Created by P.Kokabi on 6/29/16.
 */
public class ReadJSON {

    public static String readRawResource(String jsonPathName) {
        try {
            return readStream(AppController.getCurrentContext().getAssets().open("Json/" + jsonPathName));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String readStream(InputStream is) {
        // http://stackoverflow.com/a/5445161
        Scanner s = new Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
