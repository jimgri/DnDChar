package com.jim_griggs.model;

import java.util.ArrayList;

public class Attack {
    private String name;
    private String statType;
    private boolean proficient;
    private Dice attackDice = new Dice(1, 20);
    private Dice damageDice;
    private ArrayList<Bonus> attackBonuses;
    private ArrayList<Bonus> damageBonuses;
    private Bonus mCritBonus;
    private Bonus mRageBonus;
    private int attackRoll;
    private int damageRoll;
    private int attackResult;
    private int damageResult;

    private Character mCharacter;

    public Attack(Character character, String name, String statType, boolean proficient){
        this.mCharacter = character;
        this.name = name;
        this.statType = statType;
        this.proficient = proficient;
        this.attackBonuses = new ArrayList<>();
        this.damageBonuses = new ArrayList<>();
    }

    public void setName(String name) { this.name = name; }
    public String getName() { return this.name; }

    public String getStatType() {return statType;}
    public void setStatType(String statType) {this.statType = statType;}

    public boolean isProficient() {return proficient;}
    public void setProficient(boolean proficient) {this.proficient = proficient;}

    public void setDamageDice(Dice damageDice) {this.damageDice = damageDice;}
    public Dice getDamageDice() { return this.damageDice; }

    public Dice getAttackDice() { return this.attackDice; }

    public void addAttackBonus(Bonus newBonus){this.attackBonuses.add(newBonus);}
    public ArrayList<Bonus> getAttackBonuses() { return this.attackBonuses; }

    public void addDamageBonus(Bonus newBonus){this.damageBonuses.add(newBonus);}
    public ArrayList<Bonus> getDamageBonuses() { return this.damageBonuses; }

    public int getAttackRoll() {return attackRoll;}
    public void setAttackRoll(int attackRoll) {this.attackRoll = attackRoll;}

    public int getDamageRoll() {return damageRoll;}
    public void setDamageRoll(int damageRoll) {this.damageRoll = damageRoll;}

    public int getAttackResult() {return attackResult;}
    public void setAttackResult(int attackResult) {this.attackResult = attackResult;}

    public int getDamageResult() {return damageResult;}
    public void setDamageResult(int damageResult) {this.damageResult = damageResult;}

    public int getTotalAttackBonus(){
        int total = 0;
        for(Bonus b: attackBonuses){
            total += b.getValue();
        }
        return total;
    }

    public int getTotalDamageBonus(){
        int total = 0;
        for(Bonus b: damageBonuses){
            total += b.getValue();
        }
        return total;
    }

    private boolean isCrit(){
        return (attackRoll >= mCharacter.getCritRange());
    }

    private void addCritBonus() {
        if (isCrit()) {
            mCritBonus = new Bonus(Bonus.BONUS_CRIT, damageRoll);
            addDamageBonus(mCritBonus);
            mCharacter.setRaged(true);
        }
    }

    private void removeCritBonus(){
        if (mCritBonus != null){
            damageBonuses.remove(mCritBonus);
            mCritBonus = null;
        }
    }

    private void removeRageBonus(){
        if (mRageBonus != null){
            damageBonuses.remove(mRageBonus);
            mRageBonus = null;
        }
    }

    private void addRageBonus(){
        mRageBonus = mCharacter.getRageBonus();
        addDamageBonus(mRageBonus);
    }

    public void calcResults(int attackRoll, int damageRoll){
        this.attackRoll = attackRoll;
        this.damageRoll = damageRoll;

        // Add crit damage bonus, if applicable.
        if (isCrit() && mCritBonus==null){
            addCritBonus();
        } else if (!isCrit() && mCritBonus!=null) {
            removeCritBonus();
        }

        // Add rage damage bonus, if applicable.
        if (mCharacter.isRaged() && mRageBonus==null){
            addRageBonus();
        } else if (!mCharacter.isRaged() && mRageBonus!=null) {
            removeRageBonus();
        }

        this.attackResult = attackRoll + getTotalAttackBonus();
        this.damageResult = damageRoll + getTotalDamageBonus();
        // Todo:  Improve UI
        // Todo:  Code review
        // Todo:  Add interface for hitDice.
        // Todo:  Add interface for long and short rest.
        // Todo:  Add menu options for:  view last attack results;
        // Todo:  Add interface for temp HP

    }
}
