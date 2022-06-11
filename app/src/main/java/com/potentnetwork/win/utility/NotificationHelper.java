package com.potentnetwork.win.utility;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.potentnetwork.win.MainMenu;
import com.potentnetwork.win.R;

import java.util.Random;

public class NotificationHelper extends Application {

    public static void displayNotification(Context context, String title,String body){
        Random random = new Random();
        int randomN = random.nextInt(100);
        Intent intent = new Intent(context, MainMenu.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,randomN,intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,MainMenu.CHANNEL_ID);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title);

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setStyle(new NotificationCompat.InboxStyle().addLine(body)
                .setSummaryText("Game Update"));
        mBuilder.setAutoCancel(true);

        NotificationManagerCompat mNotifiactionMgr = NotificationManagerCompat.from(context);
        mNotifiactionMgr.notify(1,mBuilder.build());



    }
    public static void displayNotification2(Context context, String title, String body){
        Random random = new Random();
        int randomN = random.nextInt(100);
        Intent intent = new Intent(context, MainMenu.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context,randomN,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context,MainMenu.CHANNEL_ID_2);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title);
        mBuilder.setContentText(body);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setStyle(new NotificationCompat.InboxStyle().addLine(body)
                .setSummaryText("Basketball"));
        mBuilder.setAutoCancel(true);

        NotificationManagerCompat mNotifiactionMgr = NotificationManagerCompat.from(context);
        mNotifiactionMgr.notify(2,mBuilder.build());



    }
}
