package zfani.assaf.jobim.utils;

import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import zfani.assaf.jobim.R;
import zfani.assaf.jobim.adapters.JobsAdapter;
import zfani.assaf.jobim.models.Job;

public class FilteredAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {

    public static ArrayList<Job> filteredList;

    public FilteredAdapter(final ArrayList<Integer> businessesNumbers, final String location, final String firm) {

        filteredList = new ArrayList<>();

        for (Job job : JobsAdapter.jobsList)
            if ((firm.isEmpty() || job.getFirm().equalsIgnoreCase(firm)) &&
                    (location.isEmpty() || job.getAddress().endsWith(location)))
                if (businessesNumbers == null)
                    filteredList.add(job);
                else for (int businessNumber : businessesNumbers)
                    if (job.getBusinessNumber() == businessNumber)
                        filteredList.add(job);
    }

    @NonNull
    @Override
    public JobsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new JobsAdapter.ViewHolder(View.inflate(parent.getContext(), R.layout.layouts_container, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final JobsAdapter.ViewHolder viewHolder, int position) {

        //JobsAdapter.populateViewHolder(viewHolder, filteredList.get(position));
    }

    @Override
    public int getItemCount() {

        return filteredList == null ? 0 : filteredList.size();
    }

    public void remove(int position) {

        filteredList.remove(position);

        notifyItemRemoved(position);
    }
}
