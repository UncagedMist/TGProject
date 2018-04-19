package tbc.techbytecare.kk.touristguideproject.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hitomi.cmlibrary.CircleMenu;
import com.hitomi.cmlibrary.OnMenuSelectedListener;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import info.hoang8f.widget.FButton;
import io.paperdb.Paper;
import tbc.techbytecare.kk.touristguideproject.Assistant.AssistantActivity;
import tbc.techbytecare.kk.touristguideproject.Common.Common;
import tbc.techbytecare.kk.touristguideproject.GuideSystem.GuideSignIn;
import tbc.techbytecare.kk.touristguideproject.GuideSystem.GuideSignUp;
import tbc.techbytecare.kk.touristguideproject.Helper.LocaleHelper;
import tbc.techbytecare.kk.touristguideproject.Model.HomePlace;
import tbc.techbytecare.kk.touristguideproject.R;
import tbc.techbytecare.kk.touristguideproject.TouristSystem.TouristSignIn;
import tbc.techbytecare.kk.touristguideproject.TouristSystem.TouristSignUp;
import tbc.techbytecare.kk.touristguideproject.ViewHolder.HomePlaceViewHolder;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_CALL = 1;

    FloatingActionButton fabRegister,fabSignIn,fabSignOut,fabCallMan,fabCallAuto;
    TextToSpeech textToSpeech;
    EditText edtSpeak;

    FirebaseDatabase database;
    DatabaseReference reference;

    RecyclerView recyclerView;

    EditText edtCall;
    ImageView imgCall;

    FirebaseRecyclerAdapter<HomePlace,HomePlaceViewHolder> adapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase,"en"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Travel Management");
        setSupportActionBar(toolbar);

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("PlacesCategory");

        Paper.init(this);

        recyclerView = findViewById(R.id.recyclerPlace);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        fabRegister = findViewById(R.id.fabRegister);
        fabSignIn = findViewById(R.id.fabSignIn);
        fabSignOut = findViewById(R.id.fabSignOut);
        fabCallAuto = findViewById(R.id.fabCallAuto);
        fabCallMan = findViewById(R.id.fabCallMan);

        //language
        String language = Paper.book().read("language");

        if (language == null)   {
            Paper.book().write("language","en");
        }

        updateView((String)Paper.book().read("language"));

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView txtFullName = headerView.findViewById(R.id.txtFullName);
        TextView txtCategory = headerView.findViewById(R.id.txtCategory);

        if (Common.currentGuide != null && Common.currentTourist != null)   {
            txtFullName.setText(Common.currentTourist.getName());

            txtCategory.setText(Common.currentTourist.getUserType());
        }


        fabRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRegDialog();
            }
        });

        fabSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogDialog();
            }
        });

        fabSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            }
        });

        fabCallMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog();
            }
        });

        fabCallAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCallAuto();
            }
        });

        loadListPlace();
    }

    private void makePhoneCallAuto() {
        String number = "123456789";

        if (ContextCompat.checkSelfPermission(HomeActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)   {

            ActivityCompat.requestPermissions(HomeActivity.this,new String[]
                    {Manifest.permission.CALL_PHONE},REQUEST_CALL);
        }
        else {
            String dial = "tel:" + number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
        }
    }

    private void callDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View phone = inflater.inflate(R.layout.phone_dialog,null,false);

        edtCall = phone.findViewById(R.id.edtPhoneNumber);
        imgCall = phone.findViewById(R.id.imgCall);

        builder.setView(phone);

        imgCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCallManually();
            }
        });

        builder.show();
    }

    private void makePhoneCallManually() {
        try {
            String number = edtCall.getText().toString();

        if (number.trim().length() > 0)    {

            if (ContextCompat.checkSelfPermission(HomeActivity.this,
                    Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)   {

                ActivityCompat.requestPermissions(HomeActivity.this,new String[]
                        {Manifest.permission.CALL_PHONE},REQUEST_CALL);
            }
            else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        }
        else {
            Toast.makeText(this, "Enter Phone Number!!!!", Toast.LENGTH_SHORT).show();
        }
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL)    {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)    {
                makePhoneCallManually();
                makePhoneCallAuto();
            }
            else {
                Toast.makeText(this, "Please Provide Permission!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadListPlace() {

        FirebaseRecyclerOptions<HomePlace> allPlace = new FirebaseRecyclerOptions.Builder<HomePlace>()
                .setQuery(reference,HomePlace.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<HomePlace, HomePlaceViewHolder>(allPlace) {
            @Override
            protected void onBindViewHolder(@NonNull HomePlaceViewHolder holder, final int position, @NonNull HomePlace model) {
                holder.txtPlaceName.setText(model.getPlaceName());
                Picasso.with(HomeActivity.this).load(model.getPlaceImage()).into(holder.imgPlaceImage);


                holder.imgPlaceImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this,DetailsActivity.class);
                        intent.putExtra("placeName",adapter.getRef(position).getKey());
                        startActivity(intent);


                    }
                });

                holder.txtPlaceName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this,DetailsActivity.class);
                        intent.putExtra("placeName",adapter.getRef(position).getKey());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public HomePlaceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.home_place,parent,false);

                return new HomePlaceViewHolder(view);
            }
        };
        adapter.startListening();

        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)    {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the menu action
        }
        else if (id == R.id.nav_assistant) {
            startActivity(new Intent(HomeActivity.this, AssistantActivity.class));
        }

        else if (id == R.id.nav_Sell) {
            startActivity(new Intent(HomeActivity.this, SellActivity.class));
        }

        else if (id == R.id.nav_Rent) {
            startActivity(new Intent(HomeActivity.this, PointerTagActivity.class));
        }

        else if (id == R.id.nav_Transfer) {
            transferDialog();
        }

        else if (id == R.id.nav_GTranslate) {
            startActivity(new Intent(HomeActivity.this, TranslateActivity.class));
        }
        else if (id == R.id.nav_speech) {
            textToSpeech();
        }

        else if (id == R.id.nav_language) {
            languageDialog();
        }

        else if (id == R.id.nav_exit) {
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void transferDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Transfer Mode");

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View transfer = inflater.inflate(R.layout.transfer_options,null,false);

        FButton btnOffline = transfer.findViewById(R.id.btnOffline);
        FButton btnOnline = transfer.findViewById(R.id.btnOnline);
        FButton btnPaypal = transfer.findViewById(R.id.btnPaypal);

        builder.setIcon(R.drawable.ic_payment_black_24dp);
        builder.setView(transfer);

        btnOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, PaytmActivity.class));
            }
        });

        btnOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, MoneyOrderActivity.class));
            }
        });

        btnPaypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this,PaypalDev.class));
            }
        });
        builder.show();
    }

    private void showRegDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Register As");

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View logReg = inflater.inflate(R.layout.log_or_reg,null,false);

        FButton btnGuide = logReg.findViewById(R.id.btnGuide);
        FButton btnTravel = logReg.findViewById(R.id.btnTraveller);

        builder.setIcon(R.drawable.ic_person_add_black_24dp);
        builder.setView(logReg);

        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, GuideSignUp.class));
            }
        });

        btnTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, TouristSignUp.class));
            }
        });
        builder.show();
    }

    private void showLogDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Log-in As");

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View logReg = inflater.inflate(R.layout.log_or_reg,null,false);

        FButton btnGuide = logReg.findViewById(R.id.btnGuide);
        FButton btnTravel = logReg.findViewById(R.id.btnTraveller);

        builder.setIcon(R.drawable.ic_person_black_24dp);
        builder.setView(logReg);

        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, GuideSignIn.class));
            }
        });

        btnTravel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, TouristSignIn.class));
            }
        });
        builder.show();
    }

    private void updateView(String language) {
        Context context = LocaleHelper.setLocale(this,language);

        Resources resources = context.getResources();

//        TextView textView = findViewById(R.id.txtHlw);
//        textView.setText(resources.getString(R.string.hello));
    }

    private void languageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Language");

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View changeLan = inflater.inflate(R.layout.language_list,null,false);

        FButton btnEnglish = changeLan.findViewById(R.id.btnEng);
        FButton btnVietnam = changeLan.findViewById(R.id.btnVietnam);
        FButton btnKannada = changeLan.findViewById(R.id.btnKannada);
        FButton btnHindi = changeLan.findViewById(R.id.btnHindi);

        builder.setIcon(R.drawable.ic_language_black_24dp);
        builder.setView(changeLan);

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().write("language","en");
                updateView((String)Paper.book().read("language"));
            }
        });

        btnVietnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().write("language","vi");
                updateView((String)Paper.book().read("language"));
            }
        });

        btnKannada.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().write("language","kn");
                updateView((String)Paper.book().read("language"));
            }
        });

        btnHindi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().write("language","hi");
                updateView((String)Paper.book().read("language"));
            }
        });
        builder.show();
    }

    private void textToSpeech() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
        View textSpeech = inflater.inflate(R.layout.text_to_speech,null,false);

        final FButton btnSpeak = textSpeech.findViewById(R.id.btnSpeak);
        edtSpeak = textSpeech.findViewById(R.id.edtSpeak);

        builder.setView(textSpeech);

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {

                if (status == TextToSpeech.SUCCESS) {

                    int result = textToSpeech.setLanguage(Locale.ENGLISH);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED)  {

                        Toast.makeText(HomeActivity.this, "Language Not Supported!!!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        btnSpeak.setEnabled(true);
                        textToSpeech.setPitch(0.6f);
                        textToSpeech.setSpeechRate(1.0f);
                        speak();
                    }
                }
            }
        });

        btnSpeak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                speak();
            }
        });

        builder.show();
    }

    private void speak() {
        String text = edtSpeak.getText().toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)  {
            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        }
        else {
            textToSpeech.speak(text,TextToSpeech.QUEUE_FLUSH,null);
        }
    }

    @Override
    protected void onDestroy() {

        if (textToSpeech != null)   {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
