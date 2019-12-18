package cs.Eureka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.EurekaClientConfigBean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/13.
 * @Description :
 */
@RestController
public class ComputeController {

    @Autowired
    private EurekaClientConfigBean eurekaClientConfigBean;

    @GetMapping("/eureka-service")
    public Object getEurekaServiceUrl(){
        return eurekaClientConfigBean.getServiceUrl();
    }

    @RequestMapping("/add")
    public Integer add(@RequestParam Integer a, @RequestParam Integer b){
        return a + b;
    }

}
