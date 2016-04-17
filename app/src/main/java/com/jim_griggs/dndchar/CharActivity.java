package com.jim_griggs.dndchar;

import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.jim_griggs.data_adapter.JSONLoader;
import com.jim_griggs.data_adapter.JSONSaver;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Stat;

public class CharActivity extends AppCompatActivity implements SpinnerDialogFragment.SpinnerButtonListener {
    private static final String MODULE_NAME = "CharActivity";
    private static final int CHARACTER_SUMMARY_PAGE = 0;
    private static final int CHARACTER_SKILLS_PAGE = 1;
    private static final int CHARACTER_FEATS_PAGE = 2;

    private ViewPager mViewPager;
    private ActivityController mController;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the Character from local file.
        loadCharacter();

        // Create the controller for launching new activities.
        mController = new ActivityController(this);

        setContentView(R.layout.activity_char);

        // Init the Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init the navigation drawer layout.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        // Build the navigation drawer toggle.
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        // Show the navigation menu option in the toolbar.
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        mDrawerToggle.syncState();

        //Init the navigation drawer
        NavigationView navigationView;
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //set up the Navigation Drawer selected item listener
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_char_summary:
                                // Change the tab page to the summary fragment.
                                mViewPager.setCurrentItem(CHARACTER_SUMMARY_PAGE);
                                // Close the navigation drawer after click.
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_char_skills:
                                // Change the tab page to the skills fragment.
                                mViewPager.setCurrentItem(CHARACTER_SKILLS_PAGE);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_char_feats:
                                // Change the tab page to the feats fragment.
                                mViewPager.setCurrentItem(CHARACTER_FEATS_PAGE);
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_attack_new:
                                // Launch the Attack Activity.
                                mController.launchAttackActivity();
                                mDrawerLayout.closeDrawers();
                                return true;
                            case R.id.nav_attack_view:
                                // Launch the Attack Results Activity.
                                mController.launchAttackResultsActivity();
                                mDrawerLayout.closeDrawers();
                                return true;
                        }
                        return true;
                    }
                });

        // Setup the pager adapter for the main character sheet view.
        CharPagerAdapter pagerAdapter = new CharPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.charPager);
        mViewPager.setAdapter(pagerAdapter);

        // Setup the tab bar at the top of the character screen to flip between pages.
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setTabsFromPagerAdapter(pagerAdapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        // Create the Tab Layout onSelected listener.
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Clicking on the tab advances the View Pager to the appropriate fragment.
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
        // When the activity pauses, save the character in case the app is shutdown before return.
        saveCharacter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Update the summary screen when we return to this activity.
        updateSummaryFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Build the Action Bar menu.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle Action Bar menu item selection
        switch (item.getItemId()) {
            case R.id.toolbarShortRest:
                // Launch the Attack Activity.
                Character.getInstance().shortRest();
                updateSummaryFragment();
                updateFeatsFragment();
                return true;
            case R.id.toolbarLongRest:
                Character.getInstance().longRest();
                updateSummaryFragment();
                updateFeatsFragment();
                return true;
            case R.id.toolbarReset:
                // Reload the character from the .json file distributed with the app.
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
            // First try to load from the local app file where the character is regularly stored.
            jl.loadCharacterFile(this);
        } catch (Exception ex){
            // If that fails, fallback to loading the character from the .json asset file provided with the app.
            jl.loadCharacterFromAsset(this, getString(R.string.saveFile));
        }
    }

    public void performReset(){
        // Load the character from the asset file.
        JSONLoader jl = new JSONLoader();
        jl.loadCharacterFromAsset(this, getString(R.string.saveFile));
        updateSummaryFragment();
    }

    public void showHPDialog(){
        // Display the dialog fragment for updating Hit Points.
        SpinnerDialogFragment hp = new SpinnerDialogFragment();
        hp.setMinValue(1);
        hp.setMaxValue(100);
        hp.setNegativeLabel("Damage");
        hp.setPositiveLabel("Heal");
        hp.show(getSupportFragmentManager(), "hp_dialog");
    }

    public void showHDDialog(){
        // Display the dialog fragment for updating Hit Dice.
        SpinnerDialogFragment hd = new SpinnerDialogFragment();
        hd.setMinValue(1);
        hd.setMaxValue(Character.getInstance().getHitDiceMax());
        hd.setNegativeLabel("Use");
        hd.setPositiveLabel("Restore");
        hd.show(getSupportFragmentManager(), "hd_dialog");
    }

    public void updateSummaryFragment() {
        // If the Feats Fragment is available, update it.
        // Called when character info may have changed and the screen needs refreshing.
        String tagName = "android:switcher:" + mViewPager.getId() + ":" + CHARACTER_SUMMARY_PAGE;
        Fragment frag = getSupportFragmentManager().findFragmentByTag(tagName);
        if (frag != null){
            if (frag instanceof CharSheetBaseFrag) {
                ((CharSheetBaseFrag)frag).refresh();
            }
        }
    }

    public void updateFeatsFragment() {
        // If the Feats Fragment is available, update it.
        // Called when character info may have changed and the screen needs refreshing.
        String tagName = "android:switcher:" + mViewPager.getId() + ":" + CHARACTER_FEATS_PAGE;
        Fragment frag = getSupportFragmentManager().findFragmentByTag(tagName);
        if (frag != null){
            if (frag instanceof FeatsFragment) {
                ((FeatsFragment)frag).refresh();
            }
        }
    }


    public void onNegativeClick(String tag, int value){
        Log.i (MODULE_NAME, "onNegativeClick called: " + tag + " " + value);
        switch (tag){
            case("hp_dialog"):
                Log.i (MODULE_NAME, "hp_dialog section: " + tag + " " + value);
                Character.getInstance().takeDamage(value);
                break;
            case("hd_dialog"):
                Log.i (MODULE_NAME, "hd_dialog section: " + tag + " " + value);
                // Heal the character for each Hit Die used.
                int heal = (Character.getInstance().getHitDie().getTakeTen()
                        + Character.getInstance().getStats().get(Stat.TYPE_CON).getStatBonus().getValue())
                        * value;
                Character.getInstance().heal(heal);
                // Subtract the hit dice from the character's total.
                Character.getInstance().subtractHitDice(value);
        }
        updateSummaryFragment();
        saveCharacter();
    }

    public void onPositiveClick(String tag, int value){
        switch (tag){
            case("hp_dialog"):
                Character.getInstance().heal(value);
            case("hd_dialog"):
                Character.getInstance().addHitDice(value);
        }
        updateSummaryFragment();
        saveCharacter();
    }

    private class CharPagerAdapter extends FragmentPagerAdapter{
        public CharPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position){
                case CHARACTER_SUMMARY_PAGE:
                    fragment = CharSheetBaseFrag.newInstance();
                    break;
                case CHARACTER_SKILLS_PAGE:
                    fragment = SkillsFrag.newInstance();
                    break;
                case CHARACTER_FEATS_PAGE:
                    fragment = FeatsFragment.newInstance();
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
