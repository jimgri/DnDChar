package com.jim_griggs.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by tdevjwg on 3/3/2016.
 */
public class Feat {

    public String name;
    public String description;
    public int levelAcquired;
    public int numUsage;
    public String refresh;
    private int currentUsage;

    public Feat (String name, String description){
        this.name = name;
        this.description = description;
    }

    public Feat (JSONObject json) {
        try {
            name = json.getString("name");
            description = json.getString("description");
            levelAcquired = json.getInt("levelAcquired");
            numUsage = json.getInt("numUsage");
            refresh = json.getString("refresh");
        } catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public void use(){
        if (currentUsage > 0){
            currentUsage -= 1;
        }
    }

    public void restore(){
        if (currentUsage < numUsage){
            currentUsage += 1;
        }
    }
}
