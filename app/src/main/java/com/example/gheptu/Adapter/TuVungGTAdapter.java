package com.example.gheptu.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gheptu.Database.SQLiteConnect;
import com.example.gheptu.Model.TuVungGhepTu;
import com.example.gheptu.QuanLiTuActivity;
import com.example.gheptu.activities.SuaTuActivity;
import com.example.minivocabularywithp2l.R;


import java.util.ArrayList;

public class TuVungGTAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    ArrayList<TuVungGhepTu> listTu, listTuBackup, listTuFilter;
    public TuVungGTAdapter(Activity context, int resource, ArrayList<TuVungGhepTu> listTu)
    {
        super(context, resource, listTu);
        this.context = context;
        this.resource = resource;
        this.listTu = this.listTuBackup = listTu;
    }
    public int getCount()
    {
        return listTu.size();
    }

    public ArrayList<TuVungGhepTu> getListTu() {
        return listTu;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View customView = inflater.inflate(resource, null);

        TextView tvMaTu = customView.findViewById(R.id.tvMaTu);
        TextView tvTiengAnh = customView.findViewById(R.id.tvTiengAnh);
        TextView tvTiengViet = customView.findViewById(R.id.tvTiengViet);
        ImageView imvSua = customView.findViewById(R.id.imvSua);
        ImageView imvXoa = customView.findViewById(R.id.imvXoa);

        TuVungGhepTu tu = listTu.get(position);

        tvMaTu.setText(tu.getMaTu());
        tvTiengAnh.setText(tu.getTiengAnh());
        tvTiengViet.setText(tu.getTiengViet());

        imvSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();
                data.putSerializable("tu", tu);
                Intent suaTuIntent = new Intent(context, SuaTuActivity.class);
                suaTuIntent.putExtras(data);
                context.startActivityForResult(suaTuIntent, 123);




            }
        });
        imvXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Xoa" + tu.getMaTu() + "-" + tu.getTiengAnh() + " ", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Xóa từ");
                builder.setMessage("Bạn thực sự muốn xóa từ " + tu.getMaTu() + "-" + tu.getTiengAnh() + "?");
                builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            String query = "DELETE FROM tuvungGT_v2 WHERE id = '" + tu.getId() + "'";
                            SQLiteConnect sqlitesConnect = new SQLiteConnect(context, context.getString(R.string.db_name),
                                    null, SQLiteConnect.DATABASE_VERSION);
                            sqlitesConnect.queryData(query);
                            Toast.makeText(context, "Đã xóa " + tu.getMaTu() + "-" + tu.getTiengAnh(), Toast.LENGTH_SHORT).show();
                            ((QuanLiTuActivity) context).loadDataTuVung();
                            dialog.dismiss();

                        } catch (Exception e){
                            Log.d("Loi DELETE CSDL", e.toString());
                            Toast.makeText(context, "Loi DELETE CSDL", Toast.LENGTH_SHORT);
                        }
                    }
                });
                builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.create().show();


            }
        });


        return customView;

    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String query = constraint.toString().trim().toLowerCase();
                if (query.length() < 1){
                    listTuFilter = listTuBackup;
                } else {
                    listTuFilter = new ArrayList<>();
                    for (TuVungGhepTu tu : listTuBackup){
                        if (tu.getTiengAnh().toLowerCase().contains(query)){
                            listTuFilter.add(tu);
                        }

                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = listTuFilter;







                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                listTu = (ArrayList<TuVungGhepTu>) results.values;
                notifyDataSetChanged();

            }
        };
    }
}
