package egovframework.admin.banners.vo;

import egovframework.framework.common.util.file.vo.NtsysFileVO;

public class BannersImgFileVo extends NtsysFileVO {

	public int bannerSeq;
	public String linkUrl;
	public String attrb1;
	public String attrb2;
	public String attrb3;

	public int getBannerSeq() {
		return bannerSeq;
	}
	public void setBannerSeq(int bannerSeq) {
		this.bannerSeq = bannerSeq;
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
	public void setAttrb3(String attrb3) {
		this.attrb3 = attrb3;
	}
}
