package com.example.harsh183.eatwell;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalTime;

/**
 * This class holds the functions that manipulate and use iCal/ics data
 */

public final class ICal {

    /**
     * Extracts the data out of the ical text into a 2d ArrayList.
     *
     * @param icalText string that has all the text from the ical text file
     * @return A 2dArray list. The first level is the days of the week (MTWRF), the second level is
     * just the events for that day.
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static WeeklySchedule extractEvents(String icalText) {
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
     * gets calendar and turns it into one long string
     * @param name
     * @return name in string form
     */
    public static String getCalendar(String name) {
        //when calling get calendar ask for file name
        try {
            return openFileInput(name).toString();
        } catch(Exception e) {
            System.out.println("File not found. Please retry.");
            return "";
        }
    }

    /**
     * Helper function to convert the formatted string from the ics file
     *
     * @param time
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.O) // TODO: Do something about this
    private static LocalTime convertStringToLocalTime(String time) {
        int hourTime = Integer.parseInt(time.substring(0, 2));
        int minuteTime = Integer.parseInt(time.substring(2, 4));
        return LocalTime.of(hourTime, minuteTime, 0);
    }

}
