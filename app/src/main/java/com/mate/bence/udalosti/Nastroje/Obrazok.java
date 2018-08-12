package com.mate.bence.udalosti.Nastroje;

import android.app.Activity;
import android.content.Context;

import com.mate.bence.udalosti.Activity.Navigacia.Fragment.Nastavenia.Nastavenia;
import com.mate.bence.udalosti.R;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class Obrazok {
    public static void spustiOrezavanie(Activity activity) {
        CropImage.activity()
                .setActivityTitle("Udalosti")
                .setCropMenuCropButtonTitle("Hotovo")
                .setCropMenuCropButtonIcon(R.drawable.ic_pouzit)
                .setRequestedSize(800, 800)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAllowFlipping(false)
                .setAllowRotation(true)
                .start(activity);
    }

    public static void spustiOrezavanie(Context context, Nastavenia fragment) {
        CropImage.activity()
                .setActivityTitle("Udalosti")
                .setCropMenuCropButtonTitle("Hotovo")
                .setCropMenuCropButtonIcon(R.drawable.ic_pouzit)
                .setRequestedSize(800, 800)
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAllowFlipping(false)
                .setAllowRotation(true)
                .start(context, fragment);
    }
}