package zfani.assaf.jobim.Fragments.FeedFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import zfani.assaf.jobim.Activities.HomePage;
import zfani.assaf.jobim.R;

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

        view.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                getActivity().getIntent().putExtra("ViewPager", container.getId());

                HomePage.displayDialog(getActivity(), R.layout.delete_job_dialog, getArguments().getString("JobId"));
            }
        });

        return view;
    }
}
