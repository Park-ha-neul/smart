package egovframework.framework.common.util.file.vo;

import java.io.Serializable;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : NtsysFileVO.java
 * 3. Package  : egovframework.com.cmm.service
 * 4. Comment  : TB_FILE 테이블 VO Class
 * 5. 작성자   : sshinb
 * 6. 작성일   : 2013. 9. 30. 오후 5:50:31
 * @Modification Information
 *
 *    수정일       	수정자         수정내용
 *    -------       -------      -------------------
 *    2013. 9. 30.  sshinb		 최초생성
 *
 * </PRE>
 */
@SuppressWarnings("serial")
public class NtsysFileVO implements Serializable {

	/**
	 * 파일id
	 */
	public String 	fileId;
	/**
	 * 파일그룹id
	 */
	public String 	docId;
	/**
	 * 파일 실제 이름
	 */
	public String 	fileNm;
	/**
	 * 파일사이즈
	 */
	public long 	fileSize;
	/**
	 * 파일상대저장경로
	 */
	public String 	fileRltvPath;
	/**
	 * 파일절대저장경로
	 */
	public String 	fileAsltPath;
	/**
	 * srot
	 */
	public int 		srtOrd;
	/**
	 * 파일 비고
	 */
	public String 	fileRmk;
	/**
	 * 파일확장자
	 */
	public String 	fileExtNm;
	/**
	 * 저장 유저 id
	 */
	public String	ssUserId;

	/**
	 * 파일 ContentType
	 */
	public String	contentType;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getFileNm() {
		return fileNm;
	}

	public void setFileNm(String fileNm) {
		this.fileNm = fileNm;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileRltvPath() {
		return fileRltvPath;
	}

	public void setFileRltvPath(String fileRltvPath) {
		this.fileRltvPath = fileRltvPath;
	}

	public String getFileAsltPath() {
		return fileAsltPath;
	}

	public void setFileAsltPath(String fileAsltPath) {
		this.fileAsltPath = fileAsltPath;
	}

	public int getSrtOrd() {
		return srtOrd;
	}

	public void setSrtOrd(int srtOrd) {
		this.srtOrd = srtOrd;
	}

	public String getFileRmk() {
		return fileRmk;
	}

	public void setFileRmk(String fileRmk) {
		this.fileRmk = fileRmk;
	}

	public String getFileExtNm() {
		return fileExtNm;
	}

	public void setFileExtNm(String fileExtNm) {
		this.fileExtNm = fileExtNm;
	}

	public String getSsUserId() {
		return ssUserId;
	}

	public void setSsUserId(String ssUserId) {
		this.ssUserId = ssUserId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
