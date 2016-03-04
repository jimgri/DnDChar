package com.jim_griggs.model;

import java.util.ArrayList;
import java.util.Collection;

public class Stat {
    public static final String TYPE_STR = "STR";
    public static final String TYPE_DEX = "DEX";
    public static final String TYPE_CON = "CON";
    public static final String TYPE_WIS = "WIS";
    public static final String TYPE_INT = "INT";
    public static final String TYPE_CHA = "CHA";

    public String type;
    public int value;
    public boolean proficient;
    private Character mChar;

    public Stat(Character character, String type, int value){
        this.mChar = character;
        this.type = type;
        this.value = value;
    }

    public Stat(Character character, String type, int value, boolean proficient){
        this.mChar = character;
        this.type = type;
        this.value = value;
        this.proficient = proficient;
    }

    public Bonus getStatBonus() {
        //TODO:  Confirm thsi equation to convert Stat to Bonus
        return new Bonus(Bonus.BONUS_STAT, (this.value/2) - 5);
    }

    public int calcSavingThrow(int roll){
        return roll + getSaveBonus();
    }

    public int getSaveBonus(){
        int result = getStatBonus().value;
        if (proficient){
            result += mChar.getProfBonus().value;
        }
        return result;
    }

    public Collection<Bonus> getSaveBonuses(){
        Collection<Bonus> result = new ArrayList<>();
        result.add(getStatBonus());
        if (proficient){
            result.add (mChar.getProfBonus());
        }
        return result;
    }
}

