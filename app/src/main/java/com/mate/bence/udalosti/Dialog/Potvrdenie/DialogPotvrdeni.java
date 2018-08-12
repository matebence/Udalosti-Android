package com.mate.bence.udalosti.Dialog.Potvrdenie;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mate.bence.udalosti.R;

public class DialogPotvrdeni {

    public DialogPotvrdeni(final Activity activity, final String titul, final String heslo, final DialogOdpoved dialogOdpoved) {
        final Dialog dialog = new Dialog(activity);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.dialog_potvrdeni);

        TextView dialogTitul = dialog.findViewById(R.id.dialog_potvrdeni_titul);
        final EditText potvrd = dialog.findViewById(R.id.dialog_potvrdeni_heslo);

        dialogTitul.setText(titul);

        Button zavorit = dialog.findViewById(R.id.dialog_potvrdeni_potvrdit);
        zavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (heslo.equals(potvrd.getText().toString())) {
                    dialogOdpoved.potvrdenie(true, potvrd.getText().toString());
                } else {
                    dialogOdpoved.potvrdenie(false, potvrd.getText().toString());
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}