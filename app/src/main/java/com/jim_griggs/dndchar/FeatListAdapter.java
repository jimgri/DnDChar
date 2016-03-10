package com.jim_griggs.dndchar;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jim_griggs.dndchar.dummy.DummyContent.DummyItem;
import com.jim_griggs.model.*;
import com.jim_griggs.model.Character;

import android.view.View.OnClickListener;

import java.util.Collection;
import java.util.List;

public class FeatListAdapter extends ArrayAdapter<Feat> {
    private final Context mContext;

    public FeatListAdapter(Context context, int resource, Collection<Feat> objects) {
        super(context, resource, (List) objects);
        Log.i("NUMBER OF FEATS", Integer.toString(objects.size()));
        mContext = context;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FeatListItem layout;

        Log.i("GETVIEW CALLED", "");

        if (convertView == null) {
            layout = new FeatListItem(mContext);
        } else {
            layout = (FeatListItem) convertView;
        };
        layout.setFeat(getItem(position));
        return layout;
    }
}
