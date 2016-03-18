package com.jim_griggs.dndchar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import static com.jim_griggs.dndchar.Utils.toBonusString;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Stat;

public class CharSheetBaseFrag extends Fragment {
    // TODO:  Why are these IDs numbers???
    private static final int SAVE_STR_ID = 100;
    private static final int SAVE_DEX_ID = 101;
    private static final int SAVE_CON_ID = 102;
    private static final int SAVE_INT_ID = 103;
    private static final int SAVE_WIS_ID = 104;
    private static final int SAVE_CHA_ID = 105;

    private Character mCharacter;

    private TextView charName;
    private TextView charClass;
    private TextView charRace;
    private TextView charProf;
    private TextView charMove;
    private TextView charAlignment;
    private TextView charHP;
    private TextView charAC;
    private TextView charInit;
    private CheckBox charRage;
    private CheckBox charInspiration;
    private RelativeLayout acBox;
    private RelativeLayout moveBox;
    private LinearLayout statLayout;
    private LinearLayout saveLayout;

    public CharSheetBaseFrag() {
        // Required empty public constructor
    }

    public static CharSheetBaseFrag newInstance() {
        return new CharSheetBaseFrag();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i("BASE FRAG CREATE", "BASE FRAG CREATE");
        View rootView = inflater.inflate(R.layout.fragment_char_sheet_base, container, false);
        charName = (TextView) rootView.findViewById(R.id.charName);
        charClass = (TextView) rootView.findViewById(R.id.charClass);
        charRace = (TextView) rootView.findViewById(R.id.charRace);
        charProf = (TextView) rootView.findViewById(R.id.charProf);
        charMove = (TextView) rootView.findViewById(R.id.charMove);
        charAlignment = (TextView) rootView.findViewById(R.id.charAlignment);
        charHP = (TextView) rootView.findViewById(R.id.charHP);
        charAC = (TextView) rootView.findViewById(R.id.charAC);
        charInit = (TextView) rootView.findViewById(R.id.charInit);
        charRage = (CheckBox) rootView.findViewById(R.id.charRage);
        charInspiration = (CheckBox) rootView.findViewById(R.id.charInspiration);
        acBox = (RelativeLayout) rootView.findViewById(R.id.acBox);
        moveBox = (RelativeLayout) rootView.findViewById(R.id.moveBox);
        statLayout = (LinearLayout) rootView.findViewById(R.id.statLayout);
        saveLayout = (LinearLayout) rootView.findViewById(R.id.saveLayout);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh();
    }

