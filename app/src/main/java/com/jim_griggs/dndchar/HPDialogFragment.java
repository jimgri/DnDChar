package com.jim_griggs.dndchar;

import android.app.Activity;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.NumberPicker;
import com.jim_griggs.model.Character;

public class HPDialogFragment extends DialogFragment {

    private onCharacterUpdateListener mListener;
    private NumberPicker mPicker;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof onCharacterUpdateListener){
            mListener = (onCharacterUpdateListener) activity;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.hp_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button damageButton;
        Button cancelButton;
        Button healButton;

        mPicker = (NumberPicker) view.findViewById(R.id.hp_picker);
        damageButton = (Button) view.findViewById(R.id.damage);
        cancelButton = (Button) view.findViewById(R.id.cancel);
        healButton = (Button) view.findViewById(R.id.heal);

        mPicker.setMinValue(1);
        mPicker.setMaxValue(100);
        damageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Character.getInstance().takeDamage(mPicker.getValue());
                if (mListener != null){
                    mListener.onCharacterUpdate();
                }
                dismiss();
            }
        });
        healButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Character.getInstance().heal(mPicker.getValue());
                if (mListener != null){
                    mListener.onCharacterUpdate();
                }
                dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The dialog includes a title by default, remove it.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }
}


