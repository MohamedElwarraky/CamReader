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
    private TextToSpeech speak;

    static final int CAMERA_PIC_REQUEST = 1;
    static final int GALLERY_PIC_REQUEST = 0;

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
        //TODO: Ali: AlertDialog
        //Create AlertDialog with two items to choose between them
        // Initialize AlertDialog
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        // Set Title of AlertDialog
        pictureDialog.setTitle("Select Action");
        // Array contains the Items to show in Dialog
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Switch Case between the two options in the ALertDialog
                        switch (which) {
                            case 0:
                                // Camera option
                                GetPictureIntent();
                                break;
                            case 1:
                                // Gallery option
                                TakePictureIntent();
                                break;
                        }
                    }
                });
        // Show AlertDialog
        pictureDialog.show();
    }


    private void TakePictureIntent() {
        //TODO: Ali: intent
        //Intent to to open the Camera application on user's phone
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Start Camera Intent
            startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        }
    }

    private void GetPictureIntent() {
        //TODO: Abdallah: implementaion
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //TODO: Ali: intent
        super.onActivityResult(requestCode, resultCode, data);
        // Switch Case to see if the requestCode is from Camera Intent or Gallery Intent
        switch (requestCode) {
            case GALLERY_PIC_REQUEST:
                if (data != null) {
                    // Get Data (Image)
                    Uri contentURI = data.getData();
                    try {
                        // Set data to Bitmap Image
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        // Call TextFromImage function
                        getTextFromImage(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case CAMERA_PIC_REQUEST:
                if(resultCode == RESULT_OK) {
                    // Get data (Image)
                    Bundle extras = data.getExtras();
                    // Set data to Bitmap image
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    // Call TextFromImage function
                    getTextFromImage(imageBitmap);
                }
                break;
        }
    }

    public void getTextFromImage(Bitmap bitmap) {
        //TODO: Khaled: read text from image
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        //if couldn't recognize any text
        if (!textRecognizer.isOperational()) {
            speak.speak("could not get the text", TextToSpeech.QUEUE_FLUSH, null);
        } else {
            // Extract the text from bitmap photo and save the text in array
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
    }

    @Override
    public void onUtteranceCompleted(String utteranceId) {
        //TODO: Abdallah: implementaion
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
