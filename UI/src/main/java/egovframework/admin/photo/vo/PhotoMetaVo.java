package egovframework.admin.photo.vo;

public class PhotoMetaVo {
	
	private static final long serialVersionUID = 1000L;

	private String metaInfo;			//메타 정보
	private int width;					//가로
	private int height;					//세로
	
	public String getMetaInfo() {
		return metaInfo;
	}
	public void setMetaInfo(String metaInfo) {
		this.metaInfo = metaInfo;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
	
}
