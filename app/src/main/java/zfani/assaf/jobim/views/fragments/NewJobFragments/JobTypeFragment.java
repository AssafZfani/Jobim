package zfani.assaf.jobim.views.fragments.NewJobFragments;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.ListFragment;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.JobType;
import zfani.assaf.jobim.views.activities.AddNewJob;

public class JobTypeFragment extends ListFragment {

    private Activity activity;
    private ArrayList<Integer> businessesNumbers;
    private ArrayList<JobType> jobsTypes = new ArrayList<>();
    private boolean isComeFromShowBy;

    public static JobTypeFragment newInstance(boolean isComeFromShowBy) {
        JobTypeFragment jobTypeFragment = new JobTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isComeFromShowBy", isComeFromShowBy);
        jobTypeFragment.setArguments(bundle);
        return jobTypeFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = getActivity();
        Bundle bundle = getArguments();
        isComeFromShowBy = bundle != null && bundle.getBoolean("isComeFromShowBy");
        return inflater.inflate(R.layout.job_type_fragment, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        RadioGroup radioGroup = new RadioGroup(activity);
        setContentJobTypes(activity, radioGroup, null);
        if (!isComeFromShowBy) {
            activity.findViewById(R.id.titleLayout).setVisibility(View.VISIBLE);
        }
    }

    private void setContentJobTypes(Activity activity, RadioGroup radioGroup, CharSequence s) {
        if (isComeFromShowBy) {
            businessesNumbers = new ArrayList<>();
        }
        /*jobsTypes = s == null ? JobsAdapter.jobsTypesList : new ArrayList<>();

        if (s != null)
            for (JobType jobType : JobsAdapter.jobsTypesList)
                if (jobType.getJobType().contains(s))
                    jobsTypes.add(jobType);*/
        setListAdapter(new ArrayAdapter<JobType>(activity, android.R.layout.simple_list_item_1, jobsTypes) {
            @NonNull
            @Override
            public View getView(int position, View convertView, @NonNull ViewGroup parent) {
                JobType jobType = jobsTypes.get(position);
                CheckBox checkBox = null;
                RadioButton radioButton = null;
                if (isComeFromShowBy) {
                    checkBox = (CheckBox) (convertView == null ? LayoutInflater.from(activity).inflate(R.layout.check_box, parent, false) : convertView);
                    checkBox.setHint(jobType.getJobType());
                    checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        if (isChecked) {
                            if (!businessesNumbers.contains(jobType.getId())) {
                                businessesNumbers.add(jobType.getId());
                            }
                        } else {
                            businessesNumbers.remove(((Integer) jobType.getId()));
                        }
                        jobType.setSelected(isChecked);
                        if (!businessesNumbers.isEmpty()) {
                            activity.getIntent().putIntegerArrayListExtra("BusinessesNumbers", businessesNumbers);
                        }
                    });
                    checkBox.setChecked(jobType.isSelected());
                } else {
                    radioButton = (RadioButton) (convertView == null ? LayoutInflater.from(activity).inflate(R.layout.radio_button, parent, false) : convertView);
                    radioButton.setId(View.generateViewId());
                    radioGroup.addView(radioButton);
                    radioButton.setHint(jobType.getJobType());
                    radioButton.setOnClickListener(view -> {
                        AddNewJob.newJob.setBusinessNumber(jobType.getId());
                        activity.findViewById(R.id.jobTitleButton).performClick();
                    });
                    int addedJobType = AddNewJob.newJob.getBusinessNumber();
                    if (addedJobType != 0) {
                        radioButton.setChecked(addedJobType == jobType.getId());
                    }
                }
                Drawable drawable1 = isComeFromShowBy ? activity.getDrawable(R.drawable.checkbox) : activity.getDrawable(R.drawable.radiobutton);
                if (drawable1 != null) {
                    drawable1.setBounds(0, 0, 75, 75);
                }
                Drawable drawable2 = null;
                try {
                    drawable2 = Drawable.createFromStream(activity.getAssets().open("1_" + jobType.getId() + ".png"), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (drawable2 != null) {
                    drawable2.setBounds(0, 0, 150, 150);
                }
                if (isComeFromShowBy) {
                    checkBox.setCompoundDrawablesRelative(drawable1, null, drawable2, null);
                } else {
                    radioButton.setCompoundDrawablesRelative(drawable1, null, drawable2, null);
                }
                return isComeFromShowBy ? checkBox : radioButton;
            }
        });
    }

    public boolean isValidValue() {
        boolean result = AddNewJob.newJob.getBusinessNumber() != 0;
        if (!result) {
            Toast.makeText(getActivity(), "חובה לבחור מקצוע", Toast.LENGTH_SHORT).show();
        }
        return result;
    }
}