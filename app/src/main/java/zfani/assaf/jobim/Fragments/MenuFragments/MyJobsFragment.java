package zfani.assaf.jobim.Fragments.MenuFragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.google.firebase.database.DatabaseReference;

import zfani.assaf.jobim.Activities.AddNewJob;
import zfani.assaf.jobim.Models.Job;
import zfani.assaf.jobim.Models.NewJob;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.Utils.Adapter;
import zfani.assaf.jobim.Utils.GPSTracker;

public class MyJobsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RadioGroup myJobsLayout;
    private RecyclerView recyclerView;
    private View message;

    public static Fragment newInstance() {

        return new MyJobsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.my_jobs, container, false);

        myJobsLayout = (RadioGroup) view.findViewById(R.id.myJobsLayout);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.myJobsRecyclerView);

        message = view.findViewById(R.id.message);

        myJobsLayout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                onRefresh();

                setMessageVisibility();

                int messageDrawable = 0;

                switch (i) {

                    case R.id.favoriteTab:
                        messageDrawable = R.drawable.no_favorites_message;
                        break;
                    case R.id.appliedTab:
                        messageDrawable = R.drawable.no_applies_message;
                        break;
                    case R.id.postedTab:
                        messageDrawable = R.drawable.no_posted_jobs_message;
                        break;
                }

                message.setBackgroundResource(messageDrawable);
            }
        });

        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {

            @Override
            public void onChildViewAttachedToWindow(View view) {

                setMessageVisibility();
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {

                setMessageVisibility();
            }
        });

        NewJob newJob = AddNewJob.newJob;

        if (newJob != null) {

            myJobsLayout.getChildAt(1).setBackgroundResource(R.drawable.middle);

            myJobsLayout.getChildAt(2).setVisibility(View.VISIBLE);
        }

        myJobsLayout.check(newJob == null ? R.id.favoriteTab : R.id.postedTab);

        if (newJob != null) {

            DatabaseReference job = Adapter.query.getRef().push();

            String branch = newJob.getBranchName();

            job.setValue(new Job(newJob.getAddress(), false, newJob.getBusinessNumber(), newJob.getDistance(), false,
                    newJob.getFirm() + (branch == null ? "" : " " + newJob.getBranchName()), job.getKey(), true, newJob.getTitle()));
        }

        AddNewJob.newJob = null;

        return view;
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);

        if (GPSTracker.location != null)
            refreshList();
    }

    void refreshList() {

        int messageDrawable = 0;

        String key = "";

        switch (myJobsLayout.getCheckedRadioButtonId()) {

            case R.id.favoriteTab:
                messageDrawable = R.drawable.no_favorites_message;
                key = "favorite";
                break;
            case R.id.appliedTab:
                messageDrawable = R.drawable.no_applies_message;
                key = "applied";
                break;
            case R.id.postedTab:
                messageDrawable = R.drawable.no_posted_jobs_message;
                key = "posted";
                break;
        }

        message.setBackgroundResource(messageDrawable);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        recyclerView.getItemAnimator().setAddDuration(750);

        recyclerView.getItemAnimator().setRemoveDuration(750);

        recyclerView.setAdapter(new Adapter(key, "true"));
    }

    void setMessageVisibility() {

        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        message.setVisibility((adapter == null || adapter.getItemCount() == 0) ? View.VISIBLE : View.INVISIBLE);
    }
}
