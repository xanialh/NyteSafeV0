package com.example.nytesafev0;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.nytesafev0.databinding.ActivityMapsBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Arrays;
import java.util.Objects;
/**READ ME**/
//this project will take in lat and long, push the values to a firebase and display on a google map
//i have not included the .json firebase file
//i have made it so that you can set a location for the phone but we can use "getLastLocation()" to find the users last location: https://developer.android.com/training/location/retrieve-current


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    //ADDED OBJECTS
    private DatabaseReference databaseReference;        //used to reference a part of the database
    private LocationManager locationManager;            //used to access methods within android stuido to find device location
    private final long MIN_TIME = 1000;                 //variables used to show how long to wait/ how much distance needs to be moved before location manager will ask for location data
    private final long MIN_DIST = 5;
    private EditText editTextLatitude;                  //used to hold values for long and lat
    private EditText editTextLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        com.example.nytesafev0.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //this along with the <uses-permission> found in the xml file will handle permissions when accessing phone data
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PackageManager.PERMISSION_GRANTED);

        editTextLatitude = findViewById(R.id.editText);         //used as vairables that hold inputted Lat and Lang
        editTextLongitude = findViewById(R.id.editText2);
        //sets the get instance of the database as in the location of the database is not US central the URL is needed
        databaseReference = FirebaseDatabase.getInstance("https://nd-test-753a7-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Location");
        //adds a listener that will change LAT and LANG if data is changed
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    String databaseLatitudeString = Objects.requireNonNull(snapshot.child("latitude").getValue()).toString().substring(1, snapshot.child("latitude").getValue().toString().length()-1);
                    String databaseLongitudeString = Objects.requireNonNull(snapshot.child("longitude").getValue()).toString().substring(1, snapshot.child("longitude").getValue().toString().length()-1);
                    //upon data change this will read new values from the firebase and plot onto map
                    // this is sometimes basically randomly throwing null pointer errors, i think is is because if the database is empty it will try and pull empty values, placing starting data into the database may fix
                    String[] stringLat = databaseLatitudeString.split(", ");
                    Arrays.sort(stringLat);
                    String latitude = stringLat[stringLat.length-1].split("=")[1];

                    String[] stringLong = databaseLongitudeString.split(", ");
                    Arrays.sort(stringLong);
                    String longitude = stringLong[stringLong.length-1].split("=")[1];

                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                    //above 7 lines will format read inputs from the database and place into an double called latLng
                    mMap.addMarker(new MarkerOptions().position(latLng).title(latitude + " , " + longitude));
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    //plots new values onto the map
                }catch(Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //upon location change this will set the text to the new lat/lang
        //will listen to the location of the phone, has methods to do stuff when location is changed
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                try {
                    //upon location change this will set the text to the new lat/lang
                    editTextLatitude.setText(Double.toString(location.getLatitude()));
                    editTextLongitude.setText(Double.toString(location.getLongitude()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //creates a location manager that get the location service

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //above If statement is used to check if the app has permissions to use location data
            try {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
                //location manager will request location updates using .NETWORK_PROVIDER to locate accurate location, MIN_TIME/DIST defines minimum time/distance that has to be changed to update
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void updateButtonOnclick(View view){
        //once button is clicked it will push the two values of lat and long into the database
        databaseReference.child("latitude").push().setValue(editTextLatitude.getText().toString());
        databaseReference.child("longitude").push().setValue(editTextLongitude.getText().toString());
    }


}
