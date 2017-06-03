import javax.swing.*;
import java.awt.*;

/**
 * Created by Michał on 28.05.2017.
 */
public class matrixN extends JDialog {
    private JTable table;

    public matrixN(MyFrame owner, String[][] data, String[] column) {
        super(owner, true);

        table = new JTable(data, column);
//        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.setPreferredScrollableViewportSize(new Dimension(1000, 500));
        table.setFillsViewportHeight(true);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        add(new JScrollPane(table));
        pack();
        setVisible(true);
    }
}
