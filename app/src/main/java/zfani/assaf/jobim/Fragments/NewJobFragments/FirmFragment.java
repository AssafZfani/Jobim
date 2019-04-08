package zfani.assaf.jobim.Fragments.NewJobFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import zfani.assaf.jobim.Activities.AddNewJob;
import zfani.assaf.jobim.Activities.HomePage;
import zfani.assaf.jobim.R;

public class FirmFragment extends Fragment {

    EditText firmName, branchName;
    private View support;

    public static FirmFragment newInstance() {

        return new FirmFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.firm_fragment, container, false);

        firmName = (EditText) view.findViewById(R.id.firmName);

        branchName = (EditText) view.findViewById(R.id.branchName);

        support = view.findViewById(R.id.support);

        return view;
    }

    @Override
    public void onStart() {

        super.onStart();

        support.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                HomePage.displayDialog(getActivity(), R.layout.add_new_job_dialog, null);
            }
        });
    }

    public boolean isValidValue() {

        String firmNameText = firmName.getText().toString(), branchNameText = branchName.getText().toString();

        boolean result = !firmNameText.isEmpty();

        if (result) {

            AddNewJob.newJob.setFirm(firmNameText);

            if (!branchNameText.isEmpty())
                AddNewJob.newJob.setBranchName(branchNameText);
        } else
            Toast.makeText(getActivity(), "חובה למלא את שם החברה", Toast.LENGTH_SHORT).show();

        return result;
    }
}
