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
import zfani.assaf.jobim.interfaces.OnItemSelectedListener;
import zfani.assaf.jobim.models.JobType;

public class JobTypesAdapter extends FirebaseRecyclerAdapter<JobType, JobTypesAdapter.JobTypeViewHolder> {

    private boolean isComeFromShowBy;
    private int selectedItem = -1;
    private OnItemSelectedListener onItemSelectedListener;

    public JobTypesAdapter(boolean isComeFromShowBy, String queryText) {
        super(new FirebaseRecyclerOptions.Builder<JobType>().setQuery(getQuery(queryText), JobType.class).build());
        this.isComeFromShowBy = isComeFromShowBy;
    }

    @NonNull
    private static Query getQuery(String queryText) {
        Query query = FirebaseDatabase.getInstance().getReference().child("jobs_types");
        if (queryText != null && !queryText.isEmpty()) {
            query = query.orderByChild("jobType").startAt(queryText).endAt(queryText + "\uf8ff");
        }
        return query;
    }

    @NonNull
    @Override
    public JobTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(isComeFromShowBy ? R.layout.row_list_check_box : R.layout.row_list_radio_button, parent, false);
        return isComeFromShowBy ? new CheckBoxViewHolder(view) : new RadioButtonViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull JobTypeViewHolder viewHolder, int position, @NonNull JobType jobType) {
        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(viewHolder.itemView.getContext().getAssets().open("1_" + jobType.getId() + ".png"), null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (drawable != null) {
            drawable.setBounds(0, 0, 150, 150);
        }
        viewHolder.populateJobTypeViewHolder(jobType, drawable, position);
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    abstract class JobTypeViewHolder extends RecyclerView.ViewHolder {

        JobTypeViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        abstract void populateJobTypeViewHolder(JobType jobType, Drawable drawable, int position);
    }

    class CheckBoxViewHolder extends JobTypeViewHolder {

        CheckBox checkBox;

        CheckBoxViewHolder(View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView;
        }

        @Override
        void populateJobTypeViewHolder(JobType jobType, Drawable drawable, int position) {
            checkBox.setHint(jobType.getJobType());
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                jobType.setSelected(isChecked);
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(getItem(selectedItem).getJobType());
                }
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
        void populateJobTypeViewHolder(JobType jobType, Drawable drawable, int position) {
            radioButton.setHint(jobType.getJobType());
            radioButton.setChecked(position == selectedItem);
            radioButton.setOnClickListener(view -> {
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(getItem(selectedItem).getJobType());
                }
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

