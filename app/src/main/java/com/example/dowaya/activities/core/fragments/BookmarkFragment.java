package com.example.dowaya.activities.core.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.core.MedicineDescriptionActivity;
import com.example.dowaya.daos.BookmarkDAO;
import com.example.dowaya.models.Medicine;

import java.util.ArrayList;

public class BookmarkFragment extends Fragment {

    private View fragmentView;
    private Context context;
    private BookmarkDAO bookmarkDAO;
    private ListView bookmarkLV;
    private ArrayAdapter adapter;
    private ArrayList<String> bookmarkList;
    private TextView emptyListTV;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_bookmark, container, false);
        context = fragmentView.getContext();
        emptyListTV = fragmentView.findViewById(R.id.emptyListTV);
        bookmarkLV = fragmentView.findViewById(R.id.bookmarkLV);
        bookmarkDAO = new BookmarkDAO(context);
        bookmarkList = bookmarkDAO.getAllNames();
        if (!bookmarkList.isEmpty()){
            setListView();
        }else{
            emptyListTV.setVisibility(View.VISIBLE);
            bookmarkLV.setVisibility(View.GONE);
        }

        return fragmentView;
    }
    public void setListView(){
        emptyListTV.setVisibility(View.GONE);
        bookmarkLV.setVisibility(View.VISIBLE);
        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, bookmarkList);
        bookmarkLV.setAdapter(adapter);
        bookmarkLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(context, MedicineDescriptionActivity.class)
                .putExtra(StaticClass.MEDICINE_ID, position+1));
            }
        });
        bookmarkLV.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showAlert(position+1);
                return true;
            }
        });
    }
    private void showAlert(final int id){
        new AlertDialog.Builder(context)
                .setTitle("Remove Medicine")
                .setMessage("Are you sure you want to remove this medicine from bookmark?")
                .setPositiveButton(
                        Html.fromHtml("<font color=\"#FF0000\"> Remove </font>")
                        , new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                bookmarkDAO.deleteMedicine(id);
                                bookmarkList.remove(id-1);
                                adapter.notifyDataSetChanged();
                                if(bookmarkList.isEmpty()){
                                    emptyListTV.setVisibility(View.VISIBLE);
                                    bookmarkLV.setVisibility(View.GONE);
                                }
                            }
                        })
                .setNegativeButton(
                        Html.fromHtml("<font color=\"#1976D2\"> Cancel </font>"),
                        null)
                .show();
    }
}









