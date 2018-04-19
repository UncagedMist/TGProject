package tbc.techbytecare.kk.touristguideproject.Activity;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.NavigableMap;

import tbc.techbytecare.kk.touristguideproject.R;

public class PointerTagActivity extends FragmentActivity implements OnMapReadyCallback {
    DatabaseReference data;
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointer_tag);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        data = FirebaseDatabase.getInstance().getReference("Locations").child("users");
        this.data = data;
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String x = snapshot.child("name").getValue().toString();
                        System.out.println(x);

                        Float y = Float.parseFloat(snapshot.child("lat").getValue().toString());
                        System.out.println(y);
                        Float z = Float.parseFloat(snapshot.child("lng").getValue().toString());
                        System.out.println(z);
                        String q = snapshot.child("vehicle").getValue().toString();
                        System.out.println(q);
                        mMap.addMarker(new MarkerOptions().position(new LatLng(y, z))
                                .title(x)
                        .snippet(snapshot.getKey()));
                    }
                }
                catch (NullPointerException e)  {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getApplicationContext(), OnClick.class);
                String title=marker.getSnippet();
                intent.putExtra("markertitle",title);
                startActivity(intent);

            }
        });

    }}