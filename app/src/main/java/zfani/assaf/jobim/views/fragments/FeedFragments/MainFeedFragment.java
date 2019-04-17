package zfani.assaf.jobim.views.fragments.FeedFragments;

import android.Manifest;
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
import zfani.assaf.jobim.models.FilterItem;
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
    @BindView(R.id.ivNoResultsMessage)
    View ivNoResultsMessage;
    private MainFeedViewModel mainFeedViewModel;
    private ShowByBottomSheetViewModel showByBottomSheetViewModel;
    private JobsAdapter jobsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_feed, container, false);
        ButterKnife.bind(this, view);
        srlMainFeed.setOnRefreshListener(this);
        mainFeedViewModel = ViewModelProviders.of(this).get(MainFeedViewModel.class);
        showByBottomSheetViewModel = ViewModelProviders.of(requireActivity()).get(ShowByBottomSheetViewModel.class);
        initView();
        return view;
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
        showByBottomSheetViewModel.cleanUserChoices();
        new ShowByBottomSheet().show(getChildFragmentManager(), null);
    }

    @OnClick(R.id.tvClean)
    void clean() {
        showByBottomSheetViewModel.cleanUserChoices();
        showByBottomSheetViewModel.setFilter(false);
    }

    private void initView() {
        mainFeedViewModel.getFilterItem().observe(this, this::designShowByLayout);
        new GPSTracker(requireActivity());
        mainFeedViewModel.getShouldCheckPermission().observe(this, isShouldCheckLocationPermission -> {
            if (isShouldCheckLocationPermission) {
                checkLocationPermission();
            } else {
                initMainFeedList();
            }
        });
    }

    private void designShowByLayout(FilterItem filterItem) {
        boolean isFilteringMode = filterItem != null;
        int color = isFilteringMode && filterItem.getColor() != 0 ? filterItem.getColor() : requireActivity().getResources().getColor(R.color.orange);
        String showByTitle = isFilteringMode ? filterItem.getTitle() : "מציג את כל הג'ובים סביבי";
        GradientDrawable border = new GradientDrawable();
        border.setStroke(5, color);
        rlShowBy.setBackground(border);
        ivSearch.setVisibility(isFilteringMode ? View.INVISIBLE : View.VISIBLE);
        ivArrow.setColorFilter(color);
        tvShowBY.setText(showByTitle);
        tvShowBY.setTextColor(color);
        tvClean.setVisibility(isFilteringMode ? View.VISIBLE : View.GONE);
        tvClean.setTextColor(color);
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
        showByBottomSheetViewModel.getFilter().observe(this, isFilter -> {
            if (isFilter) {
                jobsAdapter.submitList(mainFeedViewModel.getJobLiveList(showByBottomSheetViewModel.getChosenJobTypeList(),
                        showByBottomSheetViewModel.getChosenLocation().getValue(),
                        showByBottomSheetViewModel.getChosenFirm()),
                        () -> ivNoResultsMessage.setVisibility(jobsAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE));
            } else {
                mainFeedViewModel.setFilterItem(null);
                mainFeedViewModel.getJobLiveList().observe(this, jobList -> jobsAdapter.submitList(jobList,
                        () -> ivNoResultsMessage.setVisibility(jobsAdapter.getItemCount() == 0 ? View.VISIBLE : View.GONE)));
            }
        });
    }
}