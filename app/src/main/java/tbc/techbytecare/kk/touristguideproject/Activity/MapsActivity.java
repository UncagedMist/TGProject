package tbc.techbytecare.kk.touristguideproject.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import tbc.techbytecare.kk.touristguideproject.Common.Common;
import tbc.techbytecare.kk.touristguideproject.MapModel.MyPlaces;
import tbc.techbytecare.kk.touristguideproject.MapModel.Results;
import tbc.techbytecare.kk.touristguideproject.R;
import tbc.techbytecare.kk.touristguideproject.Remote.IGoogleAPIService;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {

    private static final int MY_PERMISSION_CODE = 1000;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private double latitude,longitude;
    private Location mLastLocation;
    private Marker mMarker;
    private LocationRequest mLocationRequest;

    IGoogleAPIService mService;

    CircleImageView civDoctor,civPolice,civChef,civHotel,civTourist;

    FButton btnZoomOut,btnZoomIn;
    private int zoom_amount = 15;

    MyPlaces currentPlaces;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mService = Common.getGoogleAPIService();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            checkLocationPermission();
        }

        civDoctor = findViewById(R.id.civDoctor);
        civPolice = findViewById(R.id.civPolice);
        civChef = findViewById(R.id.civChef);
        civHotel = findViewById(R.id.civHotels);
        civTourist = findViewById(R.id.civTourist);

        btnZoomIn = findViewById(R.id.btnZoomIn);
        btnZoomOut = findViewById(R.id.btnZoomOut);

        civDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nearByPlaces("hospital");
            }
        });
        civChef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nearByPlaces("restaurant");
            }
        });

        civPolice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nearByPlaces("police");
            }
        });

        civHotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nearByPlaces("lodging");
            }
        });

        civTourist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nearByPlaces("point_of_interest");
            }
        });

        btnZoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomOutMap();
            }
        });

        btnZoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomInMap();
            }
        });
    }

    private void zoomInMap() {
        if (zoom_amount < 160) {
            zoom_amount += 2;
            set_map_zoom(zoom_amount);
        }
        else {
            Toast.makeText(MapsActivity.this, "Zoom at Max...", Toast.LENGTH_SHORT).show();
        }
    }

    private void zoomOutMap() {
        if (zoom_amount > 2) {
            zoom_amount -= 2;

            set_map_zoom(zoom_amount);
        }
        else {
            Toast.makeText(MapsActivity.this, "Zoom at min...", Toast.LENGTH_SHORT).show();
        }
    }

    private void set_map_zoom(int zoom_amount) {

        if (mMap != null) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(zoom_amount));
        }
        else {
            Toast.makeText(this, "wait....", Toast.LENGTH_SHORT).show();
        }
    }

    private void nearByPlaces(final String placeType) {
        mMap.clear();
        String url = getUrl(latitude,longitude,placeType);

        mService.getNearByPlaces(url)
                .enqueue(new Callback<MyPlaces>() {
                    @Override
                    public void onResponse(Call<MyPlaces> call, Response<MyPlaces> response) {

                        currentPlaces = response.body();

                        if (response.isSuccessful())    {
                            for (int i = 0; i < response.body().getResults().length; i++)   {

                                MarkerOptions markerOptions = new MarkerOptions();
                                Results googlePlaces = response.body().getResults()[i];

                                final double lat = Double.parseDouble(googlePlaces.getGeometry().getLocation().getLat());
                                final double lng = Double.parseDouble(googlePlaces.getGeometry().getLocation().getLng());

                                String placeName = googlePlaces.getName();
                                String vicinity = googlePlaces.getVicinity();
                                LatLng latLng = new LatLng(lat,lng);
                                markerOptions.position(latLng);
                                markerOptions.title(placeName);

                                if (placeType.equals("hospital")) {
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_hospital));
                                }
                                else if (placeType.equals("restaurant")) {
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_restaurant));
                                }
                                else if (placeType.equals("police")) {
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_police));
                                }
                                else if (placeType.equals("lodging")) {
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_hotel));
                                }
                                else if (placeType.equals("point_of_interest")) {
                                    markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_tourist));
                                }
                                else    {
                                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                                }

                                markerOptions.snippet(String.valueOf(i));

                                mMap.addMarker(markerOptions);

                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<MyPlaces> call, Throwable t) {

                    }
                });
    }

    private String getUrl(double latitude, double longitude, String placeType) {
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location="+latitude+","+longitude);
        googlePlacesUrl.append("&radius="+10000);
        googlePlacesUrl.append("&type="+placeType);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key="+getResources().getString(R.string.browser_key));

        Log.d("getUrl",googlePlacesUrl.toString());

        return googlePlacesUrl.toString();
    }

    private boolean checkLocationPermission() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)) {

                ActivityCompat.requestPermissions(this,new String[]{

                        Manifest.permission.ACCESS_FINE_LOCATION

                },MY_PERMISSION_CODE);
            }
            else    {
                ActivityCompat.requestPermissions(this,new String[]{

                        Manifest.permission.ACCESS_FINE_LOCATION

                },MY_PERMISSION_CODE);
            }
            return false;
        }
        else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)    {

            case MY_PERMISSION_CODE :   {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)    {

                    if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)  {

                        if (mGoogleApiClient == null)   {
                            buildGoogleAPIClient();
                        }
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else    {
                    Toast.makeText(this, "Permission Denied!!!!", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                buildGoogleAPIClient();
                mMap.setMyLocationEnabled(true);
            }
        }
        else    {
            buildGoogleAPIClient();
            mMap.setMyLocationEnabled(true);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Common.currentResults = currentPlaces.getResults()[Integer.parseInt(marker.getSnippet())];

                startActivity(new Intent(MapsActivity.this,ViewPlaceDetail.class));

                return true;
            }
        });
    }

    private synchronized void buildGoogleAPIClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);

        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;

        if (mMarker != null)    {
            mMarker.remove();
        }

        latitude = location.getLatitude();
        longitude = location.getLongitude();

        LatLng latLng = new LatLng(latitude,longitude);

        MarkerOptions markerOptions = new MarkerOptions().position(latLng)
                .title("Your Location")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        mMarker = mMap.addMarker(markerOptions);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        if (mGoogleApiClient != null)   {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,this);
        }

        System.out.println(latitude);
        System.out.println(longitude);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("UsersTourist").child("Locations");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,Object> push = new HashMap<>();
                push.put("lat",latitude);
                push.put("lng",longitude);

                databaseReference.updateChildren(push);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
