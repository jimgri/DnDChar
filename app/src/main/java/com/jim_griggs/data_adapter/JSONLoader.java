package com.jim_griggs.data_adapter;

import android.content.res.Resources;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import com.jim_griggs.model.Character;


public class JSONLoader {

    public void loadCharacterFile(InputStream is) {
        String json = loadJSONFromInputStream(is);
        try {
            JSONObject jsonChar = new JSONObject(json);
            new Character(jsonChar);
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public String loadJSONFromInputStream(InputStream is) {
        String json = null;
        try {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}
