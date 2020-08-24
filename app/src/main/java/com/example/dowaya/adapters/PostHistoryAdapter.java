package com.example.dowaya.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dowaya.R;
import com.example.dowaya.daos.PostHistoryDAO;
import com.example.dowaya.models.Medicine;

import java.io.IOException;
import java.util.List;

public class PostHistoryAdapter extends RecyclerView.Adapter<PostHistoryAdapter.ViewHolder> {

    private List<Medicine> list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    public PostHistoryAdapter(Context context, List<Medicine> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.post_history_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTV.setText(list.get(position).getName());
        holder.descriptionTV.setText(list.get(position).getDescription());
        holder.addressTV.setText(list.get(position).getPostAddress());
        holder.priceTV.setText(list.get(position).getPriceRange());
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
        holder.timeTV.setText(list.get(position).getPostTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTV, descriptionTV, timeTV, addressTV, priceTV;
        ImageView medicineIV, deleteIV, toggleDeleteIV;
        boolean isShown;

        PostHistoryDAO postHistoryDAO;

        public ViewHolder(final View itemView) {
            super(itemView);
            postHistoryDAO = new PostHistoryDAO(itemView.getContext());
            nameTV = itemView.findViewById(R.id.nameTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            addressTV = itemView.findViewById(R.id.addressTV);
            priceTV = itemView.findViewById(R.id.priceTV);
            timeTV = itemView.findViewById(R.id.timeTV);
            medicineIV = itemView.findViewById(R.id.medicineIV);
            deleteIV = itemView.findViewById(R.id.deleteIV);
            toggleDeleteIV = itemView.findViewById(R.id.toggleDeleteIV);
            toggleDeleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleDeleteIV.setImageResource(isShown ?
                            R.drawable.ic_show : R.drawable.ic_hide);
                    deleteIV.setVisibility(isShown ?
                            View.GONE : View.VISIBLE);
                    isShown = !isShown;
                }
            });

            /*deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Post")
                            .setMessage("Are you sure you want to delete this post?")
                            .setPositiveButton(
                                    Html.fromHtml("<font color=\"#FF0000\"> Delete </font>"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            postHistoryDAO.deletePostHistory(list.get(getAdapterPosition()).getId());
                                            list.remove(getAdapterPosition());
                                            notifyDataSetChanged();
                                        }
                                    })
                            .setNegativeButton(
                                    Html.fromHtml("<font color=\"#1976D2\"> Cancel </font>"),
                                    null)
                            .show();
                }
            });*/

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
