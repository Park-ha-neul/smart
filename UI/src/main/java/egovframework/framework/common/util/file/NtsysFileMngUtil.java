package egovframework.framework.common.util.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

import egovframework.admin.photo.vo.PhotoMetaVo;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.EgovWebUtil;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.framework.common.util.imageUtil.Image;
import egovframework.framework.common.util.imageUtil.ImageLoader;

/**
 *
 * <PRE>
 * 1. ClassName : NtsysFileMngUtil.java
 * 2. FileName  : NtsysFileMngUtil.java
 * 3. Package  : egovframework.com.cmm.service
 * 4. Comment  :
 * 5. 작성자   : sshinb
 * 6. 작성일   : 2013. 9. 30. 오후 5:31:29
 * @Modification Information
 *   수정일		   수정자			수정내용
 *   -------		--------		---------------------------
 *   2013.09.30	 신승복			최초 생성
 *   2013.12.27	 jjh   			  수정
 * </PRE>
 */
@Component("NtsysFileMngUtil")
public class NtsysFileMngUtil {

	public static final int BUFF_SIZE = 2048;
	private static Log log = LogFactory.getLog(NtsysFileMngUtil.class);


	/**
	 * <PRE>
	 * 1. MethodName : parseFileInf
	 * 2. ClassName  : NtsysFileMngUtil
	 * 3. Comment   : 다중 파일을 업로드(비고 문구 없음)
	 * 4. 작성자	: jjh
	 * 5. 작성일	: 2013. 12. 27. 오전 10:11:05
	 * </PRE>
	 *   @return List<NtsysFileVO>
	 *   @param files
	 *   @param atchDocId
	 *   @param pathKey
	 *   @param ss_user_id
	 *   @param type
	 *   @return
	 *   @throws Exception
	 */
	public List<NtsysFileVO> parseFileInf(Map<String, MultipartFile> files, String atchDocId, String pathKey, String ss_user_id) throws Exception {
		// 기본 파일 저장장소를 설정후 넘김
		return parseFileInf(files, atchDocId, pathKey, ss_user_id, EgovPropertiesUtil.getProperty("Globals.fileSaveWebRoot"));
	}

	public List<NtsysFileVO> parseFileInf(Map<String, MultipartFile> files, String atchDocId, String pathKey, String ss_user_id, String webrootYn) throws Exception {
		// 값이 없을경우 기본값 설정
		if(StringUtil.nvl(webrootYn).equals("")){
			webrootYn = EgovPropertiesUtil.getProperty("Globals.fileSaveWebRoot");
		}
		// 기본 파일 저장타입을 설정후 넘김
		return parseFileInf(files, atchDocId, pathKey, ss_user_id, webrootYn, EgovPropertiesUtil.getProperty("Globals.fileSaveType"));
	}

	public List<NtsysFileVO> parseFileInf(Map<String, MultipartFile> files, String atchDocId, String pathKey, String ss_user_id, String webrootYn, String type) throws Exception {

		Iterator<Entry<String, MultipartFile>> itr = files.entrySet().iterator();
		List<NtsysFileVO> result  = new ArrayList<NtsysFileVO>();

		while (itr.hasNext()) {
			MultipartFile file 	= (MultipartFile)itr.next();

			// 값이 없을경우 기본값 설정
			if(StringUtil.nvl(type).equals("")){
				type = EgovPropertiesUtil.getProperty("Globals.fileSaveType");
			}
			// 값이 없을경우 기본값 설정
			if(StringUtil.nvl(webrootYn).equals("")){
				webrootYn = EgovPropertiesUtil.getProperty("Globals.fileSaveWebRoot");
			}

			// 파일을 쓴다.
			result.add(parseFileInf(file, atchDocId, pathKey, ss_user_id, webrootYn, type));
		}

		return result;
	}

	/**
	 * <PRE>
	 * 1. MethodName : parseFileInf
	 * 2. ClassName  : NtsysFileMngUtil
	 * 3. Comment   : 하나의 파일을 업로드(비고 문구 없음)
	 * 4. 작성자	: jjh
	 * 5. 작성일	: 2013. 12. 27. 오전 10:11:19
	 * </PRE>
	 *   @return NtsysFileVO
	 *   @param file
	 *   @param atchDocId
	 *   @param pathKey
	 *   @param ss_user_id
	 *   @param type
	 *   @return
	 *   @throws Exception
	 */

