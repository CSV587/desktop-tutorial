package cs.concurrent.Gzip;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/22.
 * @Description :
 */
public class GZipRunnable implements Runnable {

    private final File input;

    public GZipRunnable(File f){
        this.input = f;
    }

    @Override
    public void run(){
        //不压缩已经压缩的文件
        if(!input.getName().endsWith(".gz")){
            File output = new File(input.getParent(),input.getName()+".gz");
            //不覆盖已经存在的文件
            if(!output.exists()){
                try(InputStream in = new BufferedInputStream(new FileInputStream(input));
                    OutputStream out = new BufferedOutputStream(
                            new GZIPOutputStream(new FileOutputStream(output)));
                    ){
                    int b;
                    while((b = in.read()) != -1) out.write(b);
                    out.flush();
                } catch (IOException e) {
                    System.err.println(e);
                }
            }
        }
    }

}
