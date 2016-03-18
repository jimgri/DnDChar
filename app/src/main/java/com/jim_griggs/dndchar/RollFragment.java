package com.jim_griggs.dndchar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jim_griggs.model.Dice;



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
    private static final String ARG_DICE = "dice";

    private String rollName;
    private Dice dice;
    private ViewGroup[] mSelected;
    private LinearLayout mRollGrid;

    private RollFragmentListener mListener;

    public interface RollFragmentListener {
        void onDiceRolled(String id, int result);
    }


    public RollFragment() {
        // Required empty public constructor
        mSelected = new ViewGroup[0];
    }

    public static RollFragment newInstance(String rollName, Dice dice) {
        RollFragment fragment = new RollFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ROLLNAME, rollName);
        args.putSerializable(ARG_DICE, dice);
        fragment.setArguments(args);
        Log.i("ROLLFRAGMENT", "NEWINSTANCE COMPLETE");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ROLLFRAGMENT", "ONCREATE CALLED!!!");
        if (getArguments() != null) {
            rollName = getArguments().getString(ARG_ROLLNAME);
            dice = (Dice) getArguments().getSerializable(ARG_DICE);
            Log.i("RollFragment","mSelected initialized at onCreate: " + rollName + ":" + Integer.toString(dice.getDieNum()));
            mSelected = new ViewGroup[dice.getDieNum()];
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the roll layout.  This just contains a grid.
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.fragment_roll, container, false);
        // For each Die
        for (int i=0; i < dice.getDieNum(); i++){
            // Create the Die Faces
            View dieLayout = createDie(inflater, mRollGrid, i);
            // Add to RollGrid
            layout.addView(dieLayout);
        }
        return layout;
    }

    private View createDie(LayoutInflater inflater, ViewGroup rollGrid, int dieNum){
        // Inflate the Die Layout
        GridLayout dieLayout = (GridLayout) inflater.inflate(R.layout.die_template, rollGrid, false);
        dieLayout.setColumnCount(getGridColumnCount());
        DieListener dieListener = new DieListener(dieNum);
        for (int i=0; i < dice.getDieType(); i++){
            View dieFace = inflater.inflate(R.layout.layout_dieface, dieLayout, false);
            dieFace.setOnClickListener(dieListener);
            TextView t = (TextView) dieFace.findViewById(R.id.dieFaceText);
            t.setText(String.format("%d", i+1));
            dieLayout.addView(dieFace);
        }
        return dieLayout;
    }

    private int getGridColumnCount(){
        switch (dice.getDieType()){
            case 8:
                return 8;
            case 20:
                return 8;
        }
        return 0;
    }

    public void unselect() {
        Log.i("RollFragment", "Attempting to unselect die fragment");
        for (int i =0; i < mSelected.length; i++) {
            unselectDie(i);
        }
    }

    public void unselectDie(int dieNum){
        if (mSelected[dieNum] != null){
            Log.i("RollFragment", "Unselecting die" + Integer.toString(dieNum));
            ImageView image = (ImageView) mSelected[dieNum].findViewById(R.id.dieFaceImage);
            image.setImageResource(R.drawable.d20);
            mSelected[dieNum] = null;
        }
    }

    public void selectDie(int dieNum, ViewGroup dieView){
        // Unselect the previous selection first
        unselectDie(dieNum);
        // Highlight the new die
        mSelected[dieNum] = dieView;
        ImageView image = (ImageView) dieView.findViewById(R.id.dieFaceImage);
        image.setImageResource(R.drawable.d20h);
    }

    public boolean allSelected(){
        boolean selected = true;
        for (ViewGroup vg: mSelected){
            if (vg == null){
                selected = false;
                break;
            }
        }
        return selected;
    }

    public int rollTotal(){
        int total = 0;
        for (ViewGroup vg: mSelected){
            if (vg != null){
                TextView t = (TextView) vg.findViewById(R.id.dieFaceText);
                total += Integer.valueOf(t.getText().toString());
            }
        }
        return total;
    }

    private class DieListener implements View.OnClickListener {
        private int mDieNum;

        public DieListener(int dieNum){
            this.mDieNum = dieNum;
        }

        @Override
        public void onClick(View v) {
            selectDie(mDieNum, (ViewGroup) v);

            if (mListener != null) {
                if (allSelected()) {
                    int total = rollTotal();
                    mListener.onDiceRolled(rollName, total);
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
