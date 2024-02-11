package com.example.parkmate;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
public class LotDetailsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {
    private MapView mMapView;
    private GoogleMap mMap;
    private Marker locationMarker;
    private LatLng selectedLocation;
    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_details);
        mMapView = findViewById(R.id.map_v);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);   //register a callback to be invoked when the map is ready
        databaseHelper = new DatabaseHelper(this);
        Button btnSave = findViewById(R.id.btnSaveLot);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextRegOwnerLotname = findViewById(R.id.editTextRegOwnerLotname);
                EditText editTextRegOwnerNumOfSpots = findViewById(R.id.editTextRegOwnerNumOfSpots);
                EditText editTextRegOwnerAddress = findViewById(R.id.editTextRegOwnerAddress);
                // Check if any field is empty or if a location is not selected
                if (editTextRegOwnerLotname.getText().toString().isEmpty() ||
                        editTextRegOwnerNumOfSpots.getText().toString().isEmpty() ||
                        editTextRegOwnerAddress.getText().toString().isEmpty() ||
                        selectedLocation == null) {
                    Toast.makeText(LotDetailsActivity.this, "Please fill all fields and select a location", Toast.LENGTH_SHORT).show();
                } else {
                    long rowId = databaseHelper.insertLot(editTextRegOwnerLotname.getText().toString(), // Insert the lot details into the database
                            Integer.parseInt(editTextRegOwnerNumOfSpots.getText().toString()),
                            editTextRegOwnerAddress.getText().toString(),
                            selectedLocation.toString());
                    if (rowId != -1) {
                        Toast.makeText(LotDetailsActivity.this, "Lot details saved", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LotDetailsActivity.this, LoginActivity.class));
                    } else {
                        Toast.makeText(LotDetailsActivity.this, "Error saving lot details", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(this);
        LatLng lotLocation = new LatLng(6.979132579827219, 79.92623705118756);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lotLocation, 15f));
    }
    // Method called when the map is clicked
    @Override
    public void onMapClick(LatLng latLng) {
        if (locationMarker != null) {
            locationMarker.remove();
        }
        // Save the selected location and add a marker to the map
        selectedLocation = latLng;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(latLng)
                .title("Selected Location");
        locationMarker = mMap.addMarker(markerOptions);
    }
}








