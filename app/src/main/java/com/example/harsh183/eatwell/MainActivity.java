package com.example.harsh183.eatwell;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.api.services.calendar.Calendar;

//best project ever


import java.time.LocalTime;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * gets calendar and turns it into one long string
     * @param name
     * @return name in string form
     */
    public String getCalendar(String name) {
        //when calling get calendar ask for file name
        try {
            return openFileInput(name).toString();
        } catch(Exception e) {
            System.out.println("File not found. Please retry.");
            return "";
        }
    }

    /**
     *
     * @param calendarId
     * @return should add event
     */
     public String writeToCalendar(String calendarId) {
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
     }

    /**
     * Extracts the data out of the ical text into a 2d ArrayList.
     *
     * @param icalText string that has all the text from the ical text file
     * @return A 2dArray list. The first level is the days of the week (MTWRF), the second level is
     * just the events for that day.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private WeeklySchedule extractEvents(String icalText) {
        // TODO: Or use a lib for this later, but rn it seems tedious af
        // TODO: Also parse start and end dates
        WeeklySchedule schedule = new WeeklySchedule();

        // First break into lines
        String[] lines = icalText.split("\n"); // Split based on new lines

        // Loop through the lines, and set a flag and then look for remaining lines
        // TODO: A better approach (idk if parsing as tree is worth it tho)
        StudentEvent currentEvent = new StudentEvent();
        boolean foundEvent = false;
        for (String line: lines) {
            if (line.contains("BEGIN:VEVENT")) {
                currentEvent = new StudentEvent();
                foundEvent = true;
            }

            if (line.contains("END:VEVENT")) {
                foundEvent = false;
            }

            if (foundEvent) {
                // Start time
                if (line.contains("DTSTART;")) {
                    String startTime = line.substring(line.length() - 7); // Last six chars
                    currentEvent.startTime = convertStringToLocalTime(startTime);
                }

                // End time
                if (line.contains("DTEND;")) {
                    String endTime = line.substring(line.length() - 7); // Last six chars
                    currentEvent.endTime = convertStringToLocalTime(endTime);
                }

                // Location
                if (line.contains("LOCATION:")) {
                    // TODO: Figure out if we have to change the location format later
                    // TODO: A more elegant way to do this
                    currentEvent.location = line.split("LOCATION:")[1];
                }

                // Weekdays (figure out where to insert)
                String[] weekdays = new String[0];
                if (line.contains("BYDAY=")) {
                    weekdays = line.split("BYDAY=")[1].split(",");
                }
                schedule.addEventToGivenDays(currentEvent, weekdays);

            }
        }

        return schedule;
    }

    /**
     * Helper function to convert the formatted string from the ics file
     *
     * @param time
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O) // TODO: Do something about this
    private LocalTime convertStringToLocalTime(String time) {
        int hourTime = Integer.parseInt(time.substring(0, 2));
        int minuteTime = Integer.parseInt(time.substring(2, 4));
        return LocalTime.of(hourTime, minuteTime, 0);
    }
}