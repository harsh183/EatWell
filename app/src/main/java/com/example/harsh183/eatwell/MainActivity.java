package com.example.harsh183.eatwell;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

//import com.google.api.services.calendar.Calendar;

//best project ever


import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {

    Button browseFilesButton;

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


    /**
     *
     * @param calendarId
     * @return should add event
     */
     /*public String writeToCalendar(String calendarId) {
         Calendar service = new Calendar.Builder(httpTransport, jsonFactory, credentials)
                 .setApplicationName("applicationName").build();

         Event meal = new Event;
         .setSummary("Eat well!"); //insert name of meal - will figure out how to get it later
         //import start time and date from calculator
         DateTime startDateTime = new DateTime("2015-05-28T09:00:00-07:00");
         EventDateTime start = new EventDateTime()
                 .setDateTime(startDateTime)
                 .setTimeZone("America/Chicago"); //add time zone support
         meal.setStart(start);
         //import end time and date
         DateTime endDateTime = new DateTime("2015-05-28T17:00:00-07:00");
         EventDateTime end = new EventDateTime()
                 .setDateTime(endDateTime)
                 .setTimeZone("America/Chicago");
         meal.setEnd(end);
         //handle recurrence once we decide when we are eating
         String[] recurrence = new String[] {"RRULE:FREQ=NONE;COUNT=0"};
         meal.setRecurrence(Arrays.asList(recurrence));
         meal = service.events().insert(calendarId, meal).execute();
         System.out.print("Event created: %s\n", meal.getHtmlLink());
         POST http://www.google.com/calendar/feeds/jo@gmail.com/private/full
     }*/




}