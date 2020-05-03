package com.mysasse.rentapp.ui.fragments.admin.properties;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mysasse.rentapp.R;
import com.mysasse.rentapp.data.DatabaseHandler;
import com.mysasse.rentapp.data.models.House;

import java.util.ArrayList;
import java.util.List;

public class PropertiesFragment extends Fragment {

    public PropertiesFragment() {

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.properties_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Create Database Handler instance
        DatabaseHandler dbh = new DatabaseHandler(getActivity());
        //Register necessary views
        RecyclerView housesRecyclerView = view.findViewById(R.id.houses_recycler_view);
        housesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        housesRecyclerView.setHasFixedSize(true);
        FloatingActionButton addHouseFab = view.findViewById(R.id.add_house_fab);

        //Houses list
        List<House> houses = new ArrayList<>();

        //Get all the houses
        String sql = "SELECT name, user_id, location, cost_per_room, rooms_count, occupied_rooms_count FROM houses";

        Cursor cursor = dbh.executeQuery(sql);

        //Check whether the is data
        if (!cursor.isAfterLast()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String location = cursor.getString(cursor.getColumnIndex("location"));
                int roomsCount = cursor.getInt(cursor.getColumnIndex("rooms_count"));
                int occupiedRoomsCount = cursor.getInt(cursor.getColumnIndex("occupied_rooms_count"));
                double costPerRoom = cursor.getDouble(cursor.getColumnIndex("cost_per_room"));
                String createdAt = cursor.getString(cursor.getColumnIndex("created_at"));

                House house = new House(name, costPerRoom, location, roomsCount, occupiedRoomsCount, userId);
                house.setId(id);
                house.setCreatedAt(createdAt);
                houses.add(house);

            } while (cursor.moveToNext());
        }
        cursor.moveToFirst();
        HousesAdapter adapter = new HousesAdapter(houses);
        housesRecyclerView.setAdapter(adapter);

        addHouseFab.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.addHouseFragment));
    }
}
