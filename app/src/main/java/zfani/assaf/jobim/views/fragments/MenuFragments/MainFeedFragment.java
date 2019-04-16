package zfani.assaf.jobim.views.fragments.MenuFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import zfani.assaf.jobim.viewmodels.ShowByBottomSheetViewModel;
import zfani.assaf.jobim.views.bottomsheets.ShowByBottomSheet;

public class MainFeedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rlShowBy)
    View rlShowBy;
    @BindView(R.id.ivSearch)
    View ivSearch;
    @BindView(R.id.tvShowBy)
    TextView tvShowBY;
    @BindView(R.id.ivArrow)
    ImageView ivArrow;
    @BindView(R.id.tvClean)
    TextView tvClean;
    @BindView(R.id.srlMainFeed)
    SwipeRefreshLayout srlMainFeed;
    @BindView(R.id.rvMainFeed)
    RecyclerView rvMainFeed;
    @BindView(R.id.ivLocationMessage)
    View ivLocationMessage;
    private MainFeedViewModel mainFeedViewModel;
    private ShowByBottomSheetViewModel showByBottomSheetViewModel;
    private JobsAdapter jobsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_feed, container, false);
        ButterKnife.bind(this, view);
        srlMainFeed.setOnRefreshListener(this);
        int orange = ContextCompat.getColor(container.getContext(), R.color.orange);
        mainFeedViewModel = ViewModelProviders.of(this).get(MainFeedViewModel.class);
        showByBottomSheetViewModel = ViewModelProviders.of(requireActivity()).get(ShowByBottomSheetViewModel.class);
        mainFeedViewModel.getShowByTitle().observe(this, showByTitle -> designShowByLayout(orange, showByTitle));
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
    public void onPause() {
        GPSTracker.location.endUpdates();
        super.onPause();
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> srlMainFeed.setRefreshing(false), 3000);
    }

    @OnClick(R.id.rlShowBy)
    void showBy() {
        new ShowByBottomSheet().show(getChildFragmentManager(), null);
    }

    @OnClick(R.id.tvClean)
    void clean() {
        showByBottomSheetViewModel.setFilter(false);
    }

    private void designShowByLayout(int color, String showByTitle) {
        GradientDrawable border = new GradientDrawable();
        border.setStroke(5, color);
        rlShowBy.setBackground(border);
        if (showByTitle.isEmpty()) {
            ivSearch.setVisibility(View.VISIBLE);
            tvShowBY.setText("מציג את כל הג'ובים סביבי");
            tvClean.setVisibility(View.GONE);
        } else {
            ivSearch.setVisibility(View.INVISIBLE);
            tvShowBY.setText(showByTitle);
            tvClean.setVisibility(View.VISIBLE);
            tvClean.setTextColor(color);
        }
        ivArrow.setColorFilter(color);
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
        mainFeedViewModel.getJobLiveList().observe(this, jobList -> jobsAdapter.submitList(jobList));
        showByBottomSheetViewModel.getFilter().observe(this, isFilter -> {
            if (isFilter) {
                jobsAdapter.submitList(mainFeedViewModel.getJobLiveList(showByBottomSheetViewModel.getChosenJobTypeList(),
                        showByBottomSheetViewModel.getChosenLocation().getValue(),
                        showByBottomSheetViewModel.getChosenFirm()));
            } else {
                mainFeedViewModel.setShowByTitle("");
                mainFeedViewModel.getJobLiveList().observe(this, jobList -> jobsAdapter.submitList(jobList));
            }
        });
    }
}