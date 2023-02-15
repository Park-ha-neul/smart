package egovframework.admin.quickMenu.vo;

import egovframework.framework.common.util.file.vo.NtsysFileVO;

public class QuickMenuImgFileVo extends NtsysFileVO{

	public int quickMenuImageSeq;
	public String linkUrl;
	public String attrb1;
	public String attrb2;
	public String attrb3;
	public String dispYn = "Y";


	public int getQuickMenuImageSeq() {
		return quickMenuImageSeq;
	}

	public void setQuickMenuImageSeq(int getQuickMenuImageSeq) {
		this.quickMenuImageSeq = getQuickMenuImageSeq;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getAttrb1() {
		return attrb1;
	}

	public void setAttrb1(String attrb1) {
		this.attrb1 = attrb1;
	}

	public String getAttrb2() {
		return attrb2;
	}

	public void setAttrb2(String attrb2) {
		this.attrb2 = attrb2;
	}

	public String getAttrb3() {
		return attrb3;
	}

	public void setAttrb_3(String attrb3) {
		this.attrb3 = attrb3;
	}

	public String getDispYn() {
		return dispYn;
	}

	public void setDisp_yn(String dispYn) {
		this.dispYn = dispYn;
	}

}
