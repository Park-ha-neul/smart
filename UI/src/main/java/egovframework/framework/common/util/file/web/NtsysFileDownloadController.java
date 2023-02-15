package egovframework.framework.common.util.file.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;
import egovframework.framework.common.util.file.vo.NtsysFileVO;

/**
 * 파일 다운로드를 위한 컨트롤러 클래스
 * @author 신승복
 * @since 2013.10.07
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      	수정자           수정내용
 *  -------     --------   ---------------------------
 *   2013.10.07 신승복           최초 생성
 *
 * Copyright (C) 2013 by Ntsys  All right reserved.
 * </pre>
 */
@Controller
public class NtsysFileDownloadController {

	@Resource(name = "NtsysFileMngService")
	private NtsysFileMngService fileService;

	private static final int BUFFER_SIZE = 8192;

	private static Log log = LogFactory.getLog(NtsysFileDownloadController.class);

	/**
	 * 브라우저 구분 얻기.
	 *
	 * @param request
	 * @return
	 */
	private String getBrowser(HttpServletRequest request) {
		String header = request.getHeader("User-Agent");
		if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1) {
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

		String dispositionPrefix = "attachment; filename=\"";
		String encodedFilename = null;

		if (browser.equals("MSIE")) {
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		}
		else if (browser.equals("Trident")) {	// IE11 문자열 깨짐 방지
			encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll("\\+", "%20");
		}
		else if (browser.equals("Firefox")) {

			encodedFilename = filename;
		}
		else if (browser.equals("Opera")) {
			encodedFilename = new String(filename.getBytes("UTF-8"), "8859_1");
		}
		else if (browser.equals("Chrome")) {
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

		response.setHeader("Content-Disposition", dispositionPrefix + encodedFilename +"\"");

		if ("Opera".equals(browser)) {
			response.setContentType("application/octet-stream;charset=UTF-8");
		}
	}

	/**
	 * 첨부파일로 등록된 파일에 대하여 다운로드를 제공한다.(file_id)
	 *
	 * @param commandMap
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/common/file/FileDown.do")
	public void cvplFileDownload(
				Map<String, Object> commandMap
				, HttpServletRequest request
				, HttpServletResponse response) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);

		String atchFileId = param.getString("file_id");

//		인증된 사용자가 다운로드 받도록 인증확인 코드 일단 주석처리
//		Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();

//		if (isAuthenticated) {
			NtsysFileVO fileVO = new NtsysFileVO();
			fileVO.setFileId(atchFileId);
			NtsysFileVO fvo = fileService.selectFileInf(fileVO);

			File uFile;

			// d_type 설정이 안되어있으면 기본값
	    	if(StringUtil.nvl(param.getString("d_type")).equals("")){
	    		param.put("d_type", EgovPropertiesUtil.getProperty("Globals.fileSaveType"));
	    	}

			// type 별 다운로드 방식 다름
			// 원래파일이름, 확장자
			if(param.getString("d_type").equals("O")){
				uFile = new File(fvo.getFileAsltPath(), fvo.getFileNm());
			}
			// 변환 이름, 원래 확장자
			else if(param.getString("d_type").equals("I")){
				uFile = new File(fvo.getFileAsltPath(), fvo.getFileId() + "."+fvo.getFileExtNm());
			}
			// 변환 이름, 확장자
			else {
				uFile = new File(fvo.getFileAsltPath(), fvo.getFileId() + Globals.FILE_EXT_C);
			}
			// .file 을 사용하기에 file확장자로 파일을 검색한다
//			File uFile = new File(fvo.getFile_path(), fvo.getFile_id() + "."+fvo.getFile_ext());

//			uFile = new File(fvo.getFile_path(), fvo.getFile_id() + Globals.FILE_EXT_C);
			int fSize = (int) uFile.length();
//			String file_ext = "." + fvo.getFile_ext();

			if (fSize > 0) {

				String mimetype = "";

				//if (fvo.getFile_ext_nm().equalsIgnoreCase("hwp")) mimetype = "application/x-hwp";
				if (fvo.getFileExtNm().equalsIgnoreCase("hwp")) mimetype = "application/unknown";
				else if (fvo.getFileExtNm().equalsIgnoreCase("mdb")) mimetype = "application/msaccess";
				else if (fvo.getFileExtNm().equalsIgnoreCase("doc") || fvo.getFileExtNm().equalsIgnoreCase("docx")) mimetype = "application/msword";
				else if (fvo.getFileExtNm().equalsIgnoreCase("bin")) mimetype = "application/octet-stream";
				else if (fvo.getFileExtNm().equalsIgnoreCase("pdf")) mimetype = "application/pdf";
				else if (fvo.getFileExtNm().equalsIgnoreCase("ai") || fvo.getFileExtNm().equalsIgnoreCase("ps") || fvo.getFileExtNm().equalsIgnoreCase("eps")) mimetype = "application/postscript";
				else if (fvo.getFileExtNm().equalsIgnoreCase("rtf")) mimetype = "application/rtf";
				else if (fvo.getFileExtNm().equalsIgnoreCase("js"))  mimetype = "application/x-javascript";
				else if (fvo.getFileExtNm().equalsIgnoreCase("latex")) mimetype = "application/x-latex";
				else if (fvo.getFileExtNm().equalsIgnoreCase("mif"))  mimetype = "application/x-mif";
				else if (fvo.getFileExtNm().equalsIgnoreCase("xls") || fvo.getFileExtNm().equalsIgnoreCase("xlsx")) mimetype = "application/vnd.ms-excel";
				else if (fvo.getFileExtNm().equalsIgnoreCase("ppt") || fvo.getFileExtNm().equalsIgnoreCase("pptx")) mimetype = "application/vnd.ms-powerpoint";
				else if (fvo.getFileExtNm().equalsIgnoreCase("tcl")) mimetype = "application/x-tcl";
				else if (fvo.getFileExtNm().equalsIgnoreCase("tex")) mimetype = "application/x-tex";
				else if (fvo.getFileExtNm().equalsIgnoreCase("src")) mimetype = "application/x-wais-source";
				else if (fvo.getFileExtNm().equalsIgnoreCase("zip")) mimetype = "application/zip";
				else if (fvo.getFileExtNm().equalsIgnoreCase("au") || fvo.getFileExtNm().equalsIgnoreCase("snd")) mimetype = "audio/basic";
				else if (fvo.getFileExtNm().equalsIgnoreCase("wav")) mimetype = "audio/x-wav";
				else if (fvo.getFileExtNm().equalsIgnoreCase("gif")) mimetype = "image/gif";
				else if (fvo.getFileExtNm().equalsIgnoreCase("bmp")) mimetype = "image/bmp";
				else if (fvo.getFileExtNm().equalsIgnoreCase("png")) mimetype = "image/png";
				else if (fvo.getFileExtNm().equalsIgnoreCase("jpeg") || fvo.getFileExtNm().equalsIgnoreCase("jpg") || fvo.getFileExtNm().equalsIgnoreCase("jpe")) mimetype = "image/jpeg";
				else if (fvo.getFileExtNm().equalsIgnoreCase("tiff") || fvo.getFileExtNm().equalsIgnoreCase("tif")) mimetype = "image/tiff";
				else if (fvo.getFileExtNm().equalsIgnoreCase("gzip")) mimetype = "multipart/x-gzip";
				else if (fvo.getFileExtNm().equalsIgnoreCase("zip"))  mimetype = "multipart/x-zip";
				else if (fvo.getFileExtNm().equalsIgnoreCase("css"))  mimetype = "tfileType/css";
				else if (fvo.getFileExtNm().equalsIgnoreCase("html") || fvo.getFileExtNm().equalsIgnoreCase("htm"))	mimetype = "tfileType/html";
				else if (fvo.getFileExtNm().equalsIgnoreCase("rtx")) mimetype = "tfileType/richtfileType";
				else if (fvo.getFileExtNm().equalsIgnoreCase("xml")) mimetype = "tfileType/xml";
				else if (fvo.getFileExtNm().equalsIgnoreCase("xsl")) mimetype = "tfileType/xsl";
				else if (fvo.getFileExtNm().equalsIgnoreCase("mpeg") || fvo.getFileExtNm().equalsIgnoreCase("mpg") || fvo.getFileExtNm().equalsIgnoreCase("mpe"))	mimetype = "video/mpeg";
				else if (fvo.getFileExtNm().equalsIgnoreCase("qt") || fvo.getFileExtNm().equalsIgnoreCase("mov")) mimetype = "video/quicktime";
				else if (fvo.getFileExtNm().equalsIgnoreCase("avi")) 	mimetype = "video/x-msvideo";
				else if (fvo.getFileExtNm().equalsIgnoreCase("movie")) mimetype = "video/x-sgi-movie";
				else if (fvo.getFileExtNm().equalsIgnoreCase("mp3")) mimetype = "audio/x-mpeg";
				else if (fvo.getFileExtNm().equalsIgnoreCase("txt")) mimetype = "text/plain";
				else mimetype = "application/octet-stream";

//				String mimetype = "application/octet-stream;";

//				String mimetype = getMimetype(fvo.getFileExtNm());


								//접속 기기에 따라 변경
				if(request.getHeader("User-Agent").indexOf("Android") > 0) {
					response.setHeader("Content-Disposition", "attachment;filename=\""+URLEncoder.encode(fvo.getFileNm(), "UTF-8") + "\"");
				}
				else if(request.getHeader("User-Agent").indexOf("iPhone") > 0) {
					mimetype = "application/x-download;";
					response.setHeader("Content-Disposition", "inline;filename=\""+URLEncoder.encode(fvo.getFileNm(), "UTF-8") + "\"");
				}
				else {
					response.setHeader("Content-Disposition", "attachment;filename=\""+URLEncoder.encode(fvo.getFileNm(), "UTF-8") + "\"");
				}

				response.setContentType(mimetype);
				log.debug("mimetype : " + mimetype);

				//response.setHeader("Content-Disposition", "attachment; filename=\"" + URLEncoder.encode(fvo.getOrignlFileNm(), "utf-8") + "\"");
				setDisposition(fvo.getFileNm(), request, response);
				response.setContentLength(fSize);
				response.setBufferSize(fSize);
				log.debug("fSize : " + fSize);

				/*
				 * FileCopyUtils.copy(in, response.getOutputStream());
				 * in.close();
				 * response.getOutputStream().flush();
				 * response.getOutputStream().close();
				 */
				BufferedInputStream in = null;
				//BufferedOutputStream out = null;

				try {
					in = new BufferedInputStream(new FileInputStream(uFile));
					//out = new BufferedOutputStream(response.getOutputStream());

					FileCopyUtils.copy(in, response.getOutputStream());
					response.getOutputStream().flush();
				} catch (Exception ex) {
					//ex.printStackTrace();
					// 다음 Exception 무시 처리
					// Connection reset by peer: socket write error
					//log.debug("IGNORED: " + ex.getMessage());
					System.out.println("######### 예외 발생64 ##########");
				} finally {
					in.close();
					response.getOutputStream().close();
				}
			} else {
				response.setContentType("application/octet-stream");

				response.setCharacterEncoding("UTF-8");
				setDisposition(fvo.getFileNm(), request, response);
				PrintWriter printwriter = response.getWriter();

//				printwriter.println(fvo.getFile_nm() + " : 해당파일을 찾을수 없습니다.");
				printwriter.flush();
				printwriter.close();
			}

//		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : directFileDown
	 * 2. ClassName  : NtsysFileDownloadController
	 * 3. Comment   : 해당 경로에서 해당 이름의 파일을 다운로드한다.
	 * 4. 작성자    : ntsys
	 * 5. 작성일    : 2013. 10. 30. 오후 1:21:28
	 * </PRE>
	 *   @return void
	 *   @param commandMap
	 *   @param request
	 *   @param response
	 *   @throws Exception
	 */
	@RequestMapping(value = "/common/file/directFileDown.do")
	public void directFileDown(Map<String, Object> commandMap, HttpServletRequest request, HttpServletResponse response) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		String pathKey = param.getString("path");
		String filePathString = "";

