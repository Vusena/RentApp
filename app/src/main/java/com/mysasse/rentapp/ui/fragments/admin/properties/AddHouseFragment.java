package com.mysasse.rentapp.ui.fragments.admin.properties;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.mysasse.rentapp.R;
import com.mysasse.rentapp.data.DatabaseHandler;
import com.mysasse.rentapp.data.models.House;
import com.mysasse.rentapp.data.models.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddHouseFragment extends Fragment {
    private static final String TAG = "AddHouseFragment";
    private DatabaseHandler dbh;
    private static final int SELECT_IMAGE_RC = 22;
    private ImageView propertyImage;
    private Uri imageUri;
    private List<User> users;
    private TextInputEditText nameTxt;
    private TextInputEditText locationTxt;
    private TextInputEditText roomsCountTxt;
    private TextInputEditText costPerRoomTxt;

    public AddHouseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_house, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Instantiate the database
        dbh = new DatabaseHandler(getActivity());

        //Register necessary views
        nameTxt = view.findViewById(R.id.name_txt);
        locationTxt = view.findViewById(R.id.location_txt);
        propertyImage = view.findViewById(R.id.property_iv);
        Button selectImageBtn = view.findViewById(R.id.select_image_btn);
        roomsCountTxt = view.findViewById(R.id.rooms_count_txt);
        costPerRoomTxt = view.findViewById(R.id.cost_per_room_txt);
        AutoCompleteTextView usersAtv = view.findViewById(R.id.users_atv);
        Button addHouseButton = view.findViewById(R.id.add_house_button);

        //Setup the autocomplete text view
        users = dbh.getAllUsers();
        List<String> userNameList = new ArrayList<>();

        for (User user : users) {
            userNameList.add(user.getName());
        }

        String[] userNamesArray = userNameList.toArray(new String[]{});
        assert getActivity() != null;
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_menu_popup_item, userNamesArray);
        usersAtv.setAdapter(arrayAdapter);

        selectImageBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

            ComponentName componentName = intent.resolveActivity(getActivity().getPackageManager());

            if (componentName != null) {
                startActivityForResult(intent, SELECT_IMAGE_RC);
            }
        });

        addHouseButton.setOnClickListener(v -> {
            String name = String.valueOf(nameTxt.getText());
            String location = String.valueOf(locationTxt.getText());
            String roomsCount = String.valueOf(roomsCountTxt.getText());
            String costPerRoom = String.valueOf(costPerRoomTxt.getText());
            String userName = String.valueOf(usersAtv.getText());

            if (!validationPasses(name, location, roomsCount, costPerRoom)) return;

            //Check whether a user is selected
            User houseOwner = null;
            for (User user : users) {
                if (user.getName().trim().equalsIgnoreCase(userName.trim())) {
                    houseOwner = user;
                    break;
                }
            }

            if (houseOwner == null) {
                usersAtv.setError("Select the house owner from the choices please");
                usersAtv.requestFocus();
                return;
            }

            House house = new House(name, Double.parseDouble(costPerRoom), location, Integer.parseInt(roomsCount), 0, houseOwner.getId());

            if (imageUri == null) {
                Toast.makeText(getActivity(), "Select house image please", Toast.LENGTH_SHORT).show();
                return;
            }

            Bitmap bitmap = ((BitmapDrawable) propertyImage.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            house.setImage(baos.toByteArray());

            if (dbh.addHouse(house)) {
                Toast.makeText(getActivity(), "House created", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            } else {
                Toast.makeText(getActivity(), "Adding operation failed, try again", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private boolean validationPasses(String name, String location, String roomsCount, String costPerRoom) {

        if (TextUtils.isEmpty(name)) {
            nameTxt.setError("Name is required");
            nameTxt.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(location)) {
            locationTxt.setError("Location is required");
            locationTxt.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(roomsCount)) {
            roomsCountTxt.setError("Number of rooms in the house required");
            roomsCountTxt.requestFocus();
            return false;
        }

        if (TextUtils.isEmpty(costPerRoom)) {
            costPerRoomTxt.setError("Room cost is required");
            costPerRoomTxt.requestFocus();
            return false;
        }

        return true;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SELECT_IMAGE_RC) {
                assert data != null;

                imageUri = data.getData();
                propertyImage.setImageURI(imageUri);

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }

        } else {
            Toast.makeText(getActivity(), "The result is mot OK", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onActivityResult: Result not OK checkout please");
        }
    }
}
