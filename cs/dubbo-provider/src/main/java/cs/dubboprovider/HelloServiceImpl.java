package cs.dubboprovider;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/12/11.
 * @Description :
 */
import Interface.HelloService;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Component
@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
