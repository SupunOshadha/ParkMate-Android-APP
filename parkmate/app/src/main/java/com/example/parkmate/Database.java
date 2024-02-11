package com.example.parkmate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create the Users table
        String query1 = "create table Users(Username text, Email text, Password text)";
        // Create the Owners table
        String query2 = "create table Owners(Username text, Email text, Password text)";
        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
    }
    // Register a new user in the Users table
    public void register(String username, String email, String password ){
        ContentValues cv = new ContentValues();
        cv.put("Username", username);
        cv.put("Email", email);
        cv.put("Password", password);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("Users", null,cv);
        db.close();
    }
    // Register a new owner in the Owners table
    public void ownerRegister(String ownerUsername, String ownerEmail, String ownerPassword ){
        ContentValues cv = new ContentValues();
        cv.put("Username", ownerUsername);
        cv.put("Email", ownerEmail);
        cv.put("Password", ownerPassword);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("Owners", null,cv);
        db.close();
    }
    // Check if the user or owner exists in the Users or Owners table
    public int resetPassword(String username, String email) {
        int result = 0;
        String str[] = new String[2];
        str[0] = username;
        str[1] = email;
        SQLiteDatabase db = getReadableDatabase();
        // Check if the user exists in the Users table
        Cursor userCursor = db.rawQuery("SELECT * FROM Users WHERE Username = ? AND Email = ?", str);
        // Check if the owner exists in the Owners table
        Cursor ownerCursor = db.rawQuery("SELECT * FROM Owners WHERE Username = ? AND Email = ?", str);

        if (userCursor.moveToFirst() || ownerCursor.moveToFirst()) {
            result = 1;
        }
        userCursor.close();
        ownerCursor.close();
        return result;
    }
    // Update the password for the user or owner
    public void updatePassword(String username, String email, String newPassword) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Password", newPassword);

        // Update the password in the Users table
        db.update("Users", cv, "Username = ? AND Email = ?", new String[]{username, email});
        // Update the password in the Owners table
        db.update("Owners", cv, "Username = ? AND Email = ?", new String[]{username, email});
        db.close();
    }
    // Login a user or owner
    public int login(String username, String password) {
        int result = 0;
        String str[] = new String[2];
        str[0] = username;
        str[1] = password;
        SQLiteDatabase db = getReadableDatabase();
        // Check if the user exists in the Users table
        Cursor c = db.rawQuery("select*from users where Username=? and Password=?", str);
        // Check if the user exists in the Owners table
        Cursor cursor = db.rawQuery("select*from owners where Username=? and Password=?", str);
        if (c.moveToFirst()) {
            result = 1;
        } else if (cursor.moveToFirst()) {
            result = 1;
        }
        return result;
    }
}
