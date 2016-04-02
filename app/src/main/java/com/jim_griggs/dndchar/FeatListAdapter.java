package com.jim_griggs.dndchar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import com.jim_griggs.model.*;
import java.util.ArrayList;
import java.util.List;

public class FeatListAdapter extends ArrayAdapter<Feat> {
    private final Context mContext;

    public FeatListAdapter(Context context, int resource, ArrayList<Feat> objects) {
        super(context, resource, (List) objects);
        mContext = context;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FeatListItem layout;

        // TODO:  reuse the convertView object instead of always recreating.
        layout = new FeatListItem(mContext);
        layout.setFeat(getItem(position));
        return layout;
    }
}
