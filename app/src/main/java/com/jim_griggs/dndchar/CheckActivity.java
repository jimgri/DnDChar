package com.jim_griggs.dndchar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jim_griggs.model.Bonus;
import java.util.ArrayList;
import java.util.Collection;

public class CheckActivity extends AppCompatActivity implements RollFragment.RollFragmentListener{
    private int bonusTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        // Load the bonus Collection
        Collection<Bonus> bonuses = (ArrayList<Bonus>) getIntent().getSerializableExtra(CharActivity.CHECK_MESSAGE);
        Log.i("Bonus Array Size", Integer.toString(bonuses.size()));

        LinearLayout table = (LinearLayout) findViewById(R.id.checkBonuses);
        for (Bonus b : bonuses){
            addBonusToTable(table, b);
            bonusTotal += b.value;
        }

        RollFragment rollFrag = RollFragment.newInstance("check", 1, 20);
        // Add the fragment to the Layout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.checkDice, rollFrag).commit();

    }

    private void addBonusToTable(LinearLayout table, Bonus bonus){
        LinearLayout bonusBox = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bonus_template, table, false);
        TextView bonusLabel = (TextView) bonusBox.findViewById(R.id.bonus_label);
        TextView bonusValue = (TextView) bonusBox.findViewById(R.id.bonus_value);
        bonusLabel.setText(bonus.type);
        bonusValue.setText(Utils.toBonusString(bonus.value));
        table.addView(bonusBox);
    }

    public void onDiceRolled(String rollName, int result){
        TextView resultText = (TextView) findViewById(R.id.checkResults);
        resultText.setText(String.format("%d", result + bonusTotal));
    }

    public void onCloseButton(View view){
        finish();
    }


}
