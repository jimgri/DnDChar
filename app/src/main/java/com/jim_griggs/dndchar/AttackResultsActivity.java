package com.jim_griggs.dndchar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Character;

public class AttackResultsActivity extends AppCompatActivity {
    private static final String MODULE_NAME = "AttackResultsActivity";
    private int mRunningDamage = 0;
    private TextView mRunningDamageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack_results);

        Character character = Character.getInstance();

        mRunningDamageView = (TextView) findViewById(R.id.runningDamage);
        ViewGroup mResultsList = (ViewGroup) findViewById(R.id.attackResultsList);

        int damageTotal = 0;
        DamageClick damageListener = new DamageClick();

        // Loop through the attacks, build the view for each, and add to the screen.
        // Not using a View/ViewAdapter here because this is a small, fixed size list.  No need to
        // incur the overhead of paging or view re-use.
        for (Attack a: character.getAttacks()) {
            // Only diplay the attacks for which we have an attack roll provided by the user.
            if (a.getAttackRoll()!=0) {
                damageTotal += a.getDamageResult();
                // Build the Attack Results view and assign the Click listener
                AttackResultsView result = new AttackResultsView(this);
                result.setAttack(a);
                result.setOnClickListener(damageListener);
                // Add the Attack Results view to the list.
                mResultsList.addView(result);
            }
        }
        mRunningDamageView.setText(String.format(getString(R.string.runningDamage), damageTotal));
    }

    private class DamageClick implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            AttackResultsView item = (AttackResultsView) view;
            // Highlight the selected item and add the damage to the running damage count.
            mRunningDamage += item.toggleSelected();
            // Display the running damage count.
            mRunningDamageView.setText(String.format(getString(R.string.runningDamage), mRunningDamage));
        }
    }
}
