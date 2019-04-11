package zfani.assaf.jobim.adapters;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.models.JobType;

public class JobTypesAdapter extends FirebaseRecyclerAdapter<JobType, JobTypesAdapter.JobTypeViewHolder> {

    private boolean isComeFromShowBy;

    public JobTypesAdapter(boolean isComeFromShowBy, String queryText) {
        super(new FirebaseRecyclerOptions.Builder<JobType>().setQuery(getQuery(queryText), JobType.class).build());
        this.isComeFromShowBy = isComeFromShowBy;
    }

    @NonNull
    private static Query getQuery(@NonNull String queryText) {
        Query query = FirebaseDatabase.getInstance().getReference().child("jobs_types");
        if (!queryText.isEmpty()) {
            query.startAt(queryText).endAt(queryText + "\uf8ff");
        }
        return query;
    }

    @NonNull
    @Override
    public JobTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(isComeFromShowBy ? R.layout.row_job_type_check_box : R.layout.row_job_type_radio_button, parent, false);
        return isComeFromShowBy ? new CheckBoxViewHolder(view) : new RadioButtonViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull JobTypeViewHolder viewHolder, int i, @NonNull JobType jobType) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(viewHolder.itemView.getContext().getAssets().open("1_" + jobType.getId() + ".png"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, 150, 150);
        }
        viewHolder.populateJobTypeViewHolder(jobType, drawable);
    }

    abstract class JobTypeViewHolder extends RecyclerView.ViewHolder {

        JobTypeViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void populateJobTypeViewHolder(JobType jobType, Drawable drawable1);
    }

    class CheckBoxViewHolder extends JobTypeViewHolder {

        CheckBox checkBox;

        CheckBoxViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView;
        }

        @Override
        void populateJobTypeViewHolder(JobType jobType, Drawable drawable) {
            checkBox.setHint(jobType.getJobType());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                /*if (isChecked) {
                    if (!businessesNumbers.contains(jobType.getId())) {
                        businessesNumbers.add(jobType.getId());
                    }
                } else {
                    businessesNumbers.remove(((Integer) jobType.getId()));
                }
                jobType.setSelected(isChecked);
                if (!businessesNumbers.isEmpty()) {
                    context.getIntent().putIntegerArrayListExtra("BusinessesNumbers", businessesNumbers);
                }*/
            });
            checkBox.setChecked(jobType.isSelected());
            checkBox.setCompoundDrawablesRelative(null, null, drawable, null);
        }
    }

    class RadioButtonViewHolder extends JobTypeViewHolder {

        RadioButton radioButton;

        RadioButtonViewHolder(View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView;
        }

        @Override
        void populateJobTypeViewHolder(JobType jobType, Drawable drawable) {
            radioButton.setId(View.generateViewId());
            radioButton.setHint(jobType.getJobType());
            radioButton.setOnClickListener(view -> {
                /*AddNewJob.newJob.setBusinessNumber(jobType.getId());
                context.findViewById(R.id.jobTitleButton).performClick();*/
            });
            /*int addedJobType = AddNewJob.newJob.getBusinessNumber();
            if (addedJobType != 0) {
                radioButton.setChecked(addedJobType == jobType.getId());
            }*/
            radioButton.setCompoundDrawablesRelative(null, null, drawable, null);
        }
    }
}

