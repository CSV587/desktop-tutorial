package cs.concurrent.BlockingQueue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

/*
SynchronousQueue 类实现了 BlockingQueue 接口。
SynchronousQueue 是一个特殊的队列，它的内部同时只能够容纳单个元素。如果该队列已
有一元素的话，试图向队列中插入一个新元素的线程将会阻塞，直到另一个线程将该元素从
队列中抽走。同样，如果该队列为空，试图向队列中抽取一个元素的线程将会阻塞，直到另
一个线程向队列中插入了一条新的元素。
据此，把这个类称作一个队列显然是夸大其词了。它更多像是一个汇合点。
*/
public class SynchronousQueueDemo {
    BlockingQueue bq = new SynchronousQueue();
}
