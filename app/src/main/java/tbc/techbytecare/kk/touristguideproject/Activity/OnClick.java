package tbc.techbytecare.kk.touristguideproject.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import tbc.techbytecare.kk.touristguideproject.R;

public class OnClick extends AppCompatActivity {
    DatabaseReference data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_click);
        Intent intent=getIntent();
        final String markerTitle=intent.getExtras().getString("markertitle");
        System.out.println("TAg received "+markerTitle);
        data = FirebaseDatabase.getInstance().getReference("Locations").child("users");
        this.data = data;
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String user = snapshot.getKey();

                    DatabaseReference data2=FirebaseDatabase.getInstance().getReference("Locations").child("users").child(user);
                    if(user.equals(markerTitle))
                    {
                        data2.child("Status").setValue("Demanded");
                        Toast.makeText(OnClick.this, "Complete Order By Making Payment...", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(OnClick.this,PaypalDev.class));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
