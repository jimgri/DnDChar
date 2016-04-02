package com.jim_griggs.dndchar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim_griggs.model.Feat;

public class FeatListItem extends LinearLayout {
    Feat mFeat;
    Context mContext;
    TextView mNameView;
    ViewGroup mUsageLayout;

    public FeatListItem (Context context){
        super(context);
        initalize(context);
    }

    public FeatListItem (Context context, AttributeSet attrs){
        super(context, attrs);
        initalize(context);
    }

    public FeatListItem (Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
        initalize(context);
    }

    private void initalize(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.feat_template, this);
        mNameView = (TextView) this.findViewById(R.id.featName);
        mUsageLayout = (ViewGroup) this.findViewById(R.id.usageChecks);
    }

    public void setFeat(Feat feat){
        mFeat = feat;

        mNameView.setText(mFeat.getName());

        int count = mFeat.getCurrentUsage();
        for (int i=0; i <mFeat.getMaxUsage(); i++) {
            CheckBox c = new CheckBox(mContext);
            if (count > 0){
                c.setChecked(true);
                count--;
            }
            c.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    CheckBox c = (CheckBox) v;
                    if (c.isChecked()) {
                        mFeat.use();
                    } else {
                        mFeat.restore();
                    }
                }
            });
            mUsageLayout.addView(c);
        }

        this.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController controller = new ActivityController(mContext);
                controller.launchFeatDetailsActivity(mFeat);
            }
        });
    }
}