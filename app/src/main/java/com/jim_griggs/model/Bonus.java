package com.jim_griggs.model;

import java.io.Serializable;

public class Bonus implements Serializable {
    public static final String BONUS_PROFICENCY = "Prof";
    public static final String BONUS_STAT = "Stat";
    public static final String BONUS_WEAPON = "Weapon";
    public static final String BONUS_OFFWEAPON = "Off Hand";
    public static final String BONUS_MISC = "Misc";

    public String type;
    public int value;

    public Bonus(String type, int value){
        this.type = type;
        this.value = value;
    }
}