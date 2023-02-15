/**
 * 0. Project  : baseProject
 *
 * 1. FileName : CalVo.java
 * 2. Package : egovframework.framework.common.cal.vo
 * 3. Comment : 
 * 4. 작성자  : jjh
 * 5. 작성일  : 2013. 8. 26. 오후 1:45:02
 * 6. 변경이력 : 
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    jjh 	: 2013. 12. 23. :            : 신규 개발.
 */

package egovframework.framework.common.cal.vo;

import java.io.Serializable;

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

public class CalVo implements Serializable{
	
	private static final long serialVersionUID = 12L;
	
	/* 설정년 */
    private int viewYear;
    
    /* 설정월 */
    private int viewMonth;
    
    /* 설정일 */
    private int viewDay;
    
    /* 설정요일 */
    private String viewWeekNm;
    
    /* 설정주차 */
    private int viewWeek;
    
    /* 설정마지막날 */
    private int viewLastDay;
    
	/* 현재년 */
    private int curYear;
    
    /* 현재월 */
    private int curMonth;
    
    /* 현재일 */
    private int curDay;
    
    /* 현재요일 */
    private String curWeekNm;
    
    /* 현재주차 */
    private int curWeek;
    
    /* 현재마지막날 */
    private int curLastDay;
    
	public int getViewLastDay() {
		return viewLastDay;
	}

	public void setViewLastDay(int viewLastDay) {
		this.viewLastDay = viewLastDay;
	}

	public int getCurLastDay() {
		return curLastDay;
	}

	public void setCurLastDay(int curLastDay) {
		this.curLastDay = curLastDay;
	}

	public int getViewYear() {
		return viewYear;
	}

	public void setViewYear(int viewYear) {
		this.viewYear = viewYear;
	}

	public int getViewMonth() {
		return viewMonth;
	}

	public void setViewMonth(int viewMonth) {
		this.viewMonth = viewMonth;
	}

	public int getViewDay() {
		return viewDay;
	}

	public void setViewDay(int viewDay) {
		this.viewDay = viewDay;
	}

	public String getViewWeekNm() {
		return viewWeekNm;
	}

	public void setViewWeekNm(String viewWeekNm) {
		this.viewWeekNm = viewWeekNm;
	}

	public int getViewWeek() {
		return viewWeek;
	}

	public void setViewWeek(int viewWeek) {
		this.viewWeek = viewWeek;
	}

	public int getCurYear() {
		return curYear;
	}

	public void setCurYear(int curYear) {
		this.curYear = curYear;
	}

	public int getCurMonth() {
		return curMonth;
	}

	public void setCurMonth(int curMonth) {
		this.curMonth = curMonth;
	}

	public int getCurDay() {
		return curDay;
	}

	public void setCurDay(int curDay) {
		this.curDay = curDay;
	}

	public String getCurWeekNm() {
		return curWeekNm;
	}

	public void setCurWeekNm(String curWeekNm) {
		this.curWeekNm = curWeekNm;
	}

	public int getCurWeek() {
		return curWeek;
	}

	public void setCurWeek(int curWeek) {
		this.curWeek = curWeek;
	}
    
    
}
