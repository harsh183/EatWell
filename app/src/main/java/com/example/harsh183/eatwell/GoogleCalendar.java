package com.example.harsh183.eatwell;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.google.api.services.calendar.model.CalendarList;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import org.json.JSONObject;
import com.android.volley.RequestQueue;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import java.util.Collections;
import java.util.List;


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
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    //const SCOPES = ['https://www.googleapis.com/auth/calendar'];

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR_READONLY);
    private static final String CREDENTIALS_FILE_PATH = "/client_id.json";


    private void getCalendarId() {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://www.googleapis.com/calendar/v3/users/me/calendarList",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
                            try {
                                CalendarList listofCals = (CalendarList) response.get("items");
                                calendarId = (String) listofCals.get("id");
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
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
    private void getCalendarList(String calId) {
        try {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "https://www.googleapis.com/calendar/v3/calendars/" + calendarId + "/events",
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(final JSONObject response) {
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
    private JSONObject getCalendar() {
        getCalendarId();
        getCalendarList(calendarId);
        return icsFile;
    }

}
