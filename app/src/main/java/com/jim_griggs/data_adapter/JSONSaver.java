package com.jim_griggs.data_adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Bonus;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Dice;
import com.jim_griggs.model.Feat;
import com.jim_griggs.model.Skill;
import com.jim_griggs.model.Stat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;


public class JSONSaver implements Runnable {
    public static final String FILE_NAME = "char.json";

    private OutputStream mOutStream;
    private Character mCharacter;

    public JSONSaver (OutputStream outStream, Character character) {
        mOutStream = outStream;
        mCharacter = character;
    }

    public void run() {
        saveCharacter();
    }

    private void saveCharacter(){
        String outString = "";

        try {
            outString = convertCharacter(mCharacter).toString(2);
        } catch (JSONException ex){
            ex.printStackTrace();
        }

        Log.i("JSONSAVER",outString);

        try {
            mOutStream.write(outString.getBytes());
            mOutStream.close();
        } catch (FileNotFoundException ex){
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        Log.i("save", "IT WORKED!!!");
    }

    private JSONObject convertCharacter(Character character){
        JSONObject json = new JSONObject();
        try {
            json.put ("name", character.getName());
            json.put ("playername", character.getPlayerName());
            json.put ("race", character.getRace());
            json.put ("alignment", character.getAlignment());
            json.put ("XP", character.getXP());
            json.put ("maxhp", character.getMaxHP());
            json.put ("currenthp", character.getCurrentHP());
            json.put ("critRange", character.getCritRange());
            json.put ("inspiration", character.isInspired());
            json.put ("raged", character.isRaged());

            JSONObject classObj = new JSONObject();
            classObj.put ("class", character.getCharClass());
            classObj.put ("level", character.getLevel());
            json.put ("class", classObj);

            json.put("stats", convertStats(character.getStats()));
            json.put("skills", convertSkills(character.getSkills()));

            json.put ("movement", convertBonusArray(character.getMovementBonuses()));
            json.put ("AC", convertBonusArray(character.getACBonuses()));

            json.put ("attacks", convertAttacks(character.getAttacks()));
            json.put ("feats", convertFeats(character.getFeats()));

        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return json;
    }

    private JSONArray convertBonusArray(ArrayList<Bonus> bonuses) {
        JSONArray bArray = new JSONArray();
        for (Bonus b : bonuses) {
            JSONObject bonus = convertBonus(b);
            bArray.put(bonus);
        }
        return bArray;
    }

    private JSONObject convertBonus(Bonus bonus){
        JSONObject json = new JSONObject();
        try {
            json.put ("type", bonus.getType());
            json.put ("value", bonus.getValue());
        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return json;
    }

    private JSONArray convertStats(Map<String, Stat> stats) {
        JSONArray statsArray = new JSONArray();
        for (Map.Entry<String, Stat> entry : stats.entrySet()) {
            JSONObject stat = convertStat(entry.getValue());
            statsArray.put(stat);
        }
        return statsArray;
    }

    private JSONObject convertStat(Stat stat){
        JSONObject json = new JSONObject();
        try {
            json.put("type", stat.getType());
            json.put("value", stat.getValue());
            json.put("save", stat.isProficient());
        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return json;
    }

    private JSONArray convertSkills (Map<String, Skill> skills){
        JSONArray skillsArray = new JSONArray();
        for (Map.Entry<String, Skill> entry : skills.entrySet()) {
            JSONObject stat = convertSkill(entry.getValue());
            skillsArray.put(stat);
        }
        return skillsArray;
    }

    private JSONObject convertSkill(Skill skill){
        JSONObject json = new JSONObject();
        try {
            json.put("type", skill.getSkillType());
            json.put("stat", skill.getStatType());
            json.put("proficient", skill.isProficient());
        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return json;
    }

    private JSONArray convertFeats(ArrayList<Feat> feats) {
        JSONArray featsArray = new JSONArray();
        for (Feat f : feats) {
            JSONObject feat = convertFeat(f);
            featsArray.put(feat);
        }
        return featsArray;
    }

    private JSONObject convertFeat (Feat feat) {
        JSONObject json = new JSONObject();
        try {
            json.put("name", feat.getName());
            json.put("description", feat.getDescription());
            json.put("levelAcquired", feat.getLevelAcquired());
            json.put("maxUsage", feat.getMaxUsage());
            json.put("currentUsage", feat.getCurrentUsage());
            json.put("refresh", feat.getRefresh());
        } catch (JSONException ex){
            ex.printStackTrace();
        }
        return json;
    }

    private JSONArray convertAttacks(ArrayList<Attack> attacks) {
        JSONArray attacksArray = new JSONArray();
        for (Attack a : attacks) {
            JSONObject attack = convertAttack(a);
            attacksArray.put(attack);
        }
        return attacksArray;
    }

    private JSONObject convertDice(Dice dice) {
        JSONObject json = new JSONObject();
        try {
            json.put("diceNumber", dice.getDieNum());
            json.put("diceType", dice.getDieType());
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return json;
    }

    private JSONObject convertAttack(Attack attack){
        JSONObject json = new JSONObject();
        try {
            json.put("name", attack.getName());
            json.put("stat", attack.getStatType());
            json.put("proficient", attack.isProficient());

            json.put("damageDice", convertDice(attack.getDamageDice()));

            JSONArray rollArray = new JSONArray();
            for (Bonus b: attack.getAttackBonuses()){
                if (b.getType() != Bonus.BONUS_STAT && b.getType() != Bonus.BONUS_PROFICENCY){
                    rollArray.put(convertBonus(b));
                }
            }
            json.put("attackBonuses", rollArray);

            JSONArray dmgArray = new JSONArray();
            for (Bonus b: attack.getDamageBonuses()){
                if (b.getType() != Bonus.BONUS_STAT){
                    dmgArray.put(convertBonus(b));
                }
            }
            json.put("damageBonuses", dmgArray);

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return json;
    }

}
