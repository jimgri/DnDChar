package com.jim_griggs.dndchar;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
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
    private static final String MODULE_NAME = "RollFragment";
    private static final String ARG_ROLLNAME = "rollName";
    private static final String ARG_DICE = "dice";

    private String rollName;
    private Dice dice;
    private ViewGroup[] mSelected;
    private ViewGroup mRootView;
    private int mColumns = 20;

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
        mRootView = (ViewGroup) inflater.inflate(R.layout.fragment_roll, container, false);
        mRootView.addOnLayoutChangeListener(new ResizeListener());
        return mRootView;
    }

    private class ResizeListener implements View.OnLayoutChangeListener {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            // its possible that the layout is not complete in which case
            // we will get zero value for the width, so ignore the event
            Log.i(MODULE_NAME, "Width of Roll Fragment:" + Integer.toString(v.getWidth()));
            if (v.getWidth() == 0) {
                return;
            }
            // Do what you need to do with the height/width since they are now set
            // Get the width of the screen in dp.
            float w = (float) v.getWidth();
            float widthInDP = convertPixelsToDp(w, getContext());
            // Get the width of the die face in dp.
            int dp = (int) (getResources().getDimension(R.dimen.die_face_size) / getResources().getDisplayMetrics().density);
            // Calculate the number of dice that fit across the display area.
            mColumns = (int) widthInDP / dp;
            Log.i(MODULE_NAME, "Updating columns:" + Integer.toString(mColumns));
            buildDice();
        }
    }

    private void buildDice() {
       // mRootView.removeAllViews();
        // For each Die
        for (int i = 0; i < dice.getDieNum(); i++) {
            // Create the Die Faces
            View dieLayout = createDie(i);
            // Add to Root
            mRootView.addView(dieLayout);
        }
    }

    private View createDie(int dieNum){
        // Inflate the Die Layout
        LayoutInflater li = LayoutInflater.from(getContext());
        GridLayout dieLayout = (GridLayout) li.inflate(R.layout.die_template, mRootView, false);
        Log.i (MODULE_NAME, "Setting dieLayout columns to:" + Integer.toString(mColumns));
        dieLayout.setColumnCount(mColumns);
        DieListener dieListener = new DieListener(dieNum);
        // Add the die faces
        for (int i = 0; i < dice.getDieType(); i++) {
            View dieFace = li.inflate(R.layout.layout_dieface, dieLayout, false);
            dieFace.setOnClickListener(dieListener);
            TextView t = (TextView) dieFace.findViewById(R.id.dieFaceText);
            t.setText(String.format("%d", i + 1));
            Log.i (MODULE_NAME, "Adding DieFace to Grid:" + Integer.toString(i));
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

    /**
     * This method converts device specific pixels to density independent pixels.
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @param context Context to get resources and device specific display metrics
     * @return A float value to represent dp equivalent to px value
     */
    public static float convertPixelsToDp(float px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }
}
