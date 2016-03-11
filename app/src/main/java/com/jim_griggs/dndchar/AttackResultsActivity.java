package com.jim_griggs.dndchar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Character;

public class AttackResultsActivity extends AppCompatActivity {
    private int mRunningDamage = 0;
    private TextView mRunningDamageView;
    private ListView mAttackResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack_results);

        Character c = Character.getInstance();

        mRunningDamageView = (TextView) findViewById(R.id.runningDamage);
        mAttackResults = (ListView) findViewById(R.id.attackResultsList);

        int damageTotal = 0;
        for (Attack a: c.attacks) {
            damageTotal += a.damageResult;
        }
        mRunningDamageView.setText(String.format(getString(R.string.runningDamage), damageTotal));

        AttackResultsAdapter adapter = new AttackResultsAdapter(this, R.id.attackResultsList, c.attacks);
        mAttackResults.setAdapter(adapter);
        mAttackResults.setOnItemClickListener(new DamageClick());
    }

    private class DamageClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AttackResultsListItem item = (AttackResultsListItem) view;
            mRunningDamage += item.toggleSelected();
            mRunningDamageView.setText(String.format(getString(R.string.runningDamage), mRunningDamage));
        }
    }
}
