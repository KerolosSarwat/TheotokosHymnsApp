package com.example.theotokos;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class NotificationWorker extends Worker {

    private static final String CHANNEL_ID = "scheduled_notification_channel";
    DataCache dataCache;

    public NotificationWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        dataCache = new DataCache(context);
    }

    @NonNull
    @Override
    public Result doWork() {
        // Create a notification
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create a notification channel (required for Android 8.0 and above)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,"Scheduled Notifications",NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        // Build the notification
        if (dataCache.getUser() != null){
            Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                    .setContentTitle("مدرسة ثيؤطوكوس للألحان")
                    .setContentText("مرحباً "+ dataCache.getUser().getFullName())
                    .setSmallIcon(R.drawable.oip) // Replace with your icon
                    .setAutoCancel(true)
                    .build();

            // Show the notification
            notificationManager.notify(1, notification);

            return Result.success();
        }
        else
            return Result.failure();


    }
}