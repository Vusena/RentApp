package com.mysasse.rentapp.ui.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysasse.rentapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CardView myHousesCardView = view.findViewById(R.id.my_houses_card_view);
        CardView bookingRequestsCardView = view.findViewById(R.id.booking_request_card_view);

        myHousesCardView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.myHousesFragment));
        bookingRequestsCardView.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.requestsFragment));
    }
}
