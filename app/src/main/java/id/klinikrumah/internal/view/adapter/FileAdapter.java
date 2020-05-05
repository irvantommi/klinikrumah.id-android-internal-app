package id.klinikrumah.internal.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import id.klinikrumah.internal.R;
import id.klinikrumah.internal.model.KRFile;
import id.klinikrumah.internal.util.static_.CommonFunc;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {
    private List<KRFile> krFileList;
    private TaskListener listener;
    private boolean isDetailPage;

    public FileAdapter(boolean isDetailPage) {
        this.isDetailPage = isDetailPage;
        krFileList = new ArrayList<>();
        if (!this.isDetailPage) {
            krFileList.add(new KRFile()); // minimum is 1
        }
    }

    public void setTaskListener(TaskListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.
                item_file, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        final KRFile krFile = krFileList.get(i);
        String path = krFile.getPath();
        Boolean isEmpty = CommonFunc.isEmptyString(path);
        Glide.with(holder.itemView.getContext()).load(path).into(holder.ivImg);
        holder.btnAdd.setVisibility(isDetailPage ? View.GONE : isEmpty ? View.VISIBLE : View.GONE);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.add(CommonFunc.generateUID(), i);
            }
        });
        holder.btnRemove.setVisibility(isDetailPage ? View.GONE : isEmpty ? View.GONE : View.VISIBLE);
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.remove(i);
            }
        });
        holder.ivImg.setOnClickListener(isEmpty ? new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 05/05/20 fullScreenActivity with ImagePager & download onLongClickable
            }
        } : null);
    }

    @Override
    public int getItemCount() {
        return krFileList.size();
    }

    public void add(KRFile krFile) {
        krFileList.add(krFile);
        moveEmptyToLastPos();
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        krFileList.remove(pos);
        notifyDataSetChanged();
    }

    public void addAll(@NotNull List<KRFile> krFileList) {
        this.krFileList.addAll(krFileList);
        moveEmptyToLastPos();
        notifyDataSetChanged();
    }

    public void clear() {
        krFileList.clear();
        notifyDataSetChanged();
    }

    private void moveEmptyToLastPos() {
        // remove empty on 1st position
        for (Iterator<KRFile> iterator = krFileList.iterator(); iterator.hasNext(); ) {
            KRFile KRFile = iterator.next();
            if (KRFile.getId() == null) {
                iterator.remove();
            }
        }
        krFileList.add(new KRFile()); // put empty to the last position
    }

    public List<KRFile> getKrFileList() {
        return krFileList;
    }

    public interface TaskListener {
        void add(String id, int pos);

        void remove(int pos);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImg;
        Button btnAdd;
        Button btnRemove;

        ViewHolder(View v) {
            super(v);
            ivImg = v.findViewById(R.id.iv_img);
            btnAdd = v.findViewById(R.id.btn_add);
            btnRemove = v.findViewById(R.id.btn_remove);
        }
    }
}