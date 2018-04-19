package tbc.techbytecare.kk.touristguideproject.GuideSystem;

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

import tbc.techbytecare.kk.touristguideproject.Model.UserGuide;
import tbc.techbytecare.kk.touristguideproject.R;

public class GuideSignUp extends AppCompatActivity {

    MaterialEditText edtName,edtEmail,edtPhone,edtPassword;

    Button btnGuideCreate;

    FirebaseDatabase database;
    DatabaseReference usersGuide;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_sign_up);

        database = FirebaseDatabase.getInstance();
        usersGuide = database.getReference("UsersGuide");

        edtEmail = findViewById(R.id.edtEmail);
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);

        btnGuideCreate = findViewById(R.id.btnGuideCreate);

        btnGuideCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUpGuide();
            }
        });
    }

    private void signUpGuide() {

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
            final ProgressDialog dialog = new ProgressDialog(GuideSignUp.this);
            dialog.setTitle("Signing Up");
            dialog.setMessage("Please wait! while we Register Your Account!!");
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            usersGuide.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    dialog.dismiss();

                    if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                        Toast.makeText(GuideSignUp.this, "Already Registered!!!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        UserGuide userGuide = new UserGuide(
                                edtPhone.getText().toString(),
                                edtName.getText().toString(),
                                edtEmail.getText().toString(),
                                edtPassword.getText().toString()
                        );
                        usersGuide.child(edtPhone.getText().toString()).setValue(userGuide);

                        Toast.makeText(GuideSignUp.this, "Good to go...", Toast.LENGTH_SHORT).show();
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
