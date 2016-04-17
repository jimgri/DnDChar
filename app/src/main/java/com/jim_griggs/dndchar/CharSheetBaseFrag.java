package com.jim_griggs.dndchar;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import static com.jim_griggs.dndchar.Utils.toBonusString;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Stat;

import java.util.Map;

public class CharSheetBaseFrag extends Fragment {
    private static final int HIT_DICE_COLUMNS = 8;
    private static final String MODULE_NAME = "CharSheetBaseFrag";

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
    private ViewGroup acBox;
    private ViewGroup moveBox;
    private ViewGroup hitDiceBox;
    private LinearLayout statLayout;
    private LinearLayout saveLayout;
    private TextView hitDice;

    private ActivityController mController;

    public CharSheetBaseFrag() {
        // Required empty public constructor
    }

    public static CharSheetBaseFrag newInstance() {
        return new CharSheetBaseFrag();
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mController = new ActivityController(getActivity());
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
        charRage = (CheckBox) rootView.findViewById(R.id.charRage);
        charInspiration = (CheckBox) rootView.findViewById(R.id.charInspiration);
        acBox = (RelativeLayout) rootView.findViewById(R.id.acBox);
        moveBox = (RelativeLayout) rootView.findViewById(R.id.moveBox);
        hitDiceBox = (RelativeLayout) rootView.findViewById(R.id.hit_dice_box);
        statLayout = (LinearLayout) rootView.findViewById(R.id.statLayout);
        saveLayout = (LinearLayout) rootView.findViewById(R.id.saveLayout);
        hitDice = (TextView) rootView.findViewById(R.id.hitDice);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refresh();
    }

    public void refresh(){
        Character character = Character.getInstance();

        charName.setText(character.getName());
        charName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        charClass.setText(String.format("%1$s %2$d", character.getCharClass(), character.getLevel()));
        charRace.setText(character.getRace());
        charProf.setText(String.format("%d", character.getProficiencyBonus().getValue()));
        charAlignment.setText(character.getAlignment());
        charHP.setText(String.format("%1$d / %2$d", character.getCurrentHP(), character.getMaxHP()));
        charInit.setText(toBonusString(character.getInitiative()));
        charRage.setChecked(character.isRaged());
        charInspiration.setChecked(character.isInspired());

        hitDice.setText(String.format("%1$d / %2$d", character.getRemainingHitDice(), character.getHitDiceMax()));
        hitDiceBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CharActivity) getActivity()).showHDDialog();
            }
        });

        charMove.setText(String.format("%d", character.getMovement()));
        moveBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.launchCheckActivity(getString(R.string.moveTitle),
                        CheckActivity.TYPE_BONUS, Character.getInstance().getMovementBonuses());
            }
        });

        charAC.setText(String.format("%d", character.getAC()));
        acBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.launchCheckActivity(getString(R.string.ACTitle),
                        CheckActivity.TYPE_BONUS, Character.getInstance().getACBonuses());
            }
        });

        charInspiration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox box = (CheckBox) v;
                Character.getInstance().setInspired(box.isChecked());
            }
        });

        charRage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox box = (CheckBox) v;
                Character.getInstance().setRaged(box.isChecked());
            }
        });

        charHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CharActivity) getActivity()).showHPDialog();
            }
        });

        LayoutInflater inflater = getActivity().getLayoutInflater();
        statLayout.removeAllViews();
        saveLayout.removeAllViews();

        for (Map.Entry<String, Stat> entry : character.getStats().entrySet()) {
            CreateStatView(inflater, statLayout, entry.getValue());
            CreateSaveView(inflater, saveLayout, entry.getValue());
        }
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

    private void CreateSaveView(LayoutInflater inflater, ViewGroup parent, Stat stat){
        ViewGroup saveBox = (ViewGroup) inflater.inflate(R.layout.save_template, parent, false);
        saveBox.setOnClickListener(new SaveListener(stat));
        TextView saveStat = (TextView) saveBox.findViewById(R.id.saveStat);
        TextView saveValue = (TextView) saveBox.findViewById(R.id.saveValue);
        saveStat.setText(stat.getType());
        saveValue.setText(toBonusString(stat.getSaveBonus()));
        parent.addView(saveBox);
    }

    private class SaveListener implements View.OnClickListener {
        private Stat mStat;

        public SaveListener(Stat stat) {
            mStat = stat;
        }

        @Override
        public void onClick(View v) {
            String title = String.format(getString(R.string.saveTitle), mStat.getType());
            mController.launchCheckActivity(title, CheckActivity.TYPE_CHECK,
                    mStat.getSaveBonuses());
        }
    }
}
