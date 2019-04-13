package zfani.assaf.jobim.views.fragments.MenuFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.adapters.JobsAdapter;
import zfani.assaf.jobim.utils.Constants;
import zfani.assaf.jobim.utils.GPSTracker;
import zfani.assaf.jobim.viewmodels.MainFeedViewModel;
import zfani.assaf.jobim.views.bottomsheets.ShowByBottomSheet;

public class MainFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.llShowBy)
    View llShowBy;
    @BindView(R.id.tvShowBY)
    TextView tvShowBY;
    @BindView(R.id.srlMainFeed)
    SwipeRefreshLayout srlMainFeed;
    @BindView(R.id.rvMainFeed)
    RecyclerView rvMainFeed;
    @BindView(R.id.ivLocationMessage)
    View ivLocationMessage;
    private MainFeedViewModel mainFeedViewModel;
    private JobsAdapter jobsAdapter;
    //private FilteredAdapter filteredAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_feed, container, false);
        ButterKnife.bind(this, view);
        srlMainFeed.setOnRefreshListener(this);
        int orange = ContextCompat.getColor(container.getContext(), android.R.color.holo_orange_dark);
        designShowByLayout(orange, "");
        mainFeedViewModel = ViewModelProviders.of(this).get(MainFeedViewModel.class);
        new GPSTracker(requireActivity());
        mainFeedViewModel.getShouldCheckPermission().observe(this, isShouldCheckLocationPermission -> {
            if (isShouldCheckLocationPermission) {
                checkLocationPermission();
            } else {
                initMainFeedList();
            }
        });
        checkLocationPermission();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.KEY_ACTION_APPLICATION_DETAILS_SETTINGS:
                checkLocationPermission();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (jobsAdapter != null) {
            jobsAdapter.stopListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if (filteredAdapter != null) {
            filteredAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onPause() {
        GPSTracker.location.endUpdates();
        super.onPause();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> srlMainFeed.setRefreshing(false), 3000);
        rvMainFeed.setAdapter(jobsAdapter = new JobsAdapter());
        jobsAdapter.startListening();
    }

    @OnClick(R.id.llShowBy)
    void showBy() {
        new ShowByBottomSheet().show(getChildFragmentManager(), null);
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

    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, Constants.KEY_REQUEST_LOCATION_PERMISSION);
            return;
        }
        mainFeedViewModel.setShouldCheckPermission(false);
    }

    private void initMainFeedList() {
        GPSTracker.location.beginUpdates();
        ivLocationMessage.setVisibility(View.GONE);
        mainFeedViewModel.loadJobs();
        rvMainFeed.setAdapter(jobsAdapter = new JobsAdapter());
        RecyclerView.ItemAnimator itemAnimator = rvMainFeed.getItemAnimator();
        if (itemAnimator != null) {
            rvMainFeed.getItemAnimator().setAddDuration(750);
            rvMainFeed.getItemAnimator().setRemoveDuration(750);
        }
        jobsAdapter.startListening();
    }

    /*public void filterList(View view, Intent data) {
        ArrayList<Integer> businessesNumbers = data.getIntegerArrayListExtra("BusinessesNumbers");
        String address = data.getStringExtra("Address") == null ? "" : data.getStringExtra("Address");
        String location = address.isEmpty() ? address : address.substring(address.indexOf(", ") + 2);
        String firm = data.getStringExtra("Firm") == null ? "" : data.getStringExtra("Firm");
        if (businessesNumbers != null || !location.isEmpty() || !firm.isEmpty()) {
            Button cleanButton = view.findViewById(R.id.cleanButton);
             View filter = view.findViewById(R.id.filter);
            filter.setVisibility(View.GONE);
            cleanButton.setVisibility(View.VISIBLE);
            rvMainFeed.setAdapter(filteredAdapter = new FilteredAdapter(businessesNumbers, location, firm));
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