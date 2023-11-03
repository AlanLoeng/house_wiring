import java.util.ArrayList;
import java.util.Comparator;

public class Graph {

    private int time = 0;
    //结点表，边表，最小生成树的边表
    private ArrayList<Vertex> vertexList = new ArrayList<Vertex>();

    private ArrayList<Edge> edgeList = new ArrayList<>();
    private ArrayList<Edge> minTree = new ArrayList<>();
    //结点类
    private class Vertex {
        String string;
        double x, y, z;
        Vertex pi;
        ArrayList<Integer> adj = new ArrayList<Integer>();
        ArrayList<Double> weight_of_adj = new ArrayList<>();
        int which_wall;
        //以下几个是向邻接表添加/读取结点、边的权值的函数
        void add_adj_by_string(String s, double weight) {
            for (int i = 0; i < vertexList.size(); i++) {
                if (get_vertex(i).string.equals(s)) {
                    adj.add(i);
                    weight_of_adj.add(weight);
                }
            }
        }

        void add_adj_by_index(int i, double weight) {
            adj.add(i);
            weight_of_adj.add(weight);
        }

        Vertex(String s) {
            this.string = s;
        }

        Vertex get_adj(int index) {
            return get_vertex(this.adj.get(index));
        }

        double get_weight_by_adj(int index) {
            return weight_of_adj.get(index);
        }
    }
    //边类
    private class Edge {
        Vertex u;
        Vertex v;
        double Weight;

        Edge(Vertex add_u, Vertex add_v, double add_weight) {
            u = add_u;
            v = add_v;
            Weight = add_weight;
        }
    }
    //以下2个函数改变/得到结点所在的墙壁编号
    public void change_wall_no(String vertex_name, int j) {
        for (int i = 0; i < vertexList.size(); i++) {
            if (get_vertex(i).string.equals(vertex_name)) {
                get_vertex(i).which_wall = j;
            }
        }
    }

    public int get_wall_no(String vertex_name) {
        for (int i = 0; i < vertexList.size(); i++) {
            if (get_vertex(i).string.equals(vertex_name)) {
                return get_vertex(i).which_wall;
            }
        }
        return 6;
    }
    //以下2个函数改变/得到结点的坐标
    public void change_xyz(String vertex_name, double x1, double y1, double z1) {
        for (int i = 0; i < vertexList.size(); i++) {
            if (get_vertex(i).string.equals(vertex_name)) {
                get_vertex(i).x = x1;
                get_vertex(i).y = y1;
                get_vertex(i).z = z1;
            }
        }
    }

    public double[] get_xyz(String vertex_name) {
        double[] xyz = new double[3];
        for (int i = 0; i < vertexList.size(); i++) {
            if (get_vertex(i).string.equals(vertex_name)) {
                xyz[0] = get_vertex(i).x;
                xyz[1] = get_vertex(i).y;
                xyz[2] = get_vertex(i).z;
                return xyz;
            }
        }
        xyz[0] = -1;
        xyz[1] = -1;
        xyz[2] = -1;//无匹配返回-1
        return xyz;
    }
    //以下2个函数添加/得到结点
    public Vertex get_vertex(int index) {
        return vertexList.get(index);
    }

    public void add_vertex(String s) {
        vertexList.add(new Vertex(s));
    }
    //添加边
    public void add_edge_by_String(String a, String b, double weight) {
        Vertex temp_a, temp_b;
        temp_a = get_vertex(0);
        temp_b = get_vertex(0);
        for (int i = 0; i < vertexList.size(); i++) {
            if (get_vertex(i).string.equals(a)) {
                get_vertex(i).add_adj_by_string(b, weight);
                temp_a = get_vertex(i);
            } else if (get_vertex(i).string.equals(b)) {
                get_vertex(i).add_adj_by_string(a, weight);
                temp_b = get_vertex(i);
            }
        }
        Edge edge = new Edge(temp_a, temp_b, weight);
        edgeList.add(edge);
    }
    //显示图中所有的结点，邻接表，边表以及所有边的权值，方便debug
    public void print_by_adj() {
        System.out.println("邻接表表示图:");
        for (int i = 0; i < vertexList.size(); i++) {
            double[] sck1 = get_xyz(String.valueOf(i));
            System.out.print(get_vertex(i).string + "_" + get_wall_no(String.valueOf(i)) + "__" + String.valueOf(sck1[0]) + "___" + String.valueOf(sck1[1]) + "___" + String.valueOf(sck1[2]));
            for (int j = 0; j < get_vertex(i).adj.size(); j++) {
                System.out.print(get_vertex(i).get_adj(j).string + "边长" + get_vertex(i).get_weight_by_adj(j) + "___");
            }
            System.out.println("___" + get_vertex(i).adj);
        }
        System.out.println("所有的边如下");
        for (int j = 0; j < edgeList.size(); j++) {
            System.out.println(edgeList.get(j).u.string + "到" + edgeList.get(j).v.string + "权值为" + edgeList.get(j).Weight);
        }
    }

    //清除所有的结点和边
    public void clear_edge_and_vertex(){
        edgeList.clear();
        vertexList.clear();
        minTree.clear();
    }

    //根据结点得到其在结点表中的下标
    public int get_vertex_index(Vertex vtx){
        for(int i=0;i<vertexList.size();i++){
            if(get_vertex(i).equals(vtx)){
                return(i);
            }
        }
        return (-1);
    }

    //Kruskal算法
    public void Kruskal(){
        int u,v;
        int size=vertexList.size();
        int mark[]=new int[size];
        int num=0;
        for(int i=0;i<size;i++){
            mark[i]=i;
        }
        edgeList.sort(new Comparator<Edge>() {
            @Override
            public int compare(Edge o1, Edge o2) {
                int rslt= (int) (o1.Weight- o2.Weight);
                return rslt;
            }
        });
        for(int i=0;i<edgeList.size();i++){
            u=get_vertex_index(edgeList.get(i).u);
            v=get_vertex_index(edgeList.get(i).v);
            if(mark[u]!=mark[v]){
                minTree.add(edgeList.get(i));
                num++;
                int elem=mark[v];
                for(int j=0;j<vertexList.size();j++){
                    if(mark[j]==elem){
                        mark[j]=mark[u];
                    }
                }
            }
            if(num==vertexList.size()-1){
                break;
            }
        }
    }

    //将最小生成树的边的权值求和，得到连接所有插座的最短路径
    public double sum_minTree(){
        double sum=0;
        for(int i=0;i<minTree.size();i++){
            sum+=minTree.get(i).Weight;
        }
        return sum;
    }
}

