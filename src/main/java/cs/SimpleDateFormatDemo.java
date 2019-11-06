package cs;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/24.
 * @Description : SimpleDateFormat线程不安全，想用的话需要加锁，或者声明成final
 */
public class SimpleDateFormatDemo {

    /**
     * 定义一个全局的SimpleDateFormat
     */
    private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * 使用ThreadFactoryBuilder定义一个线程池
     */
    private static ThreadFactory namedThreadFactory = new ThreadFactoryBuilder()
            .setNameFormat("demo-pool-%d").build();

    private static ExecutorService pool = new ThreadPoolExecutor(5, 200,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    /**
     * 定义一个CountDownLatch，保证所有子线程执行完之后主线程再执行
     */
    private static CountDownLatch countDownLatch = new CountDownLatch(100);

    public static void main(String[] args) throws InterruptedException {
        //定义一个线程安全的HashSet
        Set<String> dates = Collections.synchronizedSet(new HashSet<String>());
        for (int i = 0; i < 100; i++) {
            //获取当前时间
            Calendar calendar = Calendar.getInstance();
            int finalI = i;
            pool.execute(() -> {
                synchronized(simpleDateFormat) {
                    //时间增加
                    calendar.add(Calendar.DATE, finalI);
                    //通过simpleDateFormat把时间转换成字符串
                    String dateString = simpleDateFormat.format(calendar.getTime());
                    //把字符串放入Set中
                    dates.add(dateString);
                    //countDown
                    countDownLatch.countDown();
                }

            });
        }
        //阻塞，直到countDown数量为0
        countDownLatch.await();
        //输出去重后的时间个数
        System.out.println(dates.size());
        pool.shutdown();
    }

}
