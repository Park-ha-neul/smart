/**
 *
 *
 * 1. FileName : InfoVo.java
 * 2. Package : egovframework.framework.security.vo
 * 3. Comment :
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 23. 오전 10:06:26
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 23. :            : 신규 개발.
 */

package egovframework.admin.common.vo;

import java.io.Serializable;
import java.util.List;

import egovframework.framework.common.util.EgovPropertiesUtil;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : InfoVo.java
 * 3. Package  : egovframework.framework.security.vo
 * 4. Comment  :
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 23. 오전 10:06:26
 * </PRE>
 */

public class UserInfoVo implements Serializable{

	private static final long serialVersionUID = 100L;

	private String userNo;				//사용자 번호
	private String id;					//사용자 id
	private String userNm;				//성명
	private String authorId;			//권한
	private String themaOption;			//테마옵션
	private String cur_prjct_id;		//선택 프로젝트 아이디
	private String cur_prjct_type;		//선택 프로젝트 타입
	private String cur_step_cd;			//선택 프로젝트 스탭코드


	private List authorIdList;			//권한 리스트

	public String getUserNo() {
		return userNo;
	}
	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserNm() {
		return userNm;
	}
	public void setUserNm(String userNm) {
		this.userNm = userNm;
	}
	public String getAuthorId() {
		return authorId;
	}
	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getThemaOption() {
		// 데이터가 없는경우 기본값으로 셋팅한다.
		if(themaOption.equals("")) {
			themaOption = EgovPropertiesUtil.getProperty("Globals.default.theme");
		}
		return themaOption;
	}
	public void setThemaOption(String themaOption) {
		this.themaOption = themaOption;
	}
	public List getAuthorIdList() {
		return authorIdList;
	}
	public void setAuthorIdList(List authorIdList) {
		this.authorIdList = authorIdList;
	}
	public String getCur_prjct_id() {
		return cur_prjct_id;
	}
	public void setCur_prjct_id(String cur_prjct_id) {
		this.cur_prjct_id = cur_prjct_id;
	}
	public String getCur_prjct_type() {
		return cur_prjct_type;
	}
	public void setCur_prjct_type(String cur_prjct_type) {
		this.cur_prjct_type = cur_prjct_type;
	}
	public String getCur_step_cd() {
		return cur_step_cd;
	}
	public void setCur_step_cd(String cur_step_cd) {
		this.cur_step_cd = cur_step_cd;
	}


}
