package cs.zookeeper;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.junit.Before;
import org.junit.Test;
import java.util.List;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/29.
 * @Description :
 */
public class ClientDemo {

    private static String connectString = "192.168.144.128:2181,192.168.144.127:2181,192.168.144.126:2181";
    private static int sessionTimeout = 2000;
    private ZooKeeper zkClient = null;
    @Before
    public void init() throws Exception {
        zkClient = new ZooKeeper(connectString, sessionTimeout, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 收到事件通知后的回调函数（用户的业务逻辑）
                System.out.println(event.getType() + "--" + event.getPath());
                // 再次启动监听(ZooKeeper中的Watcher是一次性的，即触发一次就会被取消，
                // 如果想继续Watch的话，需要客户端重新设置Watcher,即这里的getChildren)
                try {
                    zkClient.getChildren("/", true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    // 创建子节点
    @Test
    public void create() throws Exception {
        // 数据的增删改查
        // 参数 1：要创建的节点的路径； 参数 2：节点数据 ； 参数 3：节点权限 ；参数 4：节点的类型
        String nodeCreated = zkClient.create("/eclipse", "hello zk".getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
    }

    // 获取子节点
    @Test
    public void getChildren() throws Exception {
        List<String> children = zkClient.getChildren("/", true);
        for (String child : children) {
            System.out.println(child);
        }
        // 延时阻塞
        Thread.sleep(Long.MAX_VALUE);
    }

    // 判断 znode 是否存在
    @Test
    public void exist() throws Exception {
        Stat stat = zkClient.exists("/eclipse", false);
        System.out.println(stat == null ? "not exist" : "exist");
    }

}
