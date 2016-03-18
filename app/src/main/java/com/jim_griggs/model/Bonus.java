package com.jim_griggs.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class Bonus implements Serializable {
    public static final String BONUS_PROFICENCY = "Prof";
    public static final String BONUS_STAT = "Stat";
    public static final String BONUS_WEAPON = "Weapon";
    public static final String BONUS_OFFWEAPON = "Off Hand";
    public static final String BONUS_MISC = "Misc";
    public static final String BONUS_ARMOR = "Armor";
    public static final String BONUS_FEAT = "Feat";
    public static final String BONUS_CRIT = "Crit";
    public static final String BONUS_RAGE = "Rage";

    private String type;
    private int value;

    public Bonus(String type, int value){
        this.type = type;
        this.value = value;
    }

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public int getValue() {return value;}
    public void setValue(int value) {this.value = value;}
}