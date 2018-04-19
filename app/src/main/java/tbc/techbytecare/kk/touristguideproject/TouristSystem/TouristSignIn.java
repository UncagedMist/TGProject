package tbc.techbytecare.kk.touristguideproject.TouristSystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import tbc.techbytecare.kk.touristguideproject.Activity.HomeActivity;
import tbc.techbytecare.kk.touristguideproject.Common.Common;
import tbc.techbytecare.kk.touristguideproject.GuideSystem.GuideSignIn;
import tbc.techbytecare.kk.touristguideproject.Model.UserGuide;
import tbc.techbytecare.kk.touristguideproject.Model.UserTourist;
import tbc.techbytecare.kk.touristguideproject.R;

public class TouristSignIn extends AppCompatActivity {

    MaterialEditText edtPhone,edtPassword;

    Button btnTouristLog;

    FirebaseDatabase database;
    DatabaseReference usersTourist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_sign_in);

        database = FirebaseDatabase.getInstance();
        usersTourist = database.getReference("UsersTourist");

        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);

        btnTouristLog = findViewById(R.id.btnTouristLog);

        btnTouristLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInTourist();
            }
        });
    }

    private void signInTourist() {
        if (TextUtils.isEmpty(edtPhone.getText().toString()))   {
            Toast.makeText(this, "Please Enter Phone!!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(edtPassword.getText().toString()))   {
            Toast.makeText(this, "Please Enter Password!!!", Toast.LENGTH_SHORT).show();
        }
        else {
            final ProgressDialog dialog = new ProgressDialog(TouristSignIn.this);
            dialog.setTitle("Signing In");
            dialog.setMessage("Please wait! while we Register Your Account!!");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            usersTourist.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    usersTourist.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dialog.dismiss();

                            if (dataSnapshot.child(edtPhone.getText().toString()).exists())   {

                                UserTourist userTourist = dataSnapshot.child(edtPhone.getText().toString()).getValue(UserTourist.class);
                                userTourist.setPhone(edtPhone.getText().toString());//set phone

                                if (userTourist.getPassword().equals(edtPassword.getText().toString())) {

                                    dialog.dismiss();
                                    Toast.makeText(TouristSignIn.this, "Welcome! Sign In Successful!!", Toast.LENGTH_SHORT).show();

                                    Intent homeIntent = new Intent(TouristSignIn.this, HomeActivity.class);
                                    Common.currentTourist = userTourist;
                                    startActivity(homeIntent);
                                    finish();

                                    usersTourist.removeEventListener(this);
                                }
                                else {
                                    dialog.dismiss();
                                    Toast.makeText(TouristSignIn.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(TouristSignIn.this, "Not Registered Yet! Kindly Register", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }
}
