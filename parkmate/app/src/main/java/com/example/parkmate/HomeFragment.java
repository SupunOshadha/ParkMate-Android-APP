package com.example.parkmate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
public class HomeFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private MapView mMapView;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Button btnBookings;
    private ImageView btnHelp,btnFeedback, imgLogout;;
    public HomeFragment() {
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        mMapView = rootView.findViewById(R.id.mapView2);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        btnBookings = rootView.findViewById(R.id.bookings);
        btnHelp = rootView.findViewById(R.id.imageViewHelp);
        imgLogout = rootView.findViewById(R.id.img_LogOut);
        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform logout and navigate to LoginActivity
                logout();
            }
        });
        btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), HelpActivity.class));
            }
        });
        btnFeedback = rootView.findViewById(R.id.imageViewFeedback);
        btnFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), FeedbackActivity.class));
            }
        });
        btnBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), BookingsActivity.class));
            }
        });
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
        return rootView;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        // Resume the map view
        mMapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        // Pause the map view
        mMapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // Destroy the map view
        mMapView.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // Notify the map view that it's memory is low
        mMapView.onLowMemory();
    }
    // Check if the app has location permission
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // If the app has location permission, enable "My Location" on the map and show the user's current location
            mMap.setMyLocationEnabled(true);
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                        mMap.addMarker(new MarkerOptions().position(currentLocation).title("My Location"));
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 9));
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
        LatLng lotLocation = new LatLng(6.894830530682437, 79.85473327461098);
        mMap.addMarker(new MarkerOptions().position(lotLocation).title("MC"));
        LatLng lotLocation01 = new LatLng(6.890151171866491, 79.87246111360743);
        mMap.addMarker(new MarkerOptions().position(lotLocation01).title("Lanka Hospital"));
        LatLng lotLocation02 = new LatLng(6.911235888145062, 79.8782045383096);
        mMap.addMarker(new MarkerOptions().position(lotLocation02).title("House of Fashion"));
        LatLng lotLocation03 = new LatLng(6.931659655491617, 79.84659932610997);
        mMap.addMarker(new MarkerOptions().position(lotLocation03).title("Regal Cinema"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lotLocation, 9));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lotLocation01, 9));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lotLocation02, 9));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lotLocation03, 9));
    }
    private void logout() {
        // Clear the username preference
        SharedPreferences preferences = getActivity().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("Username");
        editor.apply();
        // Start the LoginActivity
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        // Finish the current activity
        getActivity().finish();
    }
}






