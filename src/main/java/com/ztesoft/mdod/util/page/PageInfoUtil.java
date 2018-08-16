package com.ztesoft.mdod.util.page;

import com.github.pagehelper.PageInfo;

public class PageInfoUtil {

	@SuppressWarnings("rawtypes")
	public static ResultPageInfo getPageInfo(PageInfo pageInfo){
		int pageNum = pageInfo.getPageNum();
		int pageSize = pageInfo.getPageSize();
		int totalRow = (int)pageInfo.getTotal();
		int totalPage = pageInfo.getPages();
		return new ResultPageInfo(pageNum, pageSize, totalRow, totalPage);
	}
	
}
