package com.utsman.kucingapes.mobilelearningprodisejarah.FCM;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.utsman.kucingapes.mobilelearningprodisejarah.Activity.LoginActivity;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

import java.io.IOException;

public class MessagingServices extends FirebaseMessagingService {

    Bitmap bitmap;
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

        /*Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("AnotherActivity", TrueOrFalse);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 *//* Request code *//*, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(image)*//*Notification icon image*//*
                .setSmallIcon(R.drawable.firebase_icon)
                .setContentTitle(messageBody)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(image))*//*Notification with Image*//*
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 *//* ID of notification *//*, notificationBuilder.build());*/

        try {
            bitmap = Picasso.get().load(img).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("type", tipe);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, intent,
                PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setLargeIcon(bitmap)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(subtitle)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(1, builder.build());
        }

    }
}
