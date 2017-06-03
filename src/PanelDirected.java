import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.layout.mxOrganicLayout;
import com.mxgraph.swing.mxGraphComponent;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.ListenableDirectedWeightedGraph;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by Michał on 27.05.2017.
 */
public class PanelDirected extends JPanel implements ActionListener {
    private final Font font = new Font("Arial", Font.PLAIN, 20);
    private final Dimension TextFieldDim = new Dimension(50, 30);
    private JFormattedTextField vertex;
    private JFormattedTextField vertex1;
    private JFormattedTextField wage;
    private NumberFormatter format;
    private NumberFormat form;
    private JLabel vertexL;
    private JLabel vertexL1;
    private JLabel wageL;
    private JButton add;
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
    private ListenableDirectedWeightedGraph<String, MyEdge> g;
    private mxIGraphLayout layout;
    private JGraphXAdapter<String, MyEdge> graphAdapter;
    private mxGraphComponent component;

    public PanelDirected(Dimension dim) {


        g = new ListenableDirectedWeightedGraph<String, MyEdge>(MyEdge.class);
        graphAdapter =
                new JGraphXAdapter<String, MyEdge>(g);

        layout = new mxOrganicLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        component = new mxGraphComponent(graphAdapter);

        component.setPreferredSize(new Dimension(dim.width - 50, dim.height - 200));

        add(component);


        form = NumberFormat.getInstance();
        form.setGroupingUsed(false);
        format = new NumberFormatter(form);
        format.setMinimum(1);
        format.setMaximum(49);

        vertexL = new JLabel("Vertex 1");
        vertexL1 = new JLabel("Vertex 2");
        wageL = new JLabel("Wage");
        checkBox = new JCheckBox("edge");
        add = new JButton("Add");
        adjacency = new JButton("Adjacency");
        incidence = new JButton("Incidence");
        list = new JButton("List");
        kruskal = new JButton("Kruskal");


        add.addActionListener(this);
        adjacency.addActionListener(this);
        incidence.addActionListener(this);
        list.addActionListener(this);
        kruskal.addActionListener(this);

        vertex = new JFormattedTextField(format);
        vertex1 = new JFormattedTextField(format);
        wage = new JFormattedTextField(format);

        vertex.setPreferredSize(TextFieldDim);
        vertex1.setPreferredSize(TextFieldDim);
        wage.setPreferredSize(TextFieldDim);

        vertex.setFont(font);
        vertexL.setFont(font);
        vertexL1.setFont(font);
        wage.setFont(font);
        wageL.setFont(font);
        checkBox.setFont(font);
        vertex1.setFont(font);
        add.setFont(font);
        adjacency.setFont(font);
        incidence.setFont(font);
        list.setFont(font);
        kruskal.setFont(font);


        add(vertexL);
        add(vertex);
        add(vertexL1);
        add(vertex1);
        add(wageL);
        add(wage);
        add(checkBox);
        add(add);
        add(adjacency);
        add(incidence);
        add(list);
        add(kruskal);

        vertex1.setEnabled(false);
        wage.setEnabled(false);

        checkBox.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                AbstractButton abstractButton = (AbstractButton) e.getSource();
                ButtonModel buttonModel = abstractButton.getModel();
                if (buttonModel.isSelected()) {
                    vertex1.setEnabled(true);
                    checked = true;
                    wage.setEnabled(true);
                } else {
                    vertex1.setEnabled(false);
                    checked = false;
                    wage.setEnabled(false);
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
            ArrayList<Edge> temp = new ArrayList<>(edges);

            Collections.sort(temp);

            for (Edge ed : temp) {
                if (ver.contains(ed.second)) {
                    edges1.add(ed);
                    ver.remove(ed.second);
                }
            }
            KruskalGraphDirected primGraph = new KruskalGraphDirected((MyFrame) this.getRootPane().getParent(), edges1);
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
                matrix[x][y] = "-1";
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
                incidenceMatrix[y][i] = "-1";
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
                    double wageValue = Double.parseDouble(wage.getText().toString());
                    MyEdge myEdge;

                    vertexSet.add(b);
                    edges.add(new Edge(a, b, wageValue)); // zbiór krawędzi

                    g.addVertex(b);
                    myEdge = g.addEdge(a, b);
                    g.setEdgeWeight(myEdge, wageValue);
                }
                layout.execute(graphAdapter.getDefaultParent());
            }
        }
    }

    /**********************************************************************/

    public static class MyEdge extends DefaultWeightedEdge { //weight
        @Override
        public String toString() {
            return String.valueOf(getWeight());
        }
    }

    /**********************************************************************/

    public static class Edge implements Comparable<Edge> {
        private String first;
        private String second;
        private Double wage;

        public Edge(String first, String second, Double wage) {
            this.first = first;
            this.second = second;
            this.wage = wage;
        }

        public String getFirst() {
            return first;
        }

        public String getSecond() {
            return second;
        }

        public double getWage() {
            return wage;
        }

        @Override
        public String toString() {
            return "{" + first + "," + second + "}";
        }

        @Override
        public boolean equals(Object obj) {
            Edge temp = (Edge) obj;
            if (temp.getFirst().equals(first) && temp.getSecond().equals(second) && temp.getWage() == wage) return true;
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.getFirst(), this.getSecond(), this.getWage());
        }

        @Override
        public int compareTo(Edge o) {
            return this.wage.compareTo(o.getWage());
        }
    }

    /**********************************************************************/

    class Vertex {
        String vertex;

        public Vertex(String vertex) {
            this.vertex = vertex;
        }
    }


}
