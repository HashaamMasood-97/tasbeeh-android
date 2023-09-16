package com.example.tasbeeh;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Handler;
import androidx.core.app.NotificationCompat;

public class NotificationService extends Service {
    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "TasbeehNotificationChannel";

    @Override
    public void onCreate() {
        super.onCreate();

        // Create a notification channel (required for Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Tasbeeh Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Check if the count is not equal to 0
        int currentCount = intent.getIntExtra("currentCount", 0);
        if (currentCount != 0) {

            showDelayedNotification(currentCount);
        }

        // Service will not be automatically restarted
        return START_NOT_STICKY;
    }

    private void showDelayedNotification(int count) {
        // Show the first notification after 5 seconds
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showNotification(count);

                // Schedule the second notification after 4 hours
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showNotification(count);
                    }
                }, 4*60*60*1000); // 4 hours in milliseconds
            }
        }, 5000); // 5 seconds in milliseconds
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification(int count) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ac)
                .setContentTitle("Tasbeeh Count")
                .setContentText("Tasbeeh count: " + count + " - Tap to continue")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent) // Set the pending intent
                .setAutoCancel(true); // Dismiss the notification when clicked

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}