	public NtsysFileVO parseFileInf(MultipartFile file, String atchDocId, String pathKey, String ss_user_id) throws Exception {
		// 기본 파일 저장장소를 설정후 넘김
		return parseFileInf(file, atchDocId, pathKey, ss_user_id, EgovPropertiesUtil.getProperty("Globals.fileSaveWebRoot"));
	}
	public NtsysFileVO parseFileInf(MultipartFile file, String atchDocId, String pathKey, String ss_user_id, String webrootYn) throws Exception {
		// 값이 없을경우 기본값 설정
		if(StringUtil.nvl(webrootYn).equals("")){
			webrootYn = EgovPropertiesUtil.getProperty("Globals.fileSaveWebRoot");
		}
		// 기본 파일 저장타입을 설정후 넘김
		return parseFileInf(file, atchDocId, pathKey, ss_user_id, webrootYn, EgovPropertiesUtil.getProperty("Globals.fileSaveType"));
	}
	public NtsysFileVO parseFileInf(MultipartFile file, String atchDocId, String pathKey, String ss_user_id, String webrootYn, String type) throws Exception {

		// 값이 없을경우 기본값 설정
		if(StringUtil.nvl(type).equals("")){
			type = EgovPropertiesUtil.getProperty("Globals.fileSaveType");
		}
		// 값이 없을경우 기본값 설정
		if(StringUtil.nvl(webrootYn).equals("")){
			webrootYn = EgovPropertiesUtil.getProperty("Globals.fileSaveWebRoot");
		}

		// 비고는 공란으로 파일쓰기
		return chgSaveNtsysFileVO(file, atchDocId, pathKey, ss_user_id, "", webrootYn, type);
	}

	/**
	 * <PRE>
	 * 1. MethodName : chgSaveNtsysFileVO
	 * 2. ClassName  : NtsysFileMngUtil
	 * 3. Comment   : 하나의 파일을 업로드(비고 문구 있음)
	 * 4. 작성자	: jjh
	 * 5. 작성일	: 2013. 12. 27. 오전 11:41:41
	 * </PRE>
	 *   @return NtsysFileVO
	 *   @param file
	 *   @param atchDocId
	 *   @param pathKey
	 *   @param ss_user_id
	 *   @param file_rmk
	 *   @return
	 *   @throws Exception
	 */
	public NtsysFileVO chgSaveNtsysFileVO(MultipartFile file, String atchDocId, String pathKey, String ss_user_id, String file_rmk) throws Exception {
		// 기본은 변환으로 파일 저장
		return chgSaveNtsysFileVO(file, atchDocId, pathKey, ss_user_id, file_rmk, EgovPropertiesUtil.getProperty("Globals.fileSaveWebRoot"));
	}

	public NtsysFileVO chgSaveNtsysFileVO(MultipartFile file, String atchDocId, String pathKey, String ss_user_id, String file_rmk, String webrootYn) throws Exception {
		// 값이 없을경우 기본값 설정
		if(StringUtil.nvl(webrootYn).equals("")){
			webrootYn = EgovPropertiesUtil.getProperty("Globals.fileSaveWebRoot");
		}
		// 기본은 변환으로 파일 저장
		return chgSaveNtsysFileVO(file, atchDocId, pathKey, ss_user_id, file_rmk, webrootYn, EgovPropertiesUtil.getProperty("Globals.fileSaveType"));
	}

	/**
	 * <PRE>
	 * 1. MethodName : chgSaveNtsysFileVO
	 * 2. ClassName  : NtsysFileMngUtil
	 * 3. Comment   : 하나의 파일을 업로드(비고 문구 있음); FILE_EXT_C 는 globals.java 참조
	 * 4. 작성자	: 조정현
	 * 5. 작성일	: 2014. 8. 11. 오전 10:18:27
	 * </PRE>
	 *   @return NtsysFileVO
	 *   @param file
	 *   @param atchDocId
	 *   @param pathKey
	 *   <br/>&nbsp;&nbsp;1. 기본 저장경로(webroot or WEB-INF)에 추가로 패스 지정 [ex)board/]
	 *   <br/>&nbsp;&nbsp;2. properties에 선언된 문자열도 사용가능
	 *   <br/>&nbsp;&nbsp;* 처음에는 / 넣지않고 끝에는 / 를 넣어야함.
	 *   @param ss_user_id
	 *   @param file_rmk
	 *   @param webrootYn (Y : webroot 저장, N : WEB-INF 저장)
	 *   @param type (O : 이미지명.이미지확장자, C : file_id.FILE_EXT_C(선언된확장자), I : file_id.이미지확장자)
	 *   @return
	 *   @throws Exception
	 */
	public NtsysFileVO chgSaveNtsysFileVO(MultipartFile file, String atchDocId, String pathKey, String ss_user_id, String file_rmk, String webrootYn, String type) throws Exception {

		// 값이 없을경우 기본값 설정
		if(StringUtil.nvl(type).equals("")){
			type = EgovPropertiesUtil.getProperty("Globals.fileSaveType");
		}
		// 값이 없을경우 기본값 설정
		if(StringUtil.nvl(webrootYn).equals("")){
			webrootYn = EgovPropertiesUtil.getProperty("Globals.fileSaveWebRoot");
		}

		String filePathString = "";
		String atchFileIdString = "";
		String fileRPathString = "";

		// webroot 저장시거나 아닐경우
		if(webrootYn.equals("Y")){
			filePathString = EgovPropertiesUtil.getProperty("Globals.filePath");
			System.out.println("filePathString1 : " + filePathString);
		}
		else {
			filePathString = EgovPropertiesUtil.getProperty("Globals.filePath");
			System.out.println("filePathString2 : " + filePathString);
		}
		System.out.println("filePathString3 : " + filePathString);

		if (!"".equals(pathKey) && pathKey != null) {
			fileRPathString = EgovPropertiesUtil.getProperty(pathKey);

			// 해당 프로퍼티 값이 없을경우 99로 넘어옴.
			if(fileRPathString.equals("99")){
				fileRPathString = pathKey;
				filePathString += pathKey;
			}
			else {
				filePathString += fileRPathString;
			}
		}
		System.out.println("filePathString4 : " + filePathString);

		// filePathString 마지막에는 년도들 넣어서 구분해준다. PharmAi api 호출을 위해 주석 처리 2021-07-15
		/*filePathString += getTimeStamp() + "/";
		fileRPathString += getTimeStamp() + "/"; */

		File saveFolder = new File(EgovWebUtil.filePathBlackList(filePathString));

		// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjhing("ss_user_no"));
		saveFolder.setExecutable(true, true);
		saveFolder.setReadable(true);
		saveFolder.setWritable(true, true);
		// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh

		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}

