package com.mysasse.rentapp.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mysasse.rentapp.R;
import com.mysasse.rentapp.data.DatabaseHandler;
import com.mysasse.rentapp.data.models.House;
import com.mysasse.rentapp.ui.fragments.admin.properties.HousesAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyHousesFragment extends Fragment {
    private static final String TAG = "MyHousesFragment";


    public MyHousesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_houses, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Register the necessary views
        RecyclerView housesRecyclerView = view.findViewById(R.id.houses_recycler_view);
        housesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        housesRecyclerView.setHasFixedSize(true);

        assert getActivity() != null;
        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.credentials_text), Context.MODE_WORLD_READABLE);

        int user_id = preferences.getInt(getString(R.string.id_text), 0);

        Toast.makeText(getActivity(), "User id: " + user_id, Toast.LENGTH_SHORT).show();

        String sql = "SELECT * FROM houses WHERE user_id = " + user_id;

        DatabaseHandler dbh = new DatabaseHandler(getContext());

        Cursor cursor = dbh.executeQuery(sql);

        List<House> houses = new ArrayList<>();


        if (cursor.getCount() > 0) {
            try {
                cursor.moveToFirst();

                if (!cursor.isAfterLast()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndex("id"));
                        int userId = cursor.getInt(cursor.getColumnIndex("user_id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String location = cursor.getString(cursor.getColumnIndex("location"));
                        int roomsCount = cursor.getInt(cursor.getColumnIndex("rooms_count"));
                        int occupiedRoomsCount = cursor.getInt(cursor.getColumnIndex("occupied_rooms_count"));
                        double costPerRoom = cursor.getDouble(cursor.getColumnIndex("cost_per_room"));
                        byte[] image = cursor.getBlob(cursor.getColumnIndex("image"));
                        String createdAt = cursor.getString(cursor.getColumnIndex("created_at"));

                        House house = new House(name, costPerRoom, location, roomsCount, occupiedRoomsCount, userId);
                        house.setId(id);
                        house.setImage(image);
                        house.setCreatedAt(createdAt);

                        houses.add(house);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                HousesAdapter adapter = new HousesAdapter(houses);
                housesRecyclerView.setAdapter(adapter);

            } catch (Exception exception) {
                Log.e(TAG, "getAllHouses: error: ", exception);
            }

        }
    }

}
