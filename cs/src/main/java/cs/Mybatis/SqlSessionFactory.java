package cs.Mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/25.
 * @Description :
 */
public class SqlSessionFactory {

    public static void main(String[] args) throws IOException {

        String resource = "classpath:mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        org.apache.ibatis.session.SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession session = factory.openSession();
        try {
            UserMapper mapper = session.getMapper(UserMapper.class);
            System.out.println(mapper.selectById(1));
        } finally {
            session.close();
        }

    }

}
