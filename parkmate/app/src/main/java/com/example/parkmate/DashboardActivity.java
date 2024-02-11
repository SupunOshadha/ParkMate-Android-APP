package com.example.parkmate;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.parkmate.databinding.ActivityDashboardBinding;

@SuppressWarnings("ALL")
public class DashboardActivity extends AppCompatActivity implements SensorEventListener {
    SensorManager sensorManager;
    Sensor sensor;
    Context context;
    TextView txtLight;
    boolean success;
    private ActivityDashboardBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // replace the default fragment with the HomeFragment
        replaceFragment(new HomeFragment());
        // initialize sensor and sensor manager
        txtLight = findViewById(R.id.textView11);
        sensorManager = (SensorManager)getSystemService(Service.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        // handle item selection in the bottom navigation view
        binding.bottomNavigationView2.setOnItemSelectedListener(item ->{
           switch (item.getItemId()){
               case R.id.home:
                   replaceFragment(new HomeFragment());
                   break;
               case R.id.book:
                   replaceFragment(new BookFragment());
                   break;
           }
            return true;
        });
    }
    // replace current fragment with a new fragment
    private void replaceFragment (Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout,fragment);
        fragmentTransaction.commit();
    }
    // unregister the listener when the app is paused
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    // register the listener when the app is resumed
    protected void onResume (){
        super.onResume();
        sensorManager.registerListener(this,sensor,sensorManager.SENSOR_DELAY_NORMAL);
    }
    // check light sensor reading and adjust brightness accordingly
    @Override
    public void onSensorChanged(SensorEvent event) {
        context=getApplicationContext();
        if(event.sensor.getType()==Sensor.TYPE_LIGHT){
            txtLight.setText(""+event.values[0]);
            if(event.values[0]<15){
                permission();
                setBrightness(100);
            } else if (event.values[0]>80) {
                permission();;
                setBrightness(240);
            }
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    // check if the app has permission to change system settings
    private void permission(){
        boolean value;
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            value= Settings.System.canWrite(getApplicationContext());
            if(value){
                success = true;
            }
            else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getApplicationContext().getPackageName()));
                startActivityForResult(intent, 100);
            }
        }
    }
    // handle the result of the permission request
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 100) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                boolean value = Settings.System.canWrite(getApplicationContext());
                if (value) {
                    success = true;
                } else {
                    Toast.makeText(this, "Permission is not granted", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void setBrightness(int brightness){
        if(brightness<0){
            brightness=0;
        }
        else if(brightness>255) {
            brightness=255;
        }
        ContentResolver contentResolver= getApplicationContext().getContentResolver();
        Settings.System.putInt(contentResolver,Settings.System.SCREEN_BRIGHTNESS,brightness);
    }
}












