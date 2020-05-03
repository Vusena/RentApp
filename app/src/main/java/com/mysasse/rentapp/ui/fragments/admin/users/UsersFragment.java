package com.mysasse.rentapp.ui.fragments.admin.users;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.rentapp.R;
import com.mysasse.rentapp.data.DatabaseHandler;
import com.mysasse.rentapp.data.models.User;

public class UsersFragment extends Fragment implements UsersAdapter.UserClickedLister {

    private RecyclerView usersRecyclerView;

    public UsersFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.users_fragment, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //Database handler instance
        DatabaseHandler dbh = new DatabaseHandler(getActivity());

        //Get the recycler view and set the users on the recycler
        usersRecyclerView = view.findViewById(R.id.users_recycler_view);
        usersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        usersRecyclerView.setHasFixedSize(true);

        UsersAdapter adapter = new UsersAdapter(this, dbh.getAllUsers());

        usersRecyclerView.setAdapter(adapter);

    }

    @Override
    public void showUser(User user) {
        Toast.makeText(getContext(), "User id: " + user.getId(), Toast.LENGTH_SHORT).show();
        Navigation.findNavController(usersRecyclerView).navigate(R.id.userFragment);
    }
}
