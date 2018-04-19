package tbc.techbytecare.kk.touristguideproject.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import info.hoang8f.widget.FButton;
import tbc.techbytecare.kk.touristguideproject.Model.BudgetPlaceDetails;
import tbc.techbytecare.kk.touristguideproject.Model.HomePlace;
import tbc.techbytecare.kk.touristguideproject.R;

public class DetailsActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference budget;

    FButton btnViewOnMap,btnCrime;

    ImageView imgPlace;

    TextView txtPlaceName,txtPlaceDescription;

    TextView txtDuration,txtWinter,txtSummer,txtVisitDuration;
    TextView txtVisit1,txtVisit2,txtVisit3,txtVisit4,txtVisit5;
    TextView txtPackage,txtDistrict,txtState;

    String placeName = "";

    HomePlace currentPlace;

    BudgetPlaceDetails currentDetail;

    VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("PlacesCategory");
        budget = database.getReference("BudgetPlaceDetails");

        btnViewOnMap = findViewById(R.id.btnViewOnMap);

        imgPlace = findViewById(R.id.imgPlace);

        txtDuration = findViewById(R.id.txtDuration);
        txtWinter = findViewById(R.id.txtWinter);
        txtSummer = findViewById(R.id.txtSummer);

        btnCrime = findViewById(R.id.btnCrime);

        txtVisitDuration = findViewById(R.id.txtVisitDuration);

        txtVisit1 = findViewById(R.id.txtVisit1);
        txtVisit2 = findViewById(R.id.txtVisit2);
        txtVisit3 = findViewById(R.id.txtVisit3);
        txtVisit4 = findViewById(R.id.txtVisit4);
        txtVisit5 = findViewById(R.id.txtVisit5);

        videoView = findViewById(R.id.videoView);

        txtPackage = findViewById(R.id.txtPackage);
        txtDistrict = findViewById(R.id.txtDistrict);
        txtState = findViewById(R.id.txtState);

        txtPlaceName = findViewById(R.id.txtPlaceName);
        txtPlaceDescription = findViewById(R.id.txtPlaceDescription);

        if (getIntent() != null)    {
            placeName = getIntent().getStringExtra("placeName");
        }
        try {
            if (!placeName.isEmpty()) {
                loadListPlace(placeName);
            }
        }
        catch (NullPointerException e)  {
            e.printStackTrace();
        }


        btnViewOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsActivity.this,MapsActivity.class));
            }
        });

        btnCrime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DetailsActivity.this,CrimeActivity.class));
            }
        });
    }

    private void loadListPlace(String placeName) {
        reference.child(placeName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentPlace = dataSnapshot.getValue(HomePlace.class);

                Picasso.with(getBaseContext()).load(currentPlace.getPlaceImage()).into(imgPlace);
                txtPlaceName.setText(currentPlace.getPlaceName());
                txtPlaceDescription.setText(currentPlace.getPlaceDescription());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        budget.child(placeName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currentDetail = dataSnapshot.getValue(BudgetPlaceDetails.class);

                txtDuration.setText(currentDetail.getTripDuration());
                txtWinter.setText(currentDetail.getWeatherWinter());
                txtSummer.setText(currentDetail.getWeatherSummer());
                txtVisitDuration.setText(currentDetail.getVisitTime());
                txtVisit1.setText(currentDetail.getVisitPlace1());
                txtVisit2.setText(currentDetail.getVisitPlace2());
                txtVisit3.setText(currentDetail.getVisitPlace3());
                txtVisit4.setText(currentDetail.getVisitPlace4());
                txtVisit5.setText(currentDetail.getVisitPlace5());
                txtPackage.setText(currentDetail.getPackagePlace());
                txtDistrict.setText(currentDetail.getDistrict());
                txtState.setText(currentDetail.getState());

                final ProgressDialog dialog = new ProgressDialog(DetailsActivity.this);
                dialog.setTitle("Wait Till the Video Loads...");

                dialog.setMessage("Buffering...");
                dialog.setIndeterminate(false);
                dialog.setCancelable(true);
                dialog.show();

                try {
                    MediaController controller = new MediaController(DetailsActivity.this);
                    controller.setAnchorView(videoView);

                    Uri video = Uri.parse(currentDetail.getVideoLink());
                    videoView.setMediaController(controller);
                    videoView.setVideoURI(video);

                    videoView.requestFocus();

                videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        dialog.dismiss();
                        videoView.start();
                    }
                });

                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
