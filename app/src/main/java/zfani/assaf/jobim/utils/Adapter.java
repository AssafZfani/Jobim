package zfani.assaf.jobim.utils;

import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.models.JobType;
import zfani.assaf.jobim.views.fragments.FeedFragments.ContactFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.DeleteFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.JobFragment;

public class Adapter extends FirebaseRecyclerAdapter<Job, Adapter.ViewHolder> {

    public static ArrayList<Job> jobsList;
    public static ArrayList<JobType> jobsTypesList;
    public static Query query;

    public Adapter() {

        super(Job.class, R.layout.layouts_container, ViewHolder.class,
                query = FirebaseDatabase.getInstance().getReference().child("jobs").orderByChild("distance"));

        query.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                jobsList = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren())
                    jobsList.add(data.getValue(Job.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("jobs_types").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                jobsTypesList = new ArrayList<>();

                for (DataSnapshot data : dataSnapshot.getChildren())
                    jobsTypesList.add(data.getValue(JobType.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public Adapter(String key, String value) {

        super(Job.class, R.layout.layouts_container, ViewHolder.class, value.equalsIgnoreCase("true") ?
                query.getRef().orderByChild(key).equalTo(true) : query.getRef().orderByChild(key).equalTo(value));
    }

    static void populateViewHolder(ViewHolder viewHolder, final Job job) {

        final FragmentActivity activity = (FragmentActivity) viewHolder.mainView.getContext();

        viewHolder.viewPager.setId(View.generateViewId());

        viewHolder.viewPager.setPageTransformer(true, (view, position) -> view.setTranslationX(-position * (view.getWidth() / 50)));

        viewHolder.viewPager.setAdapter(new FragmentPagerAdapter(activity.getSupportFragmentManager()) {

            @Override
            public Fragment getItem(int position) {

                switch (position) {

                    case 0:
                        return ContactFragment.newInstance(job.getId());
                    case 1:
                        return JobFragment.newInstance(job.getId());
                    default:
                        return DeleteFragment.newInstance(job.getId());
                }
            }

            @Override
            public int getCount() {

                return 3;
            }
        });

        viewHolder.viewPager.setCurrentItem(1);
    }

    @Override
    protected void populateViewHolder(ViewHolder viewHolder, final Job job, int position) {

        populateViewHolder(viewHolder, job);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View mainView;
        public ViewPager viewPager;

        public ViewHolder(View mainView) {

            super(mainView);

            this.mainView = mainView;

            this.viewPager = mainView.findViewById(R.id.viewPager);
        }
    }
}

