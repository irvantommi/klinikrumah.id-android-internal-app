package id.klinikrumah.internal.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.model.Project;
import id.klinikrumah.internal.util.static_.CommonFunc;
import id.klinikrumah.internal.util.enum_.ErrorType;
import id.klinikrumah.internal.view.activity.LeadListActivity;

public class LeadAdapter extends RecyclerView.Adapter<LeadAdapter.ViewHolder> implements Filterable {
    private TaskListener listener;
    private Context ctx;
    private List<Lead> oriList = new ArrayList<>();
    private List<Lead> filteredList = new ArrayList<>();

    public LeadAdapter(Context context) {
        ctx = context;
    }

    public void setTaskListener(TaskListener taskListener) {
        listener = taskListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_lead, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final Lead lead = filteredList.get(i);
        Project project = lead.getProject();
        holder.tvLeadNo.setText(String.format("L%s", i + 1));
        holder.tvProjectName.setText(CommonFunc.setDefaultIfEmpty(project.getName()));
        holder.tvProjectLocation.setText(CommonFunc.setDefaultIfEmpty(project.getLocation()));
        holder.tvClientName.setText(CommonFunc.setDefaultIfEmpty(lead.getClient().getName()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectLead(lead);
            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    /**
     * https://stackoverflow.com/questions/14663725/list-view-filter-android
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (filteredList.isEmpty()) {
                    if (((LeadListActivity) ctx).rvLeadList.getVisibility() == View.VISIBLE) {
                        ((LeadListActivity) ctx).setError(ErrorType.NOT_FOUND);
                    }
                } else {
                    ((LeadListActivity) ctx).hideError();
                    notifyDataSetChanged();
                }
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                filteredList.clear();
                // perform your search here using the searchConstraint String.
                String query = constraint.toString().toLowerCase();
                for (int i = 0; i < oriList.size(); i++) {
                    Lead lead = oriList.get(i);
                    Project project = lead.getProject();
                    if (project.getName().toLowerCase().contains(query)) {
                        filteredList.add(lead);
                    }
                    if (project.getLocation().toLowerCase().contains(query)) {
                        filteredList.add(lead);
                    }
                    if (lead.getClient().getName().toLowerCase().contains(query)) {
                        filteredList.add(lead);
                    }
                }
                results.count = filteredList.size();
                results.values = filteredList;

                return results;
            }
        };
    }

    public List<Lead> getOriList() {
        return oriList;
    }

    public void clear() {
        filteredList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Lead> leadList) {
        oriList.addAll(leadList);
        filteredList.addAll(leadList);
        notifyDataSetChanged();
    }

    public void showAll() {
        filteredList.clear();
        filteredList.addAll(oriList);
        notifyDataSetChanged();
    }

    public interface TaskListener {
        void selectLead(Lead lead);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLeadNo;
        TextView tvProjectName;
        TextView tvProjectLocation;
        TextView tvClientName;

        ViewHolder(View v) {
            super(v);
            tvLeadNo = v.findViewById(R.id.tv_lead_no);
            tvProjectName = v.findViewById(R.id.tv_project_name);
            tvProjectLocation = v.findViewById(R.id.tv_project_location);
            tvClientName = v.findViewById(R.id.tv_client_name);
        }
    }
}