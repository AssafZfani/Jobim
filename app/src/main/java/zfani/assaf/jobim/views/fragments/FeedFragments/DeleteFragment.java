package zfani.assaf.jobim.views.fragments.FeedFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.views.activities.MainActivity;

public class DeleteFragment extends Fragment {

    public static DeleteFragment newInstance(String jobId) {

        DeleteFragment deleteFragment = new DeleteFragment();

        Bundle bundle = new Bundle();

        bundle.putString("JobId", jobId);

        deleteFragment.setArguments(bundle);

        return deleteFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.delete_job_layout, container, false);

        view.findViewById(R.id.delete).setOnClickListener(view1 -> {

            getActivity().getIntent().putExtra("ViewPager", container.getId());

            MainActivity.displayDialog(getActivity(), R.layout.delete_job_dialog, getArguments().getString("JobId"));
        });

        return view;
    }
}
