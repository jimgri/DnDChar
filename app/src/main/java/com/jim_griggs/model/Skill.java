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

    private String skillType;
    private String statType;
    private boolean proficient;
    private Character mCharacter;

    public Skill (Character character, String skill_type, String stat_type, boolean proficient){
        this.mCharacter = character;
        this.skillType = skill_type;
        this.statType = stat_type;
        this.proficient = proficient;
    }

    public String getSkillType() {return skillType;}
    public void setSkillType(String skillType) {this.skillType = skillType;}

    public String getStatType() {return statType;}
    // TODO: update setStat to check for valid types.
    public void setStatType(String statType) {this.statType = statType;}

    public boolean isProficient() {return proficient;}
    public void setProficient(boolean proficient) {this.proficient = proficient;}

    public int calcSkillCheck(int roll){
        return roll + getSkillBonus();
    }

    public int getSkillBonus(){
        int result;
        result = mCharacter.getStat(statType).getStatBonus().getValue();
        if (proficient){
            result += mCharacter.getProficiencyBonus().getValue();
        }
        return result;
    }

    public ArrayList<Bonus> getSkillBonuses(){
        ArrayList<Bonus> result = new ArrayList<>();
        result.add(mCharacter.getStat(statType).getStatBonus());
        if (proficient){
            result.add (mCharacter.getProficiencyBonus());
        }
        return result;
    }
}

