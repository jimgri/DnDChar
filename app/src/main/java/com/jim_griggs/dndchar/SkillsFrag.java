package com.jim_griggs.dndchar;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import com.jim_griggs.model.Character;
import com.jim_griggs.model.Skill;


public class SkillsFrag extends Fragment {

    public SkillsFrag() {
        // Required empty public constructor
    }

    public static SkillsFrag newInstance() {
        return new SkillsFrag();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_char_skills, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        ListView list = (ListView) view.findViewById(R.id.skillList);
        ArrayList<Skill> objects = new ArrayList<>(Character.getInstance().getSkills().values());
        list.setAdapter(new SkillAdapter(getContext(), R.id.skillList, objects));
        list.setOnItemClickListener(new ListView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Skill skill = (Skill) parent.getAdapter().getItem(position);
                CharActivity act = (CharActivity) getActivity();
                act.launchCheckActivity(String.format(getString(R.string.skillTitle), skill.getSkillType()), CheckActivity.TYPE_CHECK, skill.getSkillBonuses());
            }
        });
    }

    public class SkillAdapter extends ArrayAdapter<Skill> {
        private Context mContext;

        // Constructor
        public SkillAdapter(Context context, int resource, List<Skill> objects) {
            super(context, resource, objects);
            mContext = context;
        }

        // create a new ImageView for each item referenced by the Adapter
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View skillLayout;
            Skill skill = getItem(position);

            if (convertView == null){
                skillLayout = LayoutInflater.from(mContext).inflate(R.layout.skill_template, parent, false);
            } else{
                skillLayout = convertView;
            }
            TextView bonus = (TextView) skillLayout.findViewById(R.id.skillBonus);
            TextView name = (TextView) skillLayout.findViewById(R.id.skillName);
            bonus.setText(Integer.toString(skill.getSkillBonus()));
            name.setText(skill.getSkillType() + " (" + skill.getStatType() + ")");
            if(skill.isProficient()){
                name.setTextColor(ContextCompat.getColor(getContext(), R.color.blue));
            }
            return skillLayout;
        }
    }
}
