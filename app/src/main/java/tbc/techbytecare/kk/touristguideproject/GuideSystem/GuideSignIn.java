package tbc.techbytecare.kk.touristguideproject.GuideSystem;

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
import tbc.techbytecare.kk.touristguideproject.Model.UserGuide;
import tbc.techbytecare.kk.touristguideproject.R;

public class GuideSignIn extends AppCompatActivity {

    MaterialEditText edtPhone,edtPassword;

    Button btnGuideLog;

    FirebaseDatabase database;
    DatabaseReference usersGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_sign_in);

        database = FirebaseDatabase.getInstance();
        usersGuide = database.getReference("UsersGuide");

        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);

        btnGuideLog = findViewById(R.id.btnGuideLog);

        btnGuideLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInGuide();
            }
        });
    }

    private void signInGuide() {

        if (TextUtils.isEmpty(edtPhone.getText().toString()))   {
            Toast.makeText(this, "Please Enter Phone!!!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(edtPassword.getText().toString()))   {
            Toast.makeText(this, "Please Enter Password!!!", Toast.LENGTH_SHORT).show();
        }
        else {
            final ProgressDialog dialog = new ProgressDialog(GuideSignIn.this);
            dialog.setTitle("Signing In");
            dialog.setMessage("Please wait! while we Register Your Account!!");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            usersGuide.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    usersGuide.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dialog.dismiss();

                            if (dataSnapshot.child(edtPhone.getText().toString()).exists())   {

                                UserGuide userGuide = dataSnapshot.child(edtPhone.getText().toString()).getValue(UserGuide.class);
                                userGuide.setPhone(edtPhone.getText().toString());//set phone

                                if (userGuide.getPassword().equals(edtPassword.getText().toString())) {

                                    dialog.dismiss();
                                    Toast.makeText(GuideSignIn.this, "Welcome! Sign In Successful!!", Toast.LENGTH_SHORT).show();

                                    Intent homeIntent = new Intent(GuideSignIn.this, HomeActivity.class);
                                    Common.currentGuide = userGuide;
                                    startActivity(homeIntent);
                                    finish();

                                    usersGuide.removeEventListener(this);
                            }
                                else {
                                    dialog.dismiss();
                                    Toast.makeText(GuideSignIn.this, "Wrong Password!!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                dialog.dismiss();
                                Toast.makeText(GuideSignIn.this, "Not Registered Yet! Kindly Register", Toast.LENGTH_SHORT).show();
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
