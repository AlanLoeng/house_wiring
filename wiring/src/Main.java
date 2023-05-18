import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Graph graph = new Graph();
        int length=0, width=0, height=0;
        int num_of_sockets=0;
        double[][] door = new double[4][3];
        Scanner scanner = new Scanner(System.in);
        boolean flag=true;//异常处理
        System.out.println("这是一个帮助设计室内布线的程序\n插座和房间进线孔位置固定，且在墙上，用电线将它们相连\n为了美观，线需要与至少一条墙边平行。线不能走屋顶。\n");
        while(flag){
            try{
                System.out.println("程序每组输入数据有以下的内容：\n" +
                        "第一行包括4个数字，其中第四个为整数，依次为房间长，宽，高和插座/进线孔的数量N。 \n长方体形状的房间完全位于三维直角坐标系的第一象限内，并且有一个角落在原点上；\n长宽高分别是xyz轴正方向上的长度。地板位于x-y平面。\n" +
                        "接下来的N行各有3个整数，第i行给出一个插座/进线孔的坐标(Xi,Yi,Zi)。\n" +
                        "最后4行各3个数字(x,y,z)表示从左上开始顺时针的门框四角坐标。\n");
                length = scanner.nextInt();
                width = scanner.nextInt();
                height = scanner.nextInt();
                num_of_sockets = scanner.nextInt();
                for (int i_cnt = 0; i_cnt < num_of_sockets; i_cnt++) {
                    graph.add_vertex(String.valueOf(i_cnt));
                    graph.change_xyz(String.valueOf(i_cnt), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
                }//输入三个插座节点和坐标
                for (int j_cnt = 0; j_cnt < 4; j_cnt++) {
                    for (int k_cnt = 0; k_cnt < 3; k_cnt++) {
                        door[j_cnt][k_cnt] = scanner.nextDouble();
                    }
                }//输入门的四角
                System.out.println("输入完成，输入y开始计算，输入其他任意字符以重新输入");
                String temp0=scanner.next();
                if(temp0.equals("y")||temp0.equals("Y")){
                    flag=false;
                }
            }
            catch(InputMismatchException e){
                System.out.println("\n输入异常，请输入数字，输入格式如下\n");
                scanner.next();
            }
            catch(Exception e){
                e.getMessage();
                e.printStackTrace();
                scanner.next();
            }
        }

        calculate_edge(num_of_sockets, graph,length,width,height);

        graph.print_by_adj();

    }

    private static void calculate_edge(int num_of_sockets, Graph graph,int l,int w,int h) {
        for (int i_cnt = 0; i_cnt < num_of_sockets; i_cnt++) {
            for (int j_cnt = i_cnt + 1; j_cnt < num_of_sockets; j_cnt++) {
                int sck1,sck2;
                double edge;
                sck1=on_which_wall(graph,String.valueOf(i_cnt),l,w,h);
                sck2=on_which_wall(graph,String.valueOf(j_cnt),l,w,h);
                graph.change_wall_no(String.valueOf(i_cnt),sck1);
                graph.change_wall_no(String.valueOf(j_cnt),sck2);
                if (Math.abs(sck1-sck2)!=2){
                    edge=cal_same_or_beside(graph,String.valueOf(i_cnt),String.valueOf(j_cnt));
                } else{
                    edge=cal_opposite(graph,String.valueOf(i_cnt),String.valueOf(j_cnt),sck1,l,w,h);
                }
                graph.add_edge_by_String(String.valueOf(i_cnt),String.valueOf(j_cnt),edge);
            }
        }
    }

    private static double cal_same_or_beside(Graph graph,String vertex1,String vertex2){
        double[] socket1=new double[3];
        double[] socket2=new double[3];
        socket1=graph.get_xyz(vertex1);
        socket2=graph.get_xyz(vertex2);
        return (Math.abs(socket1[0]-socket2[0])+Math.abs(socket1[1]-socket2[1])+Math.abs(socket1[2]-socket2[2]));
        //return 114514;
    }

    private static double get_smaller(double a,double b){
        if(a<=b){
            return a;
        }
        else{
            return b;
        }
    }
    private static double cal_opposite(Graph graph,String vertex1,String vertex2,int sck1,int l,int w,int h){
        double[] socket1=new double[3];
        double[] socket2=new double[3];
        double edge;
        socket1=graph.get_xyz(vertex1);
        socket2=graph.get_xyz(vertex2);
        if(sck1==1||sck1==3){
            edge=get_smaller((l-socket1[0]+l-socket2[0]),(socket1[0]+socket2[0]))+w+Math.abs(socket1[2]-socket2[2]);
        }else{
            edge=get_smaller((w-socket1[1]+w-socket2[1]),(socket1[1]+socket2[1]))+l+Math.abs(socket1[2]-socket2[2]);
        }
        return edge;
    }
    private static int on_which_wall(Graph graph,String vertex1,int l,int w,int h){//规定贴着xz面为1，其对面为3，贴着yz面为2，其对面为4
        double[] socket1=new double[3];
        socket1=graph.get_xyz(vertex1);
        if((socket1[1]==0)){
            return 1;
        }else if(socket1[0]==0){
            return 2;
        }else if(socket1[1]==w){
            return 3;
        } else if (socket1[0]==l) {
            return 4;
        }else{
            return -1;
        }
    }
}
