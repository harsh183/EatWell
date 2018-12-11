package com.example.harsh183.eatwell;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class represents all the info we actually need about a student event.
 *
 * Basically this holds all the extracted data from the ical/google calendar file that is needed for the file
 */
@RequiresApi(api = Build.VERSION_CODES.O)
class StudentEvent implements Comparable{
    /**
     * Start time of event.
     */
    LocalTime startTime;

    /**
     * End time of event.
     */
    LocalTime endTime;

    /**
     * Location of event
     */
    String location;

    /**
     * Start date of event
     */
    LocalDate startDate;

    /**
     * End date of event
     */
    LocalDate endDate;

    /**
     * Weekdays
     */
    DayOfWeek[] weekdays = new DayOfWeek[0];

    /**
     * Checks if a given timing is within this event. Can be used to check for scheduling conflicts.
     *
     * @param timing LocalTime object that represents a single timing (ex. 20:00)
     * @return true if the timing falls within the event
     */
    public boolean isTimingWithinEvent(LocalTime timing) {
        return (timing.isAfter(startTime) && timing.isBefore(endTime));
    }

    public boolean doesEventConflict(StudentEvent anotherEvent) {
        return (isTimingWithinEvent(anotherEvent.startTime) || isTimingWithinEvent(anotherEvent.endTime));
    }

    /**
     * Get duration of event in minutes.
     *
     * @return duration of event (end-start) in minutes
     */
    public int duration() {
        return (endTime.getHour() - startTime.getHour()) * 60 + (endTime.getMinute() - startTime.getMinute());
    }

    /**
     * Compares two events based on start timing.
     *
     * @param o Other event
     * @return positive if first event is greater, negative if second event is great and 0 if equal
     */
    @Override
    public int compareTo(Object o) {
        if (o instanceof StudentEvent) {
            StudentEvent event = (StudentEvent) o;
            return startTime.compareTo(event.startTime);
        } else {
            // TODO: Some other way to throw an error
            return 0;
        }
    }

    /**
     * Convert days of the week into DayOfWeek enums
     * @param weekdaysInStrings an array of DayOfWeek objects that correspond to the string
     */
    public void parseDaysOfWeek(String[] weekdaysInStrings) {
        // TODO: Maybe a neater map, lots of reinventing the wheel in the project here that can be refactored later.
        weekdays = new DayOfWeek[weekdaysInStrings.length];
        for (int i = 0; i < weekdays.length; i++) {
            String weekdayString = weekdaysInStrings[i];
            int index = mapWeekDaysToArrayIndex(weekdayString);
            weekdays[i] = DayOfWeek.values()[index];
        }
    }

    /**
     * Helper function that takes the weekday into account
     * @param weekday Given weekday
     * @return Index in corresponding array
     */
    private int mapWeekDaysToArrayIndex(String weekday) {
        weekday = weekday.trim();
        // TODO: Double check saturday and sunday
        String[] weekdays = {"MO", "TU", "WE", "TH", "FR", "SA", "SU"};
        // TODO: This is linear search, replace
        for (int i = 0; i < weekdays.length; i++) {
            String day = weekdays[i];
            if (day.equals(weekday)) {
                return i;
            }
        }
        return -1;
    }

    public String toString() {
        return ("Location: " + location + "\n" + startTime + "\n" + endTime + "\n" + startDate + "\n" + endDate + "\n");


    }
}