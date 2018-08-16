package com.ztesoft.mdod.util.page;

import java.io.Serializable;

/**
 * 返回结果的pageInfo
 * @author Thinkpad
 *
 */
public class ResultPageInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private int pageNum;
	private int pageSize;
	private int totalRow;
	private int totalPage;
	
	public ResultPageInfo() {
	}
	
	public ResultPageInfo(int pageNum, int pageSize, int totalRow, int totalPage) {
		this.pageNum = pageNum;
		this.pageSize = pageSize;
		this.totalRow = totalRow;
		this.totalPage = totalPage;
	}
	
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
	public int getTotalRow() {
		return totalRow;
	}
	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}
	public int getTotalPage() {
		return totalPage;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	
}
