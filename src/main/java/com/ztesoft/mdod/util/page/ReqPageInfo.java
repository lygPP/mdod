package com.ztesoft.mdod.util.page;

import java.io.Serializable;

/**
 * 封装请求的pageInfo
 * @author Thinkpad
 *
 */
public class ReqPageInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private int pageNum;
	private int pageSize;
	
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	

}
