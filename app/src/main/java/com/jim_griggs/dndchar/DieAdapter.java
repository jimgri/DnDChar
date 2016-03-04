package com.jim_griggs.dndchar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DieAdapter extends ArrayAdapter<Integer> {
    private Context mContext;

    // Constructor
    public DieAdapter(Context context, int resource, Integer[] objects) {
        super(context, resource, objects);
        mContext = context;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RelativeLayout rl;

        if (convertView == null) {
            rl = (RelativeLayout) LayoutInflater.from(mContext).inflate(R.layout.layout_dieface, parent, false);
            TextView t = (TextView) rl.findViewById(R.id.dieFaceText);
            t.setText(String.format("%d", getItem(position)));
        }
        else
        {
            rl = (RelativeLayout) convertView;
        }
        return rl;
    }
}
