package cs.Transaction;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/25.
 * @Description :
 */
public class JDBCTransaction {

    public static void main(String[] args){

        java.sql.Connection conn = null;
        try{
            conn = conn = DriverManager.getConnection("jdbc:oracle:thin:@host:1521:SID","username","userpwd");
            // 将自动提交设置为 false，
            //若设置为 true 则数据库将会把每一次数据更新认定为一个事务并自动提交
            conn.setAutoCommit(false);
            Statement stmt = conn.createStatement();
            stmt = conn.createStatement();
            // 将 A 账户中的金额减少 500
            stmt.execute("update t_account set amount = amount - 500 where account_id = 'A'");
            // 将 B 账户中的金额增加 500
            stmt.execute("update t_account set amount = amount + 500 where account_id = 'B'");
            // 提交事务
            conn.commit();
            // 事务提交：转账的两步操作同时成功
        } catch(SQLException e){
            try{
                //发生异常，回滚在本事务中的操作
                conn.rollback();
                //事务回滚：转账的两步操作完全撤销
                conn.close();
            }catch(Exception ignore){

            }
            e.printStackTrace();
        }


    }

}
