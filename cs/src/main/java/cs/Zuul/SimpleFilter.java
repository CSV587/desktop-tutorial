package cs.Zuul;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/12.
 * @Description :
 */

@Component
public class SimpleFilter extends ZuulFilter {

    private Logger log = LoggerFactory.getLogger(SimpleFilter.class);

    @Override
    public String filterType() {
        //过滤器类型
        return "pre";
    }

    @Override
    public int filterOrder() {
        //过滤器优先级，越大越靠后执行
        return 1;
    }

    @Override
    public boolean shouldFilter() {
        //判断是否需要过滤
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        log.info(String.format("%s request to %s",request.getMethod(),request.getRequestURL().toString()));
        log.info(String.format("LocalAddr:%s",request.getLocalAddr()));
        log.info("LocalName:%s",request.getLocalName());
        log.info("LocalPort:%s",request.getLocalPort());
        log.info("RemoteAddr:%s",request.getRemoteAddr());
        log.info("RemoteHost:%s",request.getRemoteHost());
        log.info("RemotePort:%s",request.getRemotePort());
        return null;
    }
}
