package zfani.assaf.jobim.Fragments.NewJobFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import zfani.assaf.jobim.Activities.AddNewJob;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.Utils.Adapter;

public class JobTitleFragment extends Fragment {

    EditText title, description;
    TextView lookingFor;

    public static JobTitleFragment newInstance() {

        return new JobTitleFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.job_title_fragment, container, false);

        title = (EditText) view.findViewById(R.id.title);

        description = (EditText) view.findViewById(R.id.description);

        lookingFor = (TextView) view.findViewById(R.id.lookingFor);

        return view;
    }

    @Override
    public View getView() {

        String branchName = AddNewJob.newJob.getBranchName();

        int businessNumber = AddNewJob.newJob.getBusinessNumber() - 1;

        if (businessNumber != -1) {

            lookingFor.setText(AddNewJob.newJob.getFirm() + (branchName == null ? "" : " " +
                    branchName) + " מחפשת " + Adapter.jobsTypesList.get(businessNumber).getJobType());
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
