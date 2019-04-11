package zfani.assaf.jobim.views.fragments.FeedFragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Telephony;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.adapters.JobsAdapter;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.views.activities.MainActivity;

public class ContactFragment extends Fragment {

    public static ContactFragment newInstance(Job job) {
        ContactFragment contactFragment = new ContactFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Job", job);
        contactFragment.setArguments(bundle);
        return contactFragment;
    }

    public static void favorite(@NonNull Job job) {
        job.setFavorite(!job.isFavorite());
        JobsAdapter.query.getRef().child(job.getId()).setValue(job);
    }

    public static void contact(final FragmentActivity activity, final Job job, int id) {
        String text = null;
        if (App.sharedPreferences.contains("FullName") && job != null) {
            if (id != R.id.call) {
                Resources resources = activity.getResources();
                text = resources.getString(R.string.sendMessage1) + " " + job.getBusinessNumber() + " (סניף "
                        + job.getAddress() + ") " + resources.getString(R.string.sendMessage2);
            }
            switch (id) {
                case R.id.sendEmail: {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:assafzfani@gmail.com"));
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "ג'ובים");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, text);
                    activity.startActivity(emailIntent);
                    job.setApplied(true);
                    JobsAdapter.query.getRef().child(job.getId()).setValue(job);
                    MainActivity.displayDialog(activity, R.layout.contact_dialog, job.getId());
                    break;
                }
                case R.id.call: {
                    Intent callIntent = new Intent(Intent.ACTION_DIAL);
                    callIntent.setData(Uri.parse("tel:0509907979"));
                    ((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE)).listen(new PhoneStateListener() {

                        boolean wasRinging = false;

                        @Override
                        public void onCallStateChanged(int state, String incomingNumber) {
                            switch (state) {
                                case TelephonyManager.CALL_STATE_OFFHOOK:
                                    wasRinging = true;
                                    job.setApplied(true);
                                    JobsAdapter.query.getRef().child(job.getId()).setValue(job);
                                    break;
                                case TelephonyManager.CALL_STATE_IDLE:
                                    if (wasRinging) {
                                        activity.startActivity(activity.getPackageManager().getLaunchIntentForPackage(activity.getPackageName()).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                                    }
                                    break;
                            }
                        }
                    }, PhoneStateListener.LISTEN_CALL_STATE);
                    activity.startActivity(callIntent);
                    break;
                }
                case R.id.sendMessage: {
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    String textToSend = text;
                    sendIntent.putExtra(Intent.EXTRA_TEXT, text);
                    sendIntent.putExtra("address1", "0509907979");
                    sendIntent.putExtra("exit_on_sent", true);
                    sendIntent.setType("text/plain");
                    activity.getContentResolver().registerContentObserver(Telephony.Sms.CONTENT_URI, true, new ContentObserver(new Handler()) {

                        @Override
                        public void onChange(boolean selfChange) {
                            super.onChange(selfChange);
                            Cursor cursor = activity.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, null);
                            if (cursor != null) {
                                cursor.moveToNext();
                                String content = cursor.getString(cursor.getColumnIndex("body"));
                                if (content.equalsIgnoreCase(textToSend)) {
                                    job.setApplied(true);
                                    JobsAdapter.query.getRef().child(job.getId()).setValue(job);
                                }
                                cursor.close();
                            }
                        }
                    });
                    activity.startActivity(sendIntent);
                    break;
                }
            }
        } else {
            MainActivity.displayDialog(activity, R.layout.fill_details_dialog, job.getId());
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_layout, container, false);
        FragmentActivity activity = getActivity();
        Job job = getArguments().getParcelable("Job");
        View sendEmail, call, sendMessage, favorite;
        sendEmail = view.findViewById(R.id.sendEmail);
        call = view.findViewById(R.id.call);
        sendMessage = view.findViewById(R.id.sendMessage);
        favorite = view.findViewById(R.id.favorite);
        if (activity.getLocalClassName().equalsIgnoreCase("views.activities.JobInfoActivity")) {
            ((ViewGroup) view).removeView(favorite);
        }
        if (job != null) {
            favorite.setBackgroundResource(job.isFavorite() ? R.drawable.remove1 : R.drawable.favorite1);
        }
        favorite.setOnClickListener(view13 -> favorite(job));
        sendEmail.setOnClickListener(view12 -> {
            if (job != null)
                MainActivity.displayDialog(activity, App.sharedPreferences.contains("FullName") ?
                        R.layout.sending_mail_dialog : R.layout.fill_details_dialog, job.getId());
        });
        View.OnClickListener listener;
        call.setOnClickListener(listener = view1 -> {
            if (job != null) {
                contact(activity, job, view1.getId());
            }
        });
        sendMessage.setOnClickListener(listener);
        return view;
    }
}
