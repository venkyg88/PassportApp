package com.passport.venkatgonuguntala.passportapp.util;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;

import com.passport.venkatgonuguntala.passportapp.R;

/**
 * Created by venkatgonuguntala on 9/30/18.
 */

public class AlertDialogForBlankFields extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        String title = getArguments().getString(Constant.TITLE);
        String message = getArguments().getString(Constant.MESSAGE);


        Context context = getActivity();
        AlertDialog.Builder builer = new AlertDialog.Builder(context) //Factory method Pattern
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.error_ok_button_text, null);
        AlertDialog dialog = builer.create();
        return dialog;
    }

}
