import java.util.ArrayList;

public class Graph {

    private int time=0;
    private ArrayList<Vertex> vertexList=new ArrayList<Vertex>();

    private ArrayList<Edge> edgeList =new ArrayList<>();
    private ArrayList<Edge> edgeList_4_sort=new ArrayList<>();
    private class Vertex{
        String string;
        int color=0XFFFFFF,d,f;
        double x,y,z;
        Vertex pi;
        ArrayList<Integer> adj=new ArrayList<Integer>();
        ArrayList<Double> weight_of_adj=new ArrayList<>();
        int which_wall;

        void add_adj_by_string(String s,double weight){
            for(int i=0;i<vertexList.size();i++){
                if (get_vertex(i).string.equals(s)){
                    adj.add(i);
                    weight_of_adj.add(weight);
                }
            }
        }
        void add_adj_by_index(int i,double weight){
            adj.add(i);
            weight_of_adj.add(weight);
        }
        Vertex (String s){
            this.string=s;
        }
        Vertex get_adj(int index){
            return get_vertex(this.adj.get(index));
        }
        double get_weight_by_adj(int index){return weight_of_adj.get(index);}
    }

    private class Edge{
        Vertex u;
        Vertex v;
        double Weight;

        Edge(Vertex add_u,Vertex add_v,double add_weight){
            u=add_u;
            v=add_v;
            Weight=add_weight;
        }
    }
    public void change_wall_no(String vertex_name,int j){
        for(int i=0;i<vertexList.size();i++){
            if (get_vertex(i).string.equals(vertex_name)) {
                get_vertex(i).which_wall=j;
            }
        }
    }

    public int get_wall_no(String vertex_name){
        for(int i=0;i<vertexList.size();i++){
            if (get_vertex(i).string.equals(vertex_name)) {
                return get_vertex(i).which_wall;
            }
        }
        return 6;
    }
    public void change_xyz (String vertex_name,double x1,double y1,double z1){
        for(int i=0;i<vertexList.size();i++){
            if (get_vertex(i).string.equals(vertex_name)) {
                get_vertex(i).x=x1;
                get_vertex(i).y=y1;
                get_vertex(i).z=z1;
            }
            }
    }

    public double[] get_xyz(String vertex_name){
        double[] xyz=new double[3];
        for(int i=0;i<vertexList.size();i++){
            if (get_vertex(i).string.equals(vertex_name)) {
                xyz[0]=get_vertex(i).x;
                xyz[1]=get_vertex(i).y;
                xyz[2]=get_vertex(i).z;
                return xyz;
            }
        }
        xyz[0]=-1;
        xyz[1]=-1;
        xyz[2]=-1;//无匹配返回-1
        return xyz;
    }
    public Vertex get_vertex(int index){
        return vertexList.get(index);
    }
    public void add_vertex(String s){
        vertexList.add(new Vertex(s));
    }
    public void add_edge_by_String(String a,String b,double weight){
        Vertex temp_a,temp_b;
        temp_a=get_vertex(0);
        temp_b=get_vertex(0);
        for(int i=0;i<vertexList.size();i++){
            if (get_vertex(i).string.equals(a)){
                get_vertex(i).add_adj_by_string(b,weight);
                temp_a=get_vertex(i);
            }else if (get_vertex(i).string.equals(b)){
                get_vertex(i).add_adj_by_string(a,weight);
                temp_b=get_vertex(i);
            }
        }
        Edge edge=new Edge(temp_a,temp_b,weight);
        edgeList.add(edge);
    }

    public void print_by_adj(){
        System.out.println("邻接表表示图:");
        for(int i=0;i<vertexList.size();i++){
            double[] sck1=get_xyz(String.valueOf(i));
            System.out.print(get_vertex(i).string+"_"+get_wall_no(String.valueOf(i))+"__"+String.valueOf(sck1[0])+"___"+String.valueOf(sck1[1])+"___"+String.valueOf(sck1[2]));
            for(int j=0;j<get_vertex(i).adj.size();j++){
                System.out.print(get_vertex(i).get_adj(j).string+"边长"+get_vertex(i).get_weight_by_adj(j)+"___");
            }
            System.out.println("___"+get_vertex(i).adj);
        }
        System.out.println("所有的边如下");
        for(int j=0;j<edgeList.size();j++){
            System.out.println(edgeList.get(j).u.string+"到"+edgeList.get(j).v.string+"权值为"+edgeList.get(j).Weight);
        }
    }

    public void DFS() {
        System.out.println("DFS遍历图:");
        for (int u = 0; u < vertexList.size(); u++) {
            vertexList.get(u).color = 0xFFFFFF;
            vertexList.get(u).pi = null;
        }
        time=0;
        for (int u = 0; u < vertexList.size(); u++) {
            if(vertexList.get(u).color==0xFFFFFF){
                DFS_VISIT(vertexList.get(u));
            }
        }
        System.out.println("前驱表：");
        for(int i=0;i<vertexList.size();i++){
            String s;
            if (get_vertex(i).pi!=null){
                s=get_vertex(i).pi.string;
            }
            else{
                s=null;
            }
            System.out.println(s+"-->"+get_vertex(i).string);
        }
    }
    public void DFS_VISIT(Vertex u){
        time++;
        u.d=time;
        System.out.println(u.string+" d="+u.d);
        u.color=0x808080;
        for(int v=0;v<u.adj.size();v++){
            if(u.get_adj(v).color==0xFFFFFF){
                u.get_adj(v).pi=u;
                DFS_VISIT(u.get_adj(v));
            }
        }
        u.color=0x0;
        time++;
        u.f=time;
        System.out.println(u.string+" f="+u.f);
    }

    private void sort_edges_by_weight(){
        Edge temp=edgeList.get(0);
        for(int i=0;i<edgeList.size();i++){
            edgeList_4_sort.add(edgeList.get(i));
        }
        int pivot=0;
        for(int i=0;i<edgeList.size();i++){
            if (edgeList_4_sort.get(i).Weight<edgeList_4_sort.get(pivot).Weight){
                temp=edgeList.get(i);
            }
        }
    }
    public void Kruscal(){

    }
}
