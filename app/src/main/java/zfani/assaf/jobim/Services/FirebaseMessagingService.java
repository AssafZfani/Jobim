package zfani.assaf.jobim.Services;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (Application.sharedPreferences.getBoolean("EnableNotification", true)) {

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            RemoteMessage.Notification notification = remoteMessage.getNotification();

            int notificationsCount = Application.sharedPreferences.getInt("NotificationsCount", 0);

            Application.sharedPreferences.edit().putString("notification" + (notificationsCount + 1), notification.getBody())
                    .putLong("time" + (notificationsCount + 1), remoteMessage.getSentTime())
                    .putInt("NotificationsCount", notificationsCount + 1).apply();

            notificationManager.notify(0, new NotificationCompat.Builder(this)
                    .setContentText(notification.getBody()).setSmallIcon(R.drawable.icon).build());
        }
    }
}
