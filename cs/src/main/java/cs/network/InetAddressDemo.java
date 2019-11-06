package cs.network;

import java.awt.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/23.
 * @Description :
 */
public class InetAddressDemo {

    public static void main(String[] args) throws UnknownHostException {

        InetAddress[] inetAddresses = InetAddress.getAllByName("www.baidu.com");
        for(InetAddress i : inetAddresses){
            System.out.println(i.getHostAddress());
        }

        InetAddress inetAddress1 = InetAddress.getByName("127.0.0.1");
        System.out.println(inetAddress1.isLoopbackAddress());

        InetAddress inetAddress2 = InetAddress.getByName("0.0.0.0");
        System.out.println(inetAddress2.isAnyLocalAddress());

//        InetAddress inetAddress2 = InetAddress.getByName("180.101.49.11");
//        System.out.println(inetAddress2.getCanonicalHostName());

        byte[] address = {107,23,(byte)216,(byte)196};
        InetAddress lessWrong = InetAddress.getByAddress(address);
        InetAddress lessWrongWithName = InetAddress.getByAddress("lesswrong.com",address);
        System.out.println(lessWrong+"_____"+lessWrongWithName);

        InetAddress ibiblio = InetAddress.getByName("www.baidu.com");
        InetAddress helios = InetAddress.getByName("www.baidu.com");
        if(ibiblio.equals(helios)){
            System.out.println("same!");
        } else {
            System.out.println("different!");
        }

    }

}
