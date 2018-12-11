package com.example.harsh183.eatwell;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * This class holds the functions that manipulate and use iCal/ics data
 */
@RequiresApi(api = Build.VERSION_CODES.O)
public final class ICal {
    private static int MAX_MEAL_DURATION_MINUTES = 120; // Seems reasonable I guess

    /**
     * Finds time blocks for meal timings.
     *
     * @param schedule Schedule of entire semester
     */
    public static List<List<StudentEvent>> getTimeBlocks(SemesterSchedule schedule) {
        List<List<StudentEvent>> mealSchedule = new ArrayList<>();
        LocalDate currentDate = schedule.getStartDay();
        for (int i = 0; i < schedule.getSchedule().size(); i++) {
            List<StudentEvent> scheduleForDay = schedule.getScheduleForDay(i);
            if (scheduleForDay.size() != 0) { // Skip weekends
                mealSchedule.add(getTimeBlocksForGivenDay(scheduleForDay, currentDate));
            }
            currentDate = currentDate.plusDays(1);
        }
        return mealSchedule;
    }

    public static List<StudentEvent> getTimeBlocksForGivenDay(List<StudentEvent> daySchedule, LocalDate currentDate) {
        // 7-10:30am - breakfast
        // 10:30-3pm - lunch
        // 4-8pm - dinner
        // TODO: Non-hardcoded meal timings
        LocalTime[][] timings = {
                {LocalTime.of(7,0,0), LocalTime.of(10,30,0)},
                {LocalTime.of(10,30,0), LocalTime.of(15, 0, 0)},
                {LocalTime.of(16,0,0), LocalTime.of(20,0,0)}};
        System.out.println(currentDate);

        List<StudentEvent> mealsForDay = new ArrayList<>();

        for (LocalTime[] mealTiming: timings) {
            LocalTime start = mealTiming[0];
            LocalTime end = mealTiming[1];

            List<StudentEvent> possibleIntervals = getPossibleIntervals(daySchedule, start, end);

            // Now apply a max function
            StudentEvent maxMealTiming = possibleIntervals.get(0);
            int maxMinutes = maxMealTiming.duration();
            for (StudentEvent possibleMealTiming: possibleIntervals) {
                if (possibleMealTiming.duration() > MAX_MEAL_DURATION_MINUTES) {
                    // Shrink to max duration and push
                    possibleMealTiming.endTime = possibleMealTiming.startTime.plusMinutes(MAX_MEAL_DURATION_MINUTES);
                    maxMealTiming = possibleMealTiming;
                }
                if (possibleMealTiming.duration() > maxMinutes) {
                    maxMealTiming = possibleMealTiming;
                }
            }
            // TODO: Insert location distance based on walking

            maxMealTiming.startDate = currentDate;
            maxMealTiming.endDate = currentDate;
            maxMealTiming.location = "Generic dining hall";
            mealsForDay.add(maxMealTiming);
        }
        return mealsForDay;
    }

    public static List<StudentEvent> getPossibleIntervals(List<StudentEvent> daySchedule, LocalTime start, LocalTime end) {
        // TODO: Really cleanup this function
        // TODO: Make a better clone function
        // Generate possible intervals
        List<StudentEvent> possibleIntervals = new ArrayList<>();
        // Good rule of thumb for understanding this bit is start and end are reversed
        // TODO: Split this chunk into smaller functions
        // TODO: Check for case of two contiguous blocks and such
        // First check if the starting point is taken to get the true starting point
        for (StudentEvent event: daySchedule) {
            if (event.isTimingWithinEvent(start)) {
                start = LocalTime.of(event.endTime.getHour(), event.endTime.getMinute());
            }
        }
        // System.out.println("Start time: " + start);
        // Loop after this
        StudentEvent possibleInterval = new StudentEvent();
        possibleInterval.startTime = LocalTime.of(start.getHour(), start.getMinute()); // Hacky but whatever TODO: Fix
        boolean foundFirstEvent = false;
        for (StudentEvent event: daySchedule) {
            if (event.endTime.isBefore(end) && event.startTime.isAfter(start)) {
                foundFirstEvent = true;
                //System.out.print(event);
                //System.out.println("Found timing");
                // TODO: Code feel inelegant, maybe improve or abstract it away.
                possibleInterval.endTime = LocalTime.of(event.startTime.getHour(), event.startTime.getMinute());
                possibleIntervals.add(possibleInterval);

                possibleInterval = new StudentEvent();
                possibleInterval.startTime = LocalTime.of(event.endTime.getHour(), event.endTime.getMinute());
            } else if(foundFirstEvent) {
                break;
            }
        }
        if (!(foundFirstEvent)) {
            //System.out.println("No events in interval");
            StudentEvent possibleEvent = new StudentEvent();
            possibleEvent.startTime = LocalTime.of(start.getHour(), start.getMinute());;
            possibleEvent.endTime = LocalTime.of(end.getHour(), end.getMinute());
            possibleIntervals.add(possibleEvent);
        } else {
            // Finally check if the ending point is taken
            for (StudentEvent event: daySchedule) {
                if (event.isTimingWithinEvent(end)) {
                    end = LocalTime.of(event.startTime.getHour(), event.startTime.getMinute());
                }
            }
            possibleInterval.endTime = LocalTime.of(end.getHour(), end.getMinute());
            possibleIntervals.add(possibleInterval);
        }

        return possibleIntervals;
    }

