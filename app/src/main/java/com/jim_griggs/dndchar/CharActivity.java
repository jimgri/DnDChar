package com.jim_griggs.dndchar;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import java.io.Serializable;
import java.util.Collection;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Bonus;

public class CharActivity extends AppCompatActivity {
    public final static String ATTACK_MESSAGE = "attack_number";
    public final static String CHECK_MESSAGE = "bonuses";
    private final static int ATTACK_REQUEST = 1;
    private int currentAttack;
    private Character c;
    private CharPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c = Character.getInstance();

        setContentView(R.layout.activity_char);

        mPagerAdapter = new CharPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.charPager);
        mViewPager.setAdapter(mPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabsFromPagerAdapter(mPagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    public void onAttackButtonClick(View view){
        currentAttack = 0;
        launchAttackActivity(currentAttack);
    }

    public void launchCheckActivity(Collection<Bonus> bonuses){
        Intent i = new Intent(this, CheckActivity.class);
        i.putExtra(CHECK_MESSAGE, (Serializable) bonuses);
        startActivity(i);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.i("info", "Return from Attack Activity");
        if (requestCode == ATTACK_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                currentAttack++;
                if (currentAttack < c.attacks.size()) {
                    launchAttackActivity(currentAttack);
                } else {
                    launchAttackResultsActivity();
                }
            }
        }
    }

    private void launchAttackActivity (int currentAttack){
        Intent intent = new Intent(this, AttackActivity.class);
        intent.putExtra(ATTACK_MESSAGE, currentAttack);
        Log.i("info", "Creating Activity for Attack");
        startActivityForResult(intent, ATTACK_REQUEST);
    }

    private void launchAttackResultsActivity (){
        Intent intent = new Intent(this, AttackResultsActivity.class);
        startActivity(intent);
    }


    private class CharPagerAdapter extends FragmentPagerAdapter{
        public CharPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            Log.i("getFragmentItem", Integer.toString(position));
            switch (position){
                case 1:
                    fragment = new SkillsFrag();
                    break;
                default:
                    fragment = new CharSheetBaseFrag();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 1:
                    return "Skills";
                default:
                    return "Character Sheet";
            }
        }
    }

}
