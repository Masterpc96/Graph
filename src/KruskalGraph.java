import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.model.mxGraphModel;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxUtils;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.ListenableUndirectedGraph;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;
import java.util.Set;

/**
 * Created by Michał on 29.05.2017.
 */
public class KruskalGraph extends JDialog {
    private ListenableUndirectedGraph<String, MyEdge> g;
    private mxIGraphLayout layout;
    private JGraphXAdapter<String, MyEdge> graphAdapter;
    private mxGraphComponent component;
    private mxGraphModel graphModel;

    public KruskalGraph(MyFrame owner, Set<Panel.Edge> edge) {
        super(owner, true);
        g = new ListenableUndirectedGraph<String, MyEdge>(MyEdge.class);


        graphAdapter =
                new JGraphXAdapter<String, MyEdge>(g);

        for (Panel.Edge edge1 : edge) {
            g.addVertex(edge1.getFirst());
            g.addVertex(edge1.getSecond());
            g.addEdge(edge1.getFirst(), edge1.getSecond());
        }

        layout = new mxOrganicLayout(graphAdapter);

        component = new mxGraphComponent(graphAdapter);

        component.setPreferredSize(new Dimension(1000, 800));

        graphModel = (mxGraphModel) component.getGraph().getModel();
        Collection<Object> cells = graphModel.getCells().values();
        mxUtils.setCellStyles(component.getGraph().getModel(),
                cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);


        layout.execute(graphAdapter.getDefaultParent());

        add(component);
        pack();
        setVisible(true);


    }

    public static class MyEdge extends DefaultEdge { //weight
        @Override
        public String toString() {
            return "";
        }
    }
}
