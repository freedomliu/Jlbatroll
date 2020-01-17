package com.freedom.jlbatross.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;

import java.util.HashMap;
import java.util.Map;

/**
 * 分页
 * <p>Title: PageResult</p>  
 * <p>Description: </p>  
 * @author liuxiangtao90  
 * @date 2018年4月11日 上午9:57:30
 */
public class PageResult 
{
	/**
	 * @Decription:
	 * @Author: liuxiangtao90
	 * @CreateDate: Created in 2020/1/17 14:46
	 * @param:
	 * @param page
	 * @Return: java.util.Map<java.lang.String,java.lang.Object>
	 */
	public static Map<String,Object> getPageMap(IPage<?> page) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		resultMap.put("page_count",  page.getPages());
		resultMap.put("count", page.getTotal());
		resultMap.put("page", page.getCurrent());
		resultMap.put("data", page.getRecords());
		resultMap.put("code", 0);
		resultMap.put("msg", "");
		return resultMap;
	}
}
