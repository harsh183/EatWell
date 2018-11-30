package com.example.harsh183.eatwell;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * This class represents all the info we actually need about a student event.
 *
 * Basically this holds all the extracted data from the ical file that is needed for the file
 */
class StudentEvent {
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
}
