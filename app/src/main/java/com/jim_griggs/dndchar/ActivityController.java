package com.jim_griggs.dndchar;

import android.content.Context;
import android.content.Intent;
import com.jim_griggs.model.Bonus;
import com.jim_griggs.model.Feat;
import java.io.Serializable;
import java.util.Collection;

/**
 * This module is meant to reduce the number of listeners programmed into the application.
 * This app relies on a lot of fragments in order to support the tab interface.
 * Traditionally, the Activity would serve as a listener to all of these fragments.
 * However, that would result in a lot of extra listeners.  In this app, most fragment events
 * are meant to open a new activity to display additional details about an item that was clicked.
 * So, instead of using a listener model, we use a common Activity controller that can be called
 * from any fragment to open a new details screen.
 */
public class ActivityController {
    private static final int ATTACK_ACTIVITY_REQUEST_CODE = 1;

    Context mContext;

    public ActivityController(Context context){
        mContext = context;
    }

    public void launchCheckActivity(String pageTitle, String pageType, Collection<Bonus> bonuses){
        Intent i = new Intent(mContext, CheckActivity.class);
        i.putExtra(CheckActivity.CHECK_TITLE, pageTitle);
        i.putExtra(CheckActivity.CHECK_TYPE, pageType);
        i.putExtra(CheckActivity.CHECK_BONUSES, (Serializable) bonuses);
        mContext.startActivity(i);
    }

    public void launchAttackActivity (){
        Intent intent = new Intent(mContext, AttackActivity.class);
        mContext.startActivity(intent);
    }

    public void launchAttackResultsActivity (){
        Intent intent = new Intent(mContext, AttackResultsActivity.class);
        mContext.startActivity(intent);
    }

    public void launchFeatDetailsActivity(Feat feat) {
        Intent i = new Intent(mContext, FeatDetailsActivity.class);
        i.putExtra(FeatDetailsActivity.FEAT_DATA, (Serializable) feat);
        mContext.startActivity(i);
    }
}

