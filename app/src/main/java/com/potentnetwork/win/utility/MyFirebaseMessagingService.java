package com.potentnetwork.win.utility;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.potentnetwork.win.MainMenu;
import com.potentnetwork.win.R;

import java.util.Random;

public class MyFirebaseMessagingService extends FirebaseMessagingService {



    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        getFirebaseMessage(remoteMessage.getNotification().getTitle(),remoteMessage.getNotification().getBody());
    }



    public void getFirebaseMessage(String title,String body){

        Random random = new Random();
        int randomN = random.nextInt(100);
        Intent intent = new Intent(this, MainMenu.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,randomN,intent,PendingIntent.FLAG_IMMUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this,"Win_bet");
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title);
        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        mBuilder.setStyle(new NotificationCompat.InboxStyle().addLine(body)
                .setSummaryText("Game Update"));
        mBuilder.setAutoCancel(true);

        NotificationManagerCompat mNotifiactionMgr = NotificationManagerCompat.from(this);
        mNotifiactionMgr.notify(1,mBuilder.build());

    }

}
