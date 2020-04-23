package id.klinikrumah.internal.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.model.Lead;
import id.klinikrumah.internal.model.Project;
import id.klinikrumah.internal.util.CommonFunc;

public class LeadAdapter extends RecyclerView.Adapter<LeadAdapter.ViewHolder> {
    private List<Lead> leadList;
    private TaskListener listener;

    public LeadAdapter() {
        this.leadList = new ArrayList<>();
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

        ViewHolder(View v) {
            super(v);
            tvLeadNo = v.findViewById(R.id.tv_lead_no);
            tvProjectName = v.findViewById(R.id.tv_project_name);
            tvProjectLocation = v.findViewById(R.id.tv_project_location);
            tvClientName = v.findViewById(R.id.tv_client_name);
        }
    }
}