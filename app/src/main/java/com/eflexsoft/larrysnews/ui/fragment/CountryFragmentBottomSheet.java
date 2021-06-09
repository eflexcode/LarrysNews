package com.eflexsoft.larrysnews.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eflexsoft.larrysnews.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;


public class CountryFragmentBottomSheet extends BottomSheetDialogFragment {

     public CountryFragmentBottomSheet() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_country, container, false);
    }
}