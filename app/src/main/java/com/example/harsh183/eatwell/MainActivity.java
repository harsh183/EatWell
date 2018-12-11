package com.example.harsh183.eatwell;

// TODO: Cleanup imports
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
            }

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }
        }

        browseFilesButton = findViewById(R.id.browse_button);
        browseFilesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, 666);
                successMessage("Here so far");
            }
        });

        generateScheduleButton = findViewById(R.id.generate);
        generateScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successMessage("Button clicked");

                /*String url ="http://tgftp.nws.noaa.gov/data/raw/fz/fzus53.klot.srf.lot.txt";
                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                successMessage("Woohoo got it!");
                                Log.i(TAG, response);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        successMessage(error.getMessage());
                    }
                });

                // Add the request to the RequestQueue.
                requestQueue.add(stringRequest);*/
                String newICS = "testLine1\ntestLine2\ntestLine3\n";
                File dir = new File ("/sdcard" + "/Download");
                dir.mkdirs();
                File file = new File(dir, "meals.ics");

                // get external storage file reference
                try {
                    FileWriter writer = new FileWriter(file);
                    // Writes the content to the file
                    writer.write("This\n is\n an\n example\n");
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }

                successMessage("Check logs for file generated");
            }
        });


    }

    /**
     * Handle the response from our IP geolocation API.
     *
     * @param response response from our IP geolocation API.
     */
    void apiCallDone(final JSONObject response) {
        try {
            Log.d(TAG, response.toString(2));
            // Example of how to pull a field off the returned JSON object

        } catch (JSONException ignored) {
            Log.e(TAG, ignored.getMessage());
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)  {
        if (requestCode == 666) {
            String Fpath = data.getDataString();
            successMessage("Found file " + Fpath);
            File newFile = new File("/sdcard/Download/Fall 2018 - Urbana-Champaign.ics"); // TODO: Replace hardcode

            //Read text from file
            StringBuilder text = new StringBuilder();

            try {
                BufferedReader br = new BufferedReader(new FileReader(newFile));
                String line;

                while ((line = br.readLine()) != null) {
                    successMessage("Here");
                    //Log.d(TAG, "LINE: " + line);
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
        toast.setGravity(Gravity.TOP|Gravity.START, 400, 1000);
        toast.show();
    }

}