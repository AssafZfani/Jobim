package zfani.assaf.jobim.views.fragments.MenuFragments;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.jobim.App;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.utils.Adapter;
import zfani.assaf.jobim.utils.GPSTracker;

public class AllJobsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rvAllJobs)
    RecyclerView rvAllJobs;
    @BindView(R.id.ivLocationMessage)
    View ivLocationMessage;
    //private FilteredAdapter filteredAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_all_jobs, container, false);
        ButterKnife.bind(this, view);
        int orange = ContextCompat.getColor(container.getContext(), android.R.color.holo_orange_dark);
        designShowByLayout(orange, "");
        swipeRefreshLayout.setOnRefreshListener(this);
        onRefresh();
        return view;
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 3000);
        if (GPSTracker.location != null) {
            refreshList();
        }
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
        //LinearLayout showBy = view.findViewById(R.id.showBy);
        GradientDrawable border = new GradientDrawable();
        border.setStroke(5, color);
        //showBy.setBackground(border);
        //TextView txt = view.findViewById(R.id.txt);
        /*txt.setTextColor(color);
        txt.setText(text.isEmpty() ? "מציג את כל הג'ובים סביבי" : text);
        Drawable drawable = txt.getCompoundDrawables()[0];
        if (drawable != null) {
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }*/
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
                    jobType = Adapter.jobsTypesList.get(businessesNumbers.get(0) - 1);
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

    private void refreshList() {
        ivLocationMessage.setVisibility(View.INVISIBLE);
        Activity activity = getActivity();
        if (Adapter.jobsList == null) {
            App.loadJobs(activity);
        }
        rvAllJobs.setLayoutManager(new LinearLayoutManager(activity));
        rvAllJobs.getItemAnimator().setAddDuration(750);
        rvAllJobs.getItemAnimator().setRemoveDuration(750);
        //rvAllJobs.setAdapter(filteredAdapter == null ? new Adapter() : filteredAdapter);
        rvAllJobs.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(@NonNull View view) {
                setMessageVisibility();
            }

            @Override
            public void onChildViewDetachedFromWindow(@NonNull View view) {
                setMessageVisibility();
            }
        });
    }

    private void setMessageVisibility() {
        RecyclerView.Adapter adapter = rvAllJobs.getAdapter();
        ivLocationMessage.setVisibility((adapter == null || adapter.getItemCount() == 0) ? View.VISIBLE : View.INVISIBLE);
    }
}