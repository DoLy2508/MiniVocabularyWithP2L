package com.example.NhiemVu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.example.minivocabularywithp2l.R;

import java.util.List;

public class NhiemVuAdapter extends ArrayAdapter<NhiemVu> {

    private Context context;
    private int resource;
    private List<NhiemVu> objects;
    private OnItemActionListener listener;

    // Interface để callback sự kiện về Activity
    public interface OnItemActionListener {
        void onEdit(int position);
        void onDelete(int position);
    }

    public NhiemVuAdapter(@NonNull Context context, int resource, @NonNull List<NhiemVu> objects, OnItemActionListener listener) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
        this.listener = listener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        NhiemVu item = objects.get(position);

        TextView tvTitle = convertView.findViewById(R.id.tv_title);
        TextView tvDescription = convertView.findViewById(R.id.tv_description);
        CheckBox cbCompleted = convertView.findViewById(R.id.cb_completed);
        ImageButton btnEdit = convertView.findViewById(R.id.btn_edit);
        ImageButton btnDelete = convertView.findViewById(R.id.btn_delete);

        tvTitle.setText(item.getTitle());
        tvDescription.setText(item.getDescription());
        cbCompleted.setChecked(item.isCompleted());

        // Xử lý sự kiện click nút Sửa
        btnEdit.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(position);
            }
        });

        // Xử lý sự kiện click nút Xóa
        btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDelete(position);
            }
        });
        
        // Cập nhật trạng thái hoàn thành khi check vào checkbox
        cbCompleted.setOnCheckedChangeListener((buttonView, isChecked) -> {
             item.setCompleted(isChecked);
        });


        return convertView;
    }
}