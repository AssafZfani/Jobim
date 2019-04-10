package zfani.assaf.jobim.views.fragments.FeedFragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.views.activities.JobInfoActivity;

public class JobFragment extends Fragment {

    @BindView(R.id.tvJobLookingFor)
    TextView tvJobLookingFor;
    @BindView(R.id.ivJobBusinessSymbol)
    ImageView ivJobBusinessSymbol;
    @BindView(R.id.tvJobTitle)
    TextView tvJobTitle;
    @BindView(R.id.tvJobAddress)
    TextView tvJobAddress;
    @BindView(R.id.tvJobDistance)
    TextView tvJobDistance;

    public static JobFragment newInstance(Job job) {
        JobFragment jobFragment = new JobFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("Job", job);
        jobFragment.setArguments(bundle);
        return jobFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.job_layout, container, false);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("Job")) {
            fillJobDetails(view, Objects.requireNonNull(bundle.getParcelable("Job")));
        }
        return view;
    }

    private void fillJobDetails(View view, Job job) {
        ButterKnife.bind(this, view);
        tvJobLookingFor.setText(new StringBuilder(job.getFirm() + " מחפשת " + job.getType()));
        try {
            ivJobBusinessSymbol.setBackground(Drawable.createFromStream(Objects.requireNonNull(getContext()).getAssets().open(job.getBusinessNumber() + ".png"), null));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (view.getId() != R.id.clusterLayout) {
            tvJobTitle.setText(job.getTitle());
            tvJobAddress.setText(job.getAddress());
        }
        int distance = job.getDistance();
        tvJobDistance.setText(distance > 1000 ? new DecimalFormat("#.#").format((double) distance / 1000) + " ק\"מ ממני" : distance + " מ\' ממני");
        int bg;
        View layout = view.findViewById(R.id.layout);
        if (layout == null) {
            layout = view;
        }
        layout.setBackgroundColor(bg = Color.rgb(job.getColor1(), job.getColor2(), job.getColor3()));
        if (Objects.requireNonNull(getActivity()).getLocalClassName().equalsIgnoreCase("Activities.JobInfoActivity")) {
            layout.setPadding(0, 0, 0, 30);
            view.setEnabled(false);
            view.findViewById(R.id.mapLayout).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.whereLayout).setBackgroundColor(bg);
        }
        view.setOnClickListener(v -> startActivity(new Intent(getActivity(), JobInfoActivity.class).putExtra("Job", job)));
    }
}