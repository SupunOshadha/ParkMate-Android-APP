package com.example.parkmate;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database name and version
    private static final String DATABASE_NAME = "lots_bookings_db";
    private static final int DATABASE_VERSION = 1;
    // Table and column names
    private static final String PARKING_TABLE = "parking_lot";//table
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String TOTAL_SPOTS_COLUMN = "total_spots";
    private static final String OCCUPIED_SPOTS_COLUMN = "occupied_spots";
    private static final String AVAILABLE_SPOTS_COLUMN = "available_spots";
    private static final String AVAILABLE_SPOTS_NO_COLUMN = "available_spots_no";
    private static final String BOOKINGS_TABLE = "bookings";//table
    private static final String LOT_NAME_COLUMN = "lotName";
    private static final String LOT_NO_COLUMN = "lotNo";
    private static final String DATE_COLUMN = "date";
    private static final String CHECK_IN_TIME_COLUMN = "checkInTime";
    private static final String CHECK_OUT_TIME_COLUMN = "checkOutTime";
    private static final String USER_NAME_COLUMN = "Username";
    private static final String FEE_COLUMN = "fee";
    private static final String FEEDBACK_TABLE = "feedback"; //table
    private static final String COLUMN_NAME_FEEDBACK = "name";
    private static final String COLUMN_EMAIL_FEEDBACK = "email";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_USERNAME_FEEDBACK = "username";
    private static final String TABLE_LOTS = "Newlots";//table
    private static final String COLUMN_ID_Owner = "_id";
    private static final String COLUMN_LOT_NAME = "lot_name";
    private static final String COLUMN_NUM_OF_SPOTS = "num_of_spots";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_LOCATION = "location";
    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    // Create the database table
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createParkingTable = "CREATE TABLE " + PARKING_TABLE + " ("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + NAME_COLUMN + " TEXT,"
                + TOTAL_SPOTS_COLUMN + " INTEGER,"
                + OCCUPIED_SPOTS_COLUMN + " INTEGER,"
                + AVAILABLE_SPOTS_COLUMN + " INTEGER,"
                + AVAILABLE_SPOTS_NO_COLUMN + " TEXT)";
        db.execSQL(createParkingTable);

        String createBookingsTable = "CREATE TABLE " + BOOKINGS_TABLE + " ("
                + ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + USER_NAME_COLUMN + " TEXT,"
                + LOT_NAME_COLUMN + " TEXT,"
                + LOT_NO_COLUMN + " TEXT,"
                + DATE_COLUMN + " TEXT,"
                + CHECK_IN_TIME_COLUMN + " TEXT,"
                + CHECK_OUT_TIME_COLUMN + " TEXT,"
                + FEE_COLUMN + " FLOAT)";
        db.execSQL(createBookingsTable);

        // Insert sample data
        ContentValues values1 = new ContentValues();
        values1.put(NAME_COLUMN, "Regal Cinema");
        values1.put(TOTAL_SPOTS_COLUMN, 10);
        values1.put(OCCUPIED_SPOTS_COLUMN, 0);
        values1.put(AVAILABLE_SPOTS_COLUMN, 10);
        values1.put(AVAILABLE_SPOTS_NO_COLUMN, "1,2,3,4,5,6,7,8,9,10");
        db.insert(PARKING_TABLE, null, values1);

        ContentValues values2 = new ContentValues();
        values2.put(NAME_COLUMN, "Lanka Hospital");
        values2.put(TOTAL_SPOTS_COLUMN, 12);
        values2.put(OCCUPIED_SPOTS_COLUMN, 0);
        values2.put(AVAILABLE_SPOTS_COLUMN, 12);
        values2.put(AVAILABLE_SPOTS_NO_COLUMN, "1,2,3,4,5,6,7,8,9,10,11,12");
        db.insert(PARKING_TABLE, null, values2);

        ContentValues values3 = new ContentValues();
        values3.put(NAME_COLUMN, "MC");
        values3.put(TOTAL_SPOTS_COLUMN, 15);
        values3.put(OCCUPIED_SPOTS_COLUMN, 0);
        values3.put(AVAILABLE_SPOTS_COLUMN, 15);
        values3.put(AVAILABLE_SPOTS_NO_COLUMN, "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15");
        db.insert(PARKING_TABLE, null, values3);

        String createTableQuery = "CREATE TABLE " + FEEDBACK_TABLE + "(" +
                COLUMN_NAME_FEEDBACK + " TEXT," +
                COLUMN_EMAIL_FEEDBACK + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_USERNAME_FEEDBACK + " TEXT)";
        db.execSQL(createTableQuery);
        String createLotsTable = "CREATE TABLE " + TABLE_LOTS + " (" +
                COLUMN_ID_Owner + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_LOT_NAME + " TEXT, " +
                COLUMN_NUM_OF_SPOTS + " INTEGER, " +
                COLUMN_ADDRESS + " TEXT, " +
                COLUMN_LOCATION + " TEXT" +
                ")";
        db.execSQL(createLotsTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + PARKING_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOTS);
        db.execSQL("DROP TABLE IF EXISTS " + BOOKINGS_TABLE);
        onCreate(db);
    }
    public void storeBooking(String parkingLotName, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOT_NAME_COLUMN, parkingLotName);
        // Check if the user already has a booking
        String query = "SELECT * FROM " + BOOKINGS_TABLE + " WHERE " + USER_NAME_COLUMN + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        if (cursor.getCount() > 0) {
            // Update the existing row with the new parking lot name
            db.update(BOOKINGS_TABLE, values, USER_NAME_COLUMN + " = ?", new String[]{username});
        } else {
            // Insert a new row for the user
            values.put(USER_NAME_COLUMN, username);
            db.insert(BOOKINGS_TABLE, null, values);
        }
        cursor.close();
        db.close();
    }
    public ParkingLot getSelectedParkingLot(String parkingLotName) {
        SQLiteDatabase db = this.getReadableDatabase();
        // Construct the SQL query to retrieve the parking lot details based on the name
        String query = "SELECT * FROM " + PARKING_TABLE + " WHERE " + NAME_COLUMN + " = ?";
        Cursor cursor = db.rawQuery(query, new String[] {parkingLotName});
        ParkingLot parkingLot = null;
        if (cursor.moveToFirst()) {
            // Extract the details from the cursor and store them in variables
            int totalSpots = cursor.getInt(2);
            int occupiedSpots = cursor.getInt(3);
            int availableSpots = cursor.getInt(4);
            String availableSpotsNos = cursor.getString(5);
            // Create a new ParkingLot object using the retrieved details
            parkingLot = new ParkingLot(parkingLotName, totalSpots, occupiedSpots, availableSpots, availableSpotsNos);
        }
        // Close the cursor and database connection
        cursor.close();
        db.close();
        // Return the ParkingLot object
        return parkingLot;
    }
    @SuppressLint("Range")
    public float getBookingFee(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        int fee = 0;
        // Define the query to retrieve the fee from the Bookings table
        String query = "SELECT fee FROM Bookings WHERE username = ?";
        String[] selectionArgs = {username};

        Cursor cursor = db.rawQuery(query, selectionArgs);
        if (cursor.moveToFirst()) {
            fee = cursor.getInt(cursor.getColumnIndex("fee"));
        }
        cursor.close();
        return fee;
    }
    public void storeFeedbackDetails(String name, String email, String description, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_FEEDBACK, name);
        values.put(COLUMN_EMAIL_FEEDBACK, email);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_USERNAME_FEEDBACK, username);
        db.insert(FEEDBACK_TABLE, null, values);
        db.close();
    }
    public long insertLot(String lotName, int numOfSpots, String address, String location) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOT_NAME, lotName);
        values.put(COLUMN_NUM_OF_SPOTS, numOfSpots);
        values.put(COLUMN_ADDRESS, address);
        values.put(COLUMN_LOCATION, location);
        return db.insert(TABLE_LOTS, null, values);
    }
}

