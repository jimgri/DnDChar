package com.jim_griggs.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Attack {
    public String name;
    public int attackDieType = 20;
    public ArrayList<Bonus> attackBonuses;
    public ArrayList<Bonus> damageBonuses;
    public int damageDieNum;
    public int damageDieType;

    public int attackRoll;
    public int damageRoll;
    public int attackResult;
    public int damageResult;

    private Character mCharacter;

    public Attack(String name, int damageDieNum, int damageDieType){
        this.name = name;
        this.damageDieNum = damageDieNum;
        this.damageDieType = damageDieType;
        this.attackBonuses = new ArrayList<>();
        this.damageBonuses = new ArrayList<>();
    }

    public Attack(Character character, JSONObject json){
        try {
            this.mCharacter = character;
            this.name = json.getString("name");
            this.damageDieNum = json.getInt("damageDieNumber");
            this.damageDieType = json.getInt("damageDieType");

            int attackBonusCount = 1;
            String stat = json.getString("stat");
            Boolean prof = json.getBoolean("proficient");
            JSONArray aBonuses = json.getJSONArray("attackBonuses");
            attackBonusCount += aBonuses.length();

            if (prof) {
                attackBonusCount += 1;
                attackBonuses = new ArrayList<>(attackBonusCount);
                attackBonuses.add(new Bonus(Bonus.BONUS_PROFICENCY, mCharacter.getProfBonus().value));
            } else {
                attackBonuses = new ArrayList<>(attackBonusCount);
            }

            Bonus statBonus = new Bonus(Bonus.BONUS_STAT, mCharacter.stats.get(stat).getStatBonus().value);
            attackBonuses.add(statBonus);
            for (int i = 0; i < aBonuses.length(); i++) {
                attackBonuses.add(new Bonus((JSONObject) aBonuses.get(i)));
            }

            int damageBonusCount = 1;
            JSONArray dBonuses = json.getJSONArray("damageBonuses");
            damageBonusCount += dBonuses.length();
            damageBonuses = new ArrayList<>(damageBonusCount);
            damageBonuses.add(statBonus);
            for (int i = 0; i < dBonuses.length(); i++) {
                damageBonuses.add(new Bonus((JSONObject) dBonuses.get(i)));
            }

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void addAttackBonus(Bonus newBonus){
        this.attackBonuses.add(newBonus);
    }

    public void addDamageBonus(Bonus newBonus){
        this.damageBonuses.add(newBonus);
    }

    public int getTotalAttackBonus(){
        int total = 0;
        for(Bonus b: attackBonuses){
            total += b.value;
        }
        return total;
    }

    public int getTotalDamageBonus(){
        int total = 0;
        for(Bonus b: damageBonuses){
            total += b.value;
        }
        return total;
    }

    private void removeCritBonus(){
        for(Bonus b: this.damageBonuses){
            if (b.type == Bonus.BONUS_CRIT){
                damageBonuses.remove(b);
            }
        }
    }

    private void removeRageBonus(){
        for(Bonus b: this.damageBonuses){
            if (b.type == Bonus.BONUS_RAGE){
                damageBonuses.remove(b);
            }
        }
    }

    private void addRageBonus(){
        boolean found = false;
        for(Bonus b: this.damageBonuses){
            if (b.type == Bonus.BONUS_RAGE){
                found = true;
                b.value = mCharacter.rageBonus.value;
            }
        }
        if (!found){
            damageBonuses.add(mCharacter.rageBonus);
        }
    }

    public void calcResults(int attackRoll, int damageRoll){
        this.attackRoll = attackRoll;
        this.damageRoll = damageRoll;

        // The last attack may have included a Crit Bonus.
        // That has to be removed as it doesn't carry over from attack to attack.
        removeCritBonus();
        if (attackRoll >= mCharacter.critRange) {
            Bonus critBonus = new Bonus(Bonus.BONUS_CRIT, damageRoll);
            addDamageBonus(critBonus);
            mCharacter.raged = true;
        }

        // Add rage damage bonus, if applicable.
        if (mCharacter.raged){
            addRageBonus();
        } else {
            removeRageBonus();
        }

        this.attackResult = attackRoll + getTotalAttackBonus();
        this.damageResult = damageRoll + getTotalDamageBonus();
        // Todo:  Create off-thread task to save the Character.  Trigger it when the character model changes.
        // Todo:  Add interface for healing and damage
        // Todo:  Add interface for temp HP
        // Todo:  Add interface for long and short rest.
        // Todo:  Add menu options for:  view last attack results;

    }
}
