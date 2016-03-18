package com.jim_griggs.dndchar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Bonus;

// TODO: Change this FrameLayout to a LinearLayout
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

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(8, 8, 8, 8);
        setLayoutParams(lp);

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

        mAttackLabel.setText(mAttack.getName());
        mAttackRoll.setText(String.format(mContext.getString(R.string.roll), mAttack.getAttackRoll()));
        mDamageRoll.setText(String.format(mContext.getString(R.string.roll), mAttack.getDamageRoll()));
        mAttackResult.setText(String.format(mContext.getString(R.string.hit), mAttack.getAttackResult()));
        mDamageResult.setText(String.format(mContext.getString(R.string.damage), mAttack.getDamageResult()));

        mAttackBonuses.removeAllViews();
        for (Bonus b: mAttack.getAttackBonuses()){
            LinearLayout bonusView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.bonus_template, mAttackBonuses, false);
            TextView bl = (TextView) bonusView.findViewById(R.id.bonus_label);
            TextView bv = (TextView) bonusView.findViewById(R.id.bonus_value);
            bl.setText(b.getType());
            bv.setText(String.format("%d", b.getValue()));
            mAttackBonuses.addView(bonusView);
        }

        mDamageBonuses.removeAllViews();
        for (Bonus b: mAttack.getDamageBonuses()) {
            LinearLayout bonusView = (LinearLayout) LayoutInflater.from(mContext).inflate(R.layout.bonus_template, mDamageBonuses, false);
            TextView bl = (TextView) bonusView.findViewById(R.id.bonus_label);
            TextView bv = (TextView) bonusView.findViewById(R.id.bonus_value);
            bl.setText(b.getType());
            bv.setText(String.format("%d", b.getValue()));
            mDamageBonuses.addView(bonusView);
        }

        drawBorder();
    }

    public int toggleSelected(){
        mChecked = !mChecked;
        drawBorder();
        if (mChecked) {
            return mAttack.getDamageResult();
        } else {
            return -(mAttack.getDamageResult());
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
