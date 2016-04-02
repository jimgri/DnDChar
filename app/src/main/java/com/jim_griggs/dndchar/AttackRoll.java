package com.jim_griggs.dndchar;

import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jim_griggs.model.Attack;
import com.jim_griggs.model.Dice;

public class AttackRoll extends LinearLayout{
    AppCompatActivity mActivity;
    int mAttackNum;
    Attack mAttack;
    TextView mAttackLabel;
    ViewGroup mAttackRoll;
    ViewGroup mDamageRoll;

    public AttackRoll(AppCompatActivity activity, int attackNum, Attack attack){
        super(activity);
        initalize(activity, attackNum, attack);
    }

    private void initalize(AppCompatActivity activity, int attackNum, Attack attack) {
        mAttack = attack;
        mAttackNum = attackNum;
        mActivity = activity;

        LayoutInflater.from(mActivity).inflate(R.layout.attackroll_template, this);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(4, 4, 4, 4);
        setLayoutParams(lp);
        setOrientation(LinearLayout.VERTICAL);

        mAttackLabel = (TextView) findViewById(R.id.attackLabel);
        mAttackRoll = (ViewGroup) findViewById(R.id.attackDice);
        mDamageRoll = (ViewGroup) findViewById(R.id.damageDice);

        displayAttack();
    }

    private void displayAttack(){
        mAttackLabel.setText(mAttack.getName());

        int id = View.generateViewId();
        mAttackRoll.setId(id);

        RollFragment attackRoll = RollFragment.newInstance("Attack:" + Integer.toString(mAttackNum), new Dice(1, 20));
        // Add the fragment to the Layout

        mActivity.getSupportFragmentManager().beginTransaction()
                .add(id, attackRoll).commit();

        id = View.generateViewId();
        mDamageRoll.setId(id);

        RollFragment damageRoll = RollFragment.newInstance("Damage:" + Integer.toString(mAttackNum), mAttack.getDamageDice());
        // Add the fragment to the Layout
        mActivity.getSupportFragmentManager().beginTransaction()
                .add(id, damageRoll).commit();
    }
}
