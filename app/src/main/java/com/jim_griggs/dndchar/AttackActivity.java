package com.jim_griggs.dndchar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.jim_griggs.model.Character;

public class AttackActivity extends AppCompatActivity implements RollFragment.RollFragmentListener{
    private static final String MODULE_NAME = "AttackActivity";

    LinearLayout mAttackList;
    // Array to temporarily store the dice rolls until the user clicks "Submit"
    private int[][] mResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack);

        Character character = Character.getInstance();
        mResults = new int[character.getAttacks().size()][2];

        mAttackList = (LinearLayout) findViewById(R.id.attackList);

        // Display each attack roll.  Attack roll includes the Attack and Damage dice rolls.
        for (int i=0; i < character.getAttacks().size(); i++){
            AttackRoll attackView = new AttackRoll(this, i, character.getAttacks().get(i));
            mAttackList.addView(attackView);
        }
    }

    public void onCalcButton(View v){
        calcResults();
    }

    public void onDiceRolled(String rollName, int result){
        String[] parts = rollName.split(":");
        int attackNum = Integer.parseInt(parts[1]);

        // Store the die roll returned to the temporary array.
        if (parts[0].equals("Attack")){
            mResults[attackNum][0] = result;
        } else if (parts[0].equals("Damage")){
            mResults[attackNum][1] = result;
        }
    }

    private void calcResults() {
        // Move die results from the temporary array to the Character
        Character character = Character.getInstance();
        for (int i = 0; i < character.getAttacks().size(); i++) {
            // Calc the attack results
            character.getAttacks().get(i).calcResults(mResults[i][0], mResults[i][1]);
        }
        // Launch the Attack Results Activity
        ActivityController c = new ActivityController(this);
        c.launchAttackResultsActivity();
        // Call finish in order to release this Activity and remove it from the back stack.
        finish();
    }
}
