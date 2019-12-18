package cs.ThreadLocal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/12/4.
 * @Description :
 */
//为理解ThreadLocal而写
public class ConnectionManager {

//    private static ThreadLocal<Connection> connectionHolder = ThreadLocal.withInitial(() -> {
//        Connection conn = null;
//        try {
//            conn = DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/test", "username",
//                    "password");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return conn;
//    });
//
//    public static Connection getConnection() {
//        return connectionHolder.get();
//    }
    //若调用get方法时，当前线程共享变量没有设置，则调用initialValue获取默认值
    private static ThreadLocal<Integer> intHolder = ThreadLocal.withInitial(() -> {
        int a = 23333;
        return a;
    });

    public static int get233() {
        return intHolder.get();
    }

}