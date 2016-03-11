package com.jim_griggs.dndchar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Bonus;

public class AttackResultsListItem extends FrameLayout {
    private Attack mAttack;
    private Context mContext;
    private TextView mAttackLabel;
    private TextView mAttackRoll;
    private TextView mDamageRoll;
    private TextView mAttackResult;
    private TextView mDamageResult;
    private LinearLayout mAttackBonuses;
    private LinearLayout mDamageBonuses;
    public boolean mChecked = false;
    private View mOverlay;

    public AttackResultsListItem(Context context){
        super(context);
        initalize(context);
    }

    public AttackResultsListItem(Context context, AttributeSet attrs){
        super(context, attrs);
        initalize(context);
    }

    public AttackResultsListItem(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initalize(context);
    }

    private void initalize(Context context) {
        mContext = context;

        LayoutInflater.from(mContext).inflate(R.layout.attack_results_list_item, this);

        mOverlay = findViewById(R.id.overlay);
        mAttackLabel = (TextView) findViewById(R.id.attackLabel);
        mAttackRoll = (TextView) findViewById(R.id.attackRoll);
        mDamageRoll = (TextView) findViewById(R.id.damageRoll);
        mAttackResult = (TextView) findViewById(R.id.attackResult);
        mDamageResult = (TextView) findViewById(R.id.damageResult);
        mAttackBonuses = (LinearLayout) findViewById(R.id.attackBonuses);
        mDamageBonuses = (LinearLayout) findViewById(R.id.damageBonuses);
    }

    public void setAttack(Attack attack){
        mAttack = attack;

        Log.i("SETTING ATTACK IN ITEM", "HERE");
        mAttackLabel.setText(mAttack.name);
        mAttackRoll.setText(String.format(mContext.getString(R.string.roll), mAttack.attackRoll));
        mDamageRoll.setText(String.format(mContext.getString(R.string.roll), mAttack.damageRoll));
        mAttackResult.setText(String.format(mContext.getString(R.string.hit), mAttack.attackResult));
        mDamageResult.setText(String.format(mContext.getString(R.string.damage), mAttack.damageResult));

        mAttackBonuses.removeAllViews();
        for (Bonus b: mAttack.attackBonuses){
            Log.i("Loading Next ABonus",b.type);
            LinearLayout bonusView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.bonus_template, mAttackBonuses, false);
            TextView bl = (TextView) bonusView.findViewById(R.id.bonus_label);
            TextView bv = (TextView) bonusView.findViewById(R.id.bonus_value);
            bl.setText(b.type);
            bv.setText(String.format("%d", b.value));
            mAttackBonuses.addView(bonusView);
        }

        mDamageBonuses.removeAllViews();
        for (Bonus b: mAttack.damageBonuses) {
            LinearLayout bonusView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.bonus_template, mDamageBonuses, false);
            TextView bl = (TextView) bonusView.findViewById(R.id.bonus_label);
            TextView bv = (TextView) bonusView.findViewById(R.id.bonus_value);
            bl.setText(b.type);
            bv.setText(String.format("%d", b.value));
            mDamageBonuses.addView(bonusView);
        }

        drawBorder();
    }

    public int toggleSelected(){
        mChecked = !mChecked;
        drawBorder();
        if (mChecked) {
            return mAttack.damageResult;
        } else {
            return -(mAttack.damageResult);
        }
    }

    private void drawBorder(){
        if (mChecked) {
            // Highlight the box.
            Drawable d = mContext.getResources().getDrawable(R.drawable.blue_border, mContext.getTheme());
            mOverlay.setBackground(d);
        } else {
            // Un-highlight the box
            Drawable d = mContext.getResources().getDrawable(R.drawable.black_border, mContext.getTheme());
            mOverlay.setBackground(d);
        }
    }
}
