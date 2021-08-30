import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

//gives the user the choice between viewing the timeline and editing dates
public class TimelineRunner {
    private static Timeline t;

    public static void main(String args[]) throws IOException {
        JFrame frame = new JFrame("Input Dialog");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String file = JOptionPane.showInputDialog(frame, "Enter the data file that holds the dates", "Timeline",
                JOptionPane.PLAIN_MESSAGE);
        t = new Timeline(file);
        ProgOptions();
    }

    public static void ProgOptions() throws IOException {
        JFrame frame = new JFrame("Input Dialog");
        String[] options = { "View Timeline", "Edit Events", "Close" };
        int n = JOptionPane.showOptionDialog(frame, "What do you want to do?", "Timeline",
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (n == 0) {
            GraphicsRunner graph = new GraphicsRunner();
            graph.displayLine(t);
        } else if (n == 1) {
            DateFileEditor edit = t.getDateFileEditor();
            edit.inputDates();
            ProgOptions();
        } else {
            System.exit(0);
        }
    }
}