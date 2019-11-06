package cs.算法;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/11.
 * @Description : 图的临接矩阵实现
 */
import java.util.ArrayList;
import java.util.List;

/**
 * 利用临接矩阵实现图
 */
public class AMWGraph {
    private List vertexList;//存储点的链表
    private int[][] edges;//邻接矩阵，用来存储边
    private int numOfEdges;//边的数目

    public AMWGraph(int n) {
        //初始化矩阵，一维数组，和边的数目
        edges=new int[n][n];
        vertexList=new ArrayList(n);
        numOfEdges=0;
    }

    //得到结点的个数
    public int getNumOfVertex() {
        return vertexList.size();
    }

    //得到边的数目
    public int getNumOfEdges() {
        return numOfEdges;
    }

    //返回结点i的数据
    public Object getValueByIndex(int i) {
        return vertexList.get(i);
    }

    //返回v1,v2的权值
    public int getWeight(int v1,int v2) {
        return edges[v1][v2];
    }

    //插入结点
    public void insertVertex(Object vertex) {
        vertexList.add(vertex);
    }

    //插入边
    public void insertEdge(int v1,int v2,int weight) {
        edges[v1][v2]=weight;
        numOfEdges++;
    }

    //删除边
    public void deleteEdge(int v1,int v2) {
        edges[v1][v2]=0;
        numOfEdges--;
    }


    /**
     * 根据一个顶点的下标，返回该顶点的第一个邻接结点的下标
     * @param index 该顶点所在矩阵的下标
     * @return
     */
    public int getFirstNeighbor(int index) {
        for(int j=0;j<vertexList.size();j++) {
            if (edges[index][j]>0) {//行不变列变，横向检索
                return j;
            }
        }
        return -1;
    }


    /**
     * 根据前一个邻接结点的下标来取得下一个邻接结点
     * @param v1 指定的结点
     * @param v2 前一个邻接结点的下标
     * @return
     */
    public int getNextNeighbor(int v1,int v2) {
        for (int j=v2+1;j<vertexList.size();j++) {
            if (edges[v1][j]>0) {
                return j;
            }
        }
        return -1;
    }

    public static void main(String args[]) {
        int n=4,e=4;//分别代表结点个数和边的数目
        String labels[]={"V1","V1","V3","V4"};//结点的标识
        AMWGraph graph=new AMWGraph(n);
        for(String label:labels) {
            graph.insertVertex(label);//插入结点
        }
        //插入四条边
        graph.insertEdge(0, 1, 2);
        graph.insertEdge(0, 2, 5);
        graph.insertEdge(2, 3, 8);
        graph.insertEdge(3, 0, 7);

        System.out.println("结点个数是："+graph.getNumOfVertex());
        System.out.println("边的个数是："+graph.getNumOfEdges());

        graph.deleteEdge(0, 1);//删除<V1,V2>边
        System.out.println("删除<V1,V2>边后...");
        System.out.println("结点个数是："+graph.getNumOfVertex());
        System.out.println("边的个数是："+graph.getNumOfEdges());
    }
}
