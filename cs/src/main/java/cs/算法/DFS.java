package cs.算法;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/11.
 * @Description : DFS
 */
public class DFS {

    static class Graph {
        private List vertexList;//存储点的链表
        private int[][] edges;//邻接矩阵，用来存储边
        private int numOfEdges;//边的数目
        private boolean[] visited;//记录节点是否已被访问
        public Graph(int n) {
            //初始化矩阵，一维数组，和边的数目
            edges=new int[n][n];
            vertexList=new ArrayList(n+2);
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

        public void setVisited(boolean[] visited) {
            this.visited = visited;
        }
    }

    public static void DFS(Graph g){
        // 初始化所有的节点的访问标志
        for(int v = 0; v < g.visited.length; v++) {
            g.visited[v] = false;
        }
        Stack<Integer> stack =new Stack<>();
        for(int i=1;i<=g.vertexList.size();i++){
            if(g.visited[i]==false){
                g.visited[i]=true;
                System.out.print(g.vertexList.get(i)+" ");
                stack.push(i);
            }
            while(!stack.isEmpty()){
                // 当前出栈的节点
                int k = stack.pop();
                for(int j=1;j<=g.vertexList.size();j++){
                    // 如果是相邻的节点且没有访问过.
                    if(g.edges[k][j]==1&&g.visited[j]==false){
                        g.visited[j]=true;
                        System.out.print(g.vertexList.get(j)+" ");
                        stack.push(j);
                        // 这条路结束,返回上一个节点
                        break;
                    }
                }

            }
        }
        // 输出二维矩阵
        System.out.println(g.edges);
    }

}
