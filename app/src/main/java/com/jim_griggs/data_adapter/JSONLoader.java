package com.jim_griggs.data_adapter;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Bonus;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Dice;
import com.jim_griggs.model.Feat;
import com.jim_griggs.model.Skill;
import com.jim_griggs.model.Stat;


public class JSONLoader {

    public void loadCharacterFromAsset(Context context, String filename) {
        JSONObject jsonObject = null;

        try {
            InputStream is = context.getAssets().open(filename);
            jsonObject = loadJSONFromInputStream(is);
        } catch (IOException ex){
            ex.printStackTrace();
        }
        convertCharacter(jsonObject);
    }

    public void loadCharacterFile(Context context) {
        JSONObject jsonObject = null;
        try {
            FileInputStream inStream = context.openFileInput(JSONSaver.FILE_NAME);
            jsonObject = loadJSONFromInputStream(inStream);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        convertCharacter(jsonObject);
    }

    private JSONObject loadJSONFromInputStream(InputStream inStream) {
        String jsonString = "";
        JSONObject jsonObject = null;
        try {
            if (inStream != null) {
                InputStreamReader streamReader = new InputStreamReader(inStream);
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }
                inStream.close();
                jsonString = stringBuilder.toString();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        try {
            jsonObject = new JSONObject(jsonString);
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }

        return jsonObject;
    }

    private void convertCharacter(JSONObject json){
        try {
            Character character = Character.getInstance();
            character.setName(json.getString("name"));
            character.setPlayerName(json.getString("playername"));
            character.setRace(json.getString("race"));
            character.setAlignment(json.getString("alignment"));
            character.setXP(json.getInt("XP"));
            character.setMaxHP(json.getInt("maxhp"));
            character.setCurrentHP(json.getInt("currenthp"));
            character.setCritRange(json.getInt("critRange"));
            character.setRaged(json.getBoolean("raged"));
            character.setInspired(json.getBoolean("inspiration"));

            JSONObject classObject = json.getJSONObject("class");
            character.setLevel(classObject.getInt("level"));
            character.setCharClass(classObject.getString("class"));

            JSONArray moveBonuses = json.getJSONArray("movement");
            for (int i=0; i < moveBonuses.length(); i++) {
                Bonus b = convertBonus((JSONObject) moveBonuses.get(i));
                character.addMovementBonus(b);
            }

            JSONArray acBonuses = json.getJSONArray("AC");
            for (int i=0; i<acBonuses.length(); i++) {
                Bonus b = convertBonus((JSONObject) acBonuses.get(i));
                character.addACBonus(b);
            }

            JSONArray charStats = json.getJSONArray("stats");
            for (int i=0; i<charStats.length(); i++){
                Stat s = convertStat(character, (JSONObject) charStats.get(i));
                character.addStat(s);
            }

            JSONArray charSkills = json.getJSONArray("skills");
            for (int i=0; i<charSkills.length(); i++) {
                Skill s = convertSkill(character,(JSONObject) charSkills.get(i));
                character.addSkill(s);
            }

            JSONArray charAttacks = json.getJSONArray("attacks");
            for (int i=0; i<charAttacks.length(); i++){
                Attack a = convertAttack(character, (JSONObject) charAttacks.get(i));
                character.addAttack(a);
            }

            JSONArray jsonFeats = json.getJSONArray("feats");
            for (int i = 0; i < jsonFeats.length(); i++) {
                Feat f = convertFeat((JSONObject) jsonFeats.get(i));
                character.addFeat(f);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public Attack convertAttack(Character character, JSONObject json){
        try {
            String name = json.getString("name");
            String statType = json.getString("stat");
            boolean proficient = json.getBoolean("proficient");
            JSONObject damageDice = json.getJSONObject("damageDice");

            Dice dice = convertDice(damageDice);
            Attack newAttack = new Attack(character, name, statType, proficient);
            newAttack.setDamageDice(dice);

            newAttack.addAttackBonus(character.getStat(statType).getStatBonus());

            if (proficient) {
                newAttack.addAttackBonus(character.getProficiencyBonus());
            }

            JSONArray aBonuses = json.getJSONArray("attackBonuses");
            for (int i = 0; i < aBonuses.length(); i++) {
                newAttack.addAttackBonus(convertBonus((JSONObject) aBonuses.get(i)));
            }

            newAttack.addDamageBonus(character.getStat(statType).getStatBonus());
            JSONArray dBonuses = json.getJSONArray("damageBonuses");
            for (int i = 0; i < dBonuses.length(); i++) {
                newAttack.addDamageBonus(convertBonus((JSONObject) dBonuses.get(i)));
            }
            return newAttack;
        } catch (JSONException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Dice convertDice(JSONObject json){
        try {
            int diceNum = json.getInt("diceNumber");
            int diceType = json.getInt("diceType");
            return new Dice(diceNum, diceType);
        } catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Bonus convertBonus(JSONObject json){
        try {
            String type = json.getString("type");
            int value = json.getInt("value");
            return new Bonus(type, value);
        } catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Feat convertFeat(JSONObject json) {
        try {
            String name = json.getString("name");
            String description = json.getString("description");
            int levelAcquired = json.getInt("levelAcquired");
            int numUsage = json.getInt("maxUsage");
            int currentUsage = json.getInt("currentUsage");
            String refresh = json.getString("refresh");
            return new Feat(name, description, levelAcquired, numUsage, currentUsage, refresh);
        } catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Skill convertSkill(Character character, JSONObject json){
        try {
            String skill_type = json.getString("type");
            String stat_type = json.getString("stat");
            boolean proficient = json.getBoolean("proficient");
            return new Skill(character, skill_type, stat_type, proficient);
        } catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
    }

    public Stat convertStat(Character character, JSONObject json){
        try {
            String type = json.getString("type");
            int value = json.getInt("value");
            boolean proficient = json.getBoolean("save");
            return new Stat(character, type, value, proficient);
        } catch (JSONException ex){
            ex.printStackTrace();
            return null;
        }
    }
}
