package zfani.assaf.jobim.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.views.activities.MainActivity;

public class AlertHelper {

    public static void showPermissionRequestAlert(Activity activity) {
        new AlertDialog.Builder(activity).setCancelable(false)
                .setTitle(activity.getString(R.string.dialog_storage_permission_title))
                .setMessage(activity.getString(R.string.dialog_storage_permission_message))
                .setPositiveButton(activity.getString(R.string.dialog_storage_permission_confirm), (dialog, which) -> {
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivityForResult(intent, Constants.KEY_ACTION_APPLICATION_DETAILS_SETTINGS);
                }).create().show();
    }
}
