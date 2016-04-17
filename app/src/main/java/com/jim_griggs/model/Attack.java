package com.jim_griggs.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Iterator;

public class Attack {
    private static final String MODULE_NAME = "Attack";

    private String mName;
    private String mStatType;
    private boolean mProficient;
    private Dice mAttackDice = new Dice(1, 20);
    private Dice mDamageDice;
    private ArrayList<Bonus> mAttackBonuses;
    private ArrayList<Bonus> mDamageBonuses;
    private int mAttackRoll;
    private int mDamageRoll;
    private boolean mTakeTenDamage;
    private boolean mCrit;
    private int mCritRoll;
    private int mAttackResult;
    private int mDamageResult;

    private Character mCharacter;

    public Attack(Character character, String name, String statType, boolean proficient, boolean takeTenDamage){
        this.mCharacter = character;
        this.mName = name;
        this.mStatType = statType;
        this.mProficient = proficient;
        this.mTakeTenDamage = takeTenDamage;
        this.mAttackBonuses = new ArrayList<>();
        this.mDamageBonuses = new ArrayList<>();
    }

    public void setName(String name) { this.mName = name; }
    public String getName() { return this.mName; }

    public String getStatType() {return mStatType;}
    public void setStatType(String statType) {this.mStatType = statType;}

    public boolean isProficient() {return mProficient;}
    public void setProficient(boolean proficient) {this.mProficient = proficient;}

    public void setDamageDice(Dice damageDice) {this.mDamageDice = damageDice;}
    public Dice getDamageDice() { return this.mDamageDice; }

    public Dice getAttackDice() { return this.mAttackDice; }

    public boolean isTakeTenDamage(){
        return mTakeTenDamage;
    }

    public void setTakeTenDamage(boolean takeTenDamage){
        mTakeTenDamage = takeTenDamage;
    }

    public boolean isCrit(){
        return mCrit;
    }

    public int getCritRoll(){
        return mCritRoll;
    }

    public void addAttackBonus(Bonus newBonus){this.mAttackBonuses.add(newBonus);}
    public ArrayList<Bonus> getAttackBonuses() { return this.mAttackBonuses; }

    public void addDamageBonus(Bonus newBonus){
        Log.i(MODULE_NAME, "Adding Damage Bonus: " + newBonus.getType());
        this.mDamageBonuses.add(newBonus);
    }
    public ArrayList<Bonus> getDamageBonuses() { return this.mDamageBonuses; }

    public int getAttackRoll() {return mAttackRoll;}
    public void setAttackRoll(int attackRoll) {this.mAttackRoll = attackRoll;}

    public int getDamageRoll() {return mDamageRoll;}
    public void setDamageRoll(int damageRoll) {this.mDamageRoll = damageRoll;}

    public int getAttackResult() {return mAttackResult;}
    public void setAttackResult(int attackResult) {this.mAttackResult = attackResult;}

    public int getDamageResult() {return mDamageResult;}
    public void setDamageResult(int damageResult) {this.mDamageResult = damageResult;}

    public int getTotalAttackBonus(){
        int total = 0;
        for(Bonus b: mAttackBonuses){
            total += b.getValue();
        }
        return total;
    }

    public int getTotalDamageBonus(){
        int total = 0;
        for(Bonus b: mDamageBonuses){
            total += b.getValue();
        }
        return total;
    }

    private void checkCrit(){
        if (mAttackRoll >= mCharacter.getCritRange()){
            mCrit = true;
            mCritRoll = mDamageDice.getTakeTen();
            Character.getInstance().setRaged(true);
        } else {
            mCrit = false;
            mCritRoll = 0;
        };
    }

    private void addRageBonus(){
        Log.i(MODULE_NAME, "Adding Rage Bonus");
        addDamageBonus(mCharacter.getRageBonus());
    }

    private void removeRageBonus(){
        Iterator<Bonus> it = mDamageBonuses.iterator();
        while(it.hasNext()){
            Bonus b = it.next();
            if (b.getType().equals(Bonus.BONUS_RAGE)) {
                it.remove();
            }
        }
    }

    public void calcResults(int attackRoll, int damageRoll){
        this.mAttackRoll = attackRoll;

        // If taking ten on the damage roll for this attack, calculate it as the roll.
        if (this.mTakeTenDamage){
            this.mDamageRoll = mDamageDice.getTakeTen();
        } else {
            this.mDamageRoll = damageRoll;
        }

        // Add crit damage bonus, if applicable.
        checkCrit();

        // Add rage damage bonus, if applicable.
        removeRageBonus();
        if (mCharacter.isRaged()){
            addRageBonus();
        }

        this.mAttackResult = attackRoll + getTotalAttackBonus();
        this.mDamageResult = mCritRoll + damageRoll + getTotalDamageBonus();

        // Todo:  need an app icon and title
    }
}
