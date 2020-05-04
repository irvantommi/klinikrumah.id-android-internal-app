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
import id.klinikrumah.internal.model.File;
import id.klinikrumah.internal.util.static_.CommonFunc;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<File> fileList;
    private TaskListener listener;

    public ImageAdapter() {
        this.fileList = new ArrayList<>();
        add(new File()); // minimum is 1
    }

    public void setTaskListener(TaskListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.
                item_image, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int i) {
        final File file = fileList.get(i);
        Boolean isEmpty = CommonFunc.isEmptyString(file.getPath());
        Glide.with(holder.itemView.getContext()).load(file.getPath()).into(holder.ivImg);
        holder.btnAdd.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.add(CommonFunc.generateUID(), i);
            }
        });
        holder.btnRemove.setVisibility(isEmpty ? View.GONE : View.VISIBLE);
        holder.btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.remove(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    public void add(File file) {
        fileList.add(file);
        notifyDataSetChanged();
    }

    public void remove(int pos) {
        fileList.remove(pos);
        notifyDataSetChanged();
    }

    public void addAll(@NotNull List<File> imgList) {
        this.fileList.addAll(imgList);
        // if edit, remove empty
        for (Iterator<File> iterator = this.fileList.iterator(); iterator.hasNext(); ) {
            File file = iterator.next();
            if (file == null) {
                iterator.remove();
            }
        }
        notifyDataSetChanged();
    }

    public void clear() {
        fileList.clear();
        notifyDataSetChanged();
    }

    public List<File> getFileList() {
        return fileList;
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