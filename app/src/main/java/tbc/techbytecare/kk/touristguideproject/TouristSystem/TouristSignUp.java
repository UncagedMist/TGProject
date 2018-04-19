package tbc.techbytecare.kk.touristguideproject.TouristSystem;

import android.app.ProgressDialog;
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

import tbc.techbytecare.kk.touristguideproject.GuideSystem.GuideSignUp;
import tbc.techbytecare.kk.touristguideproject.Model.UserGuide;
import tbc.techbytecare.kk.touristguideproject.Model.UserTourist;
import tbc.techbytecare.kk.touristguideproject.R;

public class TouristSignUp extends AppCompatActivity {

    MaterialEditText edtName,edtEmail,edtPhone,edtPassword;

    Button btnTouristCreate;

    FirebaseDatabase database;
    DatabaseReference usersTourist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_sign_up);

        database = FirebaseDatabase.getInstance();
        usersTourist = database.getReference("UsersTourist");

        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);

        btnTouristCreate = findViewById(R.id.btnTouristCreate);

        btnTouristCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpTourist();
            }
        });
    }

    private void signUpTourist() {

        if (TextUtils.isEmpty(edtName.getText().toString()))    {
            Toast.makeText(this, "Please Enter Name !!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(edtEmail.getText().toString()))    {
            Toast.makeText(this, "Please Enter Email !!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(edtPhone.getText().toString()))    {
            Toast.makeText(this, "Please Enter Phone !!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(edtPassword.getText().toString()))    {
            Toast.makeText(this, "Please Enter Password !!!", Toast.LENGTH_SHORT).show();
        }
        else {
            final ProgressDialog dialog = new ProgressDialog(TouristSignUp.this);
            dialog.setTitle("Signing Up");
            dialog.setMessage("Please wait! while we Register Your Account!!");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            usersTourist.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dialog.dismiss();

                    if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                        Toast.makeText(TouristSignUp.this, "Already Registered!!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        UserTourist userTourist = new UserTourist(
                                edtPhone.getText().toString(),
                                edtName.getText().toString(),
                                edtEmail.getText().toString(),
                                edtPassword.getText().toString()
                        );
                        usersTourist.child(edtPhone.getText().toString()).setValue(userTourist);

                        Toast.makeText(TouristSignUp.this, "Good to go...", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }
}
