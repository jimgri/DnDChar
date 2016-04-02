package com.jim_griggs.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by tdevjwg on 3/3/2016.
 */
public class Feat implements Serializable {

    private String name;
    private String description;
    private int levelAcquired;
    private int maxUsage;
    private String refresh;
    private int currentUsage;

    public Feat (String name, String description, int levelAcquired, int maxUsage, int currentUsage, String refresh){
        this.name = name;
        this.description = description;
        this.levelAcquired = levelAcquired;
        this.maxUsage = maxUsage;
        this.refresh = refresh;
        this.currentUsage = currentUsage;
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getDescription() {return description;}
    public void setDescription(String description) {this.description = description;}

    public int getLevelAcquired() {return levelAcquired;}
    public void setLevelAcquired(int levelAcquired) {this.levelAcquired = levelAcquired;}

    public int getMaxUsage() {return maxUsage;}
    public void setMaxUsage(int maxUsage) {this.maxUsage = maxUsage;}

    public String getRefresh() {return refresh;}
    public void setRefresh(String refresh) {this.refresh = refresh;}

    public int getCurrentUsage() {return currentUsage;}
    public void setCurrentUsage(int currentUsage) {
        if (currentUsage < 0){
            this.currentUsage = 0;
        } else if (currentUsage > maxUsage) {
            this.currentUsage = maxUsage;
        } else {
            this.currentUsage = currentUsage;
        }
    }

    public void use(){
        if (currentUsage < maxUsage){
            currentUsage++;
        }
    }

    public void restore(){
        if (currentUsage > 0){
            currentUsage--;
        }
    }
}
