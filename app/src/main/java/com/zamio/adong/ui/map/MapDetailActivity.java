package com.zamio.adong.ui.map;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.zamio.adong.R;
import com.zamio.adong.network.ConstantsApp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by goutam on 29/2/16.
 */
public class MapDetailActivity extends AppCompatActivity
        implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {

    // A request to connect to Location Services
    private LocationRequest mLocationRequest;
    GoogleMap mGoogleMap;
    private GoogleApiClient mGoogleApiClient;
    boolean mUpdatesRequested = false;
    private LinearLayout markerLayout;
    private Geocoder geocoder;
    private List<Address> addresses;
    private TextView Address;
    double latitude;
    double longitude;
    private GPSTracker gps;
    private LatLng curentpoint;
    private RelativeLayout rlAddress;
    private ImageView imvGps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Address = (TextView) findViewById(R.id.adressText);
        imvGps = (ImageView) findViewById(R.id.imageView1);
        imvGps.setVisibility(View.GONE);
        markerLayout = (LinearLayout) findViewById(R.id.locationMarker);
        rlAddress = (RelativeLayout) findViewById(R.id.rlAddress);

        // Getting Google Play availability status
        int status = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(getBaseContext());

        if (status != ConnectionResult.SUCCESS) { // Google Play Services are
            // not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this,
                    requestCode);
            dialog.show();

        } else { // Google Play Services are available

            // Getting reference to the SupportMapFragment
            // Create a new global location parameters object
            mLocationRequest = LocationRequest.create();

            /*
             * Set the update interval
             */
            mLocationRequest.setInterval(GData.UPDATE_INTERVAL_IN_MILLISECONDS);

            // Use high accuracy
            mLocationRequest
                    .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

            // Set the interval ceiling to one minute
            mLocationRequest
                    .setFastestInterval(GData.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

            // Note that location updates are off until the user turns them on
            mUpdatesRequested = false;

            /*
             * Create a new location client, using the enclosing class to handle
             * callbacks.
             */
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();

            mGoogleApiClient.connect();
        }


        SupportMapFragment mapFrag = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

    }

    private void stupMap() {
        try {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }

            mGoogleMap.setMyLocationEnabled(false);
            mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
            mGoogleMap.getUiSettings().setMyLocationButtonEnabled(true);
            mGoogleMap.getUiSettings().setCompassEnabled(true);
            mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
            mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

            gps = new GPSTracker(this);

            gps.canGetLocation();

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();
            curentpoint = new LatLng(latitude, longitude);

            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(curentpoint).zoom(19).build();

            mGoogleMap.setMyLocationEnabled(true);
            mGoogleMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
            // Clears all the existing markers
            mGoogleMap.clear();



            if (getIntent().hasExtra(ConstantsApp.KEY_VALUES_LAT)) {
                latitude = getIntent().getDoubleExtra(ConstantsApp.KEY_VALUES_LAT, 0.0);
                longitude = getIntent().getDoubleExtra(ConstantsApp.KEY_VALUES_LONG, 0.0);
                if(mGoogleMap != null) {
                    new GetLocationAsync(latitude, longitude).execute();
                    }
            }

            rlAddress.setOnClickListener(view -> {
               onBackPressed();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnectionFailed(ConnectionResult arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onConnected(Bundle arg0) {
        // TODO Auto-generated method stub
//        stupMap();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        stupMap();
    }

    private class GetLocationAsync extends AsyncTask<String, Void, String> {

        // boolean duplicateResponse;
        double x, y;
        StringBuilder str;

        public GetLocationAsync(double latitude, double longitude) {
            // TODO Auto-generated constructor stub

            x = latitude;
            y = longitude;
        }

        @Override
        protected void onPreExecute() {
            Address.setText("---");
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                geocoder = new Geocoder(MapDetailActivity.this, Locale.getDefault());
                addresses = geocoder.getFromLocation(x, y, 1);

                str = new StringBuilder();
                if (Geocoder.isPresent() && addresses.size() > 0) {
                    Address returnAddress = addresses.get(0);

                    String localityString = returnAddress.getLocality();
                    String city = returnAddress.getCountryName();
                    String region_code = returnAddress.getCountryCode();
                    String zipcode = returnAddress.getPostalCode();

                    str.append(localityString + "");
                    str.append(city + "" + region_code + "");
                    str.append(zipcode + "");

                } else {
                }
            } catch (IOException e) {
                Log.e("tag", e.getMessage());
            }
            return null;

        }

        @Override
        protected void onPostExecute(String result) {
            try {
                Address.setText(addresses.get(0).getAddressLine(0));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        // TODO Auto-generated method stub
    }
}