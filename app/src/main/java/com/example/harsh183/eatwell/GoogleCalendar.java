package com.example.harsh183.eatwell;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * This class is where we have stored the methods that obtain the calendar through the Google calendar API.
 */
public class GoogleCalendar {
    private String calendarId;
    private JSONObject icsFile;
    /** Default logging tag for messages from the main activity. */
    private static final String TAG = "EatWell:GoogleCalendar";

    /** Request queue for our network requests. */
    private static RequestQueue requestQueue;

    void callCalendarAPI() {
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
        callCalendarAPI();
        return icsFile;
    }
    //TODO: public String outputWhenToEat()
}
