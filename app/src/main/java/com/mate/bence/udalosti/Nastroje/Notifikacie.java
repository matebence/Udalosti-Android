package com.mate.bence.udalosti.Nastroje;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Patterns;

import com.mate.bence.udalosti.R;
import com.mate.bence.udalosti.Udaje.Nastavenia.Spravy;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Notifikacie {

    private Context context;

    public Notifikacie(Context context) {
        this.context = context;
    }

    public void spravaNotifikacie(String titul, String text, String cas, Intent akcia) {
        spravaNotifikacie(titul, text, cas, akcia, null);
    }

    public void spravaNotifikacie(final String titul, final String sprava, final String cas, Intent akcia, String obrazok) {
        if (TextUtils.isEmpty(sprava)) {
            return;
        }

        final int IKONA = R.mipmap.ic_udalosti_logo;

        final PendingIntent odpoved = PendingIntent.getActivity(context, 0, akcia, PendingIntent.FLAG_CANCEL_CURRENT);
        final NotificationCompat.Builder notifikacia = new NotificationCompat.Builder(context);
        final Uri zvuk = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (!TextUtils.isEmpty(obrazok)) {
            if (obrazok.length() > 4 && Patterns.WEB_URL.matcher(obrazok).matches()) {
                Bitmap obrazokNotifikacie = ziskajObrazok(obrazok);
                if (obrazokNotifikacie != null) {
                    zlozitaNotifikacia(obrazokNotifikacie, notifikacia, IKONA, titul, sprava, cas, odpoved, zvuk);
                } else {
                    kratkaNotifikacia(notifikacia, IKONA, titul, sprava, cas, odpoved, zvuk);
                }
            }
        } else {
            kratkaNotifikacia(notifikacia, IKONA, titul, sprava, cas, odpoved, zvuk);
            prehrajZvuk();
        }
    }

    private void kratkaNotifikacia(NotificationCompat.Builder notifikacia, int ikona, String titul, String text, String cas, PendingIntent odpoved, Uri cesta) {
        NotificationCompat.InboxStyle notifikaciaNaRiadok = new NotificationCompat.InboxStyle();
        notifikaciaNaRiadok.addLine(text);
        Notification teloNotifikacie;

        teloNotifikacie = notifikacia.setSmallIcon(ikona).setTicker(titul).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(titul)
                .setContentIntent(odpoved)
                .setSound(cesta)
                .setStyle(notifikaciaNaRiadok)
                .setWhen(getTimeMilliSec(cas))
                .setSmallIcon(R.drawable.ic_notifikacne_logo)
                .setColor(context.getResources().getColor(R.color.statusbar))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), ikona))
                .setContentText(text)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(Spravy.NOTIFIKACIA, teloNotifikacie);
    }

    private void zlozitaNotifikacia(Bitmap obrazok, NotificationCompat.Builder notifikacia, int ikona, String titul, String sprava, String cas, PendingIntent odpoved, Uri cesta) {
        NotificationCompat.BigPictureStyle obrazokNotifikacie = new NotificationCompat.BigPictureStyle();
        obrazokNotifikacie.setBigContentTitle(titul);
        obrazokNotifikacie.setSummaryText(Html.fromHtml(sprava).toString());
        obrazokNotifikacie.bigPicture(obrazok);
        Notification teloNotifikacie;

        teloNotifikacie = notifikacia.setSmallIcon(ikona).setTicker(titul).setWhen(0)
                .setAutoCancel(true)
                .setContentTitle(titul)
                .setContentIntent(odpoved)
                .setSound(cesta)
                .setStyle(obrazokNotifikacie)
                .setWhen(getTimeMilliSec(cas))
                .setSmallIcon(R.drawable.ic_notifikacne_logo)
                .setColor(context.getResources().getColor(R.color.statusbar))
                .setContentText(sprava)
                .build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.notify(Spravy.NOTIFIKACIA_Z_OBRAZKOM, teloNotifikacie);
    }

    private Bitmap ziskajObrazok(String strURL) {
        try {
            URL cesta = new URL(strURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) cesta.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();
            InputStream udaje = httpURLConnection.getInputStream();
            return BitmapFactory.decodeStream(udaje);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void prehrajZvuk() {
        try {
            Uri cesta = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone zvuk = RingtoneManager.getRingtone(context, cesta);
            zvuk.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean aplikacieJePozadi(Context context) {
        boolean jePozadi = true;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            assert activityManager != null;
            List<ActivityManager.RunningAppProcessInfo> momentalneBezia = activityManager.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo informacie : momentalneBezia) {
                if (informacie.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String aktivne : informacie.pkgList) {
                        if (aktivne.equals(context.getPackageName())) {
                            jePozadi = false;
                        }
                    }
                }
            }
        } else {
            assert activityManager != null;
            List<ActivityManager.RunningTaskInfo> informacieProcesu = activityManager.getRunningTasks(1);
            ComponentName ziskajInformacie = informacieProcesu.get(0).topActivity;
            if (ziskajInformacie.getPackageName().equals(context.getPackageName())) {
                jePozadi = false;
            }
        }
        return jePozadi;
    }

    public static void odstranNotifikacie(Context context) {
        NotificationManager notifikacie = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        assert notifikacie != null;
        notifikacie.cancelAll();
    }

    private static long getTimeMilliSec(String timeStamp) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatDatumu = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date datum = formatDatumu.parse(timeStamp);
            return datum.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }
}