package com.example.harsh183.eatwell;

import java.util.ArrayList;
import java.util.List;

class WeeklySchedule {
    /**
     * Object representing the schedule
     */
    private List<List<StudentEvent>> schedule;

    /**
     * Get the entire schedule as a nested list.
     * @return A two dimensional list. The first level is the days of the week and the second level
     *         is the events for that day
     */
    public List<List<StudentEvent>> getSchedule() {
        return schedule;
    }

    /**
     * Get the schedule for a given day.
     * @param weekday Day as a string in format of {"MO", "TU", "WE", "TH", "FR"}
     * @return A list that has all the events for the day.
     */
    public List<StudentEvent> getScheduleForDay(String weekday) {
        int index = mapWeekDaysToArrayIndex(weekday);
        return getScheduleForDay(index);

    }

    /**
     * Get the schedule for a given day. The indexes are based on the array.
     *
     * @param index Day as in a number {"MO":0, "TU":1, "WE":2, "TH":3, "FR":4}
     * @return A list that has all the events for the day.
     */
    public List<StudentEvent> getScheduleForDay(int index) {
        return schedule.get(index);
    }

    /**
     * Constructor for weekly schedule. Sets up the list with 5 lists inside (one for each weekday)
     */
    public WeeklySchedule() {
        schedule = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
           schedule.add(new ArrayList<StudentEvent>());
        }
    }

    /**
     * Add a given event to the given set of weekdays.
     *
     * @param event Given StudentEvent (lecture, discussion etc.)
     * @param weekdays Array of weekdays something like ["MO", "WE", "FR"], single day events will
     *                 be ["TU"]
     */
    public void addEventToGivenDays(StudentEvent event, String[] weekdays) {
        for (String day: weekdays) {
            int index = mapWeekDaysToArrayIndex(day);
            schedule.get(index).add(event);
        }
    }

    /**
     * Helper function that takes the weekday into account
     * @param weekday
     * @return
     */
    private int mapWeekDaysToArrayIndex(String weekday) {
        String[] weekdays = {"MO", "TU", "WE", "TH", "FR"};
        // TODO: This is linear search, replace
        for (int i = 0; i < weekdays.length; i++) {
            String day = weekdays[i];
            if (day.equals(weekday)) {
                return i;
            }
        }
        return -1;
    }
}
