package com.example.harsh183.eatwell;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;   
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;


//best project ever


import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {

    Button browseFilesButton;
    private String calendarId;
    private JSONObject icsFile;
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "Lab12:Main";

    /** Request queue for our network requests. */
    private static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browseFilesButton = findViewById(R.id.browse_button);
        browseFilesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivity(intent);
            }
        });

    }
    void startAPICall() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://www.googleapis.com/calendar/" + calendarId + "/events",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            icsFile = response; //get actual ICS file
                            Log.d(TAG, response.toString());
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(final VolleyError error) {
                    Log.w(TAG, error.toString());
                }
            });
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public JSONObject getCalendar(String calendarIdInput) {
        calendarId = calendarIdInput;
        startAPICall();
        return icsFile;
    }

    /**
     * writes new event to the user's calendar using calculated times and dates
     * @param calendarId
     * @return should add event
     public String writeToCalendar(String calendarId) {
         Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials)
                 .setApplicationName("applicationName").build();

         Event meal = new Event();
         .setSummary("Eat well!"); //insert name of meal - will figure out how to get it later
         //import start time and date from calculator
         DateTime startDateTime = new DateTime("2015-05-28T09:00:00-07:00");
         EventDateTime startTime = new EventDateTime()
                 .setDateTime(startDateTime)
                 .setTimeZone("America/Chicago"); //add time zone support
         meal.setStart(startTime);
         //import end time and date
         DateTime endDateTime = new DateTime("2015-05-28T17:00:00-07:00");
         EventDateTime endTime = new EventDateTime()
                 .setDateTime(endDateTime)
                 .setTimeZone("America/Chicago");
         meal.setEnd(endTime);
         //handle recurrence once we decide when we are eating
         String[] recurrence = new String[] {"RRULE:FREQ=NONE;COUNT=0"};
         meal.setRecurrence(Arrays.asList(recurrence));
         meal = service.events().insert(calendarId, meal).execute();
         System.out.print("Event created: %s\n", meal.getHtmlLink());
         url = "http://httpbin.org/post";
         StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                 new Response.Listener<String>()
                 {
                     @Override
                     public void onResponse(String response) {
                         // response
                         Log.d("Response", response);
                     }
                 },
                 new Response.ErrorListener()
                 {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         // error
                         Log.d("Error.Response", response);
                     }
                 }
         ) {
             @Override
             protected Map<String, String> getParams()
             {
                 Map<String, String>  params = new HashMap<String, String>();
                 params.put("name", "Alif");
                 params.put("domain", "http://itsalif.info");

                 return params;
             }
         };
         queue.add(postRequest);
         //POST http://www.google.com/calendar/feeds/jo@gmail.com/private/full
     } */





}