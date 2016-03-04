package com.jim_griggs.dndchar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import com.jim_griggs.model.Character;

public class AttackResultsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attack_results);

        Character c = Character.getInstance();
        AttackResultsAdapter adapter = new AttackResultsAdapter(this, R.id.attackResultsGrid, c.attacks);
        ListView resultsGrid = (ListView) findViewById(R.id.attackResultsGrid);
        resultsGrid.setAdapter(adapter);
    }
}
