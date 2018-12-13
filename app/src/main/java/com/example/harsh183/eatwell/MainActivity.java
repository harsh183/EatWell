package com.example.harsh183.eatwell;

// TODO: Cleanup imports
import java.io.InputStream;
import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


//best project ever


import java.time.LocalTime;
import java.util.Map;
import java.util.Scanner;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "EatWell:Main";

    Button browseFilesButton;
    Button generateScheduleButton;

    /** Request queue for our API requests. */
    private static RequestQueue requestQueue;

    String icsFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Permissions for read and write (mostly off google docs)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }

        // Set up the queue for our API requests
        requestQueue = Volley.newRequestQueue(this);

        browseFilesButton = findViewById(R.id.browse_button);
        browseFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                //intent.setType("*/*");
                //startActivityForResult(intent, 666);



                    // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
                    // browser.
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

                    // Filter to only show results that can be "opened", such as a
                    // file (as opposed to a list of contacts or timezones)
                    intent.addCategory(Intent.CATEGORY_OPENABLE);

                    // Filter to show only images, using the image MIME data type.
                    // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
                    // To search for all documents available via installed storage providers,
                    // it would be "*/*".
                    intent.setType("*/*");

                    startActivityForResult(intent, 666);

            }
        });

        generateScheduleButton = findViewById(R.id.generate);
        generateScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successMessage("Crunching your numbers on the server");

                String url ="http://10.0.2.2:4567/"; //TODO: Replace with proper remote server later
                try {
                    JSONObject jsonPayload = new JSONObject()
                            .put("content", icsFile);
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.POST,
                            url,
                            jsonPayload,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(final JSONObject response) {
                                    successMessage("API request successful");
                                    String mealTimeICS;
                                    try {
                                        mealTimeICS = response.get("result").toString();
                                        Log.w(TAG, mealTimeICS);
                                        File dir = new File ("/sdcard" + "/Download");
                                        dir.mkdirs();
                                        File file = new File(dir, "meals.ics");

                                        // get external storage file reference
                                        try {
                                            FileWriter writer = new FileWriter(file);
                                            // Writes the content to the file
                                            writer.write(mealTimeICS);
                                            writer.flush();
                                            writer.close();
                                            successMessage("Success: Check Download folder");
                                        } catch (IOException e) {
                                            Log.e(TAG, e.getMessage());
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(final VolleyError error) {
                            successMessage("Oh no");


                            Log.e(TAG, error.toString());
                        }
                    });
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                            30000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    jsonObjectRequest.setShouldCache(false);
                    requestQueue.add(jsonObjectRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if (requestCode == 666) {
            StringBuilder text = new StringBuilder();
            try {
                //Read text from file
                Uri fileUri = data.getData();
                InputStream inputStream = getContentResolver().openInputStream(fileUri);
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = br.readLine()) != null) {
                    successMessage("File read successful");
                    Log.d(TAG, "LINE: " + line);
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            }
            catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            icsFile = text.toString();
        }
    }

    public void successMessage (String successStatus) {
        Toast toast = Toast.makeText(getApplicationContext(), successStatus, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.TOP|Gravity.START, 350, 1500);
        toast.show();
    }
}