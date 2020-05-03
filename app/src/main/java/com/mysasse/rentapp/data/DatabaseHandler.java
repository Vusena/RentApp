package com.mysasse.rentapp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.mysasse.rentapp.data.models.Booking;
import com.mysasse.rentapp.data.models.House;
import com.mysasse.rentapp.data.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHandler";

    private static final String DB_NAME = "rentals.db";

    public DatabaseHandler(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create the necessary tables
        createUsersTable(db);

        createHousesTable(db);

        createBookingTable(db);

        createTransactionTable(db);

    }

    private void createTransactionTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE transactions(id INTEGER NOT NULL PRIMARY KEY, booking_id INTEGER NOT NULL, amount REAL NOT NULL, created_at TEXT NOT NULL);";
        db.execSQL(sql);
    }

    private void createBookingTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE booking(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, user_id INTEGER NOT NULL, house_id INTEGER NOT NULL, accepted INTEGER DEFAULT 0, created_at TEXT NOT NULL);";
        db.execSQL(sql);
    }

    private void createHousesTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS houses(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, location TEXT NOT NULL, cost_per_room REAL NOT NULL, image BLOB, user_id INTEGER NOT NULL, rooms_count INTEGER NOT NULL, occupied_rooms_count INTEGER DEFAULT 0, created_at TEXT NOT NULL);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropUsersTable(db);
        dropHousesTable(db);
        dropBookingTable(db);
        dropTransactionTable(db);
        onCreate(db);
    }

    private void dropTransactionTable(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS transactions");
    }

    private void dropBookingTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS booking");
    }

    private void dropHousesTable(SQLiteDatabase db) {

        db.execSQL("DROP TABLE IF EXISTS houses");
    }

    private void createUsersTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE IF NOT EXISTS users(" +
                "id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
                "name TEXT NOT NULL,\n" +
                "email TEXT NOT NULL UNIQUE,\n" +
                "phone TEXT NOT NULL UNIQUE,\n" +
                "profile_pic BLOB,\n" +
                "created_at TEXT NOT NULL,\n" +
                "password TEXT NOT NULL,\n" +
                "role TEXT NOT NULL DEFAULT 'TENANT'\n" +
                ");";

        db.execSQL(sql);
    }

    private void dropUsersTable(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS users");
    }

    //The functions should not be here though

    public boolean addUser(User user) {
        try {
            SQLiteDatabase db = getWritableDatabase();
            String sql = "INSERT INTO users(name, email, phone, profile_pic, password, created_at) VALUES(?, ?, ?, ?, ?, datetime('now'));";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.clearBindings();
            statement.bindString(1, user.getName());
            statement.bindString(2, user.getEmail());
            statement.bindString(3, user.getPhone());
            if (user.getProfilePic() != null)
                statement.bindBlob(4, user.getProfilePic());
            statement.bindString(5, user.getPassword());

            return statement.executeInsert() != -1;
        } catch (Exception e) {
            Log.e(TAG, "addUser: ", e);
        }

        return false;
    }

    public List<User> getAllUsers() {

        SQLiteDatabase db = getReadableDatabase();
        List<User> users = new ArrayList<>();

        String sql = "SELECT * FROM users";

        Cursor cursor = db.rawQuery(sql, null);

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String phone = cursor.getString(cursor.getColumnIndex("phone"));
            String role = cursor.getString(cursor.getColumnIndex("role"));
            String password = cursor.getString(cursor.getColumnIndex("password"));
            byte[] profilePic = cursor.getBlob(cursor.getColumnIndex("profile_pic"));
            String createdAt = cursor.getString(cursor.getColumnIndex("created_at"));

            User user = new User(name, email, phone, role);
            user.setId(id);
            user.setPassword(password);
            user.setProfilePic(profilePic);
            user.setCreatedAt(createdAt);

            users.add(user);

        }

        cursor.close();
        db.close();

        return users;
    }

    public boolean addHouse(House house) {

        try {

            SQLiteDatabase db = getWritableDatabase();
            String sql = "INSERT INTO houses(name, location, cost_per_room, image, user_id, rooms_count, created_at) VALUES(?, ?, ?, ?, ?, ?, datetime('now'));";
            SQLiteStatement statement = db.compileStatement(sql);

            statement.clearBindings();
            statement.bindString(1, house.getName());
            statement.bindString(2, house.getLocation());
            statement.bindDouble(3, house.getCostPerRoom());
            statement.bindBlob(4, house.getImage());
            statement.bindLong(5, house.getUserId());
            statement.bindLong(6, house.getRoomsCount());

            return statement.executeInsert() != -1;

        } catch (Exception e) {
            Log.e(TAG, "addHouse: exception", e);
        }

        return false;
    }

    public boolean addBooking(Booking booking) {

        SQLiteDatabase db = getWritableDatabase();
        try {
            String sql = "INSERT INTO booking(user_id, house_id, created_at) VALUES(?, ?, datetime('now'))";

            SQLiteStatement stmt = db.compileStatement(sql);

            stmt.clearBindings();

            stmt.bindLong(1, booking.getUserId());
            stmt.bindLong(2, booking.getHouseId());

            return stmt.executeInsert() != -1;

        } catch (Exception e) {
            Log.e(TAG, "addBooking: exception:", e);
        }
        return false;
    }

    public boolean executeUpdate(String query) {

        try {
            SQLiteDatabase db = getWritableDatabase();
            SQLiteStatement statement = db.compileStatement(query);

            return statement.executeUpdateDelete() != 0;

        } catch (SQLException exception) {
            Log.e(TAG, "executeUpdate: error: ", exception);

        }

        return false;
    }

    public Cursor executeQuery(String query) {

        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query, null);

    }
}
