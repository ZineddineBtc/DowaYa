package com.example.dowaya.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.daos.RequestHistoryDAO;
import com.example.dowaya.models.Medicine;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RequestHistoryAdapter extends RecyclerView.Adapter<RequestHistoryAdapter.ViewHolder> {

    private List<Medicine> list;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context context;

    public RequestHistoryAdapter(Context context, List<Medicine> list) {
        this.mInflater = LayoutInflater.from(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.request_history_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.nameTV.setText(list.get(position).getName());
        holder.descriptionTV.setText(list.get(position).getDescription());
        holder.doseTV.setText(list.get(position).getDose());
        holder.timeTV.setText(list.get(position).getRequestTime());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTV, descriptionTV, timeTV, doseTV;
        ImageView deleteIV, toggleDeleteIV;
        boolean isShown;
        RequestHistoryDAO requestHistoryDAO;
        FirebaseFirestore database;
        SharedPreferences sharedPreferences;

        public ViewHolder(final View itemView) {
            super(itemView);
            requestHistoryDAO = new RequestHistoryDAO(itemView.getContext());
            sharedPreferences =
                    context.getSharedPreferences(StaticClass.SHARED_PREFERENCES, MODE_PRIVATE);
            database = FirebaseFirestore.getInstance();
            nameTV = itemView.findViewById(R.id.nameTV);
            descriptionTV = itemView.findViewById(R.id.descriptionTV);
            doseTV = itemView.findViewById(R.id.doseTV);
            timeTV = itemView.findViewById(R.id.timeTV);
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

            deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setTitle("Delete Request")
                            .setMessage("Are you sure you want to delete this request?")
                            .setPositiveButton(
                                    Html.fromHtml("<font color=\"#FF0000\"> Delete </font>"), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            deleteRequester(list.get(getAdapterPosition()).getId());
                                            requestHistoryDAO.deleteRequestHistory(
                                                    list.get(getAdapterPosition()).getId());
                                            list.remove(getAdapterPosition());
                                            notifyDataSetChanged();
                                        }
                                    })
                            .setNegativeButton(
                                    Html.fromHtml("<font color=\"#1976D2\"> Cancel </font>"),
                                    null)
                            .show();
                }
            });


            itemView.setOnClickListener(this);
        }
        void deleteRequester(String medicineId) {
            DocumentReference requesterReference = database.collection("users")
                    .document(sharedPreferences.getString(StaticClass.EMAIL, ""));
            DocumentReference medicinesRequests =
                    database.collection("medicines-requests")
                            .document(medicineId);
            medicinesRequests.update("requesters",
                    FieldValue.arrayRemove(requesterReference))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context,
                                    "medicine requester successfully removed!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,
                                    "Error removing requester",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
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
