package com.practice.notificationscheduler;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;

import androidx.core.app.NotificationCompat;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class NotificationJobService extends JobService {
    NotificationManager notificationManager;

    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID = "job_notification_channel";
    CharSequence notificationChannelname= "notifyjobschannel";
    int channelImportance = NotificationManager.IMPORTANCE_MAX;


    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        createNotificationChannel();
        PendingIntent pendingIntent =  PendingIntent.getActivity
                (this, 0, new Intent(this, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationCompat.Builder mNotifyBuilder = getNotificationBuilder();
        mNotifyBuilder.setContentIntent(pendingIntent);

        notificationManager.notify(0, mNotifyBuilder.build());

        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        //Return true to ensure job is rescheduled if it fails
        return true;
    }


    public void createNotificationChannel(){
        notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(PRIMARY_CHANNEL_ID, notificationChannelname, channelImportance);
            notificationChannel.setDescription("Channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


    }


    public  NotificationCompat.Builder getNotificationBuilder(){

        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, PRIMARY_CHANNEL_ID);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("JOBS")
                .setContentTitle("JOB SERVICE")
                .setContentText("Scheduled job completed successfully");

        return builder;
    }



}
