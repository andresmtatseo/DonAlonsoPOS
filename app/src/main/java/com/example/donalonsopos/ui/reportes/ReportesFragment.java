package com.example.donalonsopos.ui.reportes;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.donalonsopos.R;


public class ReportesFragment extends Fragment {



    public ReportesFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reportes, container, false);



        return view;
    }
}