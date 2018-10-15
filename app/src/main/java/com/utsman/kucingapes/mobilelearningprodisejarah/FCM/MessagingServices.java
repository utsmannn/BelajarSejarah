package com.utsman.kucingapes.mobilelearningprodisejarah.FCM;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.utsman.kucingapes.mobilelearningprodisejarah.Activity.Launcher;
import com.utsman.kucingapes.mobilelearningprodisejarah.Activity.LoginActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.Activity.MainActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

import java.io.IOException;

import static android.support.v4.app.NotificationCompat.PRIORITY_MAX;

public class MessagingServices extends FirebaseMessagingService {

    Bitmap bitmap, largeIcon;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String title = remoteMessage.getData().get("title");
        String subtitle = remoteMessage.getData().get("sub");
        String nodePost = remoteMessage.getData().get("node");
        String idPost = remoteMessage.getData().get("id");
        String tipe = remoteMessage.getData().get("type");
        String img = remoteMessage.getData().get("img");

        builderNotif(title, subtitle, nodePost, idPost, tipe, img);
    }

    private void builderNotif(String title, String subtitle, String nodePost, String idPost, String tipe, String img) {

        try {
            bitmap = Picasso.get().load(img).get();
            largeIcon = Picasso.get().load(R.drawable.logo).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, Launcher.class);
        intent.putExtra("type", tipe);
        intent.putExtra("node", nodePost);
        intent.putExtra("id", idPost);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle(title)
                        .setLargeIcon(largeIcon)
                        .setContentText(subtitle)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap))
                        .setAutoCancel(true)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setSound(RingtoneManager
                                .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        if (notificationManager != null) {
            notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
        }

    }
}
