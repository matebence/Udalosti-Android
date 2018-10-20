package com.mate.bence.udalosti.Dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.mate.bence.udalosti.R;

public class DialogPotvrdeni extends Dialog implements android.view.View.OnClickListener {

    private Activity activity;
    private DialogOdpoved dialogOdpoved;
    private String titul, text, tlacidloA, tlacidloB;

    public DialogPotvrdeni(Activity activity, String titul, String text, String tlacidloA, String tlacidloB, DialogOdpoved dialogOdpoved) {
        super(activity);

        this.activity = activity;
        this.titul = titul;
        this.text = text;
        this.tlacidloA = tlacidloA;
        this.tlacidloB = tlacidloB;
        this.dialogOdpoved = dialogOdpoved;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_potvrdeni);

        Button TlacidloA = findViewById(R.id.dialog_potvrdeni_a_odpoved);
        Button TlacidloB = findViewById(R.id.dialog_potvrdeni_b_odpoved);

        TlacidloA.setText(tlacidloA);
        TlacidloB.setText(tlacidloB);

        TlacidloA.setOnClickListener(this);
        TlacidloB.setOnClickListener(this);

        TextView dialogTitul = findViewById(R.id.dialog_potvrdeni_titul);
        TextView dialogText = findViewById(R.id.dialog_potvrdeni_text);

        dialogTitul.setText(titul);
        dialogText.setText(text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialog_potvrdeni_a_odpoved:
                dialogOdpoved.tlacidloA();
                dismiss();
                break;
            case R.id.dialog_potvrdeni_b_odpoved:
                dialogOdpoved.tlacidloB();
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }
}