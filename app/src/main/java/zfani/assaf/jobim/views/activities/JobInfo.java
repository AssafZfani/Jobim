package zfani.assaf.jobim.views.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.fragment.app.FragmentActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.views.fragments.FeedFragments.ContactFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.JobFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.MapFragment;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.utils.Adapter;
import zfani.assaf.jobim.utils.GPSTracker;

public class JobInfo extends FragmentActivity {

    private String jobId;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.job_info);

        jobId = getIntent().getStringExtra("JobId");

        job = Job.findJobById(jobId);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.jobFragment, JobFragment.newInstance(jobId))
                .add(R.id.contactLayout, ContactFragment.newInstance(jobId))
                .add(R.id.mapLayout, MapFragment.newInstance(GPSTracker.getLatLngFromAddress(this, job.getAddress()))).commit();

        findViewById(R.id.favoriteButton).setBackgroundResource(job.isFavorite() ? R.drawable.remove2 : R.drawable.favorite2);

        ((TextView) findViewById(R.id.favoriteText)).setText(job.isFavorite() ? "הסר\nמהמועדפים" : "הוסף\nלמועדפים");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1 && resultCode == RESULT_OK) {

            ContentResolver contentResolver = getContentResolver();

            String contactName = "", phoneNumber = "";

            Cursor cursor = contentResolver.query(data.getData(), null, null, null, null);

            if (cursor != null) {

                if (cursor.moveToFirst()) {

                    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

                    phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                }

                cursor.close();
            }

            String text = "הי! " + Application.sharedPreferences.getString("FullName", "משתמש/ת Jobim") + " " +
                    getResources().getString(R.string.shareMessage) + " " + job.getFirm() + " מחפשת " +
                    Adapter.jobsTypesList.get(job.getBusinessNumber() - 1).getJobType();

            SmsManager.getDefault().sendTextMessage(phoneNumber, null, text, null, null);

            getIntent().putExtra("ContactName", contactName);

            MainActivity.displayDialog(this, R.layout.share_dialog, jobId);
        }
    }

    public void delete(View v) {

        MainActivity.displayDialog(this, R.layout.delete_job_dialog, jobId);
    }

    public void share(View v) {

        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI), 1);
    }

    public void jobs_employer(View v) {

        startActivity(new Intent(JobInfo.this, JobsEmployer.class).putExtra("Firm", job.getFirm()));
    }

    public void favorite(View v) {

        v.findViewById(R.id.favoriteButton).setBackgroundResource(!job.isFavorite() ? R.drawable.remove2 : R.drawable.favorite2);

        ContactFragment.favorite(job);

        ((TextView) findViewById(R.id.favoriteText)).setText(job.isFavorite() ? "הסר\nמהמועדפים" : "הוסף\nלמועדפים");
    }
}