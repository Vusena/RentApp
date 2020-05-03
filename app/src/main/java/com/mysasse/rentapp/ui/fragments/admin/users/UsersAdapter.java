package com.mysasse.rentapp.ui.fragments.admin.users;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mysasse.rentapp.R;
import com.mysasse.rentapp.data.models.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UserViewHolder> {

    private UserClickedLister lister;
    private List<User> users;

    public UsersAdapter(UserClickedLister lister, List<User> users) {
        this.lister = lister;
        this.users = users;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UserViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.single_user_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.nameTv.setText(users.get(position).getName());
        holder.roleTv.setText(users.get(position).getRole());

        Bitmap bitmap = BitmapFactory.decodeByteArray(users.get(position).getProfilePic(), 0, users.get(position).getProfilePic().length);
        holder.userAvatarCiv.setImageBitmap(bitmap);

        holder.mView.setOnClickListener(v -> lister.showUser(users.get(position)));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView nameTv, roleTv;
        CircleImageView userAvatarCiv;
        View mView;

        private UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            nameTv = itemView.findViewById(R.id.user_name_tv);
            roleTv = itemView.findViewById(R.id.user_role_tv);
            userAvatarCiv = itemView.findViewById(R.id.user_avatar_civ);
        }
    }

    public interface UserClickedLister {
        void showUser(User user);
    }
}
