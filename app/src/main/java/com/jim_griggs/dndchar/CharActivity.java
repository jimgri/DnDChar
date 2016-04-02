package com.jim_griggs.dndchar;

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

import com.jim_griggs.data_adapter.JSONLoader;
import com.jim_griggs.data_adapter.JSONSaver;
import com.jim_griggs.model.Character;

public class CharActivity extends AppCompatActivity implements onCharacterUpdateListener {
    private static final String MODULE_NAME = "CharActivity";
    private ViewPager mViewPager;
    private CharSheetBaseFrag mSummaryFragment;
    private ActivityController mController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mController = new ActivityController(this);

        setContentView(R.layout.activity_char);

        // Set the activity toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
    protected void onPause() {
        super.onPause();
        saveCharacter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(MODULE_NAME, "onResume Called.");
        loadCharacter();
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
                mController.launchAttackActivity();
                return true;
            case R.id.toolbarReset:
                performReset();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void saveCharacter (){
        JSONSaver.saveCharacter(this, Character.getInstance());
    }

    public void loadCharacter(){
        // Load the character from file.
        JSONLoader jl = new JSONLoader();
        try {
            jl.loadCharacterFile(this);
            Log.i (MODULE_NAME, "Loading From File Complete.");
        } catch (Exception ex){
            jl.loadCharacterFromAsset(this, getString(R.string.saveFile));
            Log.i (MODULE_NAME, "Loading From Asset Complete.");
        }
    }

    public void performReset(){
        // Load the character from the asset file.
        JSONLoader jl = new JSONLoader();
        jl.loadCharacterFromAsset(this, getString(R.string.saveFile));
        updateCharSummary();
    }

    public void showHPDialog(){
        HPDialogFragment newFragment = new HPDialogFragment();
        newFragment.show(getSupportFragmentManager(), "hp_dialog");
    }

    public void onCharacterUpdate(){
        updateCharSummary();
        saveCharacter();
    }

    public void updateCharSummary() {
        if (mSummaryFragment != null){
            mSummaryFragment.refresh();
        }
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
                    // Yes, storing a reference to the fragment outside the pager means they
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
