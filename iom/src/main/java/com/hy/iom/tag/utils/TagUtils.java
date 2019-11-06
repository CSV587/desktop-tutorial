package com.hy.iom.tag.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import com.hy.iom.common.page.PageResult;

public class TagUtils<T> extends PageResult<T>{

	@SuppressWarnings("rawtypes")
	public static TagUtils getSuccessResult(String result) {
		TagUtils tagUitls = new TagUtils<>();
		tagUitls.setCode(1);
		tagUitls.setTime(getDate());
		tagUitls.setSuccess(true);
		tagUitls.setMessage(result);
		return tagUitls;
	}
	
	@SuppressWarnings("rawtypes")
	public static TagUtils getFailureResult(String result) {
		TagUtils tagUitls = new TagUtils<>();
		tagUitls.setCode(-1);
		tagUitls.setTime(getDate());
		tagUitls.setSuccess(false);
		tagUitls.setMessage(result);
		return tagUitls;
	}
	
	//获取当前时间字符串
	public static String getDate() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
}
