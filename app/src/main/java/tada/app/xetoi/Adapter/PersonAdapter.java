package tada.app.xetoi.Adapter;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import tada.app.xetoi.ChangeOilActivity;
import tada.app.xetoi.FixPartActivity;
import tada.app.xetoi.MainActivity;
import tada.app.xetoi.Model.NotifyChannel;
import tada.app.xetoi.Model.TemplateButton;
import tada.app.xetoi.R;
import tada.app.xetoi.RefuelActivity;
import tada.app.xetoi.SOSActivity;
import tada.app.xetoi.UserDetailActivity;
import tada.app.xetoi.VehicleListActivity;

public class PersonAdapter extends RecyclerView.Adapter<PersonAdapter.ViewHolder> {
    private Context context;
    private List<TemplateButton> listButton;

    public PersonAdapter(Context context, List<TemplateButton> listButton) {
        this.context = context;
        this.listButton = listButton;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_button, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TemplateButton button = listButton.get(position);
        holder.tvPersonalItem.setText(button.getName());
        holder.ivPersonalItemImage.setImageResource(button.getImage());
        holder.btnPersonalButton.setOnClickListener(v -> {
            Intent intent;
            switch (position) {
                case 0:
                    intent = new Intent(context, UserDetailActivity.class);
                    context.startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(context, VehicleListActivity.class);
                    context.startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(context, SOSActivity.class);
                    context.startActivity(intent);
                    break;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listButton.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivPersonalItemImage;
        TextView tvPersonalItem;
        RelativeLayout btnPersonalButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPersonalItem = itemView.findViewById(R.id.tvButtonName);
            ivPersonalItemImage = itemView.findViewById(R.id.ivButtonImage);
            btnPersonalButton = itemView.findViewById(R.id.btnHomeMenuButton);
        }
    }

    private void sendNotification() {
        Uri sound = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.alert);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, (int) new Date().getTime(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long timeAtClick = System.currentTimeMillis();
        long timeToAlarm = 1000 * 10;

        alarmManager.set(AlarmManager.RTC_WAKEUP, timeAtClick + timeToAlarm, pendingIntent);

        Notification notification = new NotificationCompat.Builder(context.getApplicationContext(), NotifyChannel.CHANNEL_ID_1)
                .setContentTitle("Sắp đến hạn đăng kiểm rồi bạn ơi!!!")
                .setContentText(dateFormat.format(date))
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_baseline_build_24)
                .setColor(context.getResources().getColor(R.color.android_default))
                .setSound(sound)
                .setAutoCancel(true)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify((int) new Date().getTime(), notification);

        //NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        //if(manager != null) {
        //    manager.notify((int) new Date().getTime(), notification);
        //}
    }
}
