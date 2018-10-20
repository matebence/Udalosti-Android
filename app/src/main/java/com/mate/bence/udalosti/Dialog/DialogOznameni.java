package com.mate.bence.udalosti.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mate.bence.udalosti.R;

public class DialogOznameni {

    public DialogOznameni(Activity activity, String titul, String oznamenie) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_oznameni);

        TextView dialogTitul = dialog.findViewById(R.id.dialog_oznameni_titul);
        TextView dialogText = dialog.findViewById(R.id.dialog_oznameni_text);

        dialogTitul.setText(titul);
        dialogText.setText(oznamenie);

        Button zavorit = dialog.findViewById(R.id.dialog_oznameni_zatvorit);
        zavorit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}