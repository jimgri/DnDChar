package com.jim_griggs.model;

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
    }
}
