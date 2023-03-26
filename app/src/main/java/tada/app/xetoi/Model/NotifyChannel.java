package tada.app.xetoi.Model;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import tada.app.xetoi.R;

public class NotifyChannel extends Application {
    public static final String CHANNEL_ID_1 = "NotifyBaoDuong";
    public static final String CHANNEL_ID_2 = "NotifyDangKiem";
    public static final String CHANNEL_ID_3 = "NotifyBaoHiem";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Uri sound = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.alert);
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();


            // Channel bao duong
            CharSequence name1 = getString(R.string.channel_1_name);
            String description1 = getString(R.string.channel_1_description);
            NotificationChannel channel1 = new android.app.NotificationChannel(CHANNEL_ID_1, name1, NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription(description1);
            channel1.setSound(sound, audioAttributes);

            // Channel dang kiem
            CharSequence name2 = getString(R.string.channel_2_name);
            String description2 = getString(R.string.channel_2_description);
            NotificationChannel channel2 = new android.app.NotificationChannel(CHANNEL_ID_2, name2, NotificationManager.IMPORTANCE_HIGH);
            channel2.setDescription(description2);
            channel2.setSound(sound, audioAttributes);

            // Channel dang kiem
            CharSequence name3 = getString(R.string.channel_3_name);
            String description3 = getString(R.string.channel_3_description);
            NotificationChannel channel3 = new android.app.NotificationChannel(CHANNEL_ID_3, name3, NotificationManager.IMPORTANCE_HIGH);
            channel3.setDescription(description3);
            channel3.setSound(sound, audioAttributes);

            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
            notificationManager.createNotificationChannel(channel2);
        }
    }
}
