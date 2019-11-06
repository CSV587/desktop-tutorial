package cs.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/16.
 * @Description :
 */
public class ForkJoinPoolDemo {

    public static void main(String[] arg) {

//        MyRecursiveAction myRecursiveAction = new MyRecursiveAction(24);
//        new ForkJoinPool().invoke(myRecursiveAction);
        MyRecursiveTask myRecursiveTask = new MyRecursiveTask(128);
        long mergedResult = new ForkJoinPool().invoke(myRecursiveTask);
        System.out.println("mergedResult = " + mergedResult);

    }


}

class MyRecursiveAction extends RecursiveAction {
    private long workLoad = 0;
    public MyRecursiveAction(long workLoad) {
        this.workLoad = workLoad;
    }
    @Override
    protected void compute() {
        //if work is above threshold, break tasks up into smaller tasks
        if(this.workLoad > 16) {
            System.out.println("Splitting workLoad : " + this.workLoad);
            List<MyRecursiveAction> subtasks =
                    new ArrayList<>();
            subtasks.addAll(createSubtasks());
            for(RecursiveAction subtask : subtasks){
                subtask.fork();
            }
        } else {
            System.out.println("Doing workLoad myself: " + this.workLoad);
        }
    }
    private List<MyRecursiveAction> createSubtasks() {
        List<MyRecursiveAction> subtasks =
                new ArrayList<>();
        MyRecursiveAction subtask1 = new MyRecursiveAction(this.workLoad / 2);
        MyRecursiveAction subtask2 = new MyRecursiveAction(this.workLoad / 2);
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        return subtasks;
    }
}
/*
MyRecursiveAction 将一个虚构的 workLoad 作为参数传给自己的构造子。如果 workLoad 高于一个特定阀值，该工作
将被分割为几个子工作，子工作继续分割。如果 workLoad 低于特定阀值，该工作将由 MyRecursiveAction 自己执行。
recursive -- adj.递归的;循环的  英[rɪˈkɜːsɪv]  美[rɪˈkɜːrsɪv]
*/

class MyRecursiveTask extends RecursiveTask<Long> {
    private long workLoad = 0;
    public MyRecursiveTask(long workLoad) {
        this.workLoad = workLoad;
    }
    protected Long compute() {
        //if work is above threshold, break tasks up into smaller tasks
        if(this.workLoad > 16) {
            System.out.println("Splitting workLoad : " + this.workLoad);
            List<MyRecursiveTask> subtasks =
                    new ArrayList<>();
            subtasks.addAll(createSubtasks());
            for(MyRecursiveTask subtask : subtasks){
                subtask.fork();
            }
            long result = 0;
            for(MyRecursiveTask subtask : subtasks) {
                result += subtask.join();
            }
            return result;
        } else {
            System.out.println("Doing workLoad myself: " + this.workLoad);
            return workLoad * 3;
        }
    }
    private List<MyRecursiveTask> createSubtasks() {
        List<MyRecursiveTask> subtasks =
                new ArrayList<MyRecursiveTask>();
        MyRecursiveTask subtask1 = new MyRecursiveTask(this.workLoad / 2);
        MyRecursiveTask subtask2 = new MyRecursiveTask(this.workLoad / 2);
        subtasks.add(subtask1);
        subtasks.add(subtask2);
        return subtasks;
    }
}
/*
除了有一个结果返回之外，这个示例和 RecursiveAction 的例子很像。MyRecursiveTask 类
继承自 RecursiveTask<Long>，这也就意味着它将返回一个 Long 类型的结果。
MyRecursiveTas 示例也会将工作分割为子任务，并通过 fork() 方法对这些子任务计划执
行。
此外，本示例还通过调用每个子任务的 join() 方法收集它们返回的结果。子任务的结果随
后被合并到一个更大的结果，并最终将其返回。对于不同级别的递归，这种子任务的结果合
并可能会发生递归。
*/