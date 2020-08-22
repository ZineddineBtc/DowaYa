package com.example.dowaya.adapters;

import android.content.Context;
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
import com.example.dowaya.daos.RequestHistoryDAO;
import com.example.dowaya.models.Medicine;

import java.io.IOException;
import java.util.List;

public class RequestsHistoryAdapter extends RecyclerView.Adapter<RequestsHistoryAdapter.ViewHolder> {

    private List<Medicine> list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    public RequestsHistoryAdapter(Context context, List<Medicine> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.requests_history_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTV.setText(list.get(position).getName());
        holder.descriptionTV.setText(list.get(position).getDescription());
        String photoUri = list.get(position).getPhoto();
        if(photoUri != null){
            Bitmap imageBitmap = null;
            try {
                imageBitmap = MediaStore.Images.Media.getBitmap(
                        context.getContentResolver(), Uri.parse(photoUri));
            } catch (IOException e) {
                Toast.makeText(context, "IO Exception",
                        Toast.LENGTH_LONG).show();
            }
            holder.medicineIV.setImageBitmap(imageBitmap);
        }else{
            holder.medicineIV.setVisibility(View.GONE);
        }
        holder.timeTV.setText(list.get(position).getRequestTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTV, descriptionTV, timeTV;
        ImageView medicineIV;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            medicineIV = itemView.findViewById(R.id.medicineIV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Medicine getItem(int id) {
        return list.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
