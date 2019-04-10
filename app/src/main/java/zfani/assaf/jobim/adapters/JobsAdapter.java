package zfani.assaf.jobim.adapters;

import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.views.fragments.FeedFragments.ContactFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.DeleteFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.JobFragment;

public class JobsAdapter extends FirebaseRecyclerAdapter<Job, JobsAdapter.ViewHolder> {

    public static Query query;

    public JobsAdapter() {
        super(new FirebaseRecyclerOptions.Builder<Job>()
                .setQuery(query = FirebaseDatabase.getInstance().getReference().child("jobs").orderByChild("distance"), Job.class)
                .build());
    }

    public JobsAdapter(String key, String value) {
        super(new FirebaseRecyclerOptions.Builder<Job>().setQuery((value.equalsIgnoreCase("true") ?
                query.getRef().orderByChild(key).equalTo(true) : query.getRef().orderByChild(key).equalTo(value)), Job.class).build());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(parent.getContext(), R.layout.layouts_container, null));
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder viewHolder, int i, @NonNull Job job) {
        viewHolder.viewPager.setId(View.generateViewId());
        viewHolder.viewPager.setPageTransformer(true, (view, position) -> view.setTranslationX(-position * (view.getWidth() / 50f)));
        viewHolder.viewPager.setAdapter(new FragmentPagerAdapter(((AppCompatActivity) viewHolder.itemView.getContext()).getSupportFragmentManager()) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return ContactFragment.newInstance(job);
                    case 1:
                        return JobFragment.newInstance(job);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.viewPager)
        public ViewPager viewPager;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

