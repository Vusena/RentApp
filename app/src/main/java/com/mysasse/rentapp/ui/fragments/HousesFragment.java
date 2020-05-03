package com.mysasse.rentapp.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.rentapp.R;
import com.mysasse.rentapp.data.DatabaseHandler;
import com.mysasse.rentapp.data.models.Booking;
import com.mysasse.rentapp.data.models.House;

import java.util.ArrayList;
import java.util.List;

public class HousesFragment extends Fragment implements HousesAdapter.HouseClickedListener {
    private static final String TAG = "HousesFragment";
    private DatabaseHandler dbh;

    public HousesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.houses_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Create Database Handler instance
        dbh = new DatabaseHandler(getActivity());
        //Register necessary views
        RecyclerView housesRecyclerView = view.findViewById(R.id.houses_recycler_view);
        housesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        housesRecyclerView.setHasFixedSize(true);

        //Houses list
        List<House> houses = new ArrayList<>();

        //Get all the houses
        String sql = "SELECT id, name, user_id, location, cost_per_room, rooms_count, occupied_rooms_count FROM houses";

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

                House house = new House(name, costPerRoom, location, roomsCount, occupiedRoomsCount, userId);
                house.setId(id);
                houses.add(house);

            } while (cursor.moveToNext());
        }
        cursor.moveToFirst();
        HousesAdapter adapter = new HousesAdapter(this, houses);
        housesRecyclerView.setAdapter(adapter);
    }

    @Override
    public void sendABookingNotification(House house) {

        try {
            Log.d(TAG, "sendABookingNotification: house id: " + house.getId());

            assert getActivity() != null;

            SharedPreferences preferences = getActivity().getSharedPreferences(getActivity().getString(R.string.credentials_text), Context.MODE_WORLD_READABLE);

            int id = preferences.getInt(getString(R.string.id_text), 0);
            String email = preferences.getString(getString(R.string.email_text), null);

            Toast.makeText(getActivity(), "User id: " + id, Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "User email: " + email, Toast.LENGTH_SHORT).show();

            if (id == 0) {
                Toast.makeText(getActivity(), "Create account and login before you can book a house please", Toast.LENGTH_SHORT).show();
                return;
            }

            Booking booking = new Booking(id, house.getId());

            if (dbh.addBooking(booking)) {
                Toast.makeText(getActivity(), "You have successfully booked that house wait for the owners confirmation", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Something bad happened, try again later", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exception) {
            Log.e(TAG, "sendABookingNotification: ", exception);
        }
    }
}
