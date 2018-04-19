package tbc.techbytecare.kk.touristguideproject.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import tbc.techbytecare.kk.touristguideproject.Common.Common;
import tbc.techbytecare.kk.touristguideproject.R;

public class SellActivity extends AppCompatActivity {

    Button btnSend;

    EditText edtName,edtVehicle,edtPhone;

    FirebaseDatabase database;
    DatabaseReference locationDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        database = FirebaseDatabase.getInstance();

        btnSend = findViewById(R.id.btnSend);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtVehicle = findViewById(R.id.edtVehicle);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToFirebase();
            }
        });
    }

    private void sendToFirebase() {

        locationDb = database.getReference("Locations").child("users").child(edtPhone.getText().toString());

        locationDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Object> locationMap = new HashMap<>();
                locationMap.put("name", edtName.getText().toString());
                locationMap.put("vehicle",edtVehicle.getText().toString());
                locationMap.put("lat","12.8874275");
                locationMap.put("lng","77.6428139");

                locationDb.updateChildren(locationMap);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
