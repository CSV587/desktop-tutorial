package com.hy.cpic.base.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * .
 * Created by of liaoxg
 * date: 2018/5/31
 * user: lxg
 * package_name: com.hy.error
 */
public final class ErrorUtil {
  /**
   * .
   * 私有构造类
   */
  private ErrorUtil() {
  }

  /**
   * .
   *
   * @param t 异常变量
   * @return 返回异常堆栈
   */
  public static String getStackTrace(final Throwable t) {
    StringWriter sw = new StringWriter();
    try (PrintWriter pw = new PrintWriter(sw)) {
      t.printStackTrace(pw);
      return sw.toString();
    }
  }
}
