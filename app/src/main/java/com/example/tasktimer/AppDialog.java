package com.example.tasktimer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

public class AppDialog extends DialogFragment {

    public static final String TAG = "AppDialog";

    public static final String DIALOG_ID = "id";
    public static final String DIALOG_MESSAGE = "message";
    public static final String DIALOG_POSITIVE_RID = "positive_rid";
    public static final String DIALOG_NEGATIVE_RID = "negative_rid";


    interface DialogEvents {
        void onPositiveDialogResult(int dialogId, Bundle args);

        void onNegativeDialogResult(int dialogId, Bundle args);

        void onDialogCancelled(int dialogId);
    }

    private DialogEvents mDialogEvents;

    @Override
    public void onAttach(Context context) {
        Log.d(TAG, "DialogEvents: Entering onAttach, activity is " + context.toString());
        super.onAttach(context);


        if (!(context instanceof DialogEvents)) {
            throw new ClassCastException(context.toString() + " must implement DialogEvents interface.");
        }
        mDialogEvents = (DialogEvents) context;
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach: start");
        super.onDetach();
        // clear dialog event since there's no activity active
        mDialogEvents = null;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: starts");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final Bundle arguments = getArguments();
        final int dialogId;
        String messageStr;
        int positiveStringId;
        int negativeStringId;

        if (arguments != null) {
            dialogId = arguments.getInt(DIALOG_ID);
            messageStr = arguments.getString(DIALOG_MESSAGE);

            if (dialogId == 0 || messageStr == null) {
                throw new IllegalArgumentException("DIALOG_ID and DIALOG_MESSAGE not find in the bundle.");
            }

            positiveStringId = arguments.getInt(DIALOG_POSITIVE_RID);
            if (positiveStringId == 0) {
                positiveStringId = R.string.ok;
            }
            negativeStringId = arguments.getInt(DIALOG_NEGATIVE_RID);
            if (negativeStringId == 0) {
                negativeStringId = R.string.cancel;
            }
        } else {
            throw new IllegalArgumentException("Must pass DIALOG_ID and DIALOG_MESSAGE in the bundle.");
        }

        builder.setMessage(messageStr)
                .setPositiveButton(positiveStringId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //positive call back
                        if (mDialogEvents != null) {
                            mDialogEvents.onPositiveDialogResult(dialogId, getArguments());

                        }
                    }
                })
                .setNegativeButton(negativeStringId, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        //negative call back
                        if (mDialogEvents != null) {
                            mDialogEvents.onNegativeDialogResult(dialogId, getArguments());
                        }
                    }
                });
        return builder.create();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        Log.d(TAG, "onCancel: called");
        if (mDialogEvents != null) {
            int dialogId = getArguments().getInt(DIALOG_ID);
            mDialogEvents.onDialogCancelled(dialogId);
        }
//        super.onCancel(dialog); super of onCancel for DialogFragment actually does nothing
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        // super of onDismiss for DialogFragment has to be called since onDismiss state need to ba update for rotation,
        // which has been handled by DialogFragment.onDismiss
        super.onDismiss(dialog);
    }
}
