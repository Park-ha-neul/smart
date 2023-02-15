package egovframework.framework.common.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : Qrcode.java
 * 3. Package  : egovframework.framework.common.util
 * 4. Comment  : qrcode 생성
 * 5. 작성자   : 조정현
 * 6. 작성일   : 2014. 9. 17. 오후 3:11:19
 * </PRE>
 */
@Component("qrCodeUtil")
public class QrcodeUtil {

	protected Log log = LogFactory.getLog(this.getClass());

	@Resource(name="NtsysFileMngUtil")
    private NtsysFileMngUtil ntsysFileMngUtil;

	private final String DEFALUT_CODE_SIZE = "200";
	private final String DEFALUT_CODE_PADDING = "0";
	private final String DEFALUT_CODE_WIDTH_MARGIN = "0";
	private final String DEFALUT_CODE_HEIGTH_MARGIN = "0";
	private final String DEFALUT_CODE_COLOR = "black";
	private final String DEFALUT_CODE_BACK_COLOR = "white";
	private final String DEFALUT_CODE_TMP_FILE_PATH = EgovPropertiesUtil.getProperty("qrcode.temp.img.path");

	/**
	 * <PRE>
	 * 1. MethodName : makeQRcode
	 * 2. ClassName  : Qrcode
	 * 3. Comment   : 코드 임시 생성
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 9. 23. 오후 4:17:00
	 * </PRE>
	 *   @return NtsysFileVO
	 *   @param param
	 *   @param fileList
	 *   @return
	 *   @throws Exception
	 */
	public NtsysFileVO makeQRcode(DataMap param, List<MultipartFile> fileList) throws Exception{

		String codeCnts = param.getString("code_cn");											    				// qr code 들어갈 내용
		String codeNm = param.getString("code_nm", SysUtil.getDocId()) + ".png";									// qr code 이미지 이름
		String codeDir = param.getString("code_dir", DEFALUT_CODE_TMP_FILE_PATH); 									// qr code 저장될 위치
		int size = Integer.parseInt(param.getString("code_size", DEFALUT_CODE_SIZE));								// qr code 사이즈
		int padding = Integer.parseInt(param.getString("code_padding", DEFALUT_CODE_PADDING));						// qr code padding
		//2018.07.13 mk 기존  margin값을 배경이미지를 원본 크기로 보여주기 위해 width , height로 나눔
		int widthMargin = Integer.parseInt(param.getString("code_width_margin", DEFALUT_CODE_WIDTH_MARGIN));		// qr code width margin
		int heightMargin = Integer.parseInt(param.getString("code_height_margin", DEFALUT_CODE_HEIGTH_MARGIN));	// qr code height margin
		Color color = getColor(param.getString("code_color", DEFALUT_CODE_COLOR));									// qr code 색상
		Color backColor = getColor(param.getString("code_back_color", DEFALUT_CODE_BACK_COLOR));					// qr code 백그라운드 색상

		//2018.07.13 mk
		if(widthMargin - size > 0){
			widthMargin = widthMargin - size;
		}

		if(heightMargin - size > 0){
			heightMargin = heightMargin - size;
		}
		//end

		File bf = null;

		if(fileList != null){
			if(!fileList.isEmpty()){
				MultipartFile mf = (MultipartFile)fileList.get(0);

				File f = new File(mf.getOriginalFilename());
				mf.transferTo(f);

				bf = f;
			}
		}

		Image backImage = (bf != null) ? ImageIO.read(bf) :  null;			// 백그라운드 이미지

		String fileType = "png";
		File fileDir = new File(codeDir);

		// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh
		fileDir.setExecutable(false, true);
		fileDir.setReadable(true);
		fileDir.setWritable(true, true);
		// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh

		if(!fileDir.isDirectory()){
			fileDir.mkdirs();
		}

		File qrFile = new File(codeDir + codeNm);

		return createQRImage(qrFile, codeCnts, size, fileType, widthMargin, heightMargin, padding, color, backColor, backImage);
	}

	/**
	 * <PRE>
	 * 1. MethodName : createQRImage
	 * 2. ClassName  : Qrcode
	 * 3. Comment   : 코드 생성
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 9. 23. 오후 4:17:11
	 * </PRE>
	 *   @return NtsysFileVO
	 *   @param qrFile
	 *   @param qrCodeText
	 *   @param size
	 *   @param fileType
	 *   @param width_margin
	 *   @param height_margin
	 *   @param padding
	 *   @param color
	 *   @param back_color
	 *   @param back_image
	 *   @return
	 *   @throws WriterException
	 *   @throws IOException
	 */
	private NtsysFileVO createQRImage(File qrFile, String codeCnts, int size, String fileType, int widthMargin, int heightMargin, int padding, Color color, Color backColor, Image backImage) throws WriterException, IOException {
		// Create the ByteMatrix for the QR-Code that encodes the given String

		Hashtable hintMap = new Hashtable();
		hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");							// 인코딩 UTF-8로 설정
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		hintMap.put(EncodeHintType.MARGIN, padding);

		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix byteMatrix = qrCodeWriter.encode(codeCnts, BarcodeFormat.QR_CODE, size, size, hintMap);
		// Make the BufferedImage that are to hold the QRCode
		int matrixWidth = byteMatrix.getWidth();
		int matrixHeight = byteMatrix.getHeight();
		BufferedImage image = new BufferedImage(matrixWidth + widthMargin, matrixHeight + heightMargin, BufferedImage.TYPE_INT_RGB);
		image.createGraphics();

		Graphics2D graphics = (Graphics2D) image.getGraphics();

		// 백그라운드 이미지
		if(backImage != null){ graphics.drawImage(backImage, 0, 0, matrixWidth + widthMargin, matrixWidth + heightMargin, null); }
		else{
			graphics.setColor(backColor);
			graphics.fillRect(0, 0, matrixWidth + widthMargin, matrixWidth + heightMargin);
		}

		graphics.setColor(backColor);
		graphics.fillRect(widthMargin / 2, heightMargin / 2, matrixWidth, matrixWidth);
		// Paint and save the image using the ByteMatrix
		graphics.setColor(color);

		for (int i = 0; i < matrixWidth; i++) {
			for (int j = 0; j < matrixWidth; j++) {
				if (byteMatrix.get(i, j)) {
					graphics.fillRect(i + widthMargin / 2, j + heightMargin / 2, 1, 1);
				}
			}
		}

		NtsysFileVO fvo = null;

		if(ImageIO.write(image, fileType, qrFile)){
			//===========================================================
			// 파일정보 생성
			fvo = new NtsysFileVO();

			fvo.setFileAsltPath(qrFile.getAbsolutePath());
			fvo.setFileNm(qrFile.getName());
			fvo.setFileSize(qrFile.length());
			fvo.setFileExtNm("png");
			fvo.setContentType("image/png");
			//===========================================================
		}

		return fvo;
	}

