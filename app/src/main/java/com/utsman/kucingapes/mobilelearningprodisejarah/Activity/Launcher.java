package com.utsman.kucingapes.mobilelearningprodisejarah.Activity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.utsman.kucingapes.mobilelearningprodisejarah.R;

public class Launcher extends AppCompatActivity {
    String type, node, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        /*FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);*/

        setMessagingSubs();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            String channelId  = getString(R.string.default_notification_channel_id);
            String channelName = getString(R.string.default_notification_channel_name);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                        channelName, NotificationManager.IMPORTANCE_DEFAULT));
            }
        }

        /*if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                String value = getIntent().getExtras().getString(key);

                if (key.equals("type") && value.equals("materi")) {
                    final String node = getIntent().getStringExtra("node");
                    final String idPost = getIntent().getStringExtra("id");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("materi", 1);
                            intent.putExtra("node", node);
                            intent.putExtra("id", idPost);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);
                }

                if (key.equals("type") && value.equals("app")) {
                    Toast.makeText(getApplicationContext(), "adsfd", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.utsman.kucingapes.mobilelearningprodisejarah&hl=en_US"));
                    startActivity(intent);
                }

            }
        }

        if (getIntent().getExtras() == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }
            }, 4000);
        }*/

        //setMessagingSubs();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            type = bundle.getString("type");
            node = bundle.getString("node");
            id = bundle.getString("id");
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.putExtra("type", type);
                intent.putExtra("node", node);
                intent.putExtra("id", id);
                startActivity(intent);
                finish();
            }
        }, 4000);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setElevation(0);
    }

    private void setMessagingSubs() {
        FirebaseMessaging.getInstance().subscribeToTopic(getString(R.string.default_notification_channel_name));
    }
}
