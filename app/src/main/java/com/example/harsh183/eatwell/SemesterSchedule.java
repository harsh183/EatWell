package com.example.harsh183.eatwell;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;

// TODO: Put in a proper csv parser instead of this hacky shit later

/**
 * Class that represents the schedule for a semester.
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public class SemesterSchedule {
    // TODO: Generalize for more arbitrary periods
    /**
     * Object representing the schedule. Each inner list is for a single day and there is a new one for every semester
     */
    private List<List<StudentEvent>> schedule;

    /**
     * The day the semester starts.
     */
    private LocalDate startDay;

    /**
     * The day the semester ends
     */
    private LocalDate endDay;

    /**
     * Constructor to initialize the object. Start and end inclusive.
     *
     * @param setStartDay The day the calendar starts
     * @param setEndDay The day it ends
     */
    public SemesterSchedule(LocalDate setStartDay, LocalDate setEndDay) {
        startDay = setStartDay;
        endDay = setEndDay;

        int days = (int) DAYS.between(startDay, endDay) + 1;
        schedule = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            schedule.add(new ArrayList<StudentEvent>());
        }
    }

    /**
     * Get the entire schedule as a nested list.
     *
     * @return A two dimensional list. The first level is the days of the week and the second level
     *         is the events for that day
     */
    public List<List<StudentEvent>> getSchedule() {
        return schedule;
    }

    /**
     * Get start day of schedule
     *
     * @return day as LocalDate
     */
    public LocalDate getStartDay() {
        return startDay;
    }

    /**
     * Get end day of schedule
     *
     * @return day as LocalDay
     */
    public LocalDate getEndDay() {
        return endDay;
    }

    /**
     * Get the schedule for a given day.
     *
     * @param day A date where we want the schedule.
     * @return A sorted list containing events for the day
     */
    public List<StudentEvent> getScheduleForDay(LocalDate day) {
        int index = dateToIndex(day);
        return getScheduleForDay(index);
    }

    /**
     * Get the schedule for a given day.
     * @param index Index that corresponds to the internal array.
     * @return A sorted list containing events for that day
     */
    public List<StudentEvent> getScheduleForDay(int index) {
        // TODO: Move sorting on creation
        List<StudentEvent> daySchedule = schedule.get(index);
        Collections.sort(daySchedule);
        return daySchedule;
    }

    /**
     * Add a given event to the schedule (both recurring and non).
     *
     * @param event Event object
     */
    public void addEventToSchedule(StudentEvent event) {
        // Go from the course start to end dates
        LocalDate currentDate = event.startDate;
        while (event.endDate.isAfter(currentDate) || event.endDate.isEqual(currentDate)) {
            // Match weekday
            DayOfWeek weekday = currentDate.getDayOfWeek();

            DayOfWeek[] possibleWeekdays = event.weekdays;
            // Linear search it TODO: Replace linear search
            for (DayOfWeek dayOfWeek: possibleWeekdays) {
                if (dayOfWeek == weekday) {
                    addEventToGivenDay(event, currentDate);
                }
            }

            currentDate = currentDate.plusDays(1); // Increment
        }
    }

    /**
     * Add a given event on a given day.
     *
     * @param event Event object
     * @param date given day
     */
    public void addEventToGivenDay(StudentEvent event, LocalDate date) {
        int index = dateToIndex(date);
        schedule.get(index).add(event);
    }

    /**
     * Convert a given date to an index for the internal list.
     *
     * @param givenDate A given date for which we need the array index
     */
    private int dateToIndex(LocalDate givenDate) {
        return (int) DAYS.between(startDay, givenDate);
    }
}

