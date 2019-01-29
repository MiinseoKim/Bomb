package org.androidtown.myapplication123;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    private static MySoundPlay mplay = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
       // Toast.makeText(context, "Alarm Received!", Toast.LENGTH_LONG).show();

        NotificationManager notifier = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

     //   Notification notify = new Notification(R.drawable.icon, "text",
      //          System.currentTimeMillis());

        Intent intent2 = new Intent(context, MainActivity.class);

        PendingIntent pender = PendingIntent
                .getActivity(context, 0, intent2, 0);

        /*
        * NotificationManager nm = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, SplashActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo_small)
                .setContentTitle("DSM 알리미")
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setTicker("DSM 알리미 - 소식 왔어요!");
        Notification notification = builder.build();*/


        Notification.Builder builder = new Notification.Builder(context)
                .setContentIntent(pender)
                .setSmallIcon(R.drawable.bomb)
                .setContentTitle("과제 BomB")
                .setContentText("과제가 있어요!! 어서 폭탄을 제거하세요!")
                .setVibrate(new long[] {200,200,500,300})
                .setTicker("과제밤 - 소식 왔어요!");
        Notification no = builder.build();

        mplay = new MySoundPlay(context, R.raw.flying_boom);

        mplay.play();

        no.number++;

        notifier.notify(1, no);

    }

}