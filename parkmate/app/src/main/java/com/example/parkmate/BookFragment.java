package com.example.parkmate;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
public class BookFragment extends Fragment {
    private Button booknext;
    private String selectedParkingLotName;
    private TextView availableSpotsTextView, totalSpotsTextView,availableSpotsNoTextView,occupiedSpotsTextView;
    public BookFragment() {
    }
    public static BookFragment newInstance(String param1, String param2) {
        BookFragment fragment = new BookFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);
        booknext = view.findViewById(R.id.booknext);
        booknext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),BookingspotActivity.class);
                startActivity(intent);
            }
        });
        Spinner parkingLotSpinner = view.findViewById(R.id.parkingLotSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.parking_lots, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        parkingLotSpinner.setAdapter(adapter);
        totalSpotsTextView = view.findViewById(R.id.tv_totalSpots);
        availableSpotsTextView = view.findViewById(R.id.tv_AvailableSpots);
        occupiedSpotsTextView = view.findViewById(R.id.tv_OccupiedSpots);
        availableSpotsNoTextView = view.findViewById(R.id.tv_AvailableSpotsNos);
        // Listen for changes in the spinner selection and update parking lot information accordingly
        parkingLotSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get selected parking lot name from spinner
                selectedParkingLotName = parent.getItemAtPosition(position).toString();
                String selectedParkingLotName = parent.getItemAtPosition(position).toString();
                // Get parking lot information from database
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                ParkingLot selectedParkingLot = dbHelper.getSelectedParkingLot(selectedParkingLotName);
                // Update TextViews with parking lot information
                if (selectedParkingLot != null) {
                    availableSpotsTextView.setText(String.valueOf(selectedParkingLot.getAvailableSpots()));
                    occupiedSpotsTextView.setText(String.valueOf(selectedParkingLot.getOccupiedSpots()));
                    availableSpotsNoTextView.setText(String.valueOf(selectedParkingLot.getAvailableSpotsNos()));
                    totalSpotsTextView.setText(String.valueOf(selectedParkingLot.getTotalSpots()));
               }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        booknext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
                dbHelper.storeBooking(selectedParkingLotName, getActivity());
                Intent intent = new Intent(getActivity(), BookingspotActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}



