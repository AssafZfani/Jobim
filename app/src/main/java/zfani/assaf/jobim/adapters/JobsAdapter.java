package zfani.assaf.jobim.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.views.fragments.FeedFragments.ContactFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.DeleteFragment;
import zfani.assaf.jobim.views.fragments.FeedFragments.JobFragment;

public class JobsAdapter extends ListAdapter<Job, JobsAdapter.JobViewHolder> {

    public JobsAdapter() {
        super(new DiffUtil.ItemCallback<Job>() {
            @Override
            public boolean areItemsTheSame(@NonNull Job oldItem, @NonNull Job newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Job oldItem, @NonNull Job newItem) {
                return oldItem.getDistance() == newItem.getDistance();
            }
        });
    }

    @NonNull
    @Override
    public JobsAdapter.JobViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new JobsAdapter.JobViewHolder(View.inflate(parent.getContext(), R.layout.layouts_container, null));
    }

    @Override
    public void onBindViewHolder(@NonNull JobsAdapter.JobViewHolder viewHolder, int i) {
        Job job = getItem(i);
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

    public static class JobViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.viewPager)
        public ViewPager viewPager;

        public JobViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
