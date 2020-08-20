package com.example.dowaya.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.core.MedicineDescriptionActivity;
import com.example.dowaya.activities.core.StoreDescriptionActivity;
import com.example.dowaya.models.Medicine;
import com.example.dowaya.models.Store;

import java.util.ArrayList;
import java.util.List;

public class StoreAdapter extends RecyclerView.Adapter<StoreAdapter.ViewHolder> {

    private List<Store> storeList, copyList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    public StoreAdapter(Context context, List<Store> storeList) {
        this.mInflater = LayoutInflater.from(context);
        this.storeList = storeList;
        copyList = new ArrayList<>(storeList);
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.store_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTV.setText(storeList.get(position).getName());
        holder.cityTV.setText(storeList.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTV, cityTV;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTV = itemView.findViewById(R.id.nameTV);
            cityTV = itemView.findViewById(R.id.cityTV);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(view, getAdapterPosition());
            itemView.getContext().startActivity(
                    new Intent(itemView.getContext(), StoreDescriptionActivity.class)
                    .putExtra(StaticClass.STORE_ID, getAdapterPosition())
            );
        }
    }

    Store getItem(int id) {
        return storeList.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;

    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void filter(String queryText) {
        storeList.clear();
        if(queryText.isEmpty()) {
            storeList.addAll(copyList);
        }else{
            for(Store store: copyList) {
                if(store.getName().toLowerCase().contains(queryText.toLowerCase())
                || store.getCity().toLowerCase().contains(queryText.toLowerCase())) {
                    storeList.add(store);
                }
            }
        }
        notifyDataSetChanged();
    }
}
