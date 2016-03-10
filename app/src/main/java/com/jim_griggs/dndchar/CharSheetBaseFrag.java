package com.jim_griggs.dndchar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import static com.jim_griggs.dndchar.Utils.toBonusString;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Stat;

public class CharSheetBaseFrag extends Fragment {
    private static final int SAVE_STR_ID = 100;
    private static final int SAVE_DEX_ID = 101;
    private static final int SAVE_CON_ID = 102;
    private static final int SAVE_INT_ID = 103;
    private static final int SAVE_WIS_ID = 104;
    private static final int SAVE_CHA_ID = 105;

    private Character c;

    private TextView charName;
    private TextView charClass;
    private TextView charRace;
    private TextView charProf;
    private TextView charMove;
    private TextView charAlignment;
    private TextView charHP;
    private TextView charAC;
    private TextView charInit;

    public CharSheetBaseFrag() {
        // Required empty public constructor
    }

    public static CharSheetBaseFrag newInstance() {
        CharSheetBaseFrag fragment = new CharSheetBaseFrag();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        c = Character.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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

        RelativeLayout acBox = (RelativeLayout) rootView.findViewById(R.id.acBox);
        acBox.setOnClickListener(new ACListener());

        LinearLayout statLayout = (LinearLayout) rootView.findViewById(R.id.statLayout);
        CreateStatView(inflater, statLayout, c.stats.get(Stat.TYPE_STR));
        CreateStatView(inflater, statLayout, c.stats.get(Stat.TYPE_DEX));
        CreateStatView(inflater, statLayout, c.stats.get(Stat.TYPE_CON));
        CreateStatView(inflater, statLayout, c.stats.get(Stat.TYPE_INT));
        CreateStatView(inflater, statLayout, c.stats.get(Stat.TYPE_WIS));
        CreateStatView(inflater, statLayout, c.stats.get(Stat.TYPE_CHA));

        LinearLayout saveLayout = (LinearLayout) rootView.findViewById(R.id.saveLayout);
        SaveListener listener = new SaveListener();
        CreateSaveView(inflater, saveLayout, listener, c.stats.get(Stat.TYPE_STR), SAVE_STR_ID);
        CreateSaveView(inflater, saveLayout, listener, c.stats.get(Stat.TYPE_DEX), SAVE_DEX_ID);
        CreateSaveView(inflater, saveLayout, listener, c.stats.get(Stat.TYPE_CON), SAVE_CON_ID);
        CreateSaveView(inflater, saveLayout, listener, c.stats.get(Stat.TYPE_INT), SAVE_INT_ID);
        CreateSaveView(inflater, saveLayout, listener, c.stats.get(Stat.TYPE_WIS), SAVE_WIS_ID);
        CreateSaveView(inflater, saveLayout, listener, c.stats.get(Stat.TYPE_CHA), SAVE_CHA_ID);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        charName.setText(c.name);
        charClass.setText(String.format("%1$s %2$d", c.charClass, c.level));
        charRace.setText(c.race);
        charProf.setText(String.format("%d", c.getProfBonus().value));
        charMove.setText(String.format("%d", c.getMovement()));
        charAlignment.setText(c.alignment);
        charHP.setText(String.format("%1$d / %2$d", c.currentHP, c.maxHP));
        charAC.setText(String.format("%d", c.getAC()));
        charInit.setText(toBonusString(c.getInitBonus()));

    }

    private void CreateStatView(LayoutInflater inflater, ViewGroup parent, Stat stat){
        RelativeLayout statBox = (RelativeLayout) inflater.inflate(R.layout.stat_template, parent, false);
        TextView statLabel = (TextView) statBox.findViewById(R.id.statLabel);
        TextView statValue = (TextView) statBox.findViewById(R.id.statValue);
        TextView statBonus = (TextView) statBox.findViewById(R.id.statBonus);
        statLabel.setText(stat.type);
        statValue.setText(String.format("%d", stat.value));
        statBonus.setText(toBonusString(stat.getStatBonus().value));
        parent.addView(statBox);
    }

    private void CreateSaveView(LayoutInflater inflater, ViewGroup parent, View.OnClickListener listener, Stat stat, int viewID){
        ViewGroup saveBox = (ViewGroup) inflater.inflate(R.layout.save_template, parent, false);
        saveBox.setId(viewID);
        saveBox.setOnClickListener(listener);
        TextView saveStat = (TextView) saveBox.findViewById(R.id.saveStat);
        TextView saveValue = (TextView) saveBox.findViewById(R.id.saveValue);
        saveStat.setText(stat.type);
        saveValue.setText(toBonusString(stat.getSaveBonus()));
        parent.addView(saveBox);
    }

    private class ACListener implements View.OnClickListener {
        CharActivity baseActivity = (CharActivity) getActivity();
        @Override
        public void onClick(View view){
            baseActivity.launchCheckActivity(getString(R.string.ACTitle), CheckActivity.TYPE_BONUS, c.getACBonuses());
        }
    }

    private class SaveListener implements View.OnClickListener {
        CharActivity baseActivity = (CharActivity) getActivity();
        @Override
        public void onClick(View view){
            switch (view.getId()) {
                case SAVE_STR_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, c.stats.get(Stat.TYPE_STR).getSaveBonuses());
                    break;
                case SAVE_DEX_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, c.stats.get(Stat.TYPE_STR).getSaveBonuses());
                    break;
                case SAVE_CON_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, c.stats.get(Stat.TYPE_STR).getSaveBonuses());
                    break;
                case SAVE_INT_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, c.stats.get(Stat.TYPE_STR).getSaveBonuses());
                    break;
                case SAVE_WIS_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, c.stats.get(Stat.TYPE_STR).getSaveBonuses());
                    break;
                case SAVE_CHA_ID:
                    baseActivity.launchCheckActivity(String.format(getString(R.string.saveTitle), Stat.TYPE_STR), CheckActivity.TYPE_CHECK, c.stats.get(Stat.TYPE_STR).getSaveBonuses());
                    break;
            }
        }
    }
}
