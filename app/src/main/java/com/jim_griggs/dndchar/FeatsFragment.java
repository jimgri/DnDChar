package com.jim_griggs.dndchar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.jim_griggs.model.Character;

public class FeatsFragment extends Fragment {
    private static final String MODULE_NAME = "FeatsFragment";

    ListView mRootView;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeatsFragment() {
    }

    public static FeatsFragment newInstance() {
        return new FeatsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = (ListView) inflater.inflate(R.layout.fragment_feats, container, false);

        // Set the adapter
        mRootView.setAdapter(new FeatListAdapter(getActivity(), R.id.featList, Character.getInstance().getFeats()));

        return mRootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void refresh(){
        // Reset the adapter which will re-draw the screen to the current character details.
        mRootView.setAdapter(new FeatListAdapter(getActivity(), R.id.featList, Character.getInstance().getFeats()));
    }
}