		NtsysFileVO fvo;
		String filePath = "";

		String orginFileName 				= file.getOriginalFilename();
		String content_type					= file.getContentType();			// contentType
		atchFileIdString 					= SysUtil.getFileId();				// file_id

//		atchFileIdString 					= orginFileName; //doc_id로 파일명 ----- > 기존의 파일명으로 저장 할 수 있도록 수정  2021_07_15 lsh
		int index 		= orginFileName.lastIndexOf(".");
		String fileExt 	= orginFileName.substring(index + 1);
//		String fileName = atchFileIdString.substring(0,index);
		String fileName = atchFileIdString;
		long _size 		= file.getSize();

//		if (!"".equals(orginFileName)) {
//			filePath = filePathString + File.separator + fileName;
//			file.transferTo(new File(EgovWebUtil.filePathBlackList(filePath)));
//		}

		fvo = new NtsysFileVO();
		fvo.setFileId(atchFileIdString);					// file_id
		fvo.setDocId(atchDocId);							// doc_id
		fvo.setFileRmk(file_rmk);							// 파일 설명
		fvo.setFileNm(orginFileName);						// 파일 원래명
		// webroot 저장일경우 상대경로 넣어줌
		if(webrootYn.equals("Y")){
			fvo.setFileRltvPath(EgovPropertiesUtil.getProperty("Globals.fileWebrootURL") + fileRPathString);			// 파일 저장 경로(상대)
		}
		fvo.setFileAsltPath(filePathString);				// 파일 저장 경로(절대)
		fvo.setFileSize(_size);								// 파일 크기
		fvo.setSsUserId(ss_user_id);						// 사용자 id
		fvo.setContentType(content_type);					// 파입 타입
		fvo.setFileExtNm(fileExt);							// 파일 확장자

		// type : O => 원래의 이미지명.이미지확장자
		// type : C => file_id.FILE_EXT_C(선언된 확장자)
		// type : I => fild_id.이미지확장자
		if(type.equals("O")){ writeFile(fvo, file, fileName, EgovWebUtil.filePathBlackList(filePathString)); }
		if(type.equals("C")){ writeFile2(fvo, file, fileName, EgovWebUtil.filePathBlackList(filePathString)); }
		if(type.equals("I")){ writeFile3(fvo, file, fileName, EgovWebUtil.filePathBlackList(filePathString)); }

		return fvo;

	}



	public NtsysFileVO chgSavePhotoFileVO(MultipartFile file, String atchDocId, String pathKey, String ss_user_id) throws Exception {

		String filePathString = "";
		String atchFileIdString = "";
		String fileRPathString = "";

		filePathString = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath");

		fileRPathString = pathKey;
		filePathString += pathKey;

		// filePathString 마지막에는 년도들 넣어서 구분해준다.
		filePathString += getTimeStamp() + "/";
		fileRPathString += getTimeStamp() + "/";

		File saveFolder = new File(EgovWebUtil.filePathBlackList(filePathString));

		// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh
		saveFolder.setExecutable(true, true);
		saveFolder.setReadable(true);
		saveFolder.setWritable(true, true);
		// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh

		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}

		NtsysFileVO fvo;
		String filePath = "";

		String orginFileName 				= file.getOriginalFilename();
		String content_type					= file.getContentType();			// contentType
		atchFileIdString 					= SysUtil.getFileId();				// file_id

		int index 		= orginFileName.lastIndexOf(".");
		String fileExt 	= orginFileName.substring(index + 1);
		String fileName = atchFileIdString;
		long _size 		= file.getSize();

