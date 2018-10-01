package com.passport.venkatgonuguntala.passportapp.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import com.passport.venkatgonuguntala.passportapp.listeners.AppCloseListener;
import com.passport.venkatgonuguntala.passportapp.R;

/**
 * Created by venkatgonuguntala on 9/30/18.
 */

public class AlertDialogForErrorAndConnectivity extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppCloseListener listener = (AppCloseListener) getActivity();
                listener.close();
            }
        };

        Context context = getActivity();
        AlertDialog.Builder builer = new AlertDialog.Builder(context) //Factory method Pattern
                .setTitle(R.string.error_title)
                .setMessage(R.string.error_message)
                .setPositiveButton(R.string.error_ok_button_text, listener);
        AlertDialog dialog = builer.create();
        return dialog;
    }

}
