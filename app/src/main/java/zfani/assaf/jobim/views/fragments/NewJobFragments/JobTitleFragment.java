package zfani.assaf.jobim.views.fragments.NewJobFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.adapters.JobsAdapter;
import zfani.assaf.jobim.views.activities.AddNewJob;

public class JobTitleFragment extends Fragment {

    EditText title, description;
    TextView lookingFor;

    public static JobTitleFragment newInstance() {

        return new JobTitleFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.job_title_fragment, container, false);

        title = view.findViewById(R.id.title);

        description = view.findViewById(R.id.description);

        lookingFor = view.findViewById(R.id.lookingFor);

        return view;
    }

    @Override
    public View getView() {

        String branchName = AddNewJob.newJob.getBranchName();

        int businessNumber = AddNewJob.newJob.getBusinessNumber() - 1;

        if (businessNumber != -1) {

            lookingFor.setText(AddNewJob.newJob.getFirm() + (branchName == null ? "" : " " +
                    branchName) + " מחפשת " + JobsAdapter.jobsTypesList.get(businessNumber).getJobType());
        }

        return super.getView();
    }

    public boolean isValidValue() {

        String titleText = title.getText().toString(), descriptionText = description.getText().toString();

        boolean result = !titleText.isEmpty();

        if (result) {

            AddNewJob.newJob.setTitle(titleText);

            if (!descriptionText.isEmpty())
                AddNewJob.newJob.setDescription(descriptionText);
        } else
            Toast.makeText(getActivity(), "חובה למלא כותרת", Toast.LENGTH_SHORT).show();

        return result;
    }
}