//		if (!"".equals(orginFileName)) {
//			filePath = filePathString + File.separator + fileName;
//			file.transferTo(new File(EgovWebUtil.filePathBlackList(filePath)));
//		}

		fvo = new NtsysFileVO();
		fvo.setFileId(atchFileIdString);					// file_id
		fvo.setDocId(atchDocId);							// doc_id
		fvo.setFileRmk("");								// 파일 설명
		fvo.setFileNm(orginFileName);						// 파일 원래명
		fvo.setFileRltvPath(EgovPropertiesUtil.getProperty("Globals.fileWebrootURL") + fileRPathString);			// 파일 저장 경로(상대)
		fvo.setFileAsltPath(filePathString);				// 파일 저장 경로(절대)
		fvo.setFileSize(_size);							// 파일 크기
		fvo.setSsUserId(ss_user_id);						// 사용자 id
		fvo.setContentType(content_type);					// 파입 타입
		fvo.setFileExtNm(fileExt);							// 파일 확장자

		return fvo;

	}


	/**
	 * <PRE>
	 * 1. MethodName : writeFile
	 * 2. ClassName  : NtsysFileMngUtil
	 * 3. Comment   : 파일명 그대로 쓰기
	 * 4. 작성자	: jjh
	 * 5. 작성일	: 2013. 12. 27. 오전 10:12:19
	 * </PRE>
	 *   @return void
	 *   @param fvo
	 *   @param file
	 *   @param newName
	 *   @param stordFilePath
	 *   @throws Exception
	 */
	protected static void writeFile(NtsysFileVO fvo, MultipartFile file, String newName, String stordFilePath) throws Exception {
		InputStream stream = null;
		OutputStream bos = null;

		try {
			stream = file.getInputStream();
			File cFile = new File(EgovWebUtil.filePathBlackList(stordFilePath));

				// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh
				cFile.setExecutable(true, true);
				cFile.setReadable(true);
				cFile.setWritable(true, true);
				// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh

			if (!cFile.isDirectory())
			cFile.mkdir();

			bos = new FileOutputStream(EgovWebUtil.filePathBlackList(stordFilePath + File.separator + fvo.getFileNm()));

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
			bos.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			//throw new RuntimeException(e);	// 보안점검 후속조치
			//Logger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + e.getMessage());
			System.out.println("######### 예외 발생48 ##########");
		} finally {
			if (bos != null) {
			try {
				bos.close();
			} catch (Exception ignore) {
				//Logger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
				System.out.println("######### 예외 발생49 ##########");
			}
			}
			if (stream != null) {
			try {
				stream.close();
			} catch (Exception ignore) {
				//Logger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
				System.out.println("######### 예외 발생50 ##########");
			}
			}
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : writeFile2
	 * 2. ClassName  : NtsysFileMngUtil
	 * 3. Comment   : file_id로 쓰기
	 * 4. 작성자	: jjh
	 * 5. 작성일	: 2013. 12. 27. 오전 10:12:29
	 * </PRE>
	 *   @return void
	 *   @param fvo
	 *   @param file
	 *   @param newName
	 *   @param stordFilePath
	 *   @throws Exception
	 */
	protected static void writeFile2(NtsysFileVO fvo, MultipartFile file, String newName, String stordFilePath) throws Exception {
		InputStream stream = null;
		OutputStream bos = null;

		try {
			stream = file.getInputStream();
			File cFile = new File(EgovWebUtil.filePathBlackList(stordFilePath));

			// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh
			cFile.setExecutable(true, true);
			cFile.setReadable(true);
			cFile.setWritable(true, true);
			// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh

			if (!cFile.isDirectory())
				cFile.mkdir();

			// 파일 확장자는 고정으로 설정(2013.09.24 조정현 : 파일 확장자는 .file로 고정)
			bos = new FileOutputStream(EgovWebUtil.filePathBlackList(stordFilePath + File.separator + newName + Globals.FILE_EXT_C));

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}
		} catch (Exception e) {
			//e.printStackTrace();
			//throw new RuntimeException(e);	// 보안점검 후속조치
			//Logger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + e.getMessage());
			System.out.println("######### 예외 발생51 ##########");
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception ignore) {
					//Logger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
					System.out.println("######### 예외 발생52 ##########");
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception ignore) {
					//Logger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
					System.out.println("######### 예외 발생53 ##########");
				}
			}
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : writeFile3
	 * 2. ClassName  : NtsysFileMngUtil
	 * 3. Comment   : 파일명 변경 하고 확장자는 그대로 사용
	 * 4. 작성자	: devmin
	 * 5. 작성일	: 2013. 12. 27. 오전 10:12:19
	 * </PRE>
	 *   @return void
	 *   @param fvo
	 *   @param file
	 *   @param newName
	 *   @param stordFilePath
	 *   @throws Exception
	 */
	protected static void writeFile3(NtsysFileVO fvo, MultipartFile file, String newName, String stordFilePath) throws Exception {
		InputStream stream = null;
		OutputStream bos = null;

		try {
			stream = file.getInputStream();
			File cFile = new File(EgovWebUtil.filePathBlackList(stordFilePath));

				// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh
				cFile.setExecutable(true, true);
				cFile.setReadable(true);
				cFile.setWritable(true, true);
				// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh

			if (!cFile.isDirectory())
			cFile.mkdir();

			/**
			 * 파일이름은 변경하고 확장자는 그대로 파일 저장
			 * 기존 파일이름 그대로 쓰기  ==>  bos = new FileOutputStream(EgovWebUtil.filePathBlackList(stordFilePath + File.separator + fvo.getFile_nm()));
			 */
			String ext = SysUtil.getFileExtName(fvo.getFileNm());
			bos = new FileOutputStream(EgovWebUtil.filePathBlackList(stordFilePath + File.separator + newName +"."+ ext));

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}

		} catch (Exception e) {
			//e.printStackTrace();
			//throw new RuntimeException(e);	// 보안점검 후속조치
			//Logger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + e.getMessage());
			System.out.println("######### 예외 발생54 ##########");
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception ignore) {
					//Logger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
					System.out.println("######### 예외 발생55 ##########");
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception ignore) {
					//ogger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
					System.out.println("######### 예외 발생56 ##########");
				}
			}

		}
	}



	public PhotoMetaVo writePhotoFile(NtsysFileVO fvo, MultipartFile file, String newName, String stordFilePath) throws Exception {

		InputStream stream = null;
		OutputStream bos = null;
		StringBuffer metaBuffer = new StringBuffer();

		PhotoMetaVo vo = new PhotoMetaVo();

		try {
			stream = file.getInputStream();
			File cFile = new File(EgovWebUtil.filePathBlackList(stordFilePath));

				// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh
				cFile.setExecutable(true, true);
				cFile.setReadable(true);
				cFile.setWritable(true, true);
				// 수정 : 권한 설정 2017.12.14 보안취약점 관련 수정 by pjh

			if (!cFile.isDirectory())
			cFile.mkdir();

			/**
			 * 파일이름은 변경하고 확장자는 그대로 파일 저장
			 * 기존 파일이름 그대로 쓰기  ==>  bos = new FileOutputStream(EgovWebUtil.filePathBlackList(stordFilePath + File.separator + fvo.getFile_nm()));
			 */
			String ext = SysUtil.getFileExtName(fvo.getFileNm());
			bos = new FileOutputStream(EgovWebUtil.filePathBlackList(stordFilePath + File.separator + newName +"."+ ext));

			int bytesRead = 0;
			byte[] buffer = new byte[BUFF_SIZE];

			while ((bytesRead = stream.read(buffer, 0, BUFF_SIZE)) != -1) {
				bos.write(buffer, 0, bytesRead);
			}

			/*
			 * #####################################################
			 */

			String imgLocation= stordFilePath + File.separator + newName +"."+ ext;		   // 원본 이미지 파일명


			//String imgLocation = "C:/Users/Venus/Pictures/20170208_171941.png";

			File imgFile = new File(imgLocation);


			Image img = null;
			img = ImageLoader.fromFile(imgFile);

			vo.setWidth(img.getWidth());
			vo.setHeight(img.getHeight());


			log.debug("######### getWidth ##########" + img.getWidth());
			log.debug("######### getHeight ##########" + img.getHeight());

			try {

				//Metadata metadata = JpegMetadataReader.readMetadata(imgFile);ImageMetadataReader
				Metadata metadata = ImageMetadataReader.readMetadata(imgFile);

				for (Directory directory : metadata.getDirectories()) {

					//
					// Each Directory stores values in Tag objects
					//
					for (Tag tag : directory.getTags()) {
						//System.out.println(tag);
						metaBuffer.append(tag + "\n");
					}

				}

				log.debug("######### metaBuffer ##########" + metaBuffer.toString());

				vo.setMetaInfo(metaBuffer.toString());

				//print(metadata, "Using JpegMetadataReader");
			} catch (JpegProcessingException e) {
				System.out.println("######### 예외 발생57 ##########");
			} catch (IOException e) {
				System.out.println("######### 예외 발생58 ##########");
			}

			/*
			 * #####################################################
			 */

		} catch (Exception e) {
			//e.printStackTrace();
			//throw new RuntimeException(e);	// 보안점검 후속조치
			//Logger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + e.getMessage());
			System.out.println("######### 예외 발생59 ##########");
		} finally {
			if (bos != null) {
				try {
					bos.close();
				} catch (Exception ignore) {
					//Logger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
					System.out.println("######### 예외 발생60 ##########");
				}
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception ignore) {
					//ogger.getLogger(NtsysFileMngUtil.class).debug("IGNORED: " + ignore.getMessage());
					System.out.println("######### 예외 발생61 ##########");
				}
			}
		}

		return vo;
	}


	/**
	 * <PRE>
	 * 1. MethodName : deleteFile
	 * 2. ClassName  : NtsysFileMngUtil
	 * 3. Comment   : 파일삭제
	 * 4. 작성자	: 조정현
	 * 5. 작성일	: 2014. 8. 11. 오후 3:33:28
	 * </PRE>
	 *   @return boolean
	 *   @param fvoList
	 *   @param type(O : 원래 파일명, C : file_id(확장자변경), I : file_id(확장자그대로))
	 *   @return
	 *   @throws Exception
	 */
	public boolean deleteFile(List <NtsysFileVO> fvoList) throws Exception {

		return deleteFile(fvoList, EgovPropertiesUtil.getProperty("Globals.fileSaveType"));
	}

	public boolean deleteFile(List <NtsysFileVO> fvoList, String type) throws Exception {

		for(int i = 0; i < fvoList.size(); i++){
			NtsysFileVO fvo = fvoList.get(i);

			if(!deleteFile(fvo, EgovPropertiesUtil.getProperty("Globals.fileSaveType"))){
				return false;
			}
		}

		return true;
	}
	/**
	 * <PRE>
	 * 1. MethodName : deleteFile
	 * 2. ClassName  : NtsysFileMngUtil
	 * 3. Comment   : 파일삭제
	 * 4. 작성자	: jjh
	 * 5. 작성일	: 2013. 12. 27. 오전 10:29:09
	 * </PRE>
	 *   @return void
	 *   @param fvo
	 *   @param type(O : 원래 파일명, C : file_id(확장자변경), I : file_id(확장자그대로))
	 *   @throws Exception
	 */
	public boolean deleteFile(NtsysFileVO fvo) throws Exception {
		// 기본은 변환으로 파일 저장
		return deleteFile(fvo, EgovPropertiesUtil.getProperty("Globals.fileSaveType"));
	}

	public boolean deleteFile(NtsysFileVO fvo, String type) throws Exception {
		// 값이 없을경우 기본값 설정
		if(StringUtil.nvl(type).equals("")){
			type = EgovPropertiesUtil.getProperty("Globals.fileSaveType");
		}

		EgovFileTool filetool = new EgovFileTool();

		// 파일명으로 삭제
		if(type.equals("O")){
			// 파일 존재하는지 확인
			if(EgovFileTool.checkFileExstByName(fvo.getFileAsltPath(), fvo.getFileNm())){
				if(!filetool.deletePath(fvo.getFileAsltPath() + fvo.getFileNm()).equals("")){
					return true;
				} else {
					return false;
				}
			}
		}
		// file_id로 삭제(확장자 변경)
		if(type.equals("C")){
			// 파일 존재하는지 확인
			if(EgovFileTool.checkFileExstByName(fvo.getFileAsltPath(), fvo.getFileId() + Globals.FILE_EXT_C)){
				if(!filetool.deletePath(fvo.getFileAsltPath() + fvo.getFileId() + Globals.FILE_EXT_C).equals("")){
					return true;
				} else {
					return false;
				}
			}
		}

		// file_id로 삭제(확장자는 기존)
		if(type.equals("I")){
			// 파일 존재하는지 확인
			if(EgovFileTool.checkFileExstByName(fvo.getFileAsltPath(), fvo.getFileId() + "." + fvo.getFileExtNm())){
				if(!filetool.deletePath(fvo.getFileAsltPath() + fvo.getFileId() + "." + fvo.getFileExtNm()).equals("")){
					return true;
				} else {
					return false;
				}
			}
		}

		return false;
	}

	// 파일 크기 비교(총 크기)
	public boolean checkFileSize(List fileList) throws Exception {
		return checkFileSize(fileList, "Globals.fileMaxSize");
	}

	// 파일 크기 비교(총 크기)
	public boolean checkFileSize(List fileList, String propertyName) throws Exception {
		boolean result = true;
		long size = 0;

		if(!fileList.isEmpty()){
			for(int i=0; i < fileList.size(); i++){
				MultipartFile mfile = (MultipartFile)fileList.get(i);

				if(!mfile.isEmpty()){
					// 파일 크기 합산
					size += mfile.getSize();
				}
			}
		}

		// 사이즈가 클경우
		if(size > Long.parseLong(EgovPropertiesUtil.getProperty(propertyName))){
			result = false;
		}
		return result;
	}

	// 파일 크기 비교(각각 크기)
	public boolean checkEachFileSize(List fileList) throws Exception {
		return checkEachFileSize(fileList, "Globals.fileMaxSize");
	}

	// 파일 크기 비교(각각 크기)
	public boolean checkEachFileSize(List fileList, String propertyName) throws Exception {
		boolean result = true;

		if(!fileList.isEmpty()){
			for(int i=0; i < fileList.size(); i++){
				MultipartFile mfile = (MultipartFile)fileList.get(i);

				if(!mfile.isEmpty()){
					// 파일 크기 비교
					long size 		= mfile.getSize();
					// 사이즈가 클경우
					if(size > Long.parseLong(EgovPropertiesUtil.getProperty(propertyName))){
						result = false;
						break;
					}
				}
			}
		}


		return result;
	}

	// byte 단위 크기를 보기쉽게 표현
	public static String getFileSize(String size){
		String gubn[] = {"Byte", "KB", "MB" } ;
		String returnSize = new String ();
		int gubnKey = 0;
		double changeSize = 0;
		long fileSize = 0;

		try{
			fileSize =  Long.parseLong(size);
			for( int x=0 ; (fileSize / (double)1024 ) >0 ; x++, fileSize/= (double) 1024 ){
				gubnKey = x;
				changeSize = fileSize;
			}
			returnSize = changeSize + gubn[gubnKey];
		}catch ( Exception ex){ returnSize = "0.0 Byte"; }

		return returnSize;
	}

	// MultipartHttpServletRequest 에서 file 객체 가져오기
	// upload 객체에 빈값으로 넘어와도 그냥 가져오면 size가 1이 되어서 비어도 있는것처럼 보이게 된다.
	public static List<MultipartFile> getFiles(MultipartHttpServletRequest request){

		return getFiles(request, "upload");
	}

	// MultipartHttpServletRequest 에서 file 객체 가져오기
	// upload 객체에 빈값으로 넘어와도 그냥 가져오면 size가 1이 되어서 비어도 있는것처럼 보이게 된다.
	public static List<MultipartFile> getFiles(MultipartHttpServletRequest request, String name){
		List files = new ArrayList();

		List t_files = request.getFiles(name);

		for(int i=0; i < t_files.size(); i++){
			MultipartFile mfile = (MultipartFile)t_files.get(i);
			if(!mfile.isEmpty()){
				files.add(mfile);
			}
		}

		return files;
	}

	/**
	 * <PRE>
	 * 1. MethodName : copyFiles
	 * 2. ClassName  : NtsysFileMngUtil
	 * 3. Comment   : 파일 복사
	 * 4. 작성자	: 김영배
	 * 5. 작성일	: 2015. 9. 17. 오전 10:37:39
	 * </PRE>
	 *   @return boolean
	 *   @param originalDirPath
	 *   @param fileList
	 *   @param targetDirPath
	 *   @param overwriteYn
	 *   @return
	 *   @throws Exception
	 */
	/*
	public static boolean copyFiles(NtsysFileVO _reNtsysFile) throws Exception {

		final char FILE_SEPARATOR = File.separatorChar;
//		final char FILE_SEPARATOR = '/';

		boolean result = false;
		File f = null;
		try {
			String originalDirPathAbs = EgovWebUtil.filePathBlackList(_reNtsysFile.getFile_aslt_path()); //=> D:/eGovFrameDev-3.0.0-32bit-SMP/workspace/AllNewSMP/src/main/webapp/upload//cntnts/temp/
			String targetDirPathAbs = EgovWebUtil.filePathBlackList(EgovPropertiesUtil.getProperty("Globals.fileStillcutPath")); //=> D:/eGovFrameDev-3.0.0-32bit-SMP/workspace/AllNewSMP/src/main/webapp/upload/cntnts/
			targetDirPathAbs += getTimeStamp() + "/";
			f = new File(originalDirPathAbs);


			// 원본은 유효하고 대상이 신규로 생성가능한 상태인경우만 진행한다.
			//if(f.exists() && f.isDirectory() ){ // 디렉토리만 이동할수 있도록 제한하는 경우
			if (f.exists()) {
				// 타겟으로 설정한 경로가 유효한지 확인(중간경로에 파일명 이 포함되어있으면 유효하지 못하므로 진행안함.
				File targetDir = new File(targetDirPathAbs);

				// 타겟 경로 생성
				if(!targetDir.exists()) {
					EgovFileTool.createDirectory(targetDirPathAbs);
//					targetDir.mkdirs();
				}

				String fileNm = _reNtsysFile.getFile_id(); //=> 1442463082951RXWME0LMKOE0MGZEAWKHP2UJS
				fileNm += "." + _reNtsysFile.getFile_ext_nm(); //=> 1442463082951RXWME0LMKOE0MGZEAWKHP2UJS.jpg

				result = execCommand("copy", _reNtsysFile.getFile_aslt_path()+"/"+fileNm, targetDirPathAbs);

			} else {
				// 원본자체가 유효하지 않은 경우는 false 리턴하고 종료
				result = false;
			}
		} catch (Exception e) {
			result = false;
			System.out.println("######### 예외 발생62 ##########");
		} finally {

		}
		return result;
	}

	public static boolean execCommand(String cmd, String originalDirPath, String targetDirPath) throws Exception {

		final char FILE_SEPARATOR = File.separatorChar;
		// 최대 문자길이
		final int MAX_STR_LEN = 1024;

		boolean result = false;
		BufferedReader b_err = null;
		BufferedReader b_out = null;
		try {
//			String originalDirPathAbs = Globals.FILE_PATH + FILE_SEPARATOR + originalDirPath;
//			String targetDirPathAbs = Globals.FILE_PATH + FILE_SEPARATOR + targetDirPath;

			//SHELL.win.copy
			String cmdStr = EgovPropertiesUtil.getProperty("SHELL." + EgovPropertiesUtil.getProperty("Globals.osType") + "." + cmd);

			String commandStr = cmdStr + " "+ EgovWebUtil.filePathBlackList(originalDirPath).replace('\\', '/').replace("//", "/").replace('/', FILE_SEPARATOR)
					+" "+ EgovWebUtil.filePathBlackList(targetDirPath).replace('\\', '/').replace("//", "/").replace('/', FILE_SEPARATOR);
			Process p = Runtime.getRuntime().exec(commandStr);
			//Process p = Runtime.getRuntime().exec("cmd /c copy /Y D:/eGovFrameDev-3.0.0-32bit/test/doc/temp_doc/test*, D:/eGovFrameDev-3.0.0-32bit/test/doc");

			//String access = "";
			p.waitFor();
			//프로세스 에러시 종료
			if (p != null && p.exitValue() != 0) {
				b_err = new BufferedReader(new InputStreamReader(p.getErrorStream()));
				while (b_err.ready()) {
					String line = b_err.readLine();
//					if (line.length() <= MAX_STR_LEN) log("ERR\n" + line);
				}
				//b_err.close();
			}
			//프로세스 실행 성공시 결과 확인
			else {
				result = true;
			}
		} catch (Exception e) {
//			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			b_err.close();
			b_out.close();
		}
		return result;
	}

	protected static void close(Closeable closable) {
		if (closable != null) {
			try {
				closable.close();
			} catch (IOException ignore) {
//				 log("IGNORE: " + ignore);
				System.out.println("######### 예외 발생63 ##########");
			}
		}
	}
	*/

	private static String getTimeStamp() {

		String rtnStr = null;

		// 문자열로 변환하기 위한 패턴 설정(년도-월-일 시:분:초:초(자정이후 초))
		String pattern = "yyyyMMdd";

		SimpleDateFormat sdfCurrent = new SimpleDateFormat(pattern, Locale.KOREA);
		Timestamp ts = new Timestamp(System.currentTimeMillis());

		rtnStr = sdfCurrent.format(ts.getTime());

		return rtnStr;
	}

	// 파일 확장자 비교
	public String checkFileExt(List fileList) throws Exception {
		/*String extNames = "EXE,BAT,COM,JSP,ASP,HTML,PHP,SH,JS"; ,acceptYn = "N" 기본 셋팅*/
		String extNames = "SDF";
		return checkFileExt(fileList, extNames, "Y");
	}

	// 파일 확장자 비교
	public String checkFileExt(List fileList, String extNames, String acceptYn) throws Exception {
		String result = "";
		long size = 0;

		if(acceptYn.equals("Y")){
			//해당 확장자를 허용 할 시
			if(!fileList.isEmpty()){
				for(int i=0; i < fileList.size(); i++){
					MultipartFile mfile = (MultipartFile)fileList.get(i);
					String fileNm = "";
					String file_ext = "";
					if(!mfile.isEmpty()){
						fileNm =  mfile.getOriginalFilename();
						file_ext = fileNm.substring(fileNm.lastIndexOf(".")+1, fileNm.length()).toUpperCase();
					}

					if(extNames.toUpperCase().indexOf(file_ext) == -1){
						result = extNames;
					}

				}
			}
		}else{
			//해당 확장자를 허용 하지 않을 시
			if(!fileList.isEmpty()){
				for(int i=0; i < fileList.size(); i++){
					MultipartFile mfile = (MultipartFile)fileList.get(i);
					String fileNm = "";
					String file_ext = "";
					if(!mfile.isEmpty()){
						fileNm =  mfile.getOriginalFilename();
						file_ext = fileNm.substring(fileNm.lastIndexOf(".")+1, fileNm.length()).toUpperCase();
					}
					if(extNames.toUpperCase().indexOf(file_ext) > 0){
						result = file_ext;
					}

				}
			}
		}

		return result;
	}
}
