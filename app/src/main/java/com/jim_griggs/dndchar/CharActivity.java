package com.jim_griggs.dndchar;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Collection;

import com.jim_griggs.data_adapter.JSONLoader;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Bonus;
import com.jim_griggs.model.Feat;

public class CharActivity extends AppCompatActivity implements FeatListItem.FeatItemListener {
    public final static String ATTACK_MESSAGE = "attack_number";
    private final static int ATTACK_REQUEST = 1;
    private int currentAttack;
    private Character c;
    private CharPagerAdapter mPagerAdapter;
    private ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the character from file.
        JSONLoader jl = new JSONLoader();
        try {
            jl.loadCharacterFile(this.getAssets().open(getString(R.string.saveFile)));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        c = Character.getInstance();

        setContentView(R.layout.activity_char);

        // Set the activity toolbar.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(myToolbar);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.toolbarAttack:
                performAttack();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void performAttack(){
        currentAttack = 0;
        launchAttackActivity(currentAttack);
    }

    public void launchCheckActivity(String pageTitle, String pageType, Collection<Bonus> bonuses){
        Intent i = new Intent(this, CheckActivity.class);
        i.putExtra(CheckActivity.CHECK_TITLE, pageTitle);
        i.putExtra(CheckActivity.CHECK_TYPE, pageType);
        i.putExtra(CheckActivity.CHECK_BONUSES, (Serializable) bonuses);
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

    public void onFeatItemClick(Feat feat){
        int feat_position = c.feats.indexOf(feat);
        Intent i = new Intent(this, FeatDetailsActivity.class);
        i.putExtra(FeatDetailsActivity.FEAT_ID, feat_position);
        startActivity(i);
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
                    fragment = SkillsFrag.newInstance();
                    break;
                case 2:
                    fragment = FeatsFragment.newInstance();
                    break;
                default:
                    fragment = CharSheetBaseFrag.newInstance();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 1:
                    return "Skills";
                case 2:
                    return "Feats";
                default:
                    return "Overview";
            }
        }
    }

}
