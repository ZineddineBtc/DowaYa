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

import com.example.dowaya.R;
import com.example.dowaya.StaticClass;
import com.example.dowaya.activities.core.MedicineDescriptionActivity;
import com.example.dowaya.daos.BookmarkDAO;

import java.util.ArrayList;

public class PostFragment extends Fragment {

    private View fragmentView;
    private Context context;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        fragmentView = inflater.inflate(R.layout.fragment_post, container, false);
        context = fragmentView.getContext();

        return fragmentView;
    }
}









