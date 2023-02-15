/**
 * 
 *
 * 1. FileName : pageNavigationVo.java
 * 2. Package : egovframework.framework.common.vo
 * 3. Comment : 
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 23. 오후 6:54:42
 * 6. 변경이력 : 
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 23. :            : 신규 개발.
 */

package egovframework.framework.common.page.vo;

import java.io.Serializable;

import egovframework.framework.common.util.StringUtil;

/**
 * <PRE>
 * 1. ClassName : 
 * 2. FileName  : pageNavigationVo.java
 * 3. Package  : egovframework.framework.common.vo
 * 4. Comment  : 
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 23. 오후 6:54:42
 * </PRE>
 */

public class pageNavigationVo implements Serializable{
	
	private static final long serialVersionUID = 12L;

	/* 총갯수 */
    private int totalCount;
    
    /* 기본페이지 */
    private int firstPage;
    
    /* 페이지당 행수 */
    private int rowPerPage;
    
    /* 현재페이시 번호 */
    private int currentPage;
    
    /* 네이게이션에 보일 숫자수 */
    private int naviCount;
    
    /* 마지막 페이지 */
    private int lastPage;
    
    /* 현재 데이터 No */
    private int currDataNo;
    
    /* 페이지 카운트 */
    private int pageCount;
    
    /* current Page input 네임 */
    private String pageInputName;
    
    /* 페이지 변경시 호출될 javascript 함수 */
    private String pageCallFunction;
    
    public String getPageCallFunction() {
		return pageCallFunction;
	}

	public void setPageCallFunction(String pageCallFunction) {
		this.pageCallFunction = pageCallFunction;
	}
	
	

	public String getPageInputName() {
		return pageInputName;
	}

	public void setPageInputName(String pageInputName) {
		this.pageInputName = pageInputName;
	}

	public int getPageCount() {
		return this.pageCount;
	}
	
	public void setPageCount(int pageCount){
		this.pageCount = pageCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getFirstPage() {
		return firstPage;
	}

	public void setFirstPage(int firstPage) {
		this.firstPage = firstPage;
	}

	public int getRowPerPage() {
		return rowPerPage;
	}

	public void setRowPerPage(int rowPerPage) {
		this.rowPerPage = rowPerPage;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getNaviCount() {
		return naviCount;
	}

	public void setNaviCount(int naviCount) {
		this.naviCount = naviCount;
	}
	
	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public int getCurrDataNo() {
		return currDataNo;
	}

	public void setCurrDataNo(int currDataNo) {
		this.currDataNo = currDataNo;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}    
	
}
