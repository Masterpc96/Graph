import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * Created by Michał on 29.05.2017.
 */
public class KruskalGraphDirected extends JDialog {
    private ListenableDirectedWeightedGraph<String, MyEdge> g;
    private mxIGraphLayout layout;
    private JGraphXAdapter<String, MyEdge> graphAdapter;
    private mxGraphComponent component;

    public KruskalGraphDirected(MyFrame owner, Set<PanelDirected.Edge> edge) {
        super(owner, true);
        g = new ListenableDirectedWeightedGraph<String, MyEdge>(MyEdge.class);
        graphAdapter =
                new JGraphXAdapter<String, MyEdge>(g);

        layout = new mxOrganicLayout(graphAdapter);
        MyEdge myEdge = new MyEdge();
        for (PanelDirected.Edge edge1 : edge) {
            g.addVertex(edge1.getFirst());
            g.addVertex(edge1.getSecond());
            myEdge = g.addEdge(edge1.getFirst(), edge1.getSecond());
            g.setEdgeWeight(myEdge, edge1.getWage());
            layout.execute(graphAdapter.getDefaultParent());
        }


        component = new mxGraphComponent(graphAdapter);

        component.setPreferredSize(new Dimension(1000, 800));

        add(component);
        pack();
        setVisible(true);


    }

    public static class MyEdge extends DefaultWeightedEdge { //weight
        @Override
        public String toString() {
            return String.valueOf(getWeight());
        }
    }
}
