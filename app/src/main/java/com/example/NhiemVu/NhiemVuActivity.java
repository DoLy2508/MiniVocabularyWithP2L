package com.example.NhiemVu;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gheptu.Database.SQLiteConnect;

import com.example.minivocabularywithp2l.MainActivity;
import com.example.minivocabularywithp2l.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NhiemVuActivity extends AppCompatActivity {

    private ImageButton imbtnHome;
    private FloatingActionButton fabAddTask;
    private ListView lvNhiemVu;
    private ArrayList<NhiemVu> nhiemVuList;
    private NhiemVuAdapter adapter;
    private SQLiteConnect databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trang_nhiemvuhoctap);


        databaseHelper = new SQLiteConnect(this);


        initViews();
        

        loadDataFromDB();


        adapter = new NhiemVuAdapter(this, R.layout.item_nhiemvu, nhiemVuList, new NhiemVuAdapter.OnItemActionListener() {
            @Override
            public void onEdit(int position) {
                showEditDialog(position);
            }

            @Override
            public void onDelete(int position) {
                showDeleteDialog(position);
            }
        });
        lvNhiemVu.setAdapter(adapter);


        imbtnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }


        });

        fabAddTask.setOnClickListener(v -> {
            showAddDialog();
        });
    }

    private void initViews() {
        imbtnHome = findViewById(R.id.imbtnHome);
        fabAddTask = findViewById(R.id.fab_add_task);
        lvNhiemVu = findViewById(R.id.lv_nhiemvu);
    }

    private void loadDataFromDB() {
        nhiemVuList = databaseHelper.getAllTasks();

        if (nhiemVuList.isEmpty()) {
             databaseHelper.addTask(new NhiemVu("Học 10 từ về Food ", " Từ 'grape' hay quên", true));
             databaseHelper.addTask(new NhiemVu("Học 5 từ về Animals ", "Chưa học", false));
             nhiemVuList = databaseHelper.getAllTasks(); // Load lại sau khi thêm
        }
    }


    private void refreshData() {
        nhiemVuList.clear();
        nhiemVuList.addAll(databaseHelper.getAllTasks());
        adapter.notifyDataSetChanged();
    }

    private void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.them_nhiemvu, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        EditText etTitle = view.findViewById(R.id.et_add_title);
        EditText etDescription = view.findViewById(R.id.et_add_description);
        Button btnCancel = view.findViewById(R.id.btn_add_cancel);
        Button btnSave = view.findViewById(R.id.btn_add_save);

        etTitle.setText("");
        etDescription.setText("");

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String desc = etDescription.getText().toString().trim();
            
            if (desc.isEmpty()) {
                desc = "Chưa có ghi chú";
            }

            if (!title.isEmpty()) {
                NhiemVu newTask = new NhiemVu(title, desc, false);

                boolean success = databaseHelper.addTask(newTask);
                if (success) {
                    refreshData();
                    Toast.makeText(this, "Đã thêm nhiệm vụ mới", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                     Toast.makeText(this, "Lỗi khi thêm nhiệm vụ", Toast.LENGTH_SHORT).show();
                }
            } else {
                etTitle.setError("Vui lòng nhập tiêu đề");
            }
        });

        dialog.show();
    }


    private void showEditDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.edit_nhiemvu, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        EditText etTitle = view.findViewById(R.id.et_edit_title);
        EditText etDescription = view.findViewById(R.id.et_edit_description);
        Button btnCancel = view.findViewById(R.id.btn_edit_cancel);
        Button btnSave = view.findViewById(R.id.btn_edit_save);

        NhiemVu currentItem = nhiemVuList.get(position);
        etTitle.setText(currentItem.getTitle());
        etDescription.setText(currentItem.getDescription());
        etTitle.setSelection(etTitle.getText().length());

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnSave.setOnClickListener(v -> {
            String newTitle = etTitle.getText().toString().trim();
            String newDesc = etDescription.getText().toString().trim();

            if (!newTitle.isEmpty()) {
                currentItem.setTitle(newTitle);
                currentItem.setDescription(newDesc);
                

                boolean success = databaseHelper.updateTask(currentItem);
                if (success) {
                    refreshData(); // Load lại từ DB để đồng bộ
                    Toast.makeText(this, "Đã cập nhật nhiệm vụ", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                     Toast.makeText(this, "Lỗi khi cập nhật", Toast.LENGTH_SHORT).show();
                }
            } else {
                etTitle.setError("Vui lòng nhập tiêu đề");
            }
        });

        dialog.show();
    }


    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_nhiemvu, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }

        Button btnCancel = view.findViewById(R.id.btn_delete_cancel);
        Button btnConfirm = view.findViewById(R.id.btn_delete_confirm);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            NhiemVu taskToDelete = nhiemVuList.get(position);

            boolean success = databaseHelper.deleteTask(taskToDelete.getId());
            if (success) {
                 refreshData();
                 Toast.makeText(this, "Đã xóa nhiệm vụ", Toast.LENGTH_SHORT).show();
                 dialog.dismiss();
            } else {
                 Toast.makeText(this, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
}