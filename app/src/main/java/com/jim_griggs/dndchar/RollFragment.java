package com.jim_griggs.dndchar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RollFragment.RollFragmentListener} interface
 * to handle interaction events.
 * Use the {@link RollFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class RollFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_ROLLNAME = "rollName";
    private static final String ARG_DIESIDECOUNT = "dieSideCount";
    private static final String ARG_NUMBEROFDICE = "numberOfDice";
    private static final Integer NO_SELECTION = -1;

    private String rollName;
    private int dieSideCount;
    private int numberOfDice;
    private int[] mSelected;
    private LinearLayout mRollGrid;

    private RollFragmentListener mListener;

    public interface RollFragmentListener {
        void onDiceRolled(String id, int result);
    }


    public RollFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param dieSideCount Parameter 1.
     * @return A new instance of fragment RollFragment.
     */
    public static RollFragment newInstance(String rollName, int numberOfDice, int dieSideCount) {
        RollFragment fragment = new RollFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROLLNAME, rollName);
        args.putInt(ARG_DIESIDECOUNT, dieSideCount);
        args.putInt(ARG_NUMBEROFDICE, numberOfDice);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            rollName = getArguments().getString(ARG_ROLLNAME);
            dieSideCount = getArguments().getInt(ARG_DIESIDECOUNT);
            numberOfDice = getArguments().getInt(ARG_NUMBEROFDICE);
            mSelected = new int[numberOfDice];
            for (int i=0; i < numberOfDice; i++){
                mSelected[i] = NO_SELECTION;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the roll layout.  This just contains a grid.
        View layout = inflater.inflate(R.layout.fragment_roll, container, false);
        // Get the roll layout
        mRollGrid = (LinearLayout) layout.findViewById(R.id.rollGrid);
        // For each Die
        for (int i =0; i<numberOfDice; i++){
            // Create the Die Faces
            View dieLayout = createDie(inflater, mRollGrid);
            // Add to RollGrid
            mRollGrid.addView(dieLayout);
        }
        return mRollGrid;
    }

    private View createDie(LayoutInflater inflater, ViewGroup rollGrid){
        // Inflate the Die Layout
        GridLayout dieLayout = (GridLayout) inflater.inflate(R.layout.die_template, rollGrid, false);
        dieLayout.setColumnCount(getGridColumnCount());
        DieListener dieListener = new DieListener();
        for (int i=0; i < dieSideCount; i++){
            View dieFace = inflater.inflate(R.layout.layout_dieface, dieLayout, false);
            dieFace.setOnClickListener(dieListener);
            TextView t = (TextView) dieFace.findViewById(R.id.dieFaceText);
            t.setText(String.format("%d", i+1));
            dieLayout.addView(dieFace);
        }
        return dieLayout;
    }

    private int getGridColumnCount(){
        switch (dieSideCount){
            case 8:
                return 8;
            case 20:
                return 5;
        }
        return 0;
    }


    private class DieListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int dieNum = mRollGrid.indexOfChild(v);
            Log.i("die num clicked", Integer.toString(dieNum));
            TextView numView = (TextView) v.findViewById(R.id.dieFaceText);
            int dieFaceValue = Integer.parseInt(numView.getText().toString());
            int previousSelection = mSelected[dieNum];
            if (previousSelection != NO_SELECTION){
                View selectedView = mRollGrid.getChildAt(previousSelection);
                ImageView image = (ImageView) selectedView.findViewById(R.id.dieFaceImage);
                image.setImageResource(R.drawable.d20);
            }
            mSelected[dieNum] = dieFaceValue;
            ImageView image = (ImageView) v.findViewById(R.id.dieFaceImage);
            image.setImageResource(R.drawable.d20h);
            if (mListener != null) {
                boolean allSelected = true;
                int rollTotal = 0;
                for (int i=0; i < mSelected.length; i++){
                    if (mSelected[i] == NO_SELECTION){
                        allSelected = false;
                        break;
                    }
                    rollTotal += mSelected[i] + 1;
                }
                if (allSelected) {
                    mListener.onDiceRolled(rollName, rollTotal);
                }
            }
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof RollFragmentListener) {
            mListener = (RollFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
