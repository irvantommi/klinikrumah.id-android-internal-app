package id.klinikrumah.internal.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import id.klinikrumah.internal.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    private List<String> contactList;
//    private TaskListener listener;

    public ContactAdapter() {
        this.contactList = new ArrayList<>();
        add(); // minimum is 1
    }

//    public void setTaskListener(TaskListener listener) {
//        this.listener = listener;
//    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.
                item_contact, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        final String contact = contactList.get(i);
        holder.tilContact.setHint(String.format("Kontak %s", i+1));
        holder.etContact.setText(contact);
//        holder.etContact.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean b) {
//                contactList.set(i, contact);
//            }
//        });
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        if (i == 0) {
            holder.btnRemove.setVisibility(View.GONE);
        }
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    private void add() {
        contactList.add("");
        notifyDataSetChanged();
    }

    private void remove(int pos) {
        contactList.remove(pos);
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
        TextInputLayout tilContact;
        TextInputEditText etContact;
        Button btnAdd;
        Button btnRemove;

        ViewHolder(View v) {
            super(v);
            tilContact = v.findViewById(R.id.til_contact);
            etContact = v.findViewById(R.id.et_contact);
            btnAdd = v.findViewById(R.id.btn_add);
            btnRemove = v.findViewById(R.id.btn_remove);
        }
    }
}