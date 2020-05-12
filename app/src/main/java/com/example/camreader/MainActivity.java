package com.example.camreader;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {

    EditText textView;
    Button listenBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get TextView from xml
        textView = findViewById(R.id.text);

        // get Button in xml
        listenBtn = findViewById(R.id.listenBtn);
        listenBtn.setBackgroundResource(R.drawable.ic_play_arrow_48dp);

        //TODO: Ali: intent

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });
    }

    private void showPictureDialog() {
    }

    private void TakePictureIntent() {
        //TODO: Ali: intent
    }

    private void GetPictureIntent() {
        //TODO: Abdallah: implementaion
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //TODO: Ali: intent
    }

    public void getTextFromImage(Bitmap bitmap) {
        //TODO: Khaled: read text from image
        //Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.p);
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        //if couldn't recognize any text
        if (!textRecognizer.isOperational()) {
            speak.speak("could not get the text", TextToSpeech.QUEUE_FLUSH, null);
        } else {
            // Extract the text
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < items.size(); ++i) {
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");
            }
            //here the output
            textView.setText(sb.toString());
        }

    }

    @Override
    public void onInit(int status) {
        //TODO: Abdallah: implementaion
        if (status == TextToSpeech.SUCCESS) {
            speak.setLanguage(Locale.ENGLISH);
            speak.setOnUtteranceCompletedListener(this);
        }
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        //TODO: Abdallah: implementaion
        listenBtn.setBackgroundResource(R.drawable.ic_play_arrow_48dp);
    }

    public void listen(View view) {
        //TODO: AHMED_ASHRAF: read text

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
