package tbc.techbytecare.kk.touristguideproject.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import tbc.techbytecare.kk.touristguideproject.R;


public class RentActivity extends AppCompatActivity {
    DatabaseReference data;
    private ArrayList<String> str = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rent);
        final ListView List;
        List = (ListView) findViewById(R.id.qwerty);
        data = FirebaseDatabase.getInstance().getReference();
        this.data = data;
        data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                  /* String user=snapshot.getKey();

                    System.out.println(user);
                    System.out.println(snapshot.child("Lat"));
                    System.out.println(snapshot.child("Long"));
                    System.out.println(snapshot.child("Type"));*/
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}