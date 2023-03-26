package tada.app.xetoi.Model;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Date;
import java.util.Random;

import tada.app.xetoi.MainActivity;
import tada.app.xetoi.R;

public class NotifyReceiver extends BroadcastReceiver {
    private static final String TAG = "NOTIFY RECEIVER";

    @Override
    public void onReceive(Context context, Intent intent) {
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert);
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Bao duong
        if(intent.getStringExtra("bd") != null && intent.getStringExtra("bd").equals("bd")){

            Intent i = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, new Random().nextInt(10000), i, PendingIntent.FLAG_ONE_SHOT);

            String title = intent.getStringExtra("titleBaoDuong");
            String content = intent.getStringExtra("contentBaoDuong");
            NotificationCompat.Builder builderBaoDuong = new NotificationCompat.Builder(context, NotifyChannel.CHANNEL_ID_1)
                    .setSmallIcon(R.drawable.ic_baseline_build_24)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setOngoing(false)
                    .setSound(sound)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            manager.notify(generateNotifyId(), builderBaoDuong.build());
        }

        // Dang kiem
        if(intent.getStringExtra("dk") != null && intent.getStringExtra("dk").equals("dk")){
            Intent i = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, new Random().nextInt(10000), i, PendingIntent.FLAG_ONE_SHOT);

            String title = intent.getStringExtra("titleDangKiem");
            String content = intent.getStringExtra("contentDangKiem");
            NotificationCompat.Builder builderDangKiem = new NotificationCompat.Builder(context, NotifyChannel.CHANNEL_ID_2)
                    .setSmallIcon(R.drawable.ic_baseline_build_24)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setOngoing(false)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setSound(sound)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            manager.notify(generateNotifyId(), builderDangKiem.build());
        }

        // Bao hiem
        if(intent.getStringExtra("bh") != null && intent.getStringExtra("bh").equals("bh")){
            Intent i = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent =
                    PendingIntent.getActivity(context, new Random().nextInt(10000), i, PendingIntent.FLAG_ONE_SHOT);

            String title = intent.getStringExtra("titleBaoHiem");
            String content = intent.getStringExtra("contentBaoHiem");
            NotificationCompat.Builder builderBaoHiem = new NotificationCompat.Builder(context, NotifyChannel.CHANNEL_ID_3)
                    .setSmallIcon(R.drawable.ic_baseline_build_24)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setOngoing(false)
                    .setStyle(new NotificationCompat.BigTextStyle())
                    .setSound(sound)
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            manager.notify(generateNotifyId(), builderBaoHiem.build());
        }
    }

    private int generateNotifyId() {
        Random random = new Random();
        return (int) new Date().getTime() + random.nextInt();
    }
}
