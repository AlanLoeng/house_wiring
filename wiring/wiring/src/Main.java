import java.io.File;
import java.io.FileNotFoundException;
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

        boolean flag=true;
        boolean ctn=true;
        System.out.println("这是一个帮助设计室内布线的程序\n输入房间尺寸，插座与门的坐标，可以求出让每个插座接上一根线所需电线的最短长度\n为了美观，线需要与至少一条墙边平行，且不走屋顶和门\n\n");
        System.out.println("！==========================================================！\n程序每组输入数据有以下的内容：\n\n" +
                "第一行包括4个数字，其中第四个为整数，依次为房间长，宽，高和插座/进线孔的数量N。\n"+
                "接下来的N行各有3个整数，第i行给出一个插座/进线孔的坐标(Xi,Yi,Zi)。\n" +
                "最后4行各3个数字(x,y,z)表示从左上开始顺时针的门框四角坐标。\n\n"+
                "长方体形状的房间完全位于笛卡尔坐标系的第一卦限内，并且有一个角落在原点上；\n长宽高分别是房间在xyz轴正方向上的长度。地板位于x-y平面。\n ！==========================================================！\n\n");

        while(ctn){
            while(flag){//对可能存在的已输入数据清除，防止异常输入后再尝试时出错
                length=0;
                width=0;
                height=0;
                num_of_sockets=0;
                for(int i=0;i<4;i++){
                    for(int j=0;j<3;j++){
                        door[i][j]=0;
                    }
                }
                graph.clear_edge_and_vertex();
                try{//数据输入
                    boolean ctn2=true;
                    while (ctn2){
                        System.out.println("请选择数据输入方式：\n1.文件输入        2.命令行输入");
                        String select0=scanner.next();
                        //从命令行输入数据
                        if(select0.equals("2")){
                            System.out.println("请输入房间长，宽，高和插座/进线孔的数量:\n");
                            length = scanner.nextInt();
                            width = scanner.nextInt();
                            height = scanner.nextInt();
                            num_of_sockets = scanner.nextInt();
                            //输入插座节点和坐标
                            System.out.println("请输入"+num_of_sockets+"行，每一行是一个插座的坐标X Y Z");
                            for (int i_cnt = 0; i_cnt < num_of_sockets; i_cnt++) {
                                graph.add_vertex(String.valueOf(i_cnt));
                                graph.change_xyz(String.valueOf(i_cnt), scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
                            }
                            //输入门的四角
                            System.out.println("请输入4行各3个数字X Y Z ，表示从左上开始顺时针的门框四角坐标。");
                            for (int j_cnt = 0; j_cnt < 4; j_cnt++) {
                                for (int k_cnt = 0; k_cnt < 3; k_cnt++) {
                                    door[j_cnt][k_cnt] = scanner.nextDouble();
                                }
                            }
                            break;
                        }
                        //从文件读取数据
                        else if(select0.equals("1")){
                            System.out.println("请输入文件地址");
                            boolean ctn1=true;
                            while (ctn1){
                                try {
                                    String f_path=scanner.next();
                                    File f = new File(f_path);
                                    Scanner scn_file = new Scanner(f);
                                    length = scn_file.nextInt();
                                    width = scn_file.nextInt();
                                    height = scn_file.nextInt();
                                    num_of_sockets = scn_file.nextInt();
                                    for (int i_cnt = 0; i_cnt < num_of_sockets; i_cnt++) {
                                        graph.add_vertex(String.valueOf(i_cnt));
                                        graph.change_xyz(String.valueOf(i_cnt), scn_file.nextDouble(), scn_file.nextDouble(), scn_file.nextDouble());
                                    }
                                    for (int j_cnt = 0; j_cnt < 4; j_cnt++) {
                                        for (int k_cnt = 0; k_cnt < 3; k_cnt++) {
                                            door[j_cnt][k_cnt] = scn_file.nextDouble();
                                        }
                                    }
                                    ctn1=false;
                                }
                                catch (FileNotFoundException e){//文件地址无效异常处理
                                    System.out.println("文件地址无效");

                                }
                            }
                            break;
                        }
                    }
                    System.out.println("输入完成，输入y开始计算，输入其他任意字符以重新输入");
                    String temp0=scanner.next();
                    if(temp0.equals("y")||temp0.equals("Y")){
                        flag=false;
                    }
                }
                catch(InputMismatchException e){//输入异常处理
                    System.out.println("\n输入异常，请输入数字，输入格式如下\n");
                    System.out.println("！==========================================================！\n程序每组输入数据有以下的内容：\n\n" +
                            "第一行包括4个数字，其中第四个为整数，依次为房间长，宽，高和插座/进线孔的数量N。\n"+
                            "接下来的N行各有3个整数，第i行给出一个插座/进线孔的坐标(Xi,Yi,Zi)。\n" +
                            "最后4行各3个数字(x,y,z)表示从左上开始顺时针的门框四角坐标。\n\n"+
                            "长方体形状的房间完全位于笛卡尔坐标系的第一卦限内，并且有一个角落在原点上；\n长宽高分别是房间在xyz轴正方向上的长度。地板位于x-y平面。\n ！==========================================================！\n\n");
                    scanner.nextLine();
                }
                catch(Exception e){//其他异常处理
                    e.getMessage();
                    e.printStackTrace();
                    //scanner.next();
                }
            }
            //开始计算
            int door_pos=where_is_the_door(door,width,length);
            calculate_edge(num_of_sockets, graph,length,width,height,door_pos,door);
            graph.Kruskal();
            //graph.print_by_adj();
            System.out.println("所需最小电线长度为"+graph.sum_minTree());
            flag=true;
            System.out.println("输入0以退出，输入其他任意字符以再进行一个计算");
            String temp1=scanner.next();
            if(temp1.equals("0")){
                ctn=false;
            }
        }

    }

    //计算边长，根据两个插座所在墙面相同/相邻还有相对分情况讨论
    private static void calculate_edge(int num_of_sockets, Graph graph,int l,int w,int h,int door_pos,double door[][]) {
        for (int i_cnt = 0; i_cnt < num_of_sockets; i_cnt++) {
            for (int j_cnt = i_cnt + 1; j_cnt < num_of_sockets; j_cnt++) {
                int sck1,sck2;
                double edge;
                sck1=on_which_wall(graph,String.valueOf(i_cnt),l,w,h);
                sck2=on_which_wall(graph,String.valueOf(j_cnt),l,w,h);
                graph.change_wall_no(String.valueOf(i_cnt),sck1);
                graph.change_wall_no(String.valueOf(j_cnt),sck2);
                if (Math.abs(sck1-sck2)!=2){
                    edge=cal_same_or_beside(graph,String.valueOf(i_cnt),String.valueOf(j_cnt),door_pos,sck1,sck2,door);
                } else{
                    edge=cal_opposite(graph,String.valueOf(i_cnt),String.valueOf(j_cnt),l,w,h,door_pos,sck1,sck2,door);
                }
                graph.add_edge_by_String(String.valueOf(i_cnt),String.valueOf(j_cnt),edge);
            }
        }
    }

    //计算两个插座在相邻/相同墙上的边长，这一堆if else是为了判断最短的横平竖直走线是否会穿过门
    private static double cal_same_or_beside(Graph graph,String vertex1,String vertex2,int door_pos,int sck1,int sck2,double door[][]){
        double[] socket1=new double[3];
        double[] socket2=new double[3];
        socket1=graph.get_xyz(vertex1);
        socket2=graph.get_xyz(vertex2);
        boolean cross;
        if(sck1!=door_pos&&sck2!=door_pos){
            cross=false;            
        } else if (socket1[2]==0||socket2[2]==0||socket2[2]>=door[0][2]||socket1[2]>=door[0][2]) {
            cross=false;
        } else if ( (door_pos==1)&&(    ((socket1[0]<=door[1][0])&&(socket2[0]<=door[1][0]))  ||  ((socket1[0]>=door[0][0])&&(socket2[0]<=door[0][0]))    )   ) {
            cross=false;
        } else if ( (door_pos==3)&&(    ((socket1[0]<=door[0][0])&&(socket2[0]<=door[0][0]))  ||  ((socket1[0]>=door[1][0])&&(socket2[0]<=door[1][0]))    )   ) {
            cross=false;
        }else if ( (door_pos==2)&&(    ((socket1[1]<=door[0][1])&&(socket2[1]<=door[0][1]))  ||  ((socket1[1]>=door[1][1])&&(socket2[1]<=door[1][1]))    )   ) {
            cross=false;
        }else if ( (door_pos==4)&&(    ((socket1[1]<=door[1][1])&&(socket2[1]<=door[1][1]))  ||  ((socket1[1]>=door[1][0])&&(socket2[1]<=door[1][0]))    )   ) {
            cross=false;
        }else{
            cross=true;
        }
        if(cross){
            return (Math.abs(socket1[0]-socket2[0])+Math.abs(socket1[1]-socket2[1])+Math.abs(Math.min(2*door[0][2]-socket2[2]-socket1[2],socket1[2]+socket1[2])));
        }else{
            return (Math.abs(socket1[0]-socket2[0])+Math.abs(socket1[1]-socket2[1])+Math.abs(socket1[2]-socket2[2]));
        }
    }

    //计算两个插座在相对的墙上的边长，这一堆if else是为了判断是否需要额外的距离绕过门，并且根据走线经过的墙面判断适用哪些走线方式，调用其长度的计算式。为了方便debug，计算式由cal_opposite_paths函数实现
    private static double cal_opposite(Graph graph,String vertex1,String vertex2,int l,int w,int h,int door_pos,int sck1,int sck2,double door[][]){
        double[] socket1=new double[3];
        double[] socket2=new double[3];
        double edge=-1;
        socket1=graph.get_xyz(vertex1);
        socket2=graph.get_xyz(vertex2);
        boolean cross=true;
        double max_door_x=Math.max(door[0][0],door[1][0]);
        double max_door_y=Math.max(door[0][1],door[1][1]);
        double max_door_z=Math.max(door[0][2],door[1][2]);
        double min_door_x=Math.min(door[0][0],door[1][0]);
        double min_door_y=Math.min(door[0][1],door[1][1]);
        if (socket1[2]==0||socket2[2]==0||socket2[2]>=door[0][2]||socket1[2]>=door[0][2]) {
            cross = false;
            //System.out.println(vertex1+"和"+vertex2+"不穿门");
        }
        if(sck1%2==1&&((door_pos==sck1&&max_door_x<=socket1[0])||(door_pos==sck2&&max_door_x<=socket2[0])||door_pos==2)){
            if(cross){
                edge=Math.min( cal_opposite_paths(20,socket1,socket2,door,l,w,h),cal_opposite_paths(11,socket1,socket2,door,l,w,h));
            }else{
                edge=Math.min(cal_opposite_paths(10,socket1,socket2,door,l,w,h),cal_opposite_paths(20,socket1,socket2,door,l,w,h));
            }
        }
        else if(sck1%2==1&&((door_pos==sck1&&min_door_x>=socket1[0])||(door_pos==sck2&&min_door_x>=socket2[0])||door_pos==4)){
            if(cross){
                edge=Math.min( cal_opposite_paths(21,socket1,socket2,door,l,w,h),cal_opposite_paths(10,socket1,socket2,door,l,w,h));
            }else{
                edge=Math.min(cal_opposite_paths(10,socket1,socket2,door,l,w,h),cal_opposite_paths(20,socket1,socket2,door,l,w,h));
            }
        }
        else if(sck1%2==0&&((door_pos==sck1&&min_door_y>=socket1[1])||(door_pos==sck2&&min_door_y>=socket2[1])||door_pos==3)){
            if(cross){
                edge=Math.min( cal_opposite_paths(41,socket1,socket2,door,l,w,h),cal_opposite_paths(30,socket1,socket2,door,l,w,h));
            }else{
                edge=Math.min(cal_opposite_paths(40,socket1,socket2,door,l,w,h),cal_opposite_paths(30,socket1,socket2,door,l,w,h));
            }
        }
        else if(sck1%2==0&&((door_pos==sck1&&max_door_y<=socket1[1])||(door_pos==sck2&&max_door_y<=socket2[1])||door_pos==1)){
            if(cross){
                edge=Math.min( cal_opposite_paths(40,socket1,socket2,door,l,w,h),cal_opposite_paths(31,socket1,socket2,door,l,w,h));
            }else{
                edge=Math.min(cal_opposite_paths(40,socket1,socket2,door,l,w,h),cal_opposite_paths(30,socket1,socket2,door,l,w,h));
            }
        }

        return edge;
    }

    //实现两插座处于相对的墙面时，根据走线经过的墙面不同，以及需要绕开门与否，最短长度有8种计算方法
    private static double cal_opposite_paths(int path_id,double socket1[],double socket2[],double door[][],int l,int w,int h){
        //经过的墙面 路径编号
        //123        1
        //143        2
        //214        3
        //234        4
        //最后一位0不穿门，1穿门
        if (path_id==10){
            return socket1[0]+socket2[0]+w+Math.abs(socket1[2]-socket2[2]);
        }
        if(path_id==11){
            return socket1[0]+socket2[0]+w+Math.min((2*door[0][2]-socket2[2]-socket1[2]),(socket2[2]+socket1[2]));
        }
        if (path_id==20){
            return 2*l-socket1[0]-socket2[0]+w+Math.abs(socket1[2]-socket2[2]);
        }
        if (path_id==21){
            return 2*l-socket1[0]-socket2[0]+w+Math.min((2*door[0][2]-socket2[2]-socket1[2]),(socket2[2]+socket1[2]));
        }
        if (path_id==30){
            return socket1[1]+socket2[1]+l+Math.abs(socket1[2]-socket2[2]);
        }
        if(path_id==31){
            return socket1[1]+socket2[1]+l+Math.min((2*door[0][2]-socket2[2]-socket1[2]),(socket2[2]+socket1[2]));
        }
        if(path_id==40){
            return 2*w-socket1[1]*socket2[1]+l+Math.abs(socket1[2]-socket2[2]);
        }
        if (path_id==41){
            return 2*w-socket1[1]*socket2[1]+l+Math.min((2*door[0][2]-socket2[2]-socket1[2]),(socket2[2]+socket1[2]));
        }
        return -1;
    }

    //判断插座处于哪个墙上
    //规定贴着xz面为1，其对面为3，贴着yz面为2，其对面为4
    private static int on_which_wall(Graph graph,String vertex1,int l,int w,int h){
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

    //判断门处于哪个墙上
    private static int where_is_the_door (double[][] door,int w,int l){
        if(door[0][1]==0&&door[2][1]==0){
            return 1;
        }
        if(door[0][0]==0&&door[2][0]==0){
            return 2;
        }
        if(door[0][1]==w&&door[2][1]==w){
            return 3;
        }
        if(door[0][0]==l&&door[2][1]==l){
            return 4;
        }
        return -1;
    }

}
