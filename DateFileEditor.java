import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.GridLayout;
import java.awt.BorderLayout;

//this class manages the events added and removed from the timeline, it connects the datafile and the user to the Timeline
public class DateFileEditor {
    private String file;
    Timeline t;

    public DateFileEditor(String file, Timeline timeline) {
        this.file = file;
        t = timeline;
    }

    // adds a new line to the file with the Date
    public void addToFile(Date d, String[] units) throws IOException {
        BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
        output.append("\n" + fileOutput(d, units));
        output.close();
    }

    public Date addToFile(String m, String d, String y, String e, String des, String[] units) throws IOException {
        BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
        String s = fileOutput(m, d, y, e, des, units);
        output.append("\n" + s);
        output.close();
        return (convertToDate(s));
    }

    private static String fileOutput(Date d, String[] units) {
        // organization of a Date in data file is: units !m !d !y !event !description
        // units are separated by commas
        String us = Arrays.toString(units);
        us = us.substring(1, us.length() - 1);
        if (us.length() < 0) {
            us = " ";
        }
        String evt = " ";
        if (d.getEventName().length() > 0) {
            evt = d.getEventName();
        }
        String output = us + " !" + d.getMonth() + " !" + d.getDay() + " !" + d.getYear() + " !" + evt + " !"
                + d.getDescription();
        return output;
    }

    private static String fileOutput(String m, String d, String y, String e, String des, String[] units) {
        // organization of a Date in data file is: units !m !d !y !event !description
        // units are separated by commas
        String us = Arrays.toString(units);
        us = us.substring(1, us.length() - 1);
        if (units == null || us.length() < 0) {
            us = " ";
        }
        String evt = " ";
        if (e.length() > 0) {
            evt = e;
        }
        String output = us + " !" + m + " !" + d + " !" + y + " !" + evt + " !" + des;
        return output;
    }

    // '-' at begining of line that indicates the date should be removed from the
    // file
    public void removeFromFile(Date d) throws IOException {
        removeFromFile(d.getMonth() + "", d.getDay() + "", d.getYear() + "", d.getEventName());
    }

    public void removeFromFile(String m, String d, String y, String evt) throws IOException {
        BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
        String s = "\n" + "-" + fileOutput(m, d, y, evt, "", new String[] { " " });
        output.append(s);
        output.close();
    }

    // reads from the file and adds/removes all dates to the linked list of dates in
    // the timeline class
    public void readDates() throws FileNotFoundException {
        Scanner f = new Scanner(new File(file));
        while (f.hasNextLine()) {
            String d = f.nextLine();
            // "-" at the begining indicates that the date should be removed
            if (d.charAt(0) == '-') {
                t.removefromDates(convertToDate(d));
            } else {
                t.addtoDates(convertToDate(d), getUnits(d));
            }
        }
    }

    public static String[] getUnits(String s) {
        String us = s.split(" !")[0];
        // units are separated by commas
        String[] units = us.split(", ");
        return units;
    }

    // converts a string from the file into a date
    public static Date convertToDate(String s) {
        String[] varsOfDate = s.split(" !");
        int month = convertToInt(varsOfDate[1]);
        int day = convertToInt(varsOfDate[2]);
        int year = convertToInt(varsOfDate[3]);
        Date date = new Date(month, day, year, varsOfDate[4]);
        if (varsOfDate.length > 5) {
            date.setDescription(varsOfDate[5]);
        }
        return date;
    }

    // converts a string into an int
    public static int convertToInt(String s) {
        // assumes the int is positive
        char ch = s.charAt(0);
        if (ch > 47 && ch < 58) {
            int num = (int) (ch - 48);
            if ((s.length() - 1) > 0) {
                num *= Math.pow(10, (s.length() - 1));
                num += convertToInt(s.substring(1));
            }
            return num;
        }
        return -1;
    }

