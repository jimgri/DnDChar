package com.jim_griggs.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Character {
    private static final String MODULE_NAME = "Character";

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

    private String charClass;
    private int level;
    private String name;
    private String playerName;
    private String race;
    private String alignment;
    private int XP;
    private int maxHP;
    private int currentHP;
    private int hitDiceMax;
    private int remainingHitDice;
    private Dice hitDie;
    private int critRange;
    private boolean raged;
    private Bonus rageBonus;
    private Bonus proficiencyBonus;
    private boolean inspired;
    private Map<String, Stat> stats;
    private Map<String, Skill> skills;
    private ArrayList<Bonus> ACBonuses;
    private ArrayList<Bonus> movementBonuses;
    private ArrayList<Feat> feats;
    private ArrayList<Attack> attacks;

    public static Character getInstance() {
        if(mInstance == null) {
            mInstance = new Character();
        }
        return mInstance;
    }

    public static void destroyInstance() {
        mInstance = null;
    }

    private Character(){
        stats = new LinkedHashMap<>();
        skills = new LinkedHashMap<>();
        ACBonuses = new ArrayList<>();
        movementBonuses = new ArrayList<>();
        feats = new ArrayList<>();
        attacks = new ArrayList<>();
    }

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getCharClass() {return charClass;}
    // TODO:  Make sure setting class to valid type.
    public void setCharClass(String charClass) {this.charClass = charClass;}

    public String getRace() {return race;}
    // TODO:  Make sure setting race to valid type.
    public void setRace(String race) {this.race = race;}

    public int getLevel() {return level;}
    public void setLevel(int level) {
        this.level = level;
        hitDiceMax = this.level;
        // Todo:  This shouldn't be hard coded.  It should be set based on player class.
        hitDie = new Dice(1,10);
    }

    public Dice getHitDie(){
        if (hitDie != null) {
            return hitDie;
        }
        return null;
    }

    public int getHitDiceMax() {return hitDiceMax;}

    public void addHitDice(int value){
        remainingHitDice += value;
        if (remainingHitDice > hitDiceMax){
            remainingHitDice = hitDiceMax;
        }
    }

    public void subtractHitDice(int value){
        remainingHitDice -= value;
        if (remainingHitDice < 0){
            remainingHitDice = 0;
        }
    }

    public void setRemainingHitDice(int remainingHitDice){
        if (remainingHitDice > hitDiceMax){
            this.remainingHitDice = hitDiceMax;
        } else if (remainingHitDice < 0){
            this.remainingHitDice = 0;
        } else {
            this.remainingHitDice = remainingHitDice;
        }
    }

    public int getRemainingHitDice() {return remainingHitDice;}

    public String getPlayerName() {return playerName;}
    public void setPlayerName(String playerName) {this.playerName = playerName;}

    public String getAlignment() {return alignment;}
    // TODO:  Make sure setting to valid type.
    public void setAlignment(String alignment) {this.alignment = alignment;}

    public int getXP() {return XP;}
    public void setXP(int XP) {this.XP = XP;}

    public int getMaxHP() {return maxHP;}
    public void setMaxHP(int maxHP) {this.maxHP = maxHP;}

    public int getCurrentHP() {return currentHP;}
    public void setCurrentHP(int currentHP) {this.currentHP = currentHP;}

    public int getCritRange() {return critRange;}
    public void setCritRange(int critRange) {this.critRange = critRange;}

    public boolean isRaged() {return raged;}
    public void setRaged(boolean raged) {
        this.raged = raged;
    }

    public boolean isInspired() {return inspired;}
    public void setInspired(boolean insired) {this.inspired = insired;}



    public Map<String, Stat> getStats(){
        return stats;
    }

    public Stat getStat(String statType){
        return stats.get(statType);
    }

    public void addStat(Stat stat){
        stats.put(stat.getType(), stat);
    }

    public Map<String, Skill> getSkills(){
        return skills;
    }

    public Skill getSkill(String skillType){
        return skills.get(skillType);
    }

    public void addSkill(Skill skill) {
        skills.put(skill.getSkillType(), skill);
    }

    public ArrayList<Bonus> getACBonuses(){
        return ACBonuses;
    }

    public void addACBonus(Bonus bonus){
        ACBonuses.add(bonus);
    }

    public int getAC(){
        int result = 0;
        for (Bonus b : ACBonuses){
            result += b.getValue();
        }
        return result;
    }

    public ArrayList<Bonus> getMovementBonuses(){
        return movementBonuses;
    }

    public void addMovementBonus(Bonus bonus){
        movementBonuses.add(bonus);
    }

    public int getMovement() {
        int move = 0;
        for (Bonus b : movementBonuses){
            move += b.getValue();
        }
        return move;
    }

    public ArrayList<Feat> getFeats(){
        return feats;
    }

    public void addFeat(Feat feat){
        feats.add(feat);
    }

    public ArrayList<Attack> getAttacks(){
        return attacks;
    }

    public void addAttack(Attack attack){
        attacks.add(attack);
    }

    public Bonus getRageBonus(){
        if (rageBonus == null) {
            // Todo:  make sure this is correct.
            int value = 1;
            switch (level) {
                case 1:
                case 2:
                case 3:
                    value = 1;
                    break;
                default:
                    value = 4;
            }
            rageBonus = new Bonus(Bonus.BONUS_RAGE, value);
        }
        return rageBonus;
    }

    public Bonus getProficiencyBonus(){
        if (proficiencyBonus == null) {
            int result;
            switch (level) {
                case 1:
                case 2:
                case 3:
                case 4:
                    result = 2;
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                    result = 3;
                    break;
                case 9:
                case 10:
                case 11:
                case 12:
                    result = 4;
                    break;
                case 13:
                case 14:
                case 15:
                case 16:
                    result = 5;
                    break;
                case 17:
                case 18:
                case 19:
                case 20:
                    result = 6;
                    break;
                default:
                    result = 0;
            }
            proficiencyBonus = new Bonus(Bonus.BONUS_PROFICENCY, result);
        }
        return proficiencyBonus;
    }

    public Bonus getInitiativeBonus(){
        return stats.get(Stat.TYPE_DEX).getStatBonus();
    }

    public int calcInitiative(int roll) {
        return roll + getInitiative();
    }

    public int getInitiative(){
        return stats.get(Stat.TYPE_DEX).getStatBonus().getValue();
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
        if (currentHP > maxHP){
            currentHP = maxHP;
        }
        return currentHP;
    }

    public int healFull () {
        currentHP = maxHP;
        return currentHP;
    }

    public void shortRest(){
        // Reset appropriate activity usage.
        for (Feat f: feats){
            if (f.getRefresh().equals(Feat.REFRESH_SHORTREST)){
                f.setCurrentUsage(0);
            }
        }
    }

    public void longRest(){
        // Restore all hit points.
        healFull();
        // Restore half hit dice.
        int restore = level / 2;
        addHitDice(restore);
        // Reset appropriate activity usage.
        for (Feat f: feats){
            if (f.getRefresh().equals(Feat.REFRESH_SHORTREST) || f.getRefresh().equals(Feat.REFRESH_LONGREST)){
                f.setCurrentUsage(0);
            }
        }
    }

}

