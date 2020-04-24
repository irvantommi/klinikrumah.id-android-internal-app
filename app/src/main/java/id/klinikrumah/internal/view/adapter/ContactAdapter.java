package id.klinikrumah.internal.view.adapter;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
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
        // pass MyCustomEditTextListener to viewholder in onCreateViewHolder
        // so that we don't have to do this expensive allocation in onBindViewHolder
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.
                item_contact, viewGroup, false), new CustomEditTextListener());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        final String contact = contactList.get(i);
        holder.customEditTextListener.updatePosition(i);
        holder.tilContact.setHint(String.format("Kontak %s", i+1));
        holder.etContact.setText(contact);
        boolean isFirstItem = i == 0;
        if (!isFirstItem && contactList.size() == i+1) holder.etContact.requestFocus(); // last contact but not only one, getFocus
        holder.btnAdd.setVisibility(isFirstItem ? View.VISIBLE : View.GONE);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        holder.btnRemove.setVisibility(isFirstItem ? View.GONE : View.VISIBLE);
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

    public void addAll(@NotNull List<String> contactList) {
        this.contactList.addAll(contactList);
        // if edit, remove empty
        for (Iterator<String> iterator = this.contactList.iterator(); iterator.hasNext(); ) {
            String value = iterator.next();
            if (TextUtils.isEmpty(value)) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    public void clear() {
        contactList.clear();
        notifyDataSetChanged();
    }

    public List<String> getContactList() {
        return contactList;
    }

    // we make TextWatcher to be aware of the position it currently works with
    // this way, once a new item is attached in onBindViewHolder, it will
    // update current position MyCustomEditTextListener, reference to which is kept by ViewHolder
    private class CustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(@NotNull CharSequence charSequence, int i, int i2, int i3) {
            contactList.set(position, charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }

    //    public interface TaskListener {
//        void add();
//    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextInputLayout tilContact;
        TextInputEditText etContact;
        Button btnAdd;
        Button btnRemove;
        CustomEditTextListener customEditTextListener;

        ViewHolder(View v, CustomEditTextListener customEditTextListener) {
            super(v);
            tilContact = v.findViewById(R.id.til_contact);
            etContact = v.findViewById(R.id.et_contact);
            btnAdd = v.findViewById(R.id.btn_add);
            btnRemove = v.findViewById(R.id.btn_remove);

            this.customEditTextListener = customEditTextListener;
            etContact.addTextChangedListener(this.customEditTextListener);
        }
    }
}