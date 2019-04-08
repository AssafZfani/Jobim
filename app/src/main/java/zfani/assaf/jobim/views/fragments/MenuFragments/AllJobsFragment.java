package zfani.assaf.jobim.views.fragments.MenuFragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import zfani.assaf.jobim.Application;
import zfani.assaf.jobim.models.JobType;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.utils.Adapter;
import zfani.assaf.jobim.utils.FilteredAdapter;
import zfani.assaf.jobim.utils.GPSTracker;

public class AllJobsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public FilteredAdapter filteredAdapter;
    private View view;
    private int orange;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private View message;

    public static Fragment newInstance() {

        return new AllJobsFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.all_jobs_fragment, container, false);

        orange = ContextCompat.getColor(container.getContext(), android.R.color.holo_orange_dark);

        designShowByLayout(orange, "");

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.allJobsRecyclerView);

        message = view.findViewById(R.id.message);

        onRefresh();

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

    @Override
    public void onResume() {

        super.onResume();

        if (filteredAdapter != null)
            filteredAdapter.notifyDataSetChanged();
    }

    public void clean() {

        filteredAdapter = null;

        FilteredAdapter.filteredList = null;

        view.findViewById(R.id.filter).setVisibility(View.VISIBLE);

        view.findViewById(R.id.cleanButton).setVisibility(View.GONE);

        designShowByLayout(orange, "");

        onRefresh();
    }

    void designShowByLayout(int color, String text) {

        LinearLayout showBy = (LinearLayout) view.findViewById(R.id.showBy);

        GradientDrawable border = new GradientDrawable();

        border.setStroke(5, color);

        showBy.setBackground(border);

        TextView txt = (TextView) view.findViewById(R.id.txt);

        txt.setTextColor(color);

        txt.setText(text.isEmpty() ? "מציג את כל הג'ובים סביבי" : text);

        Drawable drawable = txt.getCompoundDrawables()[0];

        if (drawable != null)
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
    }

    public void filterList(View view, Intent data) {

        ArrayList<Integer> businessesNumbers = data.getIntegerArrayListExtra("BusinessesNumbers");

        String address = data.getStringExtra("Address") == null ? "" : data.getStringExtra("Address");

        String location = address.isEmpty() ? address : address.substring(address.indexOf(", ") + 2, address.length());

        String firm = data.getStringExtra("Firm") == null ? "" : data.getStringExtra("Firm");

        if (businessesNumbers != null || !location.isEmpty() || !firm.isEmpty()) {

            Button cleanButton = (Button) view.findViewById(R.id.cleanButton);

            final View filter = view.findViewById(R.id.filter);

            filter.setVisibility(View.GONE);

            cleanButton.setVisibility(View.VISIBLE);

            recyclerView.setAdapter(filteredAdapter = new FilteredAdapter(businessesNumbers, location, firm));

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
    }

    void refreshList() {

        message.setVisibility(View.INVISIBLE);

        Activity activity = getActivity();

        if (Adapter.jobsList == null)
            Application.loadJobs(activity);

        recyclerView.setLayoutManager(new LinearLayoutManager(activity));

        recyclerView.getItemAnimator().setAddDuration(750);

        recyclerView.getItemAnimator().setRemoveDuration(750);

        recyclerView.setAdapter(filteredAdapter == null ? new Adapter() : filteredAdapter);

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
    }

    void setMessageVisibility() {

        RecyclerView.Adapter adapter = recyclerView.getAdapter();

        message.setBackgroundResource(R.drawable.no_jobs_message);

        message.setVisibility((adapter == null || adapter.getItemCount() == 0) ? View.VISIBLE : View.INVISIBLE);
    }
}