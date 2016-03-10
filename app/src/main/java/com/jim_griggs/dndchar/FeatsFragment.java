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
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public FeatsFragment() {
    }

    public static FeatsFragment newInstance() {
        FeatsFragment fragment = new FeatsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ListView view = (ListView) inflater.inflate(R.layout.fragment_feats, container, false);

        Log.i("Feat size", Integer.toString(Character.getInstance().feats.size()));
        // Set the adapter
        view.setAdapter(new FeatListAdapter(getActivity(), R.id.featList, Character.getInstance().feats));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
