package com.jim_griggs.dndchar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import com.jim_griggs.model.*;
import com.jim_griggs.model.Character;

public class FeatDetailsActivity extends AppCompatActivity {
    public static final String FEAT_ID = "feat_position";
    public Feat mFeat;
    public TextView mName;
    public TextView mDesc;
    public TextView mNum;
    public TextView mRefresh;
    public TextView mLevel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feat_details);

        Intent intent = getIntent();
        int feat_position = intent.getIntExtra(FEAT_ID, -1);

        Character character = Character.getInstance();
        mFeat = character.getFeats().get(feat_position);

        mName = (TextView) findViewById(R.id.featName);
        mDesc = (TextView) findViewById(R.id.featDescription);
        mNum = (TextView) findViewById(R.id.featNumUsage);
        mRefresh = (TextView) findViewById(R.id.featRefresh);
        mLevel = (TextView) findViewById(R.id.featLevelAcquired);

        mName.setText(mFeat.getName());
        mDesc.setText(mFeat.getDescription());
        mNum.setText(String.format("%1d", mFeat.getCurrentUsage()));
        mRefresh.setText(mFeat.getRefresh());
        mLevel.setText(String.format("%1d", mFeat.getLevelAcquired()));

    }
}
