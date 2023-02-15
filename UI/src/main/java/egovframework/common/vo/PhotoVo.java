package egovframework.common.vo;

import org.springframework.web.multipart.MultipartFile;

public class PhotoVo {
    //photo_uploader.html페이지의 form태그내에 존재하는 file 태그의 name명과 일치시켜줌
    private MultipartFile filedata;
    //callback URL
    private String callback;
    //콜백함수??
    private String callbackFunc;

	public MultipartFile getFiledata() {
		return filedata;
	}
	public void setFiledata(MultipartFile filedata) {
		this.filedata = filedata;
	}
	public String getCallback() {
		return callback;
	}
	public void setCallback(String callback) {
		this.callback = callback;
	}
	public String getCallbackFunc() {
		return callbackFunc;
	}
	public void setCallbackFunc(String callbackFunc) {
		this.callbackFunc = callbackFunc;
	}
}

