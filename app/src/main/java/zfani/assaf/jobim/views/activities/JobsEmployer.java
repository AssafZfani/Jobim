package zfani.assaf.jobim.views.activities;

import android.os.Bundle;
import android.os.Handler;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.adapters.JobsAdapter;

public class JobsEmployer extends FragmentActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.jobs_employer);

        MainActivity.setupToolBar(this);

        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);

        swipeRefreshLayout.setOnRefreshListener(this);

        recyclerView = findViewById(R.id.jobsEmployerRecyclerView);

        onRefresh();
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(() -> swipeRefreshLayout.setRefreshing(false), 3000);

        refreshList();
    }

    private void refreshList() {

        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));

        recyclerView.getItemAnimator().setAddDuration(750);

        recyclerView.getItemAnimator().setRemoveDuration(750);

        recyclerView.setAdapter(new JobsAdapter("firm", getIntent().getStringExtra("Firm")));
    }
}