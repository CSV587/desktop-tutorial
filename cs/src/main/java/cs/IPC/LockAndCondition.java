package cs.IPC;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/12/3.
 * @Description :
 */
public class LockAndCondition {
    private final Lock lock = new ReentrantLock();

    private final Condition addConditon = lock.newCondition();
    private final Condition subConditon = lock.newCondition();

    private volatile int num = 0;
    private List<String> list = new ArrayList<>();

    public void add() {
        for (int i = 0; i < 10; i++) {
            lock.lock();

            try {
                if (list.size() == 10) {
                    addConditon.await();
                }
                num++;
                Thread.sleep(100);
                list.add("add " + num);
                System.out.println("The list size is " + list.size());
                System.out.println("The add thread is " + Thread.currentThread().getName());
                System.out.println("----------------");
                subConditon.signal();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    public void sub() {
        for (int i = 0; i < 10; i++) {
            lock.lock();

            try {

                if (list.size() == 0) {
                    subConditon.await();
                }
                num--;
                Thread.sleep(100);
                list.remove(0);
                System.out.println("The list size is " + list.size());
                System.out.println("The sub thread is " + Thread.currentThread().getName());
                System.out.println("-------------");
                addConditon.signal();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }


    public static void main(String[] args) {
        LockAndCondition task = new LockAndCondition();
        new Thread(task::add).start();
        new Thread(task::sub).start();
    }

}