    // manages user input to create/remove/edit dates
    public void inputDates() throws IOException {
        int n = -1;
        while (n != 0) {
            JFrame frame = new JFrame("Input Dialog");
            String[] options = { "Done", "Add Events", "Remove Events", "Edit Event Description" };
            n = JOptionPane.showOptionDialog(frame, "What do you want to do?", "Timeline",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (n != 0) {
                int a = 0;
                while (a == 0) {
                    a = -1;
                    // https://stackoverflow.com/questions/6555040/multiple-input-in-joptionpane-showinputdialog/6555051
                    JTextField evtField = new JTextField();
                    JTextField yearField = new JTextField();
                    JTextField monthField = new JTextField();
                    JTextField dayField = new JTextField();

                    JPanel input = new JPanel();
                    input.add(new JLabel("event name:"));
                    input.add(evtField);
                    input.add(new JLabel("year:"));
                    input.add(yearField);
                    input.add(new JLabel("month (optional):"));
                    input.add(monthField);
                    input.add(new JLabel("day (optional):"));
                    input.add(dayField);
                    input.setLayout(new GridLayout(4, 2));

                    JOptionPane.showConfirmDialog(null, input, "Timeline", JOptionPane.DEFAULT_OPTION, 0,
                            new ImageIcon());
                    if (monthField.getText().isEmpty()) {
                        monthField.setText("0");
                        dayField.setText("0");
                    } else if (dayField.getText().isEmpty()) {
                        dayField.setText("0");
                    }
                    if (yearField.getText().length() != 4 || evtField.getText().length() < 1
                            || convertToInt(monthField.getText()) > 12 || convertToInt(dayField.getText()) > 31) {
                        a = 2;
                        JOptionPane.showMessageDialog(frame, "Incorrect data entered", "Timeline",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                    if ((n == 2 || n == 3) && a != 2) {
                        Date d = t.findDate(convertToInt(monthField.getText()), convertToInt(dayField.getText()),
                                convertToInt(yearField.getText()), evtField.getText());
                        if (d == null) {
                            String[] o = { "Re-enter Event", "Cancel" };
                            a = JOptionPane.showOptionDialog(frame, "Event not found.", "Timeline",
                                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, o, o[0]);

                        }

                        if (n == 2 && a == -1) {
                            // remove events
                            removeFromFile(monthField.getText(), dayField.getText(), yearField.getText(),
                                    evtField.getText());
                            t.removefromDates(d);
                            JOptionPane.showMessageDialog(frame, "Event removed", "Timeline",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }

                        if (n == 3 && a == -1) {
                            // edit description for an event
                            String[] units = t.getUnitsForDate(d);
                            removeFromFile(d);
                            String original = d.getDescription();
                            d.setDescription(inputDescription(original));
                            addToFile(d, units);
                            JOptionPane.showMessageDialog(frame, "Description edited", "Timeline",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }

                    if (n == 1 && a != 2) {
                        // add events
                        String description = inputDescription();
                        String[] units = inputUnits();
                        Date date = addToFile(monthField.getText(), dayField.getText(), yearField.getText(),
                                evtField.getText(), description, units);
                        t.addtoDates(date, units);
                        JOptionPane.showMessageDialog(frame, "Event added", "Timeline",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }

        }
    }

    private String inputDescription(String original) {
        JPanel description = new JPanel();
        // https://docs.oracle.com/javase/7/docs/api/java/awt/BorderLayout.html
        // https://docs.oracle.com/javase/tutorial/uiswing/layout/border.html
        JTextArea desField = new JTextArea(7, 20);
        description.setLayout(new BorderLayout());
        desField.setLineWrap(true);
        desField.setText(original);
        // https://stackoverflow.com/questions/8849063/adding-a-scrollable-jtextarea-java
        JScrollPane scroll = new JScrollPane(desField);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        description.add(new JLabel("Add a description"), BorderLayout.PAGE_START);
        description.add(scroll, BorderLayout.CENTER);
        JOptionPane.showConfirmDialog(null, description, "Timeline", JOptionPane.DEFAULT_OPTION, 0, new ImageIcon());
        String des = desField.getText();
        des = des.replace("\n", "");
        return des;
    }

    private String inputDescription() {
        return inputDescription("");
    }

    private String[] inputUnits() {
        String[] units = new String[0];
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        int ans = 0;
        String[] unitArray = t.getUnitstoArray();
        String[] unitOptions = new String[unitArray.length + 1];
        unitOptions[0] = "";
        if (unitArray.length > 0) {
            Arrays.sort(unitArray);
            int i = 1;
            for (String u : unitArray) {
                unitOptions[i] = u;
                i++;
            }
        }
        JComboBox<String> box = new JComboBox<String>(unitOptions);
        box.setEditable(true);
        box.setSelectedItem(unitOptions[0]);
        panel.add(new JLabel("Unit:"));
        panel.add(box);
        while (ans == 0) {
            // https://docs.oracle.com/javase/tutorial/uiswing/components/combobox.html
            // https://docs.oracle.com/javase/8/docs/api/javax/swing/JComboBox.html
            JOptionPane.showConfirmDialog(null, panel, "Timeline", JOptionPane.DEFAULT_OPTION, 0, new ImageIcon());
            String unit = (String) box.getSelectedItem();
            String[] temp = new String[units.length + 1];
            temp[0] = unit;
            int k = 1;
            for (int j = 0; j < units.length; j++) {
                temp[k] = units[j];
                k++;
            }
            units = temp;
            JFrame frame = new JFrame();
            ans = JOptionPane.showConfirmDialog(frame, "Do you want to add more units?", "Timeline",
                    JOptionPane.YES_NO_OPTION);
        }
        return units;
    }
}