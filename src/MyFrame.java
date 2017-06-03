import javax.swing.*;
import java.awt.*;

/**
 * Created by Michał on 27.05.2017.
 */
public class MyFrame extends JFrame {
    Dimension dimension = getToolkit().getScreenSize();

    public MyFrame() {
        super("Painting graph");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(dimension.width, dimension.height - 50);
        setLocation(new Point(0, 0));
        setResizable(false);
        setVisible(true);
        JTabbedPane pane = new JTabbedPane();
        pane.add("Undirected", new Panel(dimension));
        pane.add("Directed", new PanelDirected(dimension));
        add(pane);
    }

}