    public void refresh(){
        mCharacter = Character.getInstance();

        charName.setText(mCharacter.getName());
        charName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        charClass.setText(String.format("%1$s %2$d", mCharacter.getCharClass(), mCharacter.getLevel()));
        charRace.setText(mCharacter.getRace());
        charProf.setText(String.format("%d", mCharacter.getProficiencyBonus().getValue()));
        charMove.setText(String.format("%d", mCharacter.getMovement()));
        charAlignment.setText(mCharacter.getAlignment());
        charHP.setText(String.format("%1$d / %2$d", mCharacter.getCurrentHP(), mCharacter.getMaxHP()));
        charAC.setText(String.format("%d", mCharacter.getAC()));
        charInit.setText(toBonusString(mCharacter.getInitiative()));
        charRage.setChecked(mCharacter.isRaged());
        charInspiration.setChecked(mCharacter.isInspired());

        charInspiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox box = (CheckBox) v;
                mCharacter.setInspired(box.isChecked());
            }
        });

        charRage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox box = (CheckBox) v;
                mCharacter.setRaged(box.isChecked());
            }
        });

        charHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CharActivity) getActivity()).showHPDialog();
            }
        });

        acBox.setOnClickListener(new ACListener());
        moveBox.setOnClickListener(new MoveListener());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        statLayout.removeAllViews();
        CreateStatView(inflater, statLayout, mCharacter.getStat(Stat.TYPE_STR));
        CreateStatView(inflater, statLayout, mCharacter.getStat(Stat.TYPE_DEX));
        CreateStatView(inflater, statLayout, mCharacter.getStat(Stat.TYPE_CON));
        CreateStatView(inflater, statLayout, mCharacter.getStat(Stat.TYPE_INT));
        CreateStatView(inflater, statLayout, mCharacter.getStat(Stat.TYPE_WIS));
        CreateStatView(inflater, statLayout, mCharacter.getStat(Stat.TYPE_CHA));

        SaveListener listener = new SaveListener();
        saveLayout.removeAllViews();
        CreateSaveView(inflater, saveLayout, listener, mCharacter.getStat(Stat.TYPE_STR), SAVE_STR_ID);
        CreateSaveView(inflater, saveLayout, listener, mCharacter.getStat(Stat.TYPE_DEX), SAVE_DEX_ID);
        CreateSaveView(inflater, saveLayout, listener, mCharacter.getStat(Stat.TYPE_CON), SAVE_CON_ID);
        CreateSaveView(inflater, saveLayout, listener, mCharacter.getStat(Stat.TYPE_INT), SAVE_INT_ID);
        CreateSaveView(inflater, saveLayout, listener, mCharacter.getStat(Stat.TYPE_WIS), SAVE_WIS_ID);
        CreateSaveView(inflater, saveLayout, listener, mCharacter.getStat(Stat.TYPE_CHA), SAVE_CHA_ID);
    }


    private void CreateStatView(LayoutInflater inflater, ViewGroup parent, Stat stat){
        RelativeLayout statBox = (RelativeLayout) inflater.inflate(R.layout.stat_template, parent, false);
        TextView statLabel = (TextView) statBox.findViewById(R.id.statLabel);
        TextView statValue = (TextView) statBox.findViewById(R.id.statValue);
        TextView statBonus = (TextView) statBox.findViewById(R.id.statBonus);
        statLabel.setText(stat.getType());
        statValue.setText(String.format("%d", stat.getValue()));
        statBonus.setText(toBonusString(stat.getStatBonus().getValue()));
        parent.addView(statBox);
    }

    private void CreateSaveView(LayoutInflater inflater, ViewGroup parent, View.OnClickListener listener, Stat stat, int viewID){
        ViewGroup saveBox = (ViewGroup) inflater.inflate(R.layout.save_template, parent, false);
        saveBox.setId(viewID);
        saveBox.setOnClickListener(listener);
        TextView saveStat = (TextView) saveBox.findViewById(R.id.saveStat);
        TextView saveValue = (TextView) saveBox.findViewById(R.id.saveValue);
        saveStat.setText(stat.getType());
        saveValue.setText(toBonusString(stat.getSaveBonus()));
        parent.addView(saveBox);
    }

    private class ACListener implements View.OnClickListener {
        CharActivity baseActivity = (CharActivity) getActivity();
        @Override
        public void onClick(View view){
            baseActivity.launchCheckActivity(getString(R.string.ACTitle), CheckActivity.TYPE_BONUS, mCharacter.getACBonuses());
        }
    }

    private class MoveListener implements View.OnClickListener {
        CharActivity baseActivity = (CharActivity) getActivity();
        @Override
        public void onClick(View view){
            baseActivity.launchCheckActivity(getString(R.string.moveTitle), CheckActivity.TYPE_BONUS, mCharacter.getMovementBonuses());
        }
    }

    private class SaveListener implements View.OnClickListener {
        CharActivity baseActivity = (CharActivity) getActivity();
        @Override
        public void onClick(View view){
            switch (view.getId()) {
                case SAVE_STR_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, mCharacter.getStat(Stat.TYPE_STR).getSaveBonuses());
                    break;
                case SAVE_DEX_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, mCharacter.getStat(Stat.TYPE_STR).getSaveBonuses());
                    break;
                case SAVE_CON_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, mCharacter.getStat(Stat.TYPE_STR).getSaveBonuses());
                    break;
                case SAVE_INT_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, mCharacter.getStat(Stat.TYPE_STR).getSaveBonuses());
                    break;
                case SAVE_WIS_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, mCharacter.getStat(Stat.TYPE_STR).getSaveBonuses());
                    break;
                case SAVE_CHA_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, mCharacter.getStat(Stat.TYPE_STR).getSaveBonuses());
                    break;
            }
        }
    }
}
