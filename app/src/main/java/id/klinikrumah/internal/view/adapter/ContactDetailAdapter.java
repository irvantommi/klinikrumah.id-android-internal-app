package id.klinikrumah.internal.view.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.constant.S;
import id.klinikrumah.internal.util.CommonFunc;

import static id.klinikrumah.internal.util.CommonFunc.setDefaultIfEmpty;

public class ContactDetailAdapter extends RecyclerView.Adapter<ContactDetailAdapter.ViewHolder> {
    private List<String> contactList;
//    private TaskListener listener;

    public ContactDetailAdapter() {
        this.contactList = new ArrayList<>();
    }

//    public void setTaskListener(TaskListener listener) {
//        this.listener = listener;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.
                item_contact_detail, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        final String contact = contactList.get(i);
        holder.tvContact.setText(setDefaultIfEmpty(contact));
        boolean isEmpty = TextUtils.isEmpty(contact);
        holder.ivDial.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        holder.ivWA.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        holder.ivDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonFunc.callPhone(view.getContext(), contact);
            }
        });
        holder.ivWA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (contact.startsWith(S.ZERO)) {
                    String countryCodeIndonesia = S.CC_ID + contact.substring(1);
                    CommonFunc.openUrl(view.getContext(), String.format(S.WA_LINK, countryCodeIndonesia));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void add() {
        contactList.add(S.DASH);
        notifyDataSetChanged();
    }

    public void addAll(List<String> contactList) {
        this.contactList.addAll(contactList);
        notifyDataSetChanged();
    }

    public void clear() {
        contactList.clear();
        notifyDataSetChanged();
    }

    public List<String> getContactList() {
        return contactList;
    }

    //    public interface TaskListener {
//        void add();
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvContact;
        ImageView ivDial;
        ImageView ivWA;

        ViewHolder(View v) {
            super(v);
            tvContact = v.findViewById(R.id.tv_contact);
            ivDial = v.findViewById(R.id.iv_dial);
            ivWA = v.findViewById(R.id.iv_wa);
        }
    }
}