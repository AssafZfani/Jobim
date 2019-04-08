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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zfani.assaf.jobim.views.activities.MainActivity;
import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.utils.Adapter;

public class ContactFragment extends Fragment {

    public static ContactFragment newInstance(String jobId) {

        ContactFragment contactFragment = new ContactFragment();

        Bundle bundle = new Bundle();

        bundle.putString("JobId", jobId);

        contactFragment.setArguments(bundle);

        return contactFragment;
    }

    public static void favorite(Job job) {

        job.setFavorite(!job.isFavorite());

        Adapter.query.getRef().child(job.getId()).setValue(job);
    }

    public static void contact(final FragmentActivity activity, final String jobId, int id) {

        String text = null;

        final Job job = Job.findJobById(jobId);

        if (Application.sharedPreferences.contains("FullName") && job != null) {

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

                    Adapter.query.getRef().child(job.getId()).setValue(job);

                    MainActivity.displayDialog(activity, R.layout.contact_dialog, jobId);

                    break;
                }

                case R.id.call: {

                    Intent callIntent = new Intent(Intent.ACTION_DIAL);

                    callIntent.setData(Uri.parse("tel:0509907979"));

                    ((TelephonyManager) activity.getSystemService(Context.TELEPHONY_SERVICE))
                            .listen(new PhoneStateListener() {

                                boolean wasRinging = false;

                                @Override
                                public void onCallStateChanged(int state, String incomingNumber) {

                                    switch (state) {

                                        case TelephonyManager.CALL_STATE_OFFHOOK:

                                            wasRinging = true;

                                            job.setApplied(true);

                                            Adapter.query.getRef().child(job.getId()).setValue(job);

                                            break;

                                        case TelephonyManager.CALL_STATE_IDLE:

                                            if (wasRinging)
                                                activity.startActivity(activity.getPackageManager().
                                                        getLaunchIntentForPackage(activity.getPackageName())
                                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));

                                            break;
                                    }
                                }
                            }, PhoneStateListener.LISTEN_CALL_STATE);

                    activity.startActivity(callIntent);

                    break;
                }

                case R.id.sendMessage: {

                    Intent sendIntent = new Intent(Intent.ACTION_SEND);

                    final String textToSend = text;

                    sendIntent.putExtra(Intent.EXTRA_TEXT, text);

                    sendIntent.putExtra("address1", "0509907979");

                    sendIntent.putExtra("exit_on_sent", true);

                    sendIntent.setType("text/plain");

                    activity.getContentResolver().registerContentObserver(Telephony.Sms.CONTENT_URI, true,

                            new ContentObserver(new Handler()) {

                                @Override
                                public void onChange(boolean selfChange) {

                                    super.onChange(selfChange);

                                    Cursor cursor = activity.getContentResolver().query(Telephony.Sms.CONTENT_URI, null, null, null, null);

                                    if (cursor != null) {

                                        cursor.moveToNext();

                                        String content = cursor.getString(cursor.getColumnIndex("body"));

                                        if (content.equalsIgnoreCase(textToSend)) {

                                            job.setApplied(true);

                                            Adapter.query.getRef().child(jobId).setValue(job);
                                        }

                                        cursor.close();
                                    }
                                }
                            });

                    activity.startActivity(sendIntent);

                    break;
                }
            }
        } else
            MainActivity.displayDialog(activity, R.layout.fill_details_dialog, jobId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.contact_layout, container, false);

        final FragmentActivity activity = getActivity();

        final Job job = Job.findJobById(getArguments().getString("JobId"));

        View sendEmail, call, sendMessage, favorite;

        sendEmail = view.findViewById(R.id.sendEmail);

        call = view.findViewById(R.id.call);

        sendMessage = view.findViewById(R.id.sendMessage);

        favorite = view.findViewById(R.id.favorite);

        if (activity.getLocalClassName().equalsIgnoreCase("Activities.JobInfo"))
            ((ViewGroup) view).removeView(favorite);

        if (job != null)
            favorite.setBackgroundResource(job.isFavorite() ? R.drawable.remove1 : R.drawable.favorite1);

        favorite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                favorite(job);
            }
        });

        sendEmail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (job != null)
                    MainActivity.displayDialog(activity, Application.sharedPreferences.contains("FullName") ?
                            R.layout.sending_mail_dialog : R.layout.fill_details_dialog, job.getId());
            }
        });

        View.OnClickListener listener;

        call.setOnClickListener(listener = new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (job != null)
                    contact(activity, job.getId(), view.getId());
            }
        });

        sendMessage.setOnClickListener(listener);

        return view;
    }
}
