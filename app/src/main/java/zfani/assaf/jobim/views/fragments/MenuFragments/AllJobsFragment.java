package zfani.assaf.jobim.views.fragments.MenuFragments;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.adapters.JobsAdapter;
import zfani.assaf.jobim.viewmodels.AllJobsViewModel;

public class AllJobsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.llShowBy)
    View llShowBy;
    @BindView(R.id.tvShowBY)
    TextView tvShowBY;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvAllJobs)
    RecyclerView rvAllJobs;
    @BindView(R.id.ivLocationMessage)
    View ivLocationMessage;
    private JobsAdapter jobsAdapter;
    //private FilteredAdapter filteredAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_jobs, container, false);
        ButterKnife.bind(this, view);
        ViewModelProviders.of(this).get(AllJobsViewModel.class).loadJobs();
        swipeRefreshLayout.setOnRefreshListener(this);
        ivLocationMessage.setVisibility(View.GONE);
        rvAllJobs.setAdapter(jobsAdapter = new JobsAdapter());
        rvAllJobs.getItemAnimator().setAddDuration(750);
        rvAllJobs.getItemAnimator().setRemoveDuration(750);
        int orange = ContextCompat.getColor(container.getContext(), android.R.color.holo_orange_dark);
        designShowByLayout(orange, "");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        jobsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        jobsAdapter.stopListening();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 3000);
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (filteredAdapter != null) {
            filteredAdapter.notifyDataSetChanged();
        }*/
    }

    /*public void clean() {
        filteredAdapter = null;
        FilteredAdapter.filteredList = null;
        view.findViewById(R.id.filter).setVisibility(View.VISIBLE);
        view.findViewById(R.id.cleanButton).setVisibility(View.GONE);
        designShowByLayout(orange, "");
        onRefresh();
    }*/

    private void designShowByLayout(int color, String text) {
        GradientDrawable border = new GradientDrawable();
        border.setStroke(5, color);
        llShowBy.setBackground(border);
        tvShowBY.setTextColor(color);
        tvShowBY.setText(text.isEmpty() ? "מציג את כל הג'ובים סביבי" : text);
        Drawable drawable = tvShowBY.getCompoundDrawables()[0];
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }

    /*public void filterList(View view, Intent data) {
        ArrayList<Integer> businessesNumbers = data.getIntegerArrayListExtra("BusinessesNumbers");
        String address = data.getStringExtra("Address") == null ? "" : data.getStringExtra("Address");
        String location = address.isEmpty() ? address : address.substring(address.indexOf(", ") + 2);
        String firm = data.getStringExtra("Firm") == null ? "" : data.getStringExtra("Firm");
        if (businessesNumbers != null || !location.isEmpty() || !firm.isEmpty()) {
            Button cleanButton = view.findViewById(R.id.cleanButton);
            final View filter = view.findViewById(R.id.filter);
            filter.setVisibility(View.GONE);
            cleanButton.setVisibility(View.VISIBLE);
            rvAllJobs.setAdapter(filteredAdapter = new FilteredAdapter(businessesNumbers, location, firm));
            int color = orange;
            String text = "";
            if (businessesNumbers != null) {
                JobType jobType = null;
                if (businessesNumbers.size() == 1) {
                    jobType = JobsAdapter.jobsTypesList.get(businessesNumbers.get(0) - 1);
                    Integer[] colorArray = jobType.getColor().toArray(new Integer[3]);
                    color = Color.rgb(colorArray[0], colorArray[1], colorArray[2]);
                }
                text = "מציג " + (jobType != null ? jobType.getJobType() : businessesNumbers.size() + " ג'ובים");
            }
            text += (!firm.isEmpty() ? " ב" + firm : "") + (!address.isEmpty() ? " ב" + address : "");
            cleanButton.setTextColor(color);
            designShowByLayout(color, text);
            setMessageVisibility();
        }
    }*/
}