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

    public Attack(String name, int damageDieNum, int damageDieType){
        this.name = name;
        this.damageDieNum = damageDieNum;
        this.damageDieType = damageDieType;
        this.attackBonuses = new ArrayList<>();
        this.damageBonuses = new ArrayList<>();
    }

    public Attack(Character character, JSONObject json){
        try {
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
                attackBonuses.add(new Bonus(Bonus.BONUS_PROFICENCY, character.getProfBonus().value));
            } else {
                attackBonuses = new ArrayList<>(attackBonusCount);
            }

            Bonus statBonus = new Bonus(Bonus.BONUS_STAT, character.stats.get(stat).getStatBonus().value);
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

    public void calcResults(int attackRoll, int damageRoll){
        this.attackRoll = attackRoll;
        this.damageRoll = damageRoll;
        this.attackResult = attackRoll + getTotalAttackBonus();
        this.damageResult = damageRoll + getTotalDamageBonus();
        // Todo:  Add crit range to character.
        // Todo:  if attackroll is crit, {set crit boolean; if fighter, set rage)
        // Todo:  if raged, add rage damage as new Damage Bonus to attack.
        // Todo:  Will need to add code to remove the rage bonus after combat.
        // Todo:  Need to add click event to 1) highlight damage 2) provide running total at the top.  Clicking anywhere else clears the selections.
        // Todo:  Add menu options for:  view last attack results;
        // Todo:  Create off-thread task to save the Character.  Trigger it when the character model changes.
    }
}