		if ("".equals(pathKey) || pathKey == null) {
			filePathString = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath");
		} else {
			filePathString = EgovPropertiesUtil.getProperty(pathKey);
		}

		// filePathString 이 값이 없을경우(path경로라고 생각한다)
		if(filePathString.equals("99")){
			filePathString = pathKey;
		}

		// 확장자 명까지 적혀있어야 함
		String fileNm = param.getString("file_nm");

		//웹보안 관련 처리 2018.12.31 by pjh
		fileNm = fileNm.replaceAll("\\.\\.", "");
		fileNm = fileNm.replaceAll("\\/", "");
		fileNm = fileNm.replaceAll("\\\\", "");

		fileNm = URLDecoder.decode(fileNm, "UTF-8");

		//log.debug("@@@@filenm:" + fileNm);

		File uFile = new File(filePathString, fileNm);
		int fSize = (int) uFile.length();

		if (fSize > 0) {
			//String mimetype = "application/x-msdownload";
			String ext = fileNm.substring(fileNm.lastIndexOf("."));
			String mimetype = getMimetype(ext);

			response.setContentType(mimetype);
			setDisposition(fileNm, request, response);
			response.setContentLength(fSize);

			BufferedInputStream in = null;
			BufferedOutputStream out = null;

			try {
				in = new BufferedInputStream(new FileInputStream(uFile));
				out = new BufferedOutputStream(response.getOutputStream());

				FileCopyUtils.copy(in, out);
				out.flush();
			} catch (Exception ex) {
				// Connection reset by peer: socket write error
				log.debug("IGNORED: " + ex.getMessage());
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (Exception ignore) {
						// no-op
						log.debug("IGNORED: " + ignore.getMessage());
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (Exception ignore) {
						// no-op
						log.debug("IGNORED: " + ignore.getMessage());
					}
				}
			}

		} else {
			response.setContentType("application/x-msdownload");

			PrintWriter printwriter = response.getWriter();
			printwriter.println("<html>");
			printwriter.println("<br><br><br><h2>Could not get file name:<br>" + fileNm + "</h2>");
			printwriter.println("<br><br><br><center><h3><a href='javascript: history.go(-1)'>Back</a></h3></center>");
			printwriter.println("<br><br><br>&copy; webAccess");
			printwriter.println("</html>");
			printwriter.flush();
			printwriter.close();
		}
	}

	// 수정요망
	@RequestMapping(value = "/common/file/viewFile.do")
	public void viewFile(Map<String, Object> commandMap
			, HttpServletRequest request
			, HttpServletResponse response) throws Exception {

//		DataMap param = RequestUtil.getDataMap(request);
//		String atchFileId = param.getString("file_id");
//
//		NtsysFileVO fileVO = new NtsysFileVO();
//		fileVO.setFile_id(atchFileId);
//		NtsysFileVO fvo = fileService.selectFileInf(fileVO);
//
//		String mimeType 	= fvo.getContenttype();
//		String downFileName = fvo.getFile_path() + fvo.getFile_id()+"."+fvo.getFile_ext();
//
////		File file = new File(fvo.getFile_path(), fvo.getFile_id()+"."+fvo.getFile_ext());
//		File file = new File(fvo.getFile_path(), fvo.getFile_id() + Globals.FILE_EXT_C);
//
//		if (!file.exists()) {
//		    throw new FileNotFoundException(downFileName);
//		}
//
//		if (!file.isFile()) {
//		    throw new FileNotFoundException(downFileName);
//		}
//
//		byte[] b = new byte[BUFFER_SIZE];
//
//		if (mimeType == null) {
//		    mimeType = "application/octet-stream;";
//		}
//
//		response.setContentType(EgovWebUtil.removeCRLF(mimeType));
//		response.setHeader("Content-Disposition", "filename=image;");
//
//		BufferedInputStream fin = null;
//		BufferedOutputStream outs = null;
//
//		try {
//		    fin = new BufferedInputStream(new FileInputStream(file));
//		    outs = new BufferedOutputStream(response.getOutputStream());
//
//		    int read = 0;
//
//		    while ((read = fin.read(b)) != -1) {
//			outs.write(b, 0, read);
//		    }
//	// 2011.10.10 보안점검 후속조치
//		} finally {
//		    if (outs != null) {
//				try {
//				    outs.close();
//				} catch (Exception ignore) {
//				    System.out.println("IGNORE: " + ignore);
//				}
//			    }
//			    if (fin != null) {
//				try {
//				    fin.close();
//				} catch (Exception ignore) {
//				    System.out.println("IGNORE: " + ignore);
//				}
//			    }
//			}
	    }

	public String getMimetype(String ext){

		String mimetype = "application/octet-stream";
		if (ext.equalsIgnoreCase("hwp")) mimetype = "application/x-hwp";
		else if (ext.equalsIgnoreCase("mdb")) mimetype = "application/msaccess";
		else if (ext.equalsIgnoreCase("doc") || ext.equalsIgnoreCase("docx")) mimetype = "application/msword";
		else if (ext.equalsIgnoreCase("bin")) mimetype = "application/octet-stream";
		else if (ext.equalsIgnoreCase("pdf")) mimetype = "application/pdf";
		else if (ext.equalsIgnoreCase("ai") || ext.equalsIgnoreCase("ps") || ext.equalsIgnoreCase("eps")) mimetype = "application/postscript";
		else if (ext.equalsIgnoreCase("rtf")) mimetype = "application/rtf";
		else if (ext.equalsIgnoreCase("js"))  mimetype = "application/x-javascript";
		else if (ext.equalsIgnoreCase("latex")) mimetype = "application/x-latex";
		else if (ext.equalsIgnoreCase("mif"))  mimetype = "application/x-mif";
		else if (ext.equalsIgnoreCase("xls") || ext.equalsIgnoreCase("xlsx")) mimetype = "application/vnd.ms-excel";
		else if (ext.equalsIgnoreCase("ppt") || ext.equalsIgnoreCase("pptx")) mimetype = "application/vnd.ms-powerpoint";
		else if (ext.equalsIgnoreCase("tcl")) mimetype = "application/x-tcl";
		else if (ext.equalsIgnoreCase("tex")) mimetype = "application/x-tex";
		else if (ext.equalsIgnoreCase("src")) mimetype = "application/x-wais-source";
		else if (ext.equalsIgnoreCase("zip")) mimetype = "application/zip";
		else if (ext.equalsIgnoreCase("au") || ext.equalsIgnoreCase("snd")) mimetype = "audio/basic";
		else if (ext.equalsIgnoreCase("wav")) mimetype = "audio/x-wav";
		else if (ext.equalsIgnoreCase("gif")) mimetype = "image/gif";
		else if (ext.equalsIgnoreCase("bmp")) mimetype = "image/bmp";
		else if (ext.equalsIgnoreCase("png")) mimetype = "image/png";
		else if (ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpe")) mimetype = "image/jpeg";
		else if (ext.equalsIgnoreCase("tiff") || ext.equalsIgnoreCase("tif")) mimetype = "image/tiff";
		else if (ext.equalsIgnoreCase("gzip")) mimetype = "multipart/x-gzip";
		else if (ext.equalsIgnoreCase("zip"))  mimetype = "multipart/x-zip";
		else if (ext.equalsIgnoreCase("css"))  mimetype = "tfileType/css";
		else if (ext.equalsIgnoreCase("html") || ext.equalsIgnoreCase("htm"))	mimetype = "tfileType/html";
		else if (ext.equalsIgnoreCase("rtx")) mimetype = "tfileType/richtfileType";
		else if (ext.equalsIgnoreCase("xml")) mimetype = "tfileType/xml";
		else if (ext.equalsIgnoreCase("xsl")) mimetype = "tfileType/xsl";
		else if (ext.equalsIgnoreCase("mpeg") || ext.equalsIgnoreCase("mpg") || ext.equalsIgnoreCase("mpe"))	mimetype = "video/mpeg";
		else if (ext.equalsIgnoreCase("qt") || ext.equalsIgnoreCase("mov")) mimetype = "video/quicktime";
		else if (ext.equalsIgnoreCase("avi")) 	mimetype = "video/x-msvideo";
		else if (ext.equalsIgnoreCase("movie")) mimetype = "video/x-sgi-movie";
		else if (ext.equalsIgnoreCase("mp3")) mimetype = "audio/x-mpeg";
		else mimetype = "application/octet-stream";

		return mimetype;
	}
}
