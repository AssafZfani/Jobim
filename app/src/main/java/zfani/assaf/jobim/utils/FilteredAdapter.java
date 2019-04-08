package zfani.assaf.jobim.utils;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import zfani.assaf.jobim.models.Job;
import zfani.assaf.jobim.R;

public class FilteredAdapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    public static ArrayList<Job> filteredList;

    public FilteredAdapter(final ArrayList<Integer> businessesNumbers, final String location, final String firm) {

        filteredList = new ArrayList<>();

        for (Job job : Adapter.jobsList)
            if ((firm.isEmpty() || job.getFirm().equalsIgnoreCase(firm)) &&
                    (location.isEmpty() || job.getAddress().endsWith(location)))
                if (businessesNumbers == null)
                    filteredList.add(job);
                else for (int businessNumber : businessesNumbers)
                    if (job.getBusinessNumber() == businessNumber)
                        filteredList.add(job);
    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new Adapter.ViewHolder(View.inflate(parent.getContext(), R.layout.layouts_container, null));
    }

    @Override
    public void onBindViewHolder(final Adapter.ViewHolder viewHolder, int position) {

        Adapter.populateViewHolder(viewHolder, filteredList.get(position));
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
