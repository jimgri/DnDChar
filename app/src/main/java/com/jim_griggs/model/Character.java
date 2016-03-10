package com.jim_griggs.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Character {
    public static final String CLASS_BARBARIAN = "Barbarian";
    public static final String CLASS_BARD = "Bard";
    public static final String CLASS_CLERIC = "Cleric";
    public static final String CLASS_DRUID = "Druid";
    public static final String CLASS_FIGHTER = "Fighter";
    public static final String CLASS_MONK = "Monk";
    public static final String CLASS_PALADIN = "Paladin";
    public static final String CLASS_RANGER = "Ranger";
    public static final String CLASS_ROGUE = "Rogue";
    public static final String CLASS_SORCERER = "Sorcerer";
    public static final String CLASS_WARLOCK = "Warlock";
    public static final String CLASS_WIZARD = "Wizard";

    public static final String RACE_HUMAN = "Human";
    public static final String RACE_DWARF = "Dwarf";
    public static final String RACE_GNOME = "Gnome";
    public static final String RACE_ELF = "Elf";
    public static final String RACE_HALFELF = "Half-Elf";

    public static final String ALIGNMENT_LAWFUL_GOOD = "Lawful Good";
    public static final String ALIGNMENT_LAWFUL_NEUTRAL = "Lawful Neutral";
    public static final String ALIGNMENT_LAWFUL_EVIL = "Lawful Evil";
    public static final String ALIGNMENT_NEUTRAL_GOOD = "Neutral Good";
    public static final String ALIGNMENT_TRUE_NEUTRAL = "True Neutral";
    public static final String ALIGNMENT_NEUTRAL_EVIL = "Neutral Evil";
    public static final String ALIGNMENT_CHAOTIC_GOOD = "Chaotic Good";
    public static final String ALIGNMENT_CHAOTIC_NEUTRAL = "Chaotic Neutral";
    public static final String ALIGNMENT_CHAOTIC_EVIL = "Chaotic Evil";

    private static Character mInstance = null;

    public String charClass;
    public int level;

    public String name;
    public String playerName;
    public String race;
    public String alignment;
    public int XP;
    private int baseMovement;
    public int maxHP;
    public int currentHP;

    public Map<String, Stat> stats;
    public Map<String, Skill> skills;

    public ArrayList<Attack> attacks;
    public Collection<Bonus> ACBonuses;
    public Collection<Bonus> movementBonuses;
    public ArrayList<Feat> feats;

    public static Character getInstance() {
        if(mInstance == null) {
            mInstance = new Character();
        }
        return mInstance;
    }

    public Character() {
        // Exists only to defeat instantiation.
        level = 10;
        name = "Balta";
        charClass = "Fighter";
        race = "Dwarf";
        baseMovement = 25;
        alignment = ALIGNMENT_NEUTRAL_GOOD;
        maxHP = 187;
        currentHP = maxHP;

        ACBonuses = new ArrayList<>(3);
        ACBonuses.add(new Bonus(Bonus.BONUS_ARMOR, 6));
        ACBonuses.add(new Bonus(Bonus.BONUS_STAT, 3));
        ACBonuses.add(new Bonus(Bonus.BONUS_FEAT, 2));

        movementBonuses = new ArrayList<>();

        stats = new HashMap<>();
        Stat str = new Stat(this, Stat.TYPE_STR, 18, true);
        stats.put(Stat.TYPE_STR, str);

        Stat dex = new Stat(this, Stat.TYPE_DEX, 14);
        stats.put(Stat.TYPE_DEX, dex);

        Stat con = new Stat(this, Stat.TYPE_CON, 16, true);
        stats.put(Stat.TYPE_CON, con);

        Stat wis = new Stat(this, Stat.TYPE_WIS, 12);
        stats.put(Stat.TYPE_WIS, wis);

        Stat intel = new Stat(this, Stat.TYPE_INT, 10);
        stats.put(Stat.TYPE_INT, intel);

        Stat cha = new Stat(this, Stat.TYPE_CHA, 8);
        stats.put(cha.type, cha);

        skills = new HashMap<>();
        InitSkills();
        skills.get(Skill.SKILL_ATHLETICS).proficient = true;
        skills.get(Skill.SKILL_HISTORY).proficient = true;
        skills.get(Skill.SKILL_PERCEPTION).proficient = true;
        skills.get(Skill.SKILL_PERSUASION).proficient = true;

        attacks = new ArrayList<>();
        Attack a1 = new Attack("primary1", 2, 8);
        a1.addAttackBonus(getProfBonus());
        a1.addAttackBonus(str.getStatBonus());
        a1.addAttackBonus(new Bonus(Bonus.BONUS_WEAPON, 3));
        a1.addDamageBonus(str.getStatBonus());
        a1.addDamageBonus(new Bonus(Bonus.BONUS_WEAPON, 3));
        attacks.add(a1);

        Attack a2 = new Attack("primary2", 1, 8);
        a2.addAttackBonus(getProfBonus());
        a2.addAttackBonus(str.getStatBonus());
        a2.addAttackBonus(new Bonus(Bonus.BONUS_WEAPON, 3));
        attacks.add(a2);

        Attack a3 = new Attack("primary3", 1, 8);
        a3.addAttackBonus(getProfBonus());
        a3.addAttackBonus(str.getStatBonus());
        a3.addAttackBonus(new Bonus(Bonus.BONUS_WEAPON, 3));
        attacks.add(a3);

        Attack a4 = new Attack("offhand", 1, 8);
        a4.addAttackBonus(getProfBonus());
        a4.addAttackBonus(str.getStatBonus());
        a4.addAttackBonus(new Bonus(Bonus.BONUS_WEAPON, 1));
        attacks.add(a4);
        mInstance = this;
    }

    public Character(JSONObject json){
        try {
            name = json.getString("name");
            playerName = json.getString("playername");
            race = json.getString("race");
            alignment = json.getString("alignment");
            XP = json.getInt("XP");
            maxHP = json.getInt("maxhp");
            currentHP = maxHP;
            baseMovement = json.getInt("baseMovement");

            movementBonuses = new ArrayList<>();

            JSONObject charObject = json.getJSONObject("class");
            level = charObject.getInt("level");
            charClass = charObject.getString("class");
            Log.i("fileData", charClass);

            JSONArray acBonuses = json.getJSONArray("AC");
            ACBonuses = new ArrayList<>(acBonuses.length());
            for (int i=0; i<acBonuses.length(); i++) {
                ACBonuses.add(new Bonus((JSONObject) acBonuses.get(i)));
            }

            JSONArray charStats = json.getJSONArray("stats");
            stats = new HashMap<>();
            for (int i=0; i<charStats.length(); i++){
                Stat s = new Stat(this, (JSONObject) charStats.get(i));
                stats.put(s.type, s);
            }

            JSONArray charSkills = json.getJSONArray("skillProficiencies");
            skills = new HashMap<>(charSkills.length());
            for (int i=0; i<charSkills.length(); i++) {
                Skill s = new Skill(this, (JSONObject) charSkills.get(i));
                skills.put(s.skill_type, s);
            }

            JSONArray charAttacks = json.getJSONArray("attacks");
            attacks = new ArrayList<>(charAttacks.length());
            for (int i=0; i<charAttacks.length(); i++){
                attacks.add(new Attack(this, (JSONObject) charAttacks.get(i)));
            }

            Log.i ("init Feats", "");
            JSONArray jsonFeats = json.getJSONArray("feats");
            feats = new ArrayList<>(jsonFeats.length());
            for (int i = 0; i < jsonFeats.length(); i++) {
                feats.add(new Feat((JSONObject) jsonFeats.get(i)));
            }

            mInstance = this;
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public Collection<Bonus> getACBonuses(){
        return ACBonuses;
    }

    public int getAC(){
        int result = 0;
        for (Bonus b : ACBonuses){
            result += b.value;
        }
        return result;
    }

    public Bonus getProfBonus(){
        int result = 0;
        switch(level){
            case 1: case 2: case 3: case 4:
                result = 2;
                break;
            case 5: case 6: case 7:case 8:
                result = 3;
                break;
            case 9: case 10: case 11: case 12:
                result = 4;
                break;
            case 13: case 14: case 15: case 16:
                result = 5;
                break;
            case 17: case 18: case 19: case 20:
                result = 6;
                break;
         }
        return new Bonus(Bonus.BONUS_PROFICENCY, result);
    }

    public Bonus getStatBonus(String stat_type){
        return stats.get(stat_type).getStatBonus();
    }

    public int calcInitiative(int roll) {
        return roll + stats.get(Stat.TYPE_DEX).getStatBonus().value;
    }

    public int getInitBonus(){
        return stats.get(Stat.TYPE_DEX).getStatBonus().value;
    }

    public Collection<Bonus> getInitBonuses(){
        Collection<Bonus> result = new ArrayList<>();
        result.add(stats.get(Stat.TYPE_DEX).getStatBonus());
        return result;
    }

    public int getMovement() {
        int moveBonuses = 0;
        for (Bonus moveBonus : movementBonuses){
            moveBonuses += moveBonus.value;
        }
        return baseMovement + moveBonuses;
    }

    public int takeDamage(int damage){
        currentHP -= damage;
        if (currentHP < 0){
            currentHP = 0;
        }
        return currentHP;
    }

    public int heal (int heal){
        currentHP += heal;
        if (currentHP < maxHP){
            currentHP = maxHP;
        }
        return currentHP;
    }

    public int healFull () {
        currentHP = maxHP;
        return currentHP;
    }

    public void InitSkills(){
        skills.put(Skill.SKILL_ACROBATICS, new Skill(this, Skill.SKILL_ACROBATICS, Stat.TYPE_DEX));
        skills.put(Skill.SKILL_ANIMALHANDLING, new Skill(this, Skill.SKILL_ANIMALHANDLING, Stat.TYPE_WIS));
        skills.put(Skill.SKILL_ARCANA, new Skill(this, Skill.SKILL_ARCANA, Stat.TYPE_INT));
        skills.put(Skill.SKILL_ATHLETICS, new Skill(this, Skill.SKILL_ATHLETICS, Stat.TYPE_STR));
        skills.put(Skill.SKILL_DECEPTION, new Skill(this, Skill.SKILL_DECEPTION, Stat.TYPE_CHA));
        skills.put(Skill.SKILL_HISTORY, new Skill(this, Skill.SKILL_HISTORY, Stat.TYPE_INT));
        skills.put(Skill.SKILL_INSIGHT, new Skill(this, Skill.SKILL_INSIGHT, Stat.TYPE_WIS));
        skills.put(Skill.SKILL_INTIMIDATION, new Skill(this, Skill.SKILL_INTIMIDATION, Stat.TYPE_CHA));
        skills.put(Skill.SKILL_INVESTIGATION, new Skill(this, Skill.SKILL_INVESTIGATION, Stat.TYPE_INT));
        skills.put(Skill.SKILL_MEDICINE, new Skill(this, Skill.SKILL_MEDICINE, Stat.TYPE_WIS));
        skills.put(Skill.SKILL_NATURE, new Skill(this, Skill.SKILL_NATURE, Stat.TYPE_INT));
        skills.put(Skill.SKILL_PERCEPTION, new Skill(this, Skill.SKILL_PERCEPTION, Stat.TYPE_WIS));
        skills.put(Skill.SKILL_PERFORMANCE, new Skill(this, Skill.SKILL_PERFORMANCE, Stat.TYPE_CHA));
        skills.put(Skill.SKILL_PERSUASION, new Skill(this, Skill.SKILL_PERSUASION, Stat.TYPE_CHA));
        skills.put(Skill.SKILL_RELIGION, new Skill(this, Skill.SKILL_RELIGION, Stat.TYPE_INT));
        skills.put(Skill.SKILL_SLIGHTOFHAND, new Skill(this, Skill.SKILL_SLIGHTOFHAND, Stat.TYPE_DEX));
        skills.put(Skill.SKILL_STEALTH, new Skill(this, Skill.SKILL_STEALTH, Stat.TYPE_DEX));
        skills.put(Skill.SKILL_SURVIVAL, new Skill(this, Skill.SKILL_SURVIVAL, Stat.TYPE_WIS));
    }
}

