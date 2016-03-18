package com.jim_griggs.dndchar;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import java.util.List;
import com.jim_griggs.model.Attack;


public class AttackResultsAdapter extends ArrayAdapter<Attack> {
    private final Context mContext;

    // Constructor
    public AttackResultsAdapter(Context context, int resource, ArrayList<Attack> objects) {
        super(context, resource, (List) objects);
        mContext = context;
    }

    // create a new ImageView for each item referenced by the Adapter
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AttackResultsListItem layout;

        if (convertView == null) {
            layout = new AttackResultsListItem(mContext);
        } else {
            layout = (AttackResultsListItem) convertView;
        }
        layout.setAttack(getItem(position));
        return layout;
    }
}
