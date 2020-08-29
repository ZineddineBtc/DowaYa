package com.example.dowaya.activities.core.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.dowaya.R;

public class OrientationFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_orientation, container, false);
        TextView textView = root.findViewById(R.id.text_share);
        textView.setText("Share");
        return root;
    }
}