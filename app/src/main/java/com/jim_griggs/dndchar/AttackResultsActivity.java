package com.jim_griggs.dndchar;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Character;

public class AttackResultsActivity extends AppCompatActivity {
    private static final String MODULE_NAME = "AttackResultsActivity";
    private int mRunningDamage = 0;
    private TextView mRunningDamageView;
    private ViewGroup mResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack_results);

        Character character = Character.getInstance();

        mRunningDamageView = (TextView) findViewById(R.id.runningDamage);
        mResultsList = (ViewGroup) findViewById(R.id.attackResultsList);

        int damageTotal = 0;
        DamageClick damageListener = new DamageClick();
        for (Attack a: character.getAttacks()) {
            if (a.getAttackRoll()!=0) {
                damageTotal += a.getDamageResult();
                AttackResultsListItem result = new AttackResultsListItem(this);
                result.setAttack(a);
                result.setOnClickListener(damageListener);
                Log.i(MODULE_NAME, "Adding Attack Result");
                mResultsList.addView(result);
            }
        }
        mRunningDamageView.setText(String.format(getString(R.string.runningDamage), damageTotal));
    }

    private class DamageClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            AttackResultsListItem item = (AttackResultsListItem) view;
            mRunningDamage += item.toggleSelected();
            mRunningDamageView.setText(String.format(getString(R.string.runningDamage), mRunningDamage));
        }
    }
}
