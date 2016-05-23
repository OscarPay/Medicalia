package com.example.user.medicalia.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by USER on 22/05/2016.
 */
public class Day {

    private static final String DATE_FORMAT = "yyy-MM-dd'T'HH:mm";
    private static final String AM = "AM";
    private static final String PM = "PM";

    private List<Hour> hours;
    private Schedule schedule;

    private Calendar calendarStart;
    private Calendar calendarEnd;
    private SimpleDateFormat dateFormat;

    private int startHour;
    private int endHour;
    private int startMinutes;
    private int endMinutes;

    public Day(Schedule schedule) {
        hours = new ArrayList<>();
        calendarStart = GregorianCalendar.getInstance();
        calendarEnd = GregorianCalendar.getInstance();
        dateFormat = new SimpleDateFormat("yyy-MM-dd'T'HH:mm");

        setDateCalendar(schedule.getStart(), calendarStart);
        setDateCalendar(schedule.getEnd(), calendarEnd);

        startHour = calendarStart.get(Calendar.HOUR_OF_DAY);
        startMinutes = calendarStart.get(Calendar.MINUTE);
        endHour =  calendarEnd.get(Calendar.HOUR_OF_DAY);
        endMinutes = calendarEnd.get(Calendar.MINUTE);

        createListHours();
    }

    private void createListHours() {
        for (int i = startHour; i <= endHour; i++){
            String hoursFormat = AM;
            int hour = i;

            if (hour / 12 == 1){
                if (hour != 12 ){
                    hour = i - 12;
                }
                hoursFormat = PM;
            }

            String minutes = String.valueOf(startMinutes);
            if (startMinutes % 10 == 0){
                minutes = minutes + "0";
            }

            String hourString = String.valueOf(hour) +":"+minutes;
            hours.add(new Hour(hourString, hoursFormat, "Disponible"));
        }
    }

    private void setDateCalendar(String dateString, Calendar calendar) {
        try {
            Date date = dateFormat.parse(dateString);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

    public int getStartHour() {
        return startHour;
    }

    public int getEndHour() {
        return endHour;
    }

    public List<Hour> getHours() {
        return hours;
    }
}