    /**
     * Extracts the data out of the ical text into a 2d ArrayList.
     *
     * @param icalText string that has all the text from the ical text file
     * @return A 2dArray list. The first level is the days of the week (MTWRF), the second level is
     * just the events for that day.
     */
    private static SemesterSchedule extractEvents(String icalText) {
        // TODO: Or use a lib for this later, but rn it seems tedious af
        // TODO: Do something about these term dates, hardcoded for now
        LocalDate startDate = LocalDate.of(2018, 8, 27);
        LocalDate endDate = LocalDate.of(2018, 12, 12);
        SemesterSchedule schedule = new SemesterSchedule(startDate, endDate);

        // First break into lines (by newline characters)
        String[] lines = icalText.split("\n");

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
                schedule.addEventToSchedule(currentEvent);
            }

            if (foundEvent) {
                // Start time
                if (line.contains("DTSTART;")) {
                    // TODO: Remove this repetition
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

                // Weekdays
                if (line.contains("BYDAY=")) {
                    String[] extractedWeekDays = line.split("BYDAY=")[1].split(",");
                    currentEvent.parseDaysOfWeek(extractedWeekDays);
                }

                // Start date
                if (line.contains("DTSTART;")) {
                    currentEvent.startDate = extractStartDate(line);
                }

                // End date
                if (line.contains("UNTIL=")) {
                    currentEvent.endDate = extractEndDate(line);
                }
            }
        }

        return schedule;
    }

    /**
     * Helper function to convert the formatted string time from the ics file
     *
     * @param time line containing time
     * @return Returns local time from string
     */
    private static LocalTime convertStringToLocalTime(String time) {
        // TODO: Start using DateTimeFormatter
        int hourTime = Integer.parseInt(time.substring(0, 2));
        int minuteTime = Integer.parseInt(time.substring(2, 4));
        return LocalTime.of(hourTime, minuteTime, 0);
    }

    /**
     * Helper function to convert the formatted string start date from the ics file
     *
     * @param dateLine line containing the date
     * @return Return start local date from string
     */
    private static LocalDate extractStartDate(String dateLine) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        // TODO: Maybe some between two strings sorta function
        String dateString = dateLine.split(";TZID")[1].split(":")[1].split("T")[0]; // 20180826
        return LocalDate.parse(dateString, formatter);

    }

    /**
     * Helper function to extract until date.
     *
     * @param untilLine line containing until
     * @return Return until time as local date
     */
    private static LocalDate extractEndDate(String untilLine) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateString = untilLine.split("UNTIL=")[1].split(";BYDAY")[0].split("T")[0]; // 20180826
        return LocalDate.parse(dateString, formatter);
    }

    /**
     * Function to port eventToICS.
     *
     * @return a new ICS String
     */
    public static String generateICSFile(List<List<StudentEvent>> schedule, String exampleICSFile) {
        StringBuilder newICS = new StringBuilder();
        newICS.append(exampleICSFile.split("BEGIN:VEVENT")[0]); // Get all the starting bits
        for (List<StudentEvent> eventsForDay: schedule) {
            for (StudentEvent event: eventsForDay) {
                newICS.append("BEGIN:VEVENT\n");
                newICS.append("DTSTAMP:20180826T230037Z\n"); // TODO: Read this from the file
                newICS.append("DTSTART;TZID=America/Chicago:" + formatLocalDateToICS(event.startDate) + "T" + formatLocalTimeToICS(event.startTime) + "\n"); // TODO: Get timezone better
                newICS.append("DTEND;TZID=America/Chicago:" + formatLocalDateToICS(event.endDate) + "T" + formatLocalTimeToICS(event.endTime) + "\n"); // TODO: Get timezone better
                newICS.append("SUMMARY: Meal\n"); // TODO: Replace with better placeholder
                // RRULE:FREQ=WEEKLY;UNTIL=20181212T225900;BYDAY=WE
                newICS.append("TZID:America/Chicago\n");
                newICS.append("UID:20180826T230037Z-Ellucian\n"); // TODO: Replace with better placeholder
                newICS.append("LOCATION:" + event.location + "\n");
                newICS.append("DESCRIPTION: EatWell - Eat your meal at dining hall of choice\n");
                newICS.append("END:VEVENT\n");
            }
        }
        newICS.append("END:VCALENDAR");
        return newICS.toString();
    }

    // TODO: Improve these two functions
    private static String formatLocalDateToICS(LocalDate date) {
        String monthString = Integer.toString(date.getMonthValue());
        if (monthString.length() == 1) {
            monthString = "0" + monthString;
        }

        String dayString = Integer.toString(date.getDayOfMonth());
        if (dayString.length() == 1) {
            dayString = "0" + dayString;
        }
        String dateAsString = Integer.toString(date.getYear()) + monthString + dayString;
        return dateAsString;
    }

    private static String formatLocalTimeToICS(LocalTime time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hhmmss");
        return formatter.format(time);

    }
}

// TODO: ffs I'm just getting an ical library asap

// TODO: List<StudentEvent> is pretty abstract, but I think that can become it's own class too sometime.
// TODO: Stop using indexes and switch to using more higher level dates.

// TODO: It feels like the separation between the classes is a bit scattered. Another front of refactor will be
// representation vs parsing. For example, each event carries the weekdays and it's repeated a bunch, likewise with
//  start and end dates, and it's uncertain where parse methods go.

// TODO: Also breakup very large functions.