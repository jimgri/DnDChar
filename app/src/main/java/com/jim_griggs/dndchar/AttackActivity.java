package com.jim_griggs.dndchar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Dice;


public class AttackActivity extends AppCompatActivity implements RollFragment.RollFragmentListener{
    private int damageResult = 0;
    private int attackResult = 0;
    private Character mCharacter;
    private Attack mAttack;
    private int mAttackNum = 0;
    private TextView attackLabel;
    private RollFragment mAttackFrag;
    private RollFragment mDamageFrag;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack);

        mCharacter = Character.getInstance();

        attackLabel = (TextView) findViewById(R.id.attackLabel);

        mAttackFrag = RollFragment.newInstance("attack", new Dice(1,20));
        Log.i("ATTACKACTIVITY","ATTACK FRAG NEWINSTANCE CALLED");
        // Add the fragment to the Layout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.attackDice, mAttackFrag).commit();

        if (savedInstanceState != null) {
            return;
        }

        displayAttack();
    }

    private void displayAttack(){
        attackResult = 0;
        damageResult = 0;

        mAttack = mCharacter.getAttacks().get(mAttackNum);
        attackLabel.setText(String.format(getString(R.string.attackNum), mAttackNum + 1));

        Log.i("AttackActivity", "Attempting to unselect attack die fragment");
        if (mAttackFrag != null) {
            mAttackFrag.unselect();
        }

        if (mDamageFrag != null) {
            getSupportFragmentManager().beginTransaction()
                    .remove(mDamageFrag).commit();
        }
        mDamageFrag = RollFragment.newInstance("damage", mAttack.getDamageDice());
        // Add the fragment to the Layout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.damageDice, mDamageFrag).commit();
    }

    public void onDiceRolled(String rollName, int result){
        if (rollName.equals("attack")) {
            attackResult = result;
        }

        if (rollName.equals("damage")) {
            damageResult = result;
        }

        if (damageResult > 0 && attackResult > 0) {
            mAttack.calcResults(attackResult, damageResult);
            mAttackNum += 1;
            if (mAttackNum < (mCharacter.getAttacks().size())) {
                displayAttack();
            } else {
                this.setResult(RESULT_OK, this.getIntent());
                finish();
            }
        }
    }


}
