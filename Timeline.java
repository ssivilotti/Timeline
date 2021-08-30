import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.Arrays;

//the control class, it tells the lineGraphics class what to display
public class Timeline {
    private int startYear;
    private int endYear;
    private HashMap<String, Date[]> units;
    private LinkedList<Date> allDates;
    private DateFileEditor editor;
    private String[] unitsSelected = new String[0];

    public Timeline(String file) throws FileNotFoundException {
        units = new HashMap<String, Date[]>();
        allDates = new LinkedList<Date>();
        startYear = 0;
        endYear = 1;
        editor = new DateFileEditor(file, this);
        editor.readDates();
    }

    // adds a date to the linked list with all the dates
    public void addtoDates(Date date, String[] us) {
        allDates.add(date);
        Collections.sort(allDates);
        for (int i = 0; i < us.length; i++) {
            addtoUnits(us[i], date);
        }
        setDateRange(allDates.getFirst().getYear(), allDates.getLast().getYear());
    }

    public boolean removefromDates(Date date) {
        if (allDates.contains(date)) {
            allDates.remove(date);
            removefromUnits(date);
            return true;
        }
        setDateRange(allDates.getFirst().getYear(), allDates.getLast().getYear());
        return false;
    }

    // adds a date to the value array for the unit key
    public void addtoUnits(String unit, Date date) {
        Date[] value;
        if (units.containsKey(unit)) {
            Date[] old = units.get(unit);
            value = new Date[old.length + 1];
            for (int i = 0; i < old.length; i++) {
                value[i] = old[i];
            }
        } else {
            value = new Date[1];
        }
        value[value.length - 1] = date;

        units.put(unit, value);
    }

    public boolean removefromUnits(Date date) {
        boolean doesContain = false;
        for (String unit : units.keySet()) {
            Date[] old = units.get(unit);
            if (Arrays.asList(old).contains(date)) {
                doesContain = true;
                Date[] value = new Date[old.length - 1];
                int k = 0;
                for (int j = 0; j < old.length; j++) {
                    if (!old[j].equals(date)) {
                        value[k] = old[j];
                        k++;
                    }
                }
                units.put(unit, value);
            }
        }
        return doesContain;
    }

    // end must be greater than start
    public boolean setDateRange(int start, int end) {
        if (end >= start) {
            startYear = start;
            endYear = end;
            return true;
        }
        return false;
    }

    // adds a unit to the list of selected units
    public void selectUnit(String unit) {
        if (units.containsKey(unit) && (!Arrays.asList(unitsSelected).contains(unit))) {
            String[] u = new String[unitsSelected.length + 1];
            u[0] = unit;
            for (int i = 0; i < unitsSelected.length; i++) {
                u[i + 1] = unitsSelected[i];
            }
            unitsSelected = u;
        } else {
            System.out.println(unit + " not a valid unit");
        }
    }

    // removes a unit to the list of selected units
    public void unselectUnit(String unit) {
        if (Arrays.asList(unitsSelected).contains(unit)) {
            String[] u = new String[unitsSelected.length - 1];
            int j = 0;
            for (int i = 0; i < unitsSelected.length; i++) {
                if (unitsSelected[i] != unit) {
                    u[j] = unitsSelected[i];
                    j++;
                }
            }
            unitsSelected = u;
        } else {
            System.out.println(unit + " not a valid unit");
        }
    }

    // returns the linked list of all selected dates
    public LinkedList<Date> getDatesforUnits() {
        LinkedList<Date> dates = new LinkedList<Date>();
        for (String unit : unitsSelected) {
            if (this.units.containsKey(unit)) {
                for (Date date : this.units.get(unit)) {
                    if (!dates.contains(date)) {
                        dates.add(date);
                    }
                }
            }
        }
        return getDatesinRange(dates);
    }

    public LinkedList<Date> getAllDates() {
        return getDatesinRange(allDates);
    }

    public LinkedList<Date> getDates() {
        if (unitsSelected.length > 0) {
            return getDatesforUnits();
        }
        return getAllDates();
    }

    // takes a linked list of dates and returns a linked list of only the dates
    // within the start and end date (inclusive)
    private LinkedList<Date> getDatesinRange(LinkedList<Date> list) {
        LinkedList<Date> dates = new LinkedList<Date>();
        for (Date d : list) {
            if (d.getYear() >= startYear && d.getYear() <= endYear) {
                dates.add(d);
            }
        }
        Collections.sort(dates);
        return dates;
    }

    public double percentOfLine(Date d) {
        Date s = new Date(startYear, "");
        double days = (double) s.daysBetween(new Date(endYear + 1, ""));
        return (s.daysBetween(d) / days);
    }

    // get methods
    public Set<String> getUnits() {
        return units.keySet();
    }

    public String[] getUnitstoArray() {
        String[] arr = new String[units.size()];
        int i = 0;
        for (String unit : units.keySet()) {
            arr[i] = unit;
            i++;
        }
        return arr;
    }

    public int getStart() {
        return startYear;
    }

    public int getEnd() {
        return endYear;
    }

    public String[] getSelectedUnits() {
        return unitsSelected;
    }

    public DateFileEditor getDateFileEditor() {
        return editor;
    }

    public Date findDate(int m, int d, int y, String evt) {
        for (Date date : allDates) {
            if ((date.getMonth() == m || m == 0) && (date.getDay() == d || d == 0) && date.getYear() == y
                    && date.getEventName().equals(evt)) {
                return date;
            }
        }
        return null;
    }

    // returns all the units connected to a particular date
    public String[] getUnitsForDate(Date date) {
        String[] us = new String[0];
        for (String unit : units.keySet()) {
            if (Arrays.asList(units.get(unit)).contains(date)) {
                String[] temp = new String[us.length + 1];
                temp[0] = unit;
                int k = 1;
                for (int j = 0; j < us.length; j++) {
                    temp[k] = us[j];
                    k++;
                }
                us = temp;
            }
        }
        return us;
    }
}