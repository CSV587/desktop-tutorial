package cs.concurrent;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/9/16.
 * @Description :
 */

/*
一个 Lock 对象和一个 synchronized 代码块之间的主要不同点是：
 synchronized 代码块不能够保证进入访问等待的线程的先后顺序。
 你不能够传递任何参数给一个 synchronized 代码块的入口。因此，对于 synchronized
代码块的访问等待设置超时时间是不可能的事情。
 synchronized 块必须被完整地包含在单个方法里。而一个 Lock 对象可以把它的 lock()
和 unlock() 方法的调用放在不同的方法里。
 */

public class Lock {

    public static void main(String[] arg) {
        ReentrantLock lock = new ReentrantLock();
        lock.lock();
        //critical section
        lock.unlock();
    }

}
/*
Lock 接口具有以下主要方法：
     lock()
     lockInterruptibly()
     tryLock()
     tryLock(long timeout, TimeUnit timeUnit)
     unlock()
lock() 将 Lock 实例锁定。如果该 Lock 实例已被锁定，调用 lock() 方法的线程将会阻塞，
直到 Lock 实例解锁。
lockInterruptibly() 方法将会被调用线程锁定，除非该线程被打断。此外，如果一个线程在通
过这个方法来锁定 Lock 对象时进入阻塞等待，而它被打断了的话，该线程将会退出这个
方法调用。
tryLock() 方法试图立即锁定 Lock 实例。如果锁定成功，它将返回 true，如果 Lock 实例
已被锁定该方法返回 false。这一方法永不阻塞。
tryLock(long timeout, TimeUnit timeUnit) 的工作类似于 tryLock() 方法，除了它在放弃锁定
Lock 之前等待一个给定的超时时间之外。
unlock() 方法对 Lock 实例解锁。一个 Lock 实现将只允许锁定了该对象的线程来调用此方
法。其他(没有锁定该 Lock 对象的线程)线程对 unlock() 方法的调用将会抛一个未检查异
常(RuntimeException)。
*/
