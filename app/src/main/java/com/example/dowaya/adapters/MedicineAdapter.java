package com.example.dowaya.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.core.MedicineDescriptionActivity;
import com.example.dowaya.daos.MedicineHistoryDAO;
import com.example.dowaya.daos.StoreHistoryDAO;
import com.example.dowaya.models.Medicine;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MedicineAdapter extends RecyclerView.Adapter<MedicineAdapter.ViewHolder> {

    private List<Medicine> medicineList, copyList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    public MedicineAdapter(Context context, List<Medicine> medicineList) {
        this.mInflater = LayoutInflater.from(context);
        this.medicineList = medicineList;
        copyList = new ArrayList<>(medicineList);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.medicine_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTV.setText(medicineList.get(position).getName());
        holder.priceRangeTV.setText(medicineList.get(position).getPriceRange());
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTV, priceRangeTV;
        MedicineHistoryDAO medicineHistoryDAO;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            priceRangeTV = itemView.findViewById(R.id.priceRangeTV);
            medicineHistoryDAO = new MedicineHistoryDAO(itemView.getContext());
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());

            medicineHistoryDAO.insertMedicineHistory(
                    nameTV.getText().toString(),
                    new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).
                            format(Calendar.getInstance().getTime()));

            itemView.getContext().startActivity(
                    new Intent(itemView.getContext(), MedicineDescriptionActivity.class)
                    .putExtra(StaticClass.MEDICINE_ID, getAdapterPosition()+1)
            );
        }
    }

    Medicine getItem(int id) {
        return medicineList.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void filter(String queryText) {
        medicineList.clear();
        if(queryText.isEmpty()) {
            medicineList.addAll(copyList);
        }else{
            for(Medicine medicine: copyList) {
                if(medicine.getName().toLowerCase().contains(queryText.toLowerCase())) {
                    medicineList.add(medicine);
                }
            }
        }
        notifyDataSetChanged();
    }
}
