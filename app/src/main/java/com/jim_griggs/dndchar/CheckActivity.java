package com.jim_griggs.dndchar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jim_griggs.model.Bonus;
import com.jim_griggs.model.Dice;

import java.util.ArrayList;
import java.util.Collection;

public class CheckActivity extends AppCompatActivity implements RollFragment.RollFragmentListener{
    public static final String CHECK_TITLE = "Check_Title";
    public static final String CHECK_BONUSES = "Check_Bonuses";
    public static final String CHECK_TYPE = "Check_Type";
    public static final String TYPE_BONUS = "Bonus";
    public static final String TYPE_CHECK = "Check";

    private int bonusTotal = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        // Set the activity toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ViewGroup table = (ViewGroup) findViewById(R.id.checkBonuses);
        ViewGroup results = (ViewGroup) findViewById(R.id.checkResults);

        // Load the page title.
        String title = getIntent().getStringExtra(CHECK_TITLE);
        getSupportActionBar().setTitle(title);

        // Load the page type.
        String type = getIntent().getStringExtra(CHECK_TYPE);
        if (type == null) {
            type = TYPE_CHECK;
        }

        // Load the bonus Collection
        Collection<Bonus> bonuses = (ArrayList<Bonus>) getIntent().getSerializableExtra(CHECK_BONUSES);
        Log.i("Bonus Array Size", Integer.toString(bonuses.size()));

        for (Bonus b : bonuses) {
            addBonusToTable(table, b);
            bonusTotal += b.getValue();
        }

        if (type.equals(TYPE_CHECK)) {
            RollFragment rollFrag = RollFragment.newInstance("check", new Dice(1, 20));
            // Add the fragment to the Layout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.checkDice, rollFrag).commit();
            results.setVisibility(View.VISIBLE);
        }
    }

    private void addBonusToTable(ViewGroup table, Bonus bonus){
        LinearLayout bonusBox = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.bonus_template, table, false);
        TextView bonusLabel = (TextView) bonusBox.findViewById(R.id.bonus_label);
        TextView bonusValue = (TextView) bonusBox.findViewById(R.id.bonus_value);
        bonusLabel.setText(bonus.getType());
        bonusValue.setText(Utils.toBonusString(bonus.getValue()));
        table.addView(bonusBox);
    }

    public void onDiceRolled(String rollName, int result){
        TextView resultText = (TextView) findViewById(R.id.checkResultValue);
        resultText.setText(String.format("%d", result + bonusTotal));
    }
}
