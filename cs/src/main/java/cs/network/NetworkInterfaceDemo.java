package cs.network;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/10/23.
 * @Description :
 */
public class NetworkInterfaceDemo {

    public static void main(String[] args) throws SocketException, UnknownHostException {

        NetworkInterface ni1 = NetworkInterface.getByName("lo");
        if(ni1 == null){
            System.out.println("No such interface : lo");
        } else {
            System.out.println(ni1);
            System.out.println("---------------------");
        }
        Enumeration<InetAddress> e = ni1.getInetAddresses();
        while(e.hasMoreElements()){
            System.out.println(e.nextElement());
        }
        System.out.println("---------------------");

        InetAddress local = InetAddress.getByName("127.0.0.1");
        NetworkInterface ni2 = NetworkInterface.getByInetAddress(local);
        if(ni2 == null){
            System.out.println("No local loopback address");
        } else {
            System.out.println(ni2);
        }
        System.out.println("---------------------");

        Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
        while(interfaces.hasMoreElements()){
            NetworkInterface ni = interfaces.nextElement();
            System.out.println(ni);
        }

    }

}
