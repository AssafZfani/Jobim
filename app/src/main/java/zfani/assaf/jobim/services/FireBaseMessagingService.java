package zfani.assaf.jobim.services;

import android.app.NotificationManager;
import android.content.Context;

import com.google.firebase.messaging.RemoteMessage;

import androidx.core.app.NotificationCompat;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.R;

public class FireBaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (App.sharedPreferences.getBoolean("EnableNotification", true)) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            RemoteMessage.Notification notification = remoteMessage.getNotification();
            int notificationsCount = App.sharedPreferences.getInt("NotificationsCount", 0);
            App.sharedPreferences.edit().putString("notification" + (notificationsCount + 1), notification.getBody())
                    .putLong("time" + (notificationsCount + 1), remoteMessage.getSentTime())
                    .putInt("NotificationsCount", notificationsCount + 1).apply();
            notificationManager.notify(0, new NotificationCompat.Builder(this)
                    .setContentText(notification.getBody()).setSmallIcon(R.drawable.icon).build());
        }
    }
}
