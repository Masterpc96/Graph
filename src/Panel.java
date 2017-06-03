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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.*;
import java.util.Queue;

/**
 * Created by Michał on 27.05.2017.
 */
public class Panel extends JPanel implements ActionListener {
    private final Font font = new Font("Arial", Font.PLAIN, 20);
    private JFormattedTextField vertex;
    private JFormattedTextField vertex1;
    private NumberFormatter format;
    private NumberFormat form;
    private JLabel vertexL;
    private JLabel vertexL1;
    private JButton add;
    private JButton bfs;
    private JButton dfs;
    private JButton adjacency;
    private JButton incidence;
    private JButton list;
    private JButton kruskal;
    private JCheckBox checkBox;
    private boolean checked = false;
    private String[][] matrix;
    private String[][] incidenceMatrix;
    private String[] column;
    private Set<Edge> edges = new HashSet<>();
    private Set<String> vertexSet = new HashSet<>();


    private ListenableUndirectedGraph<String, MyEdge> g;
    private mxIGraphLayout layout;
    private JGraphXAdapter<String, MyEdge> graphAdapter;
    private mxGraphComponent component;
    private mxGraphModel graphModel;

    public Panel(Dimension dim) {


        g = new ListenableUndirectedGraph<String, MyEdge>(MyEdge.class);
        graphAdapter =
                new JGraphXAdapter<String, MyEdge>(g);

        layout = new mxOrganicLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        component = new mxGraphComponent(graphAdapter);

        graphModel = (mxGraphModel) component.getGraph().getModel();

        component.setPreferredSize(new Dimension(dim.width - 50, dim.height - 200));

        add(component);


        form = NumberFormat.getInstance();
        form.setGroupingUsed(false);
        format = new NumberFormatter(form);
        format.setMinimum(1);
        format.setMaximum(49);

        vertexL = new JLabel("Vertex 1");
        vertexL1 = new JLabel("Vertex 2");
        checkBox = new JCheckBox("edge");
        add = new JButton("Add");
        bfs = new JButton("BFS");
        dfs = new JButton("DFS");
        adjacency = new JButton("Adjacency");
        incidence = new JButton("Incidence");
        list = new JButton("List");
        kruskal = new JButton("Kruskal");


        add.addActionListener(this);
        bfs.addActionListener(this);
        dfs.addActionListener(this);
        adjacency.addActionListener(this);
        incidence.addActionListener(this);
        list.addActionListener(this);
        kruskal.addActionListener(this);

        vertex = new JFormattedTextField(format);
        vertex1 = new JFormattedTextField(format);
        vertex.setPreferredSize(new Dimension(50, 30));
        vertex1.setPreferredSize(new Dimension(50, 30));

        vertex.setFont(font);
        vertexL.setFont(font);
        vertexL1.setFont(font);
        checkBox.setFont(font);
        vertex1.setFont(font);
        add.setFont(font);
        bfs.setFont(font);
        dfs.setFont(font);
        adjacency.setFont(font);
        incidence.setFont(font);
        list.setFont(font);
        kruskal.setFont(font);


        add(vertexL);
        add(vertex);
        add(vertexL1);
        add(vertex1);
        add(checkBox);
        add(add);
        add(bfs);
        add(dfs);
        add(adjacency);
        add(incidence);
        add(list);
        add(kruskal);

        vertex1.setEnabled(false);

        checkBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                AbstractButton abstractButton = (AbstractButton) e.getSource();
                ButtonModel buttonModel = abstractButton.getModel();
                if (buttonModel.isSelected()) {
                    vertex1.setEnabled(true);
                    checked = true;
                } else {
                    vertex1.setEnabled(false);
                    checked = false;
                }
            }
        });


        matrix = new String[0][0];

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == kruskal) {
            boolean exist = false;
            Set<Edge> edges1 = new HashSet<Edge>();
            Set<String> ver = new HashSet<>(vertexSet);
            for (Edge ed : edges) {
                if (ver.contains(ed.second)) {
                    edges1.add(ed);
                    ver.remove(ed.second);
                }
            }
            KruskalGraph kruskalGraph = new KruskalGraph((MyFrame) this.getRootPane().getParent(), edges1);
        }
        /**********************************************************************/
        else if (e.getSource() == adjacency) {
            column = new String[vertexSet.size() + 1];
            matrix = new String[vertexSet.size() + 1][vertexSet.size() + 1];
            column[0] = "";
            matrix[0][0] = "";


            int i = 1;
            for (String v : vertexSet) {
                column[i] = v;
                matrix[i][0] = v;
                i++;
            }

            String v1;
            String v2;
            for (Edge edge : edges) {

                v1 = edge.first;
                v2 = edge.second;
                int x = 0;
                int y = 0;

                for (int j = 1; j < matrix.length; j++) {
                    if (matrix[j][0].equals(v1)) x = j;
                    if (matrix[j][0].equals(v2)) y = j;
                    if (x != 0 && y != 0) break;
                }
                matrix[x][y] = "1";
                matrix[y][x] = "1";
            }

            matrixN matrixN = new matrixN((MyFrame) this.getRootPane().getParent(), matrix, column);
        }

        /**********************************************************************/
        else if (e.getSource() == incidence) {
            String[] column1 = new String[edges.size() + 1];
            column1[0] = "";
            incidenceMatrix = new String[vertexSet.size() + 1][edges.size() + 1];
            incidenceMatrix[0][0] = "";

            int i = 1;
            for (String v : vertexSet) { // wypełnienie wierszy początkowych
                incidenceMatrix[i][0] = v;
                i++;
            }

            String v1;
            String v2;

            i = 1;
            for (Edge ed : edges) { // wypełnienie nagłówków oraz macierzy
                column1[i] = ed.toString();

                v1 = ed.first;
                v2 = ed.second;
                int x = 0;
                int y = 0;
                for (int j = 1; j < incidenceMatrix.length; j++) {
                    if (incidenceMatrix[j][0].equals(v1)) x = j;
                    if (incidenceMatrix[j][0].equals(v2)) y = j;
                    if (x != 0 && y != 0) break;
                }

                incidenceMatrix[x][i] = "1";
                incidenceMatrix[y][i] = "1";
                i++;
            }

            matrixN matrixN = new matrixN((MyFrame) this.getRootPane().getParent(), incidenceMatrix, column1);
        }
        /**********************************************************************/
        else if (e.getSource() == list) {
            String[][] list = new String[vertexSet.size() + 1][2]; // nowa tablica [i][0] wierzchołek   [i][1] elementy
            String[] col = {("Vertex"), ("Elements")}; // nagłówki

            list[0][0] = "";

            int i = 1;
            for (String v : vertexSet) {
                list[i][0] = v;
                list[i][1] = "";
                i++;
            }

            String v1;
            String v2;
            for (Edge ed : edges) { // wypełnienie macierzy
                v1 = ed.first;
                v2 = ed.second;
                int x = 0;
                int y = 0;

                for (i = 1; i < list.length; i++) {
                    if (list[i][0].equals(v1)) list[i][1] += (v2 + " ");
                    else if (list[i][0].equals(v2)) list[i][1] += (v1 + " ");
                }
            }
            matrixN matrixN = new matrixN((MyFrame) this.getRootPane().getParent(), list, col);
        }
        /**********************************************************************/
        else if (e.getSource() == add) {
            String a = vertex.getText().toString();
            if (a != null && !a.equals("")) {
                g.addVertex(a);

                vertexSet.add(a);

                if (checked) {
                    String b = vertex1.getText().toString();

                    vertexSet.add(b);
                    edges.add(new Edge(a, b)); // zbiór krawędzi


                    g.addVertex(b);
                    g.addEdge(a, b);
                }
                Collection<Object> cells = graphModel.getCells().values();
                mxUtils.setCellStyles(component.getGraph().getModel(),
                        cells.toArray(), mxConstants.STYLE_ENDARROW, mxConstants.NONE);
                layout.execute(graphAdapter.getDefaultParent());
            }
        }

        /**********************************************************************/
        else if (e.getSource() == bfs) {
            ArrayList<String> result = new ArrayList<>();
            String edge1;
            Set<Edge> set = new HashSet<>(edges);
            String a = vertex.getText().toString();
            Queue<String> queue = new ArrayDeque<>();

            if (!a.equals("") && a != null) {
                result.add(a);
                for (Edge ed : edges) {
                    if (ed.getFirst().equals(a)) {
                        queue.add(ed.getSecond());
                    } else if (ed.getSecond().equals(a)) {
                        queue.add(ed.getFirst());
                    }
                }

                while (!queue.isEmpty()) {
                    edge1 = queue.remove();
                    result.add(edge1);
                    for (Edge edge : edges) {
                        if (edge.getFirst().equals(edge1)) {
                            if (!result.contains(edge.getSecond())) {
                                queue.add(edge.getSecond());
                            }
                        }
                    }
                }

                String result1 = "";
                for (String str : result) {
                    result1 += str + " ";
                }
                JOptionPane.showMessageDialog(this, result1);
            }
        }

        /**********************************************************************/
        else if (e.getSource() == dfs) {
            ArrayList<String> result = new ArrayList<>();
            String edge1;
            Set<Edge> set = new HashSet<>(edges);
            String a = vertex.getText().toString();
            Stack<String> stack = new Stack<>();


            if (!a.equals("") && a != null) {
                result.add(a);
                for (Edge ed : edges) {
                    if (ed.getFirst().equals(a)) {
                        stack.push(ed.getSecond());
                    } else if (ed.getSecond().equals(a)) {
                        stack.push(ed.getFirst());
                    }
                }

                while (!stack.isEmpty()) {
                    edge1 = stack.pop();
                    result.add(edge1);
                    for (Edge edge : edges) {
                        if (edge.getFirst().equals(edge1)) {
                            if (!result.contains(edge.getSecond())) {
                                stack.push(edge.getSecond());
                            }
                        }
                    }
                }

                String result1 = "";
                for (String str : result) {
                    result1 += str + " ";
                }
                JOptionPane.showMessageDialog(this, result1);
            }
        }
    }

    /**********************************************************************/

    public static class MyEdge extends DefaultEdge { //weight
        @Override
        public String toString() {
            return "";
        }
    }


    public static class Edge {
        private String first;
        private String second;

        public Edge(String first, String second) {
            this.first = first;
            this.second = second;
        }

        public String getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }

        @Override
        public String toString() {
            return "{" + first + "," + second + "}";
        }

        @Override
        public boolean equals(Object obj) {
            Edge temp = (Edge) obj;
            if (temp.getFirst().equals(first) && temp.getSecond().equals(second)) return true;
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getFirst(), this.getSecond());
        }
    }


    class Vertex {
        String vertex;

        public Vertex(String vertex) {
            this.vertex = vertex;
        }
    }


}
