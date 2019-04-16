package zfani.assaf.jobim.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.views.activities.AddNewJob;
import zfani.assaf.jobim.views.activities.FillDetails;
import zfani.assaf.jobim.views.fragments.FeedFragments.ContactFragment;

public class AlertHelper {

    public static void displayDialog(Activity activity, int layout, int jobId) {
        Dialog dialog = new Dialog(activity) {

            @Override
            protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(layout);
                View cancel = findViewById(R.id.tvCancel);
                if (cancel != null) {
                    cancel.setOnClickListener(v -> {
                        dismiss();
                        if (layout == R.layout.dialog_fill_details) {
                            activity.startActivity(new Intent(activity, FillDetails.class));
                        } else if (layout == R.layout.dialog_post_job) {
                            activity.finish();
                        }
                    });
                }
                switch (layout) {
                    case R.layout.add_new_job_dialog:
                        findViewById(R.id.call).setOnClickListener(view -> {
                            Intent callIntent = new Intent(Intent.ACTION_DIAL);
                            callIntent.setData(Uri.parse("tel:0509907979"));
                            activity.startActivity(callIntent);
                        });
                        findViewById(R.id.sendEmail).setOnClickListener(view -> {
                            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:assafzfani@gmail.com"));
                            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "בנוגע ליצירת ג'וב");
                            activity.startActivity(emailIntent);
                        });
                        break;
                    case R.layout.dialog_close:
                        findViewById(R.id.tvExit).setOnClickListener(view -> {
                            dismiss();
                            App.sharedPreferences.edit().remove("FromCamera").remove("Image").remove("FullName").remove("City").remove("BirthYear").remove("Email").apply();
                            activity.finish();
                        });
                        break;
                    case R.layout.dialog_delete_job:
                        findViewById(R.id.tvDeleteFromFeed).setOnClickListener(v -> {
                            /*if (FilteredAdapter.filteredList != null) {
                                int indexToRemove = FilteredAdapter.filteredList.indexOf(Job.findJobById(jobId));
                                if (activity.getLocalClassName().equalsIgnoreCase("views.activities.MainActivity")) {
                                    MainActivity mainActivity = (MainActivity) activity;
                                    if (mainActivity.allJobsFragment.filteredAdapter != null) {
                                        mainActivity.allJobsFragment.filteredAdapter.remove(indexToRemove);
                                    }
                                } else
                                    FilteredAdapter.filteredList.remove(indexToRemove);
                            }*/
                            //new Handler().postDelayed(() -> JobsAdapter.query.getRef().child(jobId).removeValue(), 750);
                            if (activity.getLocalClassName().equalsIgnoreCase("views.activities.JobInfoActivity"))
                                activity.finish();
                            else
                                ((ViewPager) activity.findViewById(activity.getIntent().getIntExtra("ViewPager", 0))).setCurrentItem(1);
                            dismiss();
                            Toast.makeText(activity, "מעכשיו הג'וב לא יופיע יותר בפיד", Toast.LENGTH_SHORT).show();
                        });
                        break;
                    case R.layout.dialog_delete_question:
                        findViewById(R.id.tvDeleteQuestion).setOnClickListener(view -> {
                            AddNewJob.newJob.setAnswer(false);
                            AddNewJob.newJob.setQuestion(null);
                            dismiss();
                            activity.finish();
                        });
                        break;
                    case R.layout.dialog_exit:
                        findViewById(R.id.tvExit).setOnClickListener(v -> {
                            dismiss();
                            activity.finish();
                        });
                        break;
                    case R.layout.dialog_pick_image:
                        findViewById(R.id.tvCamera).setOnClickListener(view -> {
                            dismiss();
                            activity.startActivityForResult(new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), 3);
                        });
                        findViewById(R.id.tvGallery).setOnClickListener(view -> {
                            dismiss();
                            activity.startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), 4);
                        });
                        break;
                    case R.layout.dialog_sending_mail:
                        findViewById(R.id.tvSendMail).setOnClickListener(view -> {
                            dismiss();
                            ContactFragment.contact(activity, null, R.id.sendEmail);
                        });
                        break;
                    case R.layout.dialog_share:
                        ((TextView) findViewById(R.id.dialogText)).setText("שלחנו ל" + activity.getIntent().getStringExtra("ContactName") + " המלצה על המשרה");
                        break;
                }
            }
        };
        dialog.show();
    }
}
