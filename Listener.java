import java.awt.event.*;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

//holds action listeners for event buttons, unit checkboxes, and start and end year text field entry
public class Listener implements ActionListener, KeyListener {
    private Timeline t;
    private boolean isCheckbox = false;
    private Date d;
    private LineGraphics g;

    public Listener(Timeline line, LineGraphics graph) {
        t = line;
        g = graph;
    }

    public Listener() {
        t = null;
        d = null;
        g = null;
    }

    // for checkboxes
    public Listener(Timeline line, boolean checkbox, LineGraphics graph) {
        t = line;
        isCheckbox = checkbox;
        g = graph;
    }

    // for buttons
    public Listener(Timeline line, Date date) {
        t = line;
        d = date;
    }

    // use checkboxes for selecting units
    public void actionPerformed(ActionEvent evt) {
        if (isCheckbox) {
            // checkbox clicked
            JCheckBox c = (JCheckBox) evt.getSource();
            if (c.isSelected()) {
                t.selectUnit(c.getText());
            } else {
                t.unselectUnit(c.getText());
            }
            g.repaint();
        } else if (d != null) {
            // event button pressed
            buttonPressed(d.getDescription());
        }
    }

    // shows the popup with the event's description and date it occured
    private void buttonPressed(String description) {
        JFrame frame = new JFrame("Input Dialog");
        for (int i = 0; i < (description.length() / 83); i++) {
            int j = 0;
            while ((j < 60) && description.charAt((83 * (i + 1)) - j) != ' ') {
                j++;
            }
            if (j == 60) {
                description = description.substring(0, 83) + "\n" + description.substring(83);
            } else {
                description = description.substring(0, (83 * (i + 1)) - j) + "\n"
                        + description.substring((83 * (i + 1)) - j);
            }

        }
        String output = d.getVisibleDate() + "\n" + "\n" + description;
        JOptionPane.showMessageDialog(frame, output, d.getEventName(), JOptionPane.PLAIN_MESSAGE);
    }

    public void keyPressed(KeyEvent evt) {
    }

    // registers when text field is used
    public void keyReleased(KeyEvent evt) {
        // used for seleting year range
        int key = evt.getKeyCode();
        JTextField field = (JTextField) evt.getSource();
        String year = field.getText();
        boolean setRange = false;
        if (key == KeyEvent.VK_ENTER || year.length() == 4) {
            int y = DateFileEditor.convertToInt(year);
            if (y != -1) {
                int start;
                int end;
                if (field.getX() < 500) {
                    start = y;
                    end = t.getEnd();
                } else {
                    start = t.getStart();
                    end = y;
                }
                g.getEnd().transferFocus();
                setRange = t.setDateRange(start, end);

                g.repaint();
            }
            if (y == -1 || !setRange) {
                if (field.getX() < 500) {
                    y = t.getStart();
                } else {
                    y = t.getEnd();
                }
                field.setText(y + "");
            }
        }
    }

    public void keyTyped(KeyEvent evt) {
    }
}