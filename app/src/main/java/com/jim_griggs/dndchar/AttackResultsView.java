package com.jim_griggs.dndchar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Bonus;

/*
   ViewGroup containing the results of a single character attack.
   This gets returned to AttackResultsActivity for display in a list.
 */


// TODO: Change this FrameLayout to a LinearLayout
public class AttackResultsView extends FrameLayout {
    private static final int LAYOUT_MARGIN = 8;

    private Attack mAttack;
    private Context mContext;
    private TextView mAttackLabel;
    private TextView mAttackRoll;
    private TextView mCritRoll;
    private TextView mDamageRoll;
    private TextView mAttackResult;
    private TextView mDamageResult;
    private LinearLayout mAttackBonuses;
    private LinearLayout mDamageBonuses;
    private ViewGroup mRoot;
    public boolean mChecked = false;

    public AttackResultsView(Context context){
        super(context);
        initalize(context);
    }

    public AttackResultsView(Context context, AttributeSet attrs){
        super(context, attrs);
        initalize(context);
    }

    public AttackResultsView(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initalize(context);
    }

    private void initalize(Context context) {
        mContext = context;

        LayoutInflater.from(mContext).inflate(R.layout.attack_results_list_item, this);

        // As this is a programically created viewgroup, we can't define the root layout's attributes in a layout file.
        // For this viewgroup, just add margins.
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(LAYOUT_MARGIN, LAYOUT_MARGIN, LAYOUT_MARGIN, LAYOUT_MARGIN);
        setLayoutParams(lp);

        mRoot = (ViewGroup) findViewById(R.id.resultRoot);
        mAttackLabel = (TextView) findViewById(R.id.attackLabel);
        mAttackRoll = (TextView) findViewById(R.id.attackRoll);
        mDamageRoll = (TextView) findViewById(R.id.damageRoll);
        mCritRoll = (TextView) findViewById(R.id.critRoll);
        mAttackResult = (TextView) findViewById(R.id.attackResult);
        mDamageResult = (TextView) findViewById(R.id.damageResult);
        mAttackBonuses = (LinearLayout) findViewById(R.id.attackBonuses);
        mDamageBonuses = (LinearLayout) findViewById(R.id.damageBonuses);
    }

    public void setAttack(Attack attack){
        // Method accepts an attack object and renders it within the viewgroup.

        mAttack = attack;
        mAttackLabel.setText(mAttack.getName());
        mAttackRoll.setText(String.format(mContext.getString(R.string.roll), mAttack.getAttackRoll()));
        mDamageRoll.setText(String.format(mContext.getString(R.string.roll), mAttack.getDamageRoll()));
        // If this Attack reuslted in a Crit, display the Crit roll.
        if (mAttack.isCrit()){
            mCritRoll.setText(String.format(mContext.getString(R.string.crit), mAttack.getCritRoll()));
            mCritRoll.setVisibility(View.VISIBLE);
        }
        mAttackResult.setText(String.format(mContext.getString(R.string.hit), mAttack.getAttackResult()));
        mDamageResult.setText(String.format(mContext.getString(R.string.damage), mAttack.getDamageResult()));

        // Loop through and display the Attack Bonuses.
        mAttackBonuses.removeAllViews();
        for (Bonus b: mAttack.getAttackBonuses()){
            LinearLayout bonusView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.bonus_template, mAttackBonuses, false);
            TextView bl = (TextView) bonusView.findViewById(R.id.bonus_label);
            TextView bv = (TextView) bonusView.findViewById(R.id.bonus_value);
            bl.setText(b.getType());
            bv.setText(String.format("%d", b.getValue()));
            mAttackBonuses.addView(bonusView);
        }

        // Loop through and display the Damage Bonuses.
        mDamageBonuses.removeAllViews();
        for (Bonus b: mAttack.getDamageBonuses()) {
            LinearLayout bonusView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.bonus_template, mDamageBonuses, false);
            TextView bl = (TextView) bonusView.findViewById(R.id.bonus_label);
            TextView bv = (TextView) bonusView.findViewById(R.id.bonus_value);
            bl.setText(b.getType());
            bv.setText(String.format("%d", b.getValue()));
            mDamageBonuses.addView(bonusView);
        }
        //  Draw a boarder around the entire attack result view.
        drawBorder();
    }

    public int toggleSelected(){
        // Method to change if this attack roll is clicked.
        mChecked = !mChecked;
        // Update the border to highlight or un-highlight the viewgroup appropriately.
        drawBorder();
        // Return the amount of damage that should be added or subtracted from the total damage.
        if (mChecked) {
            return mAttack.getDamageResult();
        } else {
            return -(mAttack.getDamageResult());
        }
    }

    private void drawBorder(){
        if (mChecked) {
            // Highlight the box.
            Drawable d = mContext.getResources().getDrawable(R.drawable.blue_border_thick, mContext.getTheme());
            mRoot.setBackground(d);
        } else {
            // Un-highlight the box
            Drawable d = mContext.getResources().getDrawable(R.drawable.black_border_thick, mContext.getTheme());
            mRoot.setBackground(d);
        }
    }
}
