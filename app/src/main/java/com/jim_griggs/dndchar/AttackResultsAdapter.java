package com.jim_griggs.dndchar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Collection;
import java.util.List;

import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Bonus;

public class AttackResultsAdapter extends ArrayAdapter<Attack> {
    private final Context mContext;

    // Constructor
    public AttackResultsAdapter(Context context, int resource, Collection<Attack> objects) {
        super(context, resource, (List) objects);
        mContext = context;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout layout;

        if (convertView == null) {
            Attack attack = getItem(position);

            layout = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_attack_results, parent, false);

            TextView label = (TextView) layout.findViewById(R.id.attackLabel);
            label.setText(attack.name);

            TextView attackRoll = (TextView) layout.findViewById(R.id.attackRoll);
            TextView damageRoll = (TextView) layout.findViewById(R.id.damageRoll);
            TextView attackResult = (TextView) layout.findViewById(R.id.attackResult);
            TextView damageResult = (TextView) layout.findViewById(R.id.damageResult);
            attackRoll.setText(String.format(mContext.getString(R.string.roll), attack.attackRoll));
            damageRoll.setText(String.format(mContext.getString(R.string.roll), attack.damageRoll));
            attackResult.setText(String.format(mContext.getString(R.string.hit), attack.attackResult));
            damageResult.setText(String.format(mContext.getString(R.string.damage), attack.damageResult));

            LinearLayout ag = (LinearLayout) layout.findViewById(R.id.attackBonuses);
            for (Bonus b: attack.attackBonuses){
                LinearLayout bonusView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.bonus_template, ag, false);
                TextView bl = (TextView) bonusView.findViewById(R.id.bonus_label);
                TextView bv = (TextView) bonusView.findViewById(R.id.bonus_value);
                bl.setText(b.type);
                bv.setText(String.format("%d", b.value));
                ag.addView(bonusView);
            }

            LinearLayout dg = (LinearLayout) layout.findViewById(R.id.damageBonuses);
            for (Bonus b: attack.damageBonuses){
                LinearLayout bonusView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.bonus_template, dg, false);
                TextView bl = (TextView) bonusView.findViewById(R.id.bonus_label);
                TextView bv = (TextView) bonusView.findViewById(R.id.bonus_value);
                bl.setText(b.type);
                bv.setText(String.format("%d", b.value));
                dg.addView(bonusView);
            }
        }
        else
        {
            layout = (LinearLayout) convertView;
        }
        return layout;
    }
}
