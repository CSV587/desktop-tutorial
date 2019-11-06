package com.hy.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Configuration;

/**
 * .
 * Created by of liaoxg
 * date: 2019-01-23
 * user: lxg
 * package_name: com.hy.site.context
 */
@Configuration
public class SpringContextAware implements ApplicationContextAware {
  /**
   * .
   * 应用上下文
   */
  private static ApplicationContext applicationContext;

  /**
   * .
   * 设置应用上下文
   *
   * @param context 应用上下文
   * @throws BeansException bean异常
   */
  @Override
  public void setApplicationContext(
      final ApplicationContext context)
      throws BeansException {
    SpringContextAware.applicationContext = context;
  }

  /**
   * .
   * 获取上下文
   *
   * @return 上下文对象
   */
  public static ApplicationContext getCtx() {
    return SpringContextAware.applicationContext;
  }

  /**
   * .
   *
   * @param t   泛型
   * @param <T> 泛型
   * @return 泛型
   */
  public static <T> T getBean(final Class<T> t) {
    return SpringContextAware.applicationContext.getBean(t);
  }
}
