package cs.SpringMVC;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import java.util.List;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/11/11.
 * @Description :
 */

//SpringMVC配置
@Configuration
public class WebMvcConfig implements WebMvcConfigurer { //实现WebMvcConfigurer接口
//public class WebMvcConfig extends WebMvcConfigurationSupport { //继承WebMvcConfigurationSupport类也可

    @Autowired
    TokenInterceptor tokenInterceptor;

    //配置静态资源
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){

        //把来自浏览器的静态资源请求/app/**、/bower_components/**映射到classpath的/static/app/、/static/bower_components/目录
        registry.addResourceHandler("/app/**")
                .addResourceLocations("classpath:/static/app/");
        registry.addResourceHandler("/bower_components/**")
                .addResourceLocations("classpath:/static/bower_components/");

    }

    //配置拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry){
        //注册自定义拦截器，添加拦截器路径和排除拦截路径，如下：默认拦截所有请求/**，但对于请求/api/**不予拦截
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/api/**"
                );

    }

    //跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**") //配置允许跨域路径
                .allowedOrigins("*") //配置允许访问的跨域资源的请求域名
                .allowedMethods("PUT,POST,GET,DELETE,OPTIONS") //配置允许访问跨域资源服务器的请求方法
                .allowedHeaders("*"); //配置允许请求header的访问，如X-TOKEN
    }

    //视图控制器配置
    @Override
    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("/index");
        registry.addViewController("/index").setViewName("/index");
        registry.addViewController("/about").setViewName("/about");
        registry.addViewController("/error/403").setViewName("/error/403");
        registry.addViewController("/error/500").setViewName("/error/500");
        /* 以上效果跟下列代码一样
        * @GetMapping(value = "/index")
        * public String index(){
        *   return "/index"
        * }
        * */
    }

    //消息转换器配置
    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters){
        FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();
        //定制过滤JSON返回
        config.setSerializerFeatures(
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullStringAsEmpty
        );
        fastJsonHttpMessageConverter.setFastJsonConfig(config);
        converters.add(fastJsonHttpMessageConverter);
    }

    //数据格式化器配置
    @Override
    public void addFormatters(FormatterRegistry registry){
        //将来自Controller请求参数中String类型的日期数据格式化为Date日期类型
        registry.addFormatter(new DateFormatter("yyyy-mm-dd"));
    }

    //视图解析器配置
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.viewResolver(new FreeMarkerViewResolver());
    }

}
