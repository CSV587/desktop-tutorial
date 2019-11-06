package cs.算法;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/28.
 * @Description : Tree
 */
public class BFS {

    //树节点类
    static class TreeNode {

        private LinkedList<TreeNode> children;
        private TreeNode parent;
        private int value;

        public TreeNode(int value) {
            this.value = value;
            this.children = new LinkedList<>();
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public TreeNode getParent() {
            return parent;
        }

        public void setParent(TreeNode parent) {
            this.parent = parent;
        }

        public LinkedList<TreeNode> getChildren() {
            return children;
        }

        public void setChildren(LinkedList<TreeNode> children) {
            this.children = children;
        }

        public void addChild(TreeNode t){
            this.children.add(t);
        }

    }

    //构造树，返回根节点
    static TreeNode createTree() throws IOException {
        BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("请输入数字序列的对数：");
        int N = Integer.parseInt(br1.readLine());
        Map<Integer,Integer> m = new LinkedHashMap<>();
        SortedSet<Integer> set = new TreeSet<>();
        for(int i = 0; i < N; i++) {
            BufferedReader br2 = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("请输入数字序列【"+(i+1)+"】：");
            String s = br2.readLine();
            String[] num = s.split(",");
            int left = Integer.parseInt(num[0]);
            int right = Integer.parseInt(num[1]);
            set.add(left);
            set.add(right);
            if(m.containsKey(right)) {
                if(m.get(right) != left) {
                    System.out.println("Not a tree");
                    return null;
                }
            } else {
                m.put(right,left);
            }
        }
        TreeNode[] nodes = new TreeNode[set.last()+1];
        for(int c : set){
            nodes[c] = new TreeNode(c);
        }
        int k;
        int v;
        for(Map.Entry<Integer,Integer> entry : m.entrySet()) {
            k = entry.getKey();
            v = entry.getValue();
            nodes[k].setParent(nodes[v]);
            nodes[v].getChildren().add(nodes[k]);
        }
        for(TreeNode temp : nodes) {
            if(temp != null && temp.getParent() == null) return temp;
        }
        return null;
    }

    //利用队列FIFO特性，实现层次遍历
    static void bfs(TreeNode root) {
        Queue<TreeNode> q = new LinkedList<>();
        q.add(root);
        while(!q.isEmpty()){
            TreeNode temp = q.remove();
            System.out.println(temp.getValue());
            if(temp.getChildren().size() != 0){
                q.addAll(temp.getChildren());
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TreeNode t = createTree();
        bfs(t);
    }

}
