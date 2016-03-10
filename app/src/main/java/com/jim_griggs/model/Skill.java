package com.jim_griggs.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

public class Skill {
    public static final String SKILL_ACROBATICS = "Acrobatics";
    public static final String SKILL_ANIMALHANDLING = "Animal Handling";
    public static final String SKILL_ARCANA = "Arcana";
    public static final String SKILL_ATHLETICS = "Athletics";
    public static final String SKILL_DECEPTION = "Deception";
    public static final String SKILL_HISTORY = "History";
    public static final String SKILL_INSIGHT = "Insight";
    public static final String SKILL_INTIMIDATION = "Intimidation";
    public static final String SKILL_INVESTIGATION = "Investigation";
    public static final String SKILL_MEDICINE = "Medicine";
    public static final String SKILL_NATURE = "Nature";
    public static final String SKILL_PERCEPTION = "Perception";
    public static final String SKILL_PERFORMANCE = "Performance";
    public static final String SKILL_PERSUASION = "Persuasion";
    public static final String SKILL_RELIGION = "Religion";
    public static final String SKILL_SLIGHTOFHAND = "Slight of Hand";
    public static final String SKILL_STEALTH = "Stealth";
    public static final String SKILL_SURVIVAL = "Survival";

    public String skill_type;
    public String stat_type;
    public boolean proficient;
    private Character mChar;

    public Skill (Character character, String skill_type, String stat_type){
        this.mChar = character;
        this.skill_type = skill_type;
        this.stat_type = stat_type;
        this.proficient = false;
    }

    public Skill (Character character, String skill_type, String stat_type, boolean proficient){
        this.mChar = character;
        this.skill_type = skill_type;
        this.stat_type = stat_type;
        this.proficient = proficient;
    }

    public Skill (Character character, JSONObject json){
        this.mChar = character;
        try {
            this.skill_type = json.getString("type");
            this.stat_type = json.getString("stat");
            this.proficient = json.getBoolean("proficient");
        } catch (JSONException ex){
            ex.printStackTrace();
        }
    }

    public int calcSkillCheck(int roll){
        return roll + getSkillBonus();
    }

    public int getSkillBonus(){
        int result;
        result = mChar.getStatBonus(stat_type).value;
        if (proficient){
            result += mChar.getProfBonus().value;
        }
        return result;
    }

    public Collection<Bonus> getSkillBonuses(){
        Collection<Bonus> result = new ArrayList<>();
        result.add(mChar.getStatBonus(stat_type));
        if (proficient){
            result.add (mChar.getProfBonus());
        }
        return result;
    }
}

