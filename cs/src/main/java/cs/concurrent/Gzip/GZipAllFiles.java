package cs.concurrent.Gzip;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/22.
 * @Description :
 */
public class GZipAllFiles {

    public final static int THREAD_COUNT = 4;

    public static void main(String[] args){
        ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
        for(String fileName : args){
            File f = new File(fileName);
            if(f.exists()){
                if(f.isDirectory()){
                    File[] files = f.listFiles();
                    for(int i = 0;i < files.length;i++){
                        if(!files[i].isDirectory()){
                            Runnable task = new GZipRunnable(files[i]);
                            pool.submit(task);
                        }
                    }
                } else {
                    Runnable task = new GZipRunnable(f);
                    pool.submit(task);
                }
            }
        }
        pool.shutdown();
    }

}
