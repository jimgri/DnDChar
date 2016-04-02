package com.jim_griggs.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class Stat {
    public static final String TYPE_STR = "STR";
    public static final String TYPE_DEX = "DEX";
    public static final String TYPE_CON = "CON";
    public static final String TYPE_WIS = "WIS";
    public static final String TYPE_INT = "INT";
    public static final String TYPE_CHA = "CHA";

    private String type;
    private int value;
    private boolean proficient;
    private Bonus statBonus;
    private Character mCharacter;

    public Stat(Character character, String type, int value, boolean proficient){
        this.mCharacter = character;
        this.type = type;
        this.value = value;
        this.proficient = proficient;
    }

    public String getType() {return type;}
    // TODO:  update setter to check for valid type
    public void setType(String type) {this.type = type;}

    public int getValue() {return value;}
    public void setValue(int value) {this.value = value;}

    public boolean isProficient() {return proficient;}
    public void setProficient(boolean proficient) {this.proficient = proficient;}

    public Bonus getStatBonus() {
        //TODO:  Confirm this equation to convert Stat to Bonus
        if (statBonus == null) {
            statBonus = new Bonus(Bonus.BONUS_STAT, (this.value/2) - 5);
        }
        return statBonus;
    }

    public int calcSavingThrow(int roll){
        return roll + getSaveBonus();
    }

    public int getSaveBonus(){
        int result = getStatBonus().getValue();
        if (proficient){
            result += mCharacter.getProficiencyBonus().getValue();
        }
        return result;
    }

    public Collection<Bonus> getSaveBonuses(){
        Collection<Bonus> result = new ArrayList<>();
        result.add(getStatBonus());
        if (proficient){
            result.add (mCharacter.getProficiencyBonus());
        }
        return result;
    }
}

