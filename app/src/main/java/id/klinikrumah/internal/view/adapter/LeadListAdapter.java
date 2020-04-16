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
        holder.tvCity.setText(lead.getName());
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvCity;

        public ViewHolder(View view) {
            super(view);
//            tvCity = view.findViewById(R.id.tv_city);
        }
    }
}