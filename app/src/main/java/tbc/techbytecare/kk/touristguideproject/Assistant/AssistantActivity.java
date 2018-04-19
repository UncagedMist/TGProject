package tbc.techbytecare.kk.touristguideproject.Assistant;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.dnkilic.waveform.WaveView;

import java.util.ArrayList;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import tbc.techbytecare.kk.touristguideproject.R;

public class AssistantActivity extends AppCompatActivity {

    private static final int REQ_CODE_SPEECH_INPUT = 100;

    FButton btnEndSpeech,btnStartSpeech,btnPauseSpeech,btnReset,btnStart;

    private WaveView waveView;

    TextView txtSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant);

        btnStartSpeech = findViewById(R.id.btnStartSpeech);
        btnStart = findViewById(R.id.btnStart);
        btnEndSpeech = findViewById(R.id.btnEndSpeech);
        btnPauseSpeech = findViewById(R.id.btnPauseSpeech);
        btnReset = findViewById(R.id.btnReset);

        txtSpeech = findViewById(R.id.txtSpeech);

        waveView = findViewById(R.id.waveView);

        onClickListener();
    }

    private void onClickListener() {

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waveView.stop();
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                waveView.initialize(displayMetrics);
            }
        });

        btnPauseSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waveView.speechPaused();
            }
        });

        btnEndSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waveView.speechEnded();
            }
        });

        btnStartSpeech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                waveView.speechStarted();
                startVoiceInput();
            }
        });
    }

    private void startVoiceInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");

        try {
            startActivityForResult(intent,REQ_CODE_SPEECH_INPUT);
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    txtSpeech.setText(result.get(0));
                }
                break;
            }

        }
    }
}
