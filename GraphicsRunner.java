import javax.swing.JFrame;
import java.awt.event.*;
import java.io.IOException;

//manages the larger window and JFrame so the frame stays the same when the timeline is reloaded
public class GraphicsRunner extends JFrame implements WindowListener {
    private static final int WIDTH = 1000;
    private static final int HEIGHT = 620;

    public GraphicsRunner() {
        super("Timeline");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addWindowListener(this);
    }

    public void displayLine(Timeline t) {
        getContentPane().add(new LineGraphics(t));
        setVisible(true);
    }

    // https://docs.oracle.com/javase/7/docs/api/java/awt/event/WindowListener.html
    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

    }

    // when the timeline window is closed, the initial options (view timeline, edit
    // events, done) will appear
    @Override
    public void windowClosed(WindowEvent e) {
        try {
            TimelineRunner.ProgOptions();
        } catch (IOException e1) {
        }
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