	/**
	 * <PRE>
	 * 1. MethodName : downQRImage
	 * 2. ClassName  : Qrcode
	 * 3. Comment   : qr code 임시 파일 다운
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 9. 22. 오후 5:43:45
	 * </PRE>
	 *   @return void
	 *   @param param
	 *   @param request
	 *   @param response
	 *   @throws Exception
	 */
	public void downQRImage(DataMap param, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String codeNm = param.getString("code_nm");

		codeNm = codeNm.replaceAll("/", "");
		codeNm = codeNm.replaceAll("\\", "");
		codeNm = codeNm.replaceAll(".", "");
		codeNm = codeNm.replaceAll("&", "");

		String codeDir = param.getString("code_dir", DEFALUT_CODE_TMP_FILE_PATH); // qr code가 저장될 위치

		File uFile = new File(codeDir + codeNm);
		int fSize = (int) uFile.length();

		if (fSize > 0) {
			if(uFile != null){
	    		String mimetype = "image/png";

				response.setContentType(mimetype);
				setDisposition("qrcode.png", request, response);
				response.setContentLength((int) uFile.length());

				BufferedInputStream in = null;
				BufferedOutputStream out = null;

				try {
					in = new BufferedInputStream(new FileInputStream(uFile));
					out = new BufferedOutputStream(response.getOutputStream());

					FileCopyUtils.copy(in, out);
					out.flush();
				} catch (Exception ex) {
					//ex.printStackTrace();
					// 다음 Exception 무시 처리
					// Connection reset by peer: socket write error
					//System.out.println("IGNORED: " + ex.getMessage());
//					System.out.println("######### 예외 발생31 ##########");
					log.error("######### 예외 발생31 ##########");
				} finally {
					in.close();
					out.close();

					// 생성된 이미지 삭제
					NtsysFileVO fvo = new NtsysFileVO();
					fvo.setFileAsltPath(codeDir);
					fvo.setFileNm(codeNm);
					ntsysFileMngUtil.deleteFile(fvo, "O");
				}
			}
		}
	}

	/**
	 * 브라우저 구분 얻기.
	 *
	 * @param request
	 * @return
	 */
	private String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1) {
			return "MSIE";
		} else if (header.indexOf("Chrome") > -1) {
			return "Chrome";
		} else if (header.indexOf("Opera") > -1) {
			return "Opera";
		}
		return "Firefox";
	}

	/**
	 * Disposition 지정하기.
	 *
	 * @param filename
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	private void setDisposition(String filename, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String browser = getBrowser(request);

		String dispositionPrefix = "attachment; filename=";
		String encodedFilename = null;

		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		} else if (browser.equals("Firefox")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Opera")) {
			encodedFilename = "\"" + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
		} else if (browser.equals("Chrome")) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < filename.length(); i++) {
				char c = filename.charAt(i);
				if (c > '~') {
					sb.append(URLEncoder.encode("" + c, "UTF-8"));
				} else {
					sb.append(c);
				}
			}
			encodedFilename = sb.toString();
		} else {
			//throw new RuntimeException("Not supported browser");
			throw new IOException("Not supported browser");
		}

		response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename);

		if ("Opera".equals(browser)) {
			response.setContentType("application/octet-stream;charset=UTF-8");
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : getColor
	 * 2. ClassName  : Qrcode
	 * 3. Comment   : 컬러 추출
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 9. 23. 오후 2:49:51
	 * </PRE>
	 *   @return Color
	 *   @param color
	 *   @return
	 */
	private Color getColor(String color){
		Color c = null;
		color = color.toLowerCase();

		if(color.equals("blue")){
			c = Color.BLUE;
		} else if(color.equals("cyan")){
			c = Color.CYAN;
		} else if(color.equals("dark_gray")){
			c = Color.DARK_GRAY;
		} else if(color.equals("gray")){
			c = Color.GRAY;
		} else if(color.equals("green")){
			c = Color.GREEN;
		} else if(color.equals("light_gray")){
			c = Color.LIGHT_GRAY;
		} else if(color.equals("magenta")){
			c = Color.MAGENTA;
		} else if(color.equals("orange")){
			c = Color.ORANGE;
		} else if(color.equals("pink")){
			c = Color.PINK;
		} else if(color.equals("red")){
			c = Color.RED;
		} else if(color.equals("yellow")){
			c = Color.YELLOW;
		} else if(color.equals("black")){
			c = Color.BLACK;
		}

		return c;
	}
}

