package com.jim_griggs.dndchar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.jim_griggs.model.Character;

public class AttackActivity extends AppCompatActivity implements RollFragment.RollFragmentListener{
    private int damageResult = 0;
    private int attackResult = 0;
    private Character c;
    private int attackNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("info", "Attack Activity Started");
        setContentView(R.layout.activity_attack);

        c = Character.getInstance();

        Intent intent = getIntent();
        attackNum = intent.getIntExtra(CharActivity.ATTACK_MESSAGE, 0);

        if (savedInstanceState != null) {
            return;
        }

        TextView t = (TextView) findViewById(R.id.attackLabel);
        t.setText(String.format(getString(R.string.attackNum), attackNum + 1));

        RollFragment attackFrag = RollFragment.newInstance("attack", 1, c.attacks.get(attackNum).attackDieType);
        // Add the fragment to the Layout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.attackDice, attackFrag).commit();

        for (int i=0; i < c.attacks.get(attackNum).damageDieNum; i++) {
            RollFragment damageFrag = RollFragment.newInstance("damage", c.attacks.get(attackNum).damageDieNum, c.attacks.get(attackNum).damageDieType);
            // Add the fragment to the Layout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.damageDice, damageFrag).commit();
        }
        Log.i("info", "Attack Activity Created");

    }

    public void onDiceRolled(String rollName, int result){
        Log.i("info", "Die Listener Triggered: " + Integer.toString(result));
        if (rollName.equals("attack")) {
            attackResult = result;
        }

        if (rollName.equals("damage")) {
            damageResult = result;
        }

        if (damageResult > 0 && attackResult > 0) {
            Log.i("info", "Creating Return Intent");
            c.attacks.get(attackNum).calcResults(attackResult, damageResult);
            Intent intent = this.getIntent();
            this.setResult(RESULT_OK, intent);
            Log.i("temp", "Attack Activity Finishing");
            finish();
        }
    }


}
