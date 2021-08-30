import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

//JPanel containing smaller JPanels for date buttons and unit checkboxes, as well as the drawn line
public class LineGraphics extends JPanel {
    Timeline timeline;
    JPanel datePanel;
    JPanel unitPanel;
    int lineY;
    JTextField end;

    public LineGraphics(Timeline t) {
        timeline = t;
        this.setLayout(null);
        lineY = 400;
        JButton blank = new JButton();
        blank.setBackground(new Color(0, 0, 0, 0));
        blank.addActionListener(new Listener());
        blank.setBounds(0, 0, 1000, 600);
        blank.setVisible(false);
        this.add(blank);
        JPanel unitPanel = new JPanel();
        String[] us = timeline.getUnitstoArray();
        Arrays.sort(us);
        unitPanel.setLayout(new GridLayout(us.length + 1, 1));
        unitPanel.add(new JLabel("Select Units"));
        JCheckBox[] units = new JCheckBox[us.length];
        int maxLength = 0;
        for (int i = 0; i < us.length; i++) {
            units[i] = new JCheckBox(us[i]);
            units[i].addActionListener(new Listener(timeline, true, this));
            unitPanel.add(units[i]);
            if (us[i].length() > maxLength) {
                maxLength = us[i].length();
            }
        }
        int xSize = maxLength * 9;
        if ((us.length + 1) * 16 > 250) {
            unitPanel.setSize(xSize, (us.length + 1) * 16);
            JScrollPane scroll = new JScrollPane(unitPanel);
            scroll.setBounds(5, 10, xSize, 240);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            this.add(scroll);
        } else {
            unitPanel.setBounds(5, 10, xSize, (us.length + 1) * 16);
            this.add(unitPanel);
        }

        JTextField start = new JTextField(timeline.getStart() + "");
        start.setEditable(true);
        start.setBounds(10, lineY - 10, 50, 20);
        start.addKeyListener(new Listener(timeline, this));
        this.add(start);

        end = new JTextField(timeline.getEnd() + "");
        end.setEditable(true);
        end.setBounds(940, lineY - 10, 50, 20);
        end.addKeyListener(new Listener(timeline, this));
        this.add(end);

        datePanel = new JPanel();

        this.add(datePanel);
    }

    public void update(Graphics window) {
        paint(window);
    }

    // updates the dates shown and the spacing/range
    public void paintComponent(Graphics window) {
        super.paintComponent(window);
        window.setColor(Color.BLACK);
        window.drawLine(0, lineY, 1000, lineY);
        this.remove(datePanel);
        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0, 0));
        panel.setBounds(0, lineY - 150, 1000, 300);
        LinkedList<Date> ds = timeline.getDates();
        ArrayList<JButton> dates = new ArrayList<JButton>();
        int i = 0;
        for (Date d : ds) {
            JButton button = new JButton();
            dates.add(i, button);
            button.setText(d.getEventName());
            button.addActionListener(new Listener(timeline, d));
            int x = (int) (850 * timeline.percentOfLine(d)) + 75;
            int y = 0;
            // 0,30,60, 90, 120
            if (i % 2 == 0) {
                y = 160;
                // 160, 190, 220, 250, 280
            }
            y += 30 * (i % 5);
            int width = button.getText().length() * 8;
            button.setBounds(((int) (x - (width / 2.0))), y, width, 20);
            i++;
            panel.add(button);
            window.drawLine(x, lineY - 150 + y, x, lineY);
        }
        datePanel = panel;
        this.add(datePanel);
    }

    public JTextField getEnd() {
        return end;
    }
}