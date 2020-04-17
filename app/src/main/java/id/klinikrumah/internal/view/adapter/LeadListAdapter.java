package id.klinikrumah.internal.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.model.Project;
import id.klinikrumah.internal.util.CommonFunc;

public class LeadListAdapter extends RecyclerView.Adapter<LeadListAdapter.ViewHolder> {
    private List<Lead> leadList;
    private TaskListener listener;

    public LeadListAdapter(List<Lead> cityList) {
        this.leadList = cityList;
    }

    public void setTaskListener(TaskListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.item_lead, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        final Lead lead = leadList.get(i);
        Project project = lead.getProject();
        holder.tvLeadNo.setText(String.format("L%s", i + 1));
        holder.tvProjectName.setText(CommonFunc.setDashIfEmpty(project.getName()));
        holder.tvProjectLocation.setText(CommonFunc.setDashIfEmpty(project.getLocation()));
        holder.tvClientName.setText(CommonFunc.setDashIfEmpty(lead.getClient().getName()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.selectLead(lead);
            }
        });
    }

    @Override
    public int getItemCount() {
        return leadList.size();
    }

    public void clear() {
        leadList.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Lead> leadList) {
        this.leadList.addAll(leadList);
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

        ViewHolder(View view) {
            super(view);
            tvLeadNo = view.findViewById(R.id.tv_lead_no);
            tvProjectName = view.findViewById(R.id.tv_project_name);
            tvProjectLocation = view.findViewById(R.id.tv_project_location);
            tvClientName = view.findViewById(R.id.tv_client_name);
        }
    }
}