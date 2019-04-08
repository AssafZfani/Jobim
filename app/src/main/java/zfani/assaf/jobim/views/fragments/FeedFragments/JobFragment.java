package zfani.assaf.jobim.views.fragments.FeedFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.models.JobType;
import zfani.assaf.jobim.utils.Adapter;
import zfani.assaf.jobim.views.activities.JobInfo;

public class JobFragment extends Fragment {

    public static JobFragment newInstance(String jobId) {

        JobFragment jobFragment = new JobFragment();

        Bundle bundle = new Bundle();

        bundle.putString("JobId", jobId);

        jobFragment.setArguments(bundle);

        return jobFragment;
    }

    public static void fillJobDetails(View view, final Job job) {

        final ViewHolderJob viewHolderJob = new ViewHolderJob(view);

        if (view.getId() != R.id.clusterLayout) {

            viewHolderJob.setAddress(job.getAddress());
            viewHolderJob.setTitle(job.getTitle());
        }

        int businessNumber = job.getBusinessNumber();

        if (Adapter.jobsTypesList != null) {
            JobType jobType = Adapter.jobsTypesList.get(businessNumber - 1);

            viewHolderJob.setDistance(job.getDistance());
            viewHolderJob.setLayout(jobType.getColor().toArray(new Integer[3]));
            viewHolderJob.setLookingFor(job.getFirm() + " מחפשת " + jobType.getJobType());
        }

        viewHolderJob.setCircle(job.getBusinessNumber());

        view.setOnClickListener(v -> viewHolderJob.activity.startActivity(new Intent(viewHolderJob.activity, JobInfo.class).putExtra("JobId", job.getId())));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.job_layout, container, false);

        fillJobDetails(view, Job.findJobById(getArguments().getString("JobId")));

        return view;
    }

    private static class ViewHolderJob extends RecyclerView.ViewHolder {

        Activity activity;
        View view;

        ViewHolderJob(View view) {

            super(view);

            this.activity = (Activity) view.getContext();
            this.view = view;
        }

        void setAddress(String address) {

            ((TextView) view.findViewById(R.id.address)).setText(address);
        }

        void setCircle(int businessNumber) {

            try {
                view.findViewById(R.id.circle).setBackground(Drawable.createFromStream
                        (activity.getAssets().open(businessNumber + ".png"), null));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        void setDistance(int distance) {

            ((TextView) view.findViewById(R.id.distance))
                    .setText(distance > 1000 ? new DecimalFormat("#.#")
                            .format((double) distance / 1000) + " ק\"מ ממני" : distance + " מ\' ממני");
        }

        void setLayout(Integer[] color) {

            int bg;

            View layout = view.findViewById(R.id.layout);

            if (layout == null)
                layout = view;

            layout.setBackgroundColor(bg = Color.rgb(color[0], color[1], color[2]));

            if (activity.getLocalClassName().equalsIgnoreCase("Activities.JobInfo")) {

                layout.setPadding(0, 0, 0, 30);

                view.setEnabled(false);

                view.findViewById(R.id.mapLayout).setVisibility(View.VISIBLE);
            } else
                view.findViewById(R.id.whereLayout).setBackgroundColor(bg);
        }

        void setLookingFor(String lookingFor) {

            ((TextView) view.findViewById(R.id.lookingFor)).setText(lookingFor);
        }

        void setTitle(String title) {

            ((TextView) view.findViewById(R.id.title)).setText(title);
        }
    }
}