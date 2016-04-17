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

public class SpinnerDialogFragment extends DialogFragment {
    private SpinnerButtonListener mButtonListener;
    private String mPositiveLabel;
    private String mNegativeLabel;
    private int mMinValue;
    private int mMaxValue;
    private NumberPicker mPicker;

    public static interface SpinnerButtonListener{
        public void onPositiveClick(String tag, int value);
        public void onNegativeClick(String tag, int value);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.spinner_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button negativeButton;
        Button cancelButton;
        Button positiveButton;

        mPicker = (NumberPicker) view.findViewById(R.id.hp_picker);
        negativeButton = (Button) view.findViewById(R.id.negative_button);
        cancelButton = (Button) view.findViewById(R.id.cancel);
        positiveButton = (Button) view.findViewById(R.id.positive_button);

        mPicker.setMinValue(mMinValue);
        mPicker.setMaxValue(mMaxValue);

        negativeButton.setText(mNegativeLabel);
        negativeButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonListener != null) {
                    mButtonListener.onNegativeClick(getTag(), mPicker.getValue());
                }
                dismiss();
            }
        });
        positiveButton.setText(mPositiveLabel);
        positiveButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mButtonListener != null){
                    mButtonListener.onPositiveClick(getTag(), mPicker.getValue());
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof SpinnerButtonListener) {
            mButtonListener = (SpinnerButtonListener) activity;
        }
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The dialog includes a title by default, remove it.
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    public String getPositiveLabel() {
        return mPositiveLabel;
    }

    public void setPositiveLabel(String positiveLabel) {
        this.mPositiveLabel = positiveLabel;
    }

    public String getNegativeLabel() {
        return mNegativeLabel;
    }

    public void setNegativeLabel(String negativeLabel) {
        this.mNegativeLabel = negativeLabel;
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int minValue) {
        this.mMinValue = minValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;
    }
}


