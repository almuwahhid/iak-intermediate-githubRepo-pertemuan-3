
package iak.almuwahhid.com.iak1.Utilities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.content.Context;

import iak.almuwahhid.com.iak1.R;
import iak.almuwahhid.com.iak1.SavedRepo;

/**
 * Created by gueone on 5/12/2017.
 */

public class NotifUtil {

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, android.R.drawable.btn_radio);
        return largeIcon;
    }

    public void remindInsert(Context context){
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(android.R.drawable.btn_radio)
                .setLargeIcon(largeIcon(context))
                .setContentTitle("Tambah Data")
                .setContentText("Menambahkan Data Repository")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Tambah Data"))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setAutoCancel(true);

        notificationBuilder.setContentIntent(startPending(context));
//        notificationBuilder.setDeleteIntent(getDeleteIntent(context));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        }


        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());
    }

    PendingIntent startPending(Context ctx){
        Intent resultIntent = new Intent(ctx, SavedRepo.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        ctx,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        return resultPendingIntent;
    }

    public static PendingIntent getDeleteIntent(Context context) {
        Intent intent = new Intent(context, SavedRepo.class);
        intent.setAction("ACTION_DELETE_NOTIFICATION");
        return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

