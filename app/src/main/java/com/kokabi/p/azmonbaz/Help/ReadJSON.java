package com.kokabi.p.azmonbaz.Help;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by P.Kokabi on 6/29/16.
 */
public class ReadJSON {
    public static String readRawResource(String jsonPathName) {
        File fileEvents = new File(AppController.getCurrentContext().getApplicationInfo().dataDir
                + "/app_" + Constants.appFolderName + "/Json/" + jsonPathName);
        StringBuilder text = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileEvents));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
            return text.toString();
        } catch (IOException e) {
            return e.toString();
        }
    }
}
