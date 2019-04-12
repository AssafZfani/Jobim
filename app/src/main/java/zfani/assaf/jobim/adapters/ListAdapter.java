package zfani.assaf.jobim.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import zfani.assaf.jobim.R;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> implements Filterable {

    private List<String> itemList, filteredList;
    //private boolean isComeFromShowBy;


    public ListAdapter(List<String> itemList, boolean isComeFromShowBy) {
        this.itemList = itemList;
        this.filteredList = itemList;
        //this.isComeFromShowBy = isComeFromShowBy;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.row_list_radio_button, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListViewHolder holder, int position) {
        holder.radioButton.setId(View.generateViewId());
        holder.radioButton.setHint(itemList.get(position));
        holder.radioButton.setOnClickListener(view -> {
            /*if (isComeFromShowBy) {
                context.getIntent().putExtra("Firm", holder.radioButton.getHint());
            } else {
                AddNewJob.newJob.setAddress(holder.radioButton.getHint().toString());
                AddNewJob.newJob.setDistance(context);
                context.findViewById(R.id.pictureButton).performClick();
            }*/
        });
        /*if (isComeFromShowBy) {
            String firm = context.getIntent().getStringExtra("Firm");
            holder.radioButton.setChecked(firm != null && firm.equalsIgnoreCase(holder.radioButton.getHint().toString()));
        } else if (itemList.size() == 1 && itemList.get(0).equalsIgnoreCase(currentAddress)) {
            holder.radioButton.setChecked(true);
            AddNewJob.newJob.setAddress(currentAddress);
            AddNewJob.newJob.setDistance(context);
        }*/
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String query = charSequence.toString();
                if (query.isEmpty()) {
                    filteredList = itemList;
                } else {
                    List<String> tempList = new ArrayList<>();
                    for (String item : itemList) {
                        if (item.toLowerCase().contains(query.toLowerCase())) {
                            tempList.add(item);
                        }
                    }
                    filteredList = tempList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredList = (ArrayList<String>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class ListViewHolder extends RecyclerView.ViewHolder {

        RadioButton radioButton;

        ListViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = (RadioButton) itemView;
        }
    }
}
