package cs;

/**
 * @Author : Chen Shu
 * @Date : Created by cs on 2019/8/30.
 * @Description :
 */
//@Configuration
//public class DruidConfig {
//
//    @ConfigurationProperties(prefix = "spring.datasource")
//
//    @Bean
//    public DataSource druid() {
//        return new DruidDataSource();
//    }
//
//    @Bean
//    public ServletRegistrationBean statViewServlet() {
//        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
//        Map<String, String> initParams = new HashMap<>();
//        initParams.put("loginUsername", "admin");
//        initParams.put("loginPassword", "123456");
//        initParams.put("allow", "");//默认就是允许所有访问
//        initParams.put("deny", "192.168.15.21");
//        bean.setInitParameters(initParams);
//        return bean;
//    }
//
//    @Bean
//    public FilterRegistrationBean webStatFilter(){
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter(new WebStatFilter());
//        Map<String,String> initParams = new HashMap<>();
//        initParams.put("exclusions","*.js,*.css,/druid/*");
//        bean.setInitParameters(initParams);
//        bean.setUrlPatterns(Arrays.asList("/*"));
//        return bean;
//    }
//
//}