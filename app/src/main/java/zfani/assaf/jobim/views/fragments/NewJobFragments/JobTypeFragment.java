package zfani.assaf.jobim.views.fragments.NewJobFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.adapters.JobTypesAdapter;
import zfani.assaf.jobim.viewmodels.JobTypesViewModel;
import zfani.assaf.jobim.viewmodels.ShowByBottomSheetViewModel;
import zfani.assaf.jobim.views.activities.AddNewJob;

public class JobTypeFragment extends Fragment {

    @BindView(R.id.rvJobTypes)
    RecyclerView rvJobTypes;
    private JobTypesAdapter jobTypesAdapter;

    public static JobTypeFragment newInstance(boolean isComeFromShowBy) {
        JobTypeFragment jobTypeFragment = new JobTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isComeFromShowBy", isComeFromShowBy);
        jobTypeFragment.setArguments(bundle);
        return jobTypeFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.job_type_fragment, container, false);
        ButterKnife.bind(this, view);
        ViewModelProviders.of(this).get(JobTypesViewModel.class).loadJobsTypes();
        boolean isComeFromShowBy = requireArguments().getBoolean("isComeFromShowBy");
        ViewModelProviders.of(requireActivity()).get(ShowByBottomSheetViewModel.class).getJobTypeQuery().observe(this, queryText -> {
            jobTypesAdapter.stopListening();
            rvJobTypes.setAdapter(jobTypesAdapter = new JobTypesAdapter(isComeFromShowBy, queryText));
            jobTypesAdapter.startListening();
        });
        rvJobTypes.setAdapter(jobTypesAdapter = new JobTypesAdapter(isComeFromShowBy, null));
        rvJobTypes.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        if (!isComeFromShowBy) {
            requireActivity().findViewById(R.id.titleLayout).setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        jobTypesAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        jobTypesAdapter.stopListening();
    }

    public boolean isValidValue() {
        boolean result = AddNewJob.newJob.getBusinessNumber() != 0;
        if (!result) {
            Toast.makeText(getActivity(), "חובה לבחור מקצוע", Toast.LENGTH_SHORT).show();
        }
        return result;
    }
}