package com.example.mylocation2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_LOCATION = 123;
    TextView tvv;
    private LocationManager manager;
    private LocationListener listener;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvv = findViewById(R.id.textView);

        // testen zum horizontale scroll

       /* for(int i = 0; i < 100; i++){
            tvv.append("Hallo!  Keeeeeeeeeeeeeeeeeeeeeeeeeeeeemmmmmmmmmmmmmmmmmmmmmm\n");
        }
        tvv.append("Welt ... ");*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_PERMISSION_LOCATION);

            } else {
                doIt();
            }


        } else {
            doIt();
        }


    }

    private void doIt() {

        manager = (LocationManager) getSystemService(LOCATION_SERVICE);
        List<String> providers = manager.getAllProviders();


        for (String name : providers) {

            tvv.append("Name: " + name + " isEnable: " + manager.isProviderEnabled(name) + "\n");
            LocationProvider locationProvider = manager.getProvider(name);
            tvv.append("requires cell " + locationProvider.requiresCell() + "\n");
            tvv.append("requires cell " + locationProvider.requiresNetwork() + "\n");
            tvv.append("requires cell " + locationProvider.requiresSatellite() + "\n");
        }
        Criteria criteria = new Criteria();


        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        //criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        provider = manager.getBestProvider(criteria, true);
        tvv.append("Verwendet: " + provider + "\n");


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                tvv.append("Neue Huhhh -- > ");
                if (location != null) {

                    Log.e("test", "location: " +  location.getLatitude() + " - " + location.getLongitude() );

                    tvv.append("Breite: " + location.getLatitude() + " länge: " + location.getLongitude() + "\n");
                    Geocoder gc = new Geocoder(MainActivity.this);

                    if(Geocoder.isPresent()) {
                        try {

                            Log.e("test", "Geocoder.isPresent()   .............  " );

                            List<Address> adresse = gc.getFromLocation(location.getLatitude(), location.getLongitude(), 1);



                            Log.e("test", "geocoder:  " + gc.toString() );
                            Address adress = adresse.get(0);
                            tvv.append(adress.getAddressLine(0));
                        } catch (IOException e) {

                        }



                    }
                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_PERMISSION_LOCATION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            doIt();
        }
    }


    @SuppressLint("MissingPermission")
    @Override
    protected void onResume() {
        super.onResume();
        manager.requestLocationUpdates(provider, 1000, 0.005F, listener);
       // manager.requestLocationUpdates(provider, 0, 0.05F, listener);

    }


    @Override
    protected void onPause() {
        super.onPause();

        manager.removeUpdates(listener);
    }
}
