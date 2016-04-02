package com.jim_griggs.dndchar;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Dice;

import java.util.HashMap;


public class AttackActivity extends AppCompatActivity implements RollFragment.RollFragmentListener{
    private static final String MODULE_NAME = "AttackActivity";

    LinearLayout mAttackList;
    private int[][] mResults;
    private Character mCharacter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack);

        mCharacter = Character.getInstance();
        mResults = new int[mCharacter.getAttacks().size()][2];

        mAttackList = (LinearLayout) findViewById(R.id.attackList);

        for (int i=0; i < mCharacter.getAttacks().size(); i++){
            AttackRoll attackView = new AttackRoll(this, i, mCharacter.getAttacks().get(i));
            mAttackList.addView(attackView);
        }
    }

    public void onCalcButton(View v){
        calcResults();
    }

    public void onDiceRolled(String rollName, int result){
        String[] parts = rollName.split(":");

        Log.i(MODULE_NAME, "onDiceRoll:" + rollName);
        Log.i(MODULE_NAME, "onDiceRoll:" + parts[1]);

        int attackNum = Integer.parseInt(parts[1]);

        if (parts[0].equals("Attack")){
            mResults[attackNum][0] = result;
        } else if (parts[0].equals("Damage")){
            mResults[attackNum][1] = result;
        }

        boolean foundZero = false;
        for (int[] i: mResults){
            for (int j: i){
                if (j == 0){
                    foundZero = true;
                    break;
                }
            }
        }

        if (!foundZero){
            calcResults();
        }
    }

    private void calcResults() {
        // Move results to the character
        for (int i = 0; i < mCharacter.getAttacks().size(); i++) {
            // calc the attack results
            mCharacter.getAttacks().get(i).calcResults(mResults[i][0], mResults[i][1]);
        }
        // finish the activity
        ActivityController c = new ActivityController(this);
        c.launchAttackResultsActivity();
    }
}
