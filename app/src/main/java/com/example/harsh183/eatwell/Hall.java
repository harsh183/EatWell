package com.example.harsh183.eatwell;

public class Hall {

    private class MealTimings {
        public String getMealName() {
            return mealName;
        }

        public void setMealName(String mealName) {
            this.mealName = mealName;
        }

        public int getStartTime() {
            return startTime;
        }

        public void setStartTime(int startTime) {
            this.startTime = startTime;
        }

        public int getEndTime() {
            return endTime;
        }

        public void setEndTime(int endTime) {
            this.endTime = endTime;
        }

        private String mealName;
        private int startTime;
        private int endTime;

        public MealTimings(String mealName, int startTime, int endTime) {
            this.mealName = mealName;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    private String name;
    private String location;
    private MealTimings Breakfast, Lunch, Dinner;

    @Override
    public String toString() {
        return "Hall{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation() {
        this.location = "Campus: Urbana-Champaign Building: " + name; //iCalendar Friendly Location Format
    }

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public Hall(String name, String location) {
        this.name = name;
        this.location = location;
    }

    /*
     * TODO Implement hall specific meal timings
     */

}


