//a Date object represents one event
public class Date implements Comparable {
    private int year;
    private int month;
    private int day;
    private String event;
    private String description;
    private static int[] daysInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

    // dates before 1582 are in julian calendar, dates after 1582 are in gregorian
    // calendar
    public Date(int y, String evtName) {
        setAll(0, 0, y, evtName, "");
    }

    // goes in US order (month, day, year)
    public Date(int m, int y, String evtName) {
        setAll(m, 0, y, evtName, "");
    }

    public Date(int m, int d, int y, String evtName) {
        setAll(m, d, y, evtName, "");
    }

    // set methods
    private void setAll(int m, int d, int y, String evtName, String description) {
        setMonth(m);
        setDay(d);
        setYear(y);
        setEventName(evtName);
        setDescription(description);
    }

    public String setDescription(String d) {
        String prevDescription = description;
        description = d;
        return prevDescription;
    }

    public int setMonth(int m) {
        int prevMonth = month;
        if (m < 13 && m >= 0) {
            month = m;
        }
        return prevMonth;
    }

    public int setDay(int d) {
        if (month != 0 && daysInMonth[month - 1] >= d && d >= 0) {
            int prevDay = day;
            day = d;
            return prevDay;
        }
        return 0;
    }

    public int setYear(int y) {
        int prevYear = year;
        year = y;
        return prevYear;
    }

    public String setEventName(String evtName) {
        String prevEvent = event;
        event = evtName;
        return prevEvent;
    }

    // get methods
    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getVisibleDate() {
        String date = "" + year;
        if (day > 0) {
            date = day + "/" + date;
        }
        if (month > 0) {
            date = month + "/" + date;
        }
        return date;
    }

    public String getEventName() {
        return event;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public int compareTo(Object obj) {
        Date d = (Date) obj;
        if (year != d.year) {
            return year - d.year;
        }
        if (month != d.month) {
            return month - d.month;
        }
        if (day != d.day) {
            return day - d.day;
        }
        return event.compareTo(d.getEventName());
    }

    @Override
    public boolean equals(Object obj) {
        Date d = (Date) obj;
        return ((year == d.getYear() && month == d.getMonth() && day == d.getDay() && event.equals(d.getEventName())));
    }

    // finds the days between this Date and the second Date
    public int daysBetween(Date second) {
        // check here that this <= d2
        Date first = this;
        boolean isSwitched = false;
        if (this.compareTo(second) > 0) {
            first = second;
            second = this;
            isSwitched = true;
        }
        int total = 0;
        int d1days = daysToBeginingofYear(first);
        int d2days = daysToBeginingofYear(second);
        // find days in years and sum
        int tempYear = first.getYear();
        while (tempYear != second.getYear()) {
            total += daysinYear(tempYear);
            tempYear++;
        }
        total += d2days;
        total -= d1days;
        if (isSwitched) {
            total *= -1;
        }
        return total;
    }

    // returns the days in the year, accounting for leap days
    public static int daysinYear(int y) {
        int total = 365;
        if (isLeapYear(y)) {
            total += 1;
        }
        // switch to gregorian in 1582
        // although the switched happened in different parts around the world (1752 in
        // North America), dates after 1582 have been converted to the Gregorian
        // calendar
        if (y == 1582) {
            total = 355;
        }
        return total;
    }

    public static int daysToBeginingofYear(Date d) {
        int total = d.getDay();
        if (d.getYear() == 1582) {
            daysInMonth[9] = 21;
            // switch: oct 4-oct 15
            if (d.getMonth() == 10 && d.getDay() > 4) {
                total -= 10;
            }
        }
        if (isLeapYear(d.getYear())) {
            daysInMonth[1] += 1;
        }
        for (int i = 0; i < d.getMonth() - 1; i++) {
            total += daysInMonth[i];
        }
        daysInMonth = new int[] { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
        return total;
    }

    // leap year rules
    public static boolean isLeapYear(int y) {
        if (y < 1582) {
            // julian calendar
            // https://www.timeanddate.com/calendar/julian-calendar.html
            return (y % 4 == 0);
        } else {
            // gregorian calendar
            // https://www.timeanddate.com/calendar/gregorian-calendar.html
            return (((y % 4 == 0) && !(y % 100 == 0)) || (y % 400 == 0));
        }
    }

    public String toString() {
        String output = "" + getVisibleDate() + ": " + event;
        return output;
    }
}