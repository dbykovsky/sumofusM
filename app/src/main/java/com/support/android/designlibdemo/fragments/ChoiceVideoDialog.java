package com.support.android.designlibdemo.fragments;

import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.support.android.designlibdemo.dialogs.FragmentDialogOptionsPicker;

/**
 * Created by dbykovskyy on 11/3/15.
 */
public class ChoiceVideoDialog extends DialogFragment {


    public DialogInterface.OnClickListener positiveClickListener;
    public DialogInterface.OnClickListener cancelClickListener;
    public DialogInterface.OnClickListener onChoiceClickListener;

    public void setPositiveListener(DialogInterface.OnClickListener positiveListener){
        this.positiveClickListener=positiveListener;
    }

    public void setCancelClickListener(DialogInterface.OnClickListener cancelListener){
        this.cancelClickListener = cancelListener;
    }

    public void setOnChoiceClickListener(DialogInterface.OnClickListener onChoiceListener){
        this.onChoiceClickListener=onChoiceListener;
    }

    public static ChoiceVideoDialog newInstance(String title) {
        ChoiceVideoDialog frag = new ChoiceVideoDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity(),android.R.style.Theme_Material_Light_Dialog_NoActionBar);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setSingleChoiceItems(FragmentDialogOptionsPicker.videoPlayerPicker, -1, onChoiceClickListener);
        alertDialogBuilder.setPositiveButton("OK", positiveClickListener);
        alertDialogBuilder.setNegativeButton("Cancel", cancelClickListener);
        return alertDialogBuilder.create();
    }

}
