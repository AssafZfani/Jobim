package zfani.assaf.jobim.views.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.utils.AlertHelper;
import zfani.assaf.jobim.utils.GPSTracker;
import zfani.assaf.jobim.views.fragments.FeedFragments.ContactFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.JobFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.MapFragment;

public class JobInfoActivity extends AppCompatActivity {

    @BindView(R.id.btnFavorite)
    View btnFavorite;
    @BindView(R.id.tvFavorite)
    TextView tvFavorite;
    private Job job;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_info);
        ButterKnife.bind(this);
        job = getIntent().getParcelableExtra("Job");
        getSupportFragmentManager().beginTransaction()
                .add(R.id.llJobFragment, JobFragment.newInstance(job))
                .add(R.id.flContactFragment, ContactFragment.newInstance(job))
                .add(R.id.clMapFragment, MapFragment.newInstance(3, GPSTracker.getLatLngFromAddress(getApplication(), job.getAddress()))).commit();
        btnFavorite.setBackgroundResource(job.isFavorite() ? R.drawable.remove2 : R.drawable.favorite2);
        tvFavorite.setText(job.isFavorite() ? "הסר\nמהמועדפים" : "הוסף\nלמועדפים");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri dataUri = data.getData();
            if (dataUri != null) {
                ContentResolver contentResolver = getContentResolver();
                String contactName = "", phoneNumber = "";
                Cursor cursor = contentResolver.query(dataUri, null, null, null, null);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    }
                    cursor.close();
                }
                String text = "הי! " + App.sharedPreferences.getString("FullName", "משתמש/ת Jobim") + " " +
                        getResources().getString(R.string.shareMessage) + " " + job.getFirm() + " מחפשת " + job.getType();
                SmsManager.getDefault().sendTextMessage(phoneNumber, null, text, null, null);
                getIntent().putExtra("ContactName", contactName);
                AlertHelper.displayDialog(this, R.layout.dialog_share, job.getId());
            }
        }
    }

    @OnClick(R.id.llDelete)
    public void delete() {
        AlertHelper.displayDialog(this, R.layout.dialog_delete_job, job.getId());
    }

    @OnClick(R.id.llShare)
    public void share() {
        startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI), 1);
    }

    @OnClick(R.id.llJobsEmployer)
    public void jobsEmployer() {
        startActivity(new Intent(JobInfoActivity.this, JobsEmployer.class).putExtra("Firm", job.getFirm()));
    }

    @OnClick(R.id.llFavorite)
    public void favorite() {
        btnFavorite.setBackgroundResource(!job.isFavorite() ? R.drawable.remove2 : R.drawable.favorite2);
        ContactFragment.favorite(job);
        tvFavorite.setText(job.isFavorite() ? "הסר\nמהמועדפים" : "הוסף\nלמועדפים");
    }
}