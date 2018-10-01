package com.passport.venkatgonuguntala.passportapp.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.passport.venkatgonuguntala.passportapp.listeners.ProfileDeleteListener;
import com.passport.venkatgonuguntala.passportapp.R;

/**
 * Created by venkatgonuguntala on 10/1/18.
 */

public class AlertDialogForDeleteProfile extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DialogInterface.OnClickListener listener1 = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case Dialog.BUTTON_POSITIVE:
                        ProfileDeleteListener listener = (ProfileDeleteListener) getActivity();
                        listener.deleteProfile();
                        break;

                    case Dialog.BUTTON_NEGATIVE:
                        break;
                }
            }
        };


        Context context = getActivity();
        AlertDialog.Builder builer = new AlertDialog.Builder(context) //Factory method Pattern
                .setTitle(R.string.profile_delete_title)
                .setMessage(R.string.profile_delete_message)
                .setPositiveButton(R.string.error_ok_button_text, listener1)
                .setNegativeButton(android.R.string.cancel, listener1);

        AlertDialog dialog = builer.create();
        return dialog;
    }
}
