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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import com.jim_griggs.data_adapter.JSONLoader;
import com.jim_griggs.data_adapter.JSONSaver;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Bonus;
import com.jim_griggs.model.Feat;

public class CharActivity extends AppCompatActivity implements FeatListItem.FeatItemListener {
    private static final int ATTACK_ACTIVITY_REQUEST_CODE = 1;

    private Character mCharacter;
    private ViewPager mViewPager;
    private CharSheetBaseFrag mSummaryFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the character from file.
        JSONLoader jl = new JSONLoader();

        try {
            jl.loadCharacterFile(this);
        } catch (Exception ex){
            jl.loadCharacterFromAsset(this, getString(R.string.saveFile));
        }

        mCharacter = Character.getInstance();

        setContentView(R.layout.activity_char);

        // Set the activity toolbar.
        Toolbar myToolbar = (Toolbar) findViewById(R.id.appToolbar);
        setSupportActionBar(myToolbar);

        CharPagerAdapter pagerAdapter = new CharPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.charPager);
        mViewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
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
    protected void onResume() {
        super.onResume();
        updateCharSummary();
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

    public void saveCharacter (){
        FileOutputStream outStream = null;
        try {
            outStream = openFileOutput(getString(R.string.saveFile), MODE_PRIVATE);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (outStream != null) {
            Runnable saver = new JSONSaver(outStream, mCharacter);
            new Thread(saver).start();
        }
    }

    public void performAttack(){
        launchAttackActivity();
    }

    public void launchCheckActivity(String pageTitle, String pageType, Collection<Bonus> bonuses){
        Intent i = new Intent(this, CheckActivity.class);
        i.putExtra(CheckActivity.CHECK_TITLE, pageTitle);
        i.putExtra(CheckActivity.CHECK_TYPE, pageType);
        i.putExtra(CheckActivity.CHECK_BONUSES, (Serializable) bonuses);
        startActivity(i);
    }

    private void launchAttackActivity (){
        Intent intent = new Intent(this, AttackActivity.class);
        startActivityForResult(intent, ATTACK_ACTIVITY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ATTACK_ACTIVITY_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                launchAttackResultsActivity();
            }
        }
    }

    private void launchAttackResultsActivity (){
        Intent intent = new Intent(this, AttackResultsActivity.class);
        startActivity(intent);
    }

    public void onFeatItemClick(Feat feat){
        int feat_position = mCharacter.getFeats().indexOf(feat);
        Intent i = new Intent(this, FeatDetailsActivity.class);
        i.putExtra(FeatDetailsActivity.FEAT_ID, feat_position);
        startActivity(i);
    }

    public void showHPDialog(){
        HPDialogFragment newFragment = new HPDialogFragment();
        newFragment.show(getSupportFragmentManager(), "hp_dialog");
    }

    public void updateCharSummary() {
        if (mSummaryFragment != null){
            mSummaryFragment.refresh();
        }
        // Whenever we have means to update the Char Summary fragment, also save the character.
        saveCharacter();
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
                    // Yes, storing a reference to the frament ouside the pager means they
                    // with remain in memory which kind of defeats the point of using an adapter.
                    // However, we need to be able to make method calls on the Fragment as, as this
                    // article indicates:
                    // http://stackoverflow.com/questions/17685787/access-a-method-of-a-fragment-from-the-viewpager-activity
                    // It s nearly impossible to do that without holding a reference.
                    // I'll consider changing it if I have performance issues.
                    fragment = CharSheetBaseFrag.newInstance();
                    mSummaryFragment = (CharSheetBaseFrag) fragment;
                    return fragment;
            }
            // Whenever we switch tabs, perform a character save.
            saveCharacter();
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
