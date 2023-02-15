package egovframework.common.web;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.admin.user.web.UserMgtController;
import egovframework.common.vo.PhotoVo;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;
import egovframework.framework.common.util.file.vo.NtsysFileVO;

@Controller
public class PhotoUploadController {

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="egovMessageSource")
	private EgovMessageSource egovMessageSource;

	@Resource(name="NtsysFileMngService")
	private NtsysFileMngService ntsysFileMngService;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	private static Log log = LogFactory.getLog(UserMgtController.class);

	//단일파일업로드
	@RequestMapping(value = "/photoUpload.do")
	public String photoUpload(HttpServletRequest request, PhotoVo vo){
		String callback = vo.getCallback();
		String callbackFunc = vo.getCallbackFunc();
		String fileResult = "";

		try {
			if(vo.getFiledata() != null && vo.getFiledata().getOriginalFilename() != null && !vo.getFiledata().getOriginalFilename().equals("")){
				//파일이 존재하면
				String originalName = vo.getFiledata().getOriginalFilename();
				String ext = originalName.substring(originalName.lastIndexOf(".")+1);
				//파일 기본경로
				String defaultPath = request.getSession().getServletContext().getRealPath("/");
				//파일 기본경로 _ 상세경로
				String path = defaultPath + "resource" + File.separator + "photo_upload" + File.separator;
				/*String path = EgovPropertiesUtil.getProperty("img.upload.path");*/
				File file = new File(path);
				//디렉토리 존재하지 않을경우 디렉토리 생성
				if(!file.exists()) {
					file.mkdirs();
				}
				//서버에 업로드 할 파일명(한글문제로 인해 원본파일은 올리지 않는것이 좋음)
				String realname = UUID.randomUUID().toString() + "." + ext;
			///////////////// 서버에 파일쓰기 /////////////////
				vo.getFiledata().transferTo(new File(path+realname));
				fileResult += "&bNewLine=true&sFileName="+originalName+"&sFileURL=/resource/photo_upload/"+realname;
			} else {
				fileResult += "&errstr=error";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:" + callback + "?callback_func=" + callbackFunc + fileResult;
	}


	//다중파일업로드
	/**
	* <PRE>
	* 1. MethodName : multiplePhotoUpload
	* 2. ClassName  : PhotoUploadController
	* 3. Comment   :
	* 4. 작성자	: 박재현
	* 5. 작성일	: 2018. 7. 4. 오후 2:04:59
	* </PRE>
	*   @return void
	*   @param request
	*   @param response
	*/
	@RequestMapping(value = "/multiplePhotoUpload.do")
	public void multiplePhotoUpload(HttpServletRequest request, MultipartHttpServletRequest mtfRequest, HttpServletResponse response){
		try {
			DataMap param = RequestUtil.getDataMap(request);

			List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
			NtsysFileVO reNtsysFile = null;

			String sFileInfo = ""; //리턴해 줄 파일 정보
			response.setContentType("text/html; charset=utf-8");
			PrintWriter print = response.getWriter();

			// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
			if (!ntsysFileMngUtil.checkFileSize(fileList, "Globals.ImgfileMaxSize")) {
				MessageUtil.setMessage(request, egovMessageSource.getMessage(
						"error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize")) }));
				print.print("ErrorSizeFile");
				print.flush();
				print.close();

			}else if(ntsysFileMngUtil.checkFileExt(fileList) != ""){
				// 파일 확장자 체크
					MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.not.access",
							new String[] { "" }));
					print.print("ErrorExtFile");
					print.flush();
					print.close();
			}else{
				if(!fileList.isEmpty()){
					for(int i=0; i < fileList.size(); i++){
						MultipartFile mfile = (MultipartFile)fileList.get(i);
						if(!mfile.isEmpty()){
							/**
							* parseFileInf
							* 1:파일객체
							* 2:문서아이디
							* 3:서브폴더명
							* 4:사용자번호
							* 5:Web Root Yn
							*/
							// 파일을 서버에 물리적으로 저장
							reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, "", "temp/", param.getString("ss_user_no"), "Y");

							//파일정보
							// img 태그의 title 속성을 원본파일명으로 적용시켜주기 위함
							sFileInfo += "&sFileName="+ reNtsysFile.getFileNm();
							sFileInfo += "&sFileURL="+ reNtsysFile.getFileRltvPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm();
						}
					}
				}

				print.print(sFileInfo);
				print.flush();
				print.close();
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/**
	 *
	 * <PRE>
	 * 1. MethodName : multipartHttpPhotoUpload
	 * 2. ClassName  : PhotoUploadController
	 * 3. Comment   : ckeditor 이미지 업로드 httpServlertRequest를 MultipartHttpServletRequest로 받음
	 * 4. 작성자    : :LWJ
	 * 5. 작성일    : 2020. 6. 8. 오후 1:34:40
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param mtfRequest
	 *   @param response
	 */
	@RequestMapping(value = "/multipartHttpPhotoUpload.do")
	public void multipartHttpPhotoUpload(MultipartHttpServletRequest request, MultipartHttpServletRequest mtfRequest, HttpServletResponse response){
		try {
			DataMap param = RequestUtil.getDataMap(request);

			//List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
			List fileList = ntsysFileMngUtil.getFiles(request);
			NtsysFileVO reNtsysFile = null;

			String sFileInfo = ""; //리턴해 줄 파일 정보
			response.setContentType("text/html; charset=utf-8");
			PrintWriter print = response.getWriter();

			// 파일 크기 계산(최대크기 넘었을경우 다시 쓰기 페이지로 리턴)
			if (!ntsysFileMngUtil.checkFileSize(fileList, "Globals.ImgfileMaxSize")) {
				MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.size.over", new String[] { ntsysFileMngUtil.getFileSize(EgovPropertiesUtil.getProperty("Globals.ImgfileMaxSize")) }));
				print.print("ErrorSizeFile");
				print.flush();
				print.close();
			} else if (ntsysFileMngUtil.checkFileExt(fileList) != "") {
				// 파일 확장자 체크
				MessageUtil.setMessage(request, egovMessageSource.getMessage("error.file.not.access",new String[] { "" }));
				print.print("ErrorExtFile");
				print.flush();
				print.close();
			} else {
				if (!fileList.isEmpty()) {
					for (int i = 0; i < fileList.size(); i++) {
						MultipartFile mfile = (MultipartFile) fileList.get(i);
						if (!mfile.isEmpty()) {
							/**
							* parseFileInf
							* 1:파일객체
							* 2:문서아이디
							* 3:서브폴더명
							* 4:사용자번호
							* 5:Web Root Yn
							*/
							// 파일을 서버에 물리적으로 저장
							reNtsysFile = ntsysFileMngUtil.parseFileInf(mfile, "", "temp/", param.getString("ss_user_no"), "Y");

							// 파일정보
							// img 태그의 title 속성을 원본파일명으로 적용시켜주기 위함
							sFileInfo += "&sFileName=" + reNtsysFile.getFileNm();
							sFileInfo += "&sFileURL=" + reNtsysFile.getFileRltvPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm();
						}
					}
				}

				print.print(sFileInfo);
				print.flush();
				print.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

/*
	@RequestMapping(value = "/multiplePhotoUpload.do")
	public void multiplePhotoUpload(HttpServletRequest request, MultipartHttpServletRequest mtfRequest, HttpServletResponse response){
		try {
			DataMap param = RequestUtil.getDataMap(request);

			List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
			NtsysFileVO _reNtsysFile = null;

			String sFileInfo = ""; //리턴해 줄 파일 정보

			if(!fileList.isEmpty()){
				for(int i=0; i < fileList.size(); i++){
					MultipartFile mfile = (MultipartFile)fileList.get(i);
					if(!mfile.isEmpty()){
						*//**
						* parseFileInf
						* 1:파일객체
						* 2:문서아이디
						* 3:서브폴더명
						* 4:사용자번호
						* 5:Web Root Yn
						*//*
						// 파일을 서버에 물리적으로 저장
						_reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, "", "temp/", param.getString("ss_user_no"), "Y");

						//파일정보
						// img 태그의 title 속성을 원본파일명으로 적용시켜주기 위함
						sFileInfo += "&sFileName="+ _reNtsysFile.getFile_nm();
						sFileInfo += "&sFileURL="+ _reNtsysFile.getFile_rltv_path() + _reNtsysFile.getFile_id() + "." + _reNtsysFile.getFile_ext_nm();
					}
				}
			}



			//파일명을 받는다 - 일반 원본파일명
			String filename = param.getString("file-name");
			String filesize = param.getString("file-size");
			//파일 확장자
			String filename_ext = filename.substring(filename.lastIndexOf(".")+1);
			//확장자를소문자로 변경
			filename_ext = filename_ext.toLowerCase();

			//이미지 검증 배열변수
			String[] allow_file = {"jpg","png","bmp","gif","jpeg"};

			//돌리면서 확장자가 이미지인지
			int cnt = 0;
			for(int i=0; i<allow_file.length; i++) {
				if(filename_ext.equals(allow_file[i])){
					cnt++;
				}
			}

			//이미지가 아님
			if(cnt == 0) {
				PrintWriter print = response.getWriter();
				print.print("NOTALLOW_"+filename);
				print.flush();
				print.close();
			} else {
				List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
				NtsysFileVO _reNtsysFile = null;

				if(!fileList.isEmpty()){
					for(int i=0; i < fileList.size(); i++){
						MultipartFile mfile = (MultipartFile)fileList.get(i);
						if(!mfile.isEmpty()){

							* parseFileInf
							* 1:파일객체
							* 2:문서아이디
							* 3:서브폴더명
							* 4:사용자번호
							* 5:Web Root Yn

							// 파일을 서버에 물리적으로 저장하고
							_reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, "", "temp/", "user_id", "Y");


							// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
							commonMybatisDao.insert("common.file.insertAttchFile", _reNtsysFile);

						}
					}
				}

				///////////////// 서버에 파일쓰기 /////////////////
			}
				// 정보 출력

				PrintWriter print = response.getWriter();
				print.print(sFileInfo);
				print.flush();
				print.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/

	/**
	 *
	 * <PRE>
	 * 1. MethodName : fileUpload
	 * 2. ClassName  : PhotoUploadController
	 * 3. Comment   : ckeditor 이미지 업로드
	 * 4. 작성자    : :LWJ
	 * 5. 작성일    : 2020. 6. 4. 오후 3:13:50
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param multiFile
	 *   @param upload
	 *   @throws Exception
	 */
	@RequestMapping(value = "/photoServerUpload.do",method=RequestMethod.POST)
	public void fileUpload(MultipartHttpServletRequest request, HttpServletResponse response, MultipartHttpServletRequest multiFile, @RequestParam MultipartFile upload) throws Exception{
		  // 랜덤 문자 생성
        UUID uid = UUID.randomUUID();

        OutputStream out = null;
        PrintWriter printWriter = null;

        //인코딩
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

		try {
			// 파일 이름 가져오기
			String fileName = upload.getOriginalFilename();
			byte[] bytes = upload.getBytes();

			// 이미지 경로 생성
			String path = "/ckImage/";// fileDir는 전역 변수라 그냥 이미지 경로 설정해주면 된다.
			String ckUploadPath = path + uid + "_" + fileName;
			File folder = new File(path);

			// 해당 디렉토리 확인
			if (!folder.exists()) {
				try {
					folder.mkdirs(); // 폴더 생성
				} catch (Exception e) {
					e.getStackTrace();
				}
			}

			out = new FileOutputStream(new File(ckUploadPath));
            out.write(bytes);
            out.flush(); // outputStram에 저장된 데이터를 전송하고 초기화

            String callback = request.getParameter("CKEditorFuncNum");
            printWriter = response.getWriter();
            String fileUrl = "/ckImgSubmit.do?uid=" + uid + "&fileName=" + fileName;  // 작성화면

			// 업로드시 메시지 출력
			printWriter.println("{\"filename\" : \"" + fileName + "\", \"uploaded\" : 1, \"url\":\"" + fileUrl + "\"}");
			printWriter.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (printWriter != null) {
					printWriter.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return;
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : ckSubmit
	 * 2. ClassName  : PhotoUploadController
	 * 3. Comment   : ckeditor image서브밋
	 * 4. 작성자    : :LWJ
	 * 5. 작성일    : 2020. 6. 4. 오후 3:13:34
	 * </PRE>
	 *   @return void
	 *   @param uid
	 *   @param fileName
	 *   @param request
	 *   @param response
	 *   @throws ServletException
	 *   @throws IOException
	 */
	@RequestMapping(value="/ckImgSubmit.do")
    public void ckSubmit(@RequestParam(value="uid") String uid, @RequestParam(value="fileName") String fileName, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{

        //서버에 저장된 이미지 경로
        String path = "/ckImage/";

        String sDirPath = path + uid + "_" + fileName;

        File imgFile = new File(sDirPath);

        //사진 이미지 찾지 못하는 경우 예외처리로 빈 이미지 파일을 설정한다.
        if(imgFile.isFile()){
            byte[] buf = new byte[1024];
            int readByte = 0;
            int length = 0;
            byte[] imgBuf = null;

            FileInputStream fileInputStream = null;
            ByteArrayOutputStream outputStream = null;
            ServletOutputStream out = null;

            try{
                fileInputStream = new FileInputStream(imgFile);
                outputStream = new ByteArrayOutputStream();
                out = response.getOutputStream();

                while((readByte = fileInputStream.read(buf)) != -1){
                    outputStream.write(buf, 0, readByte);
                }

                imgBuf = outputStream.toByteArray();
                length = imgBuf.length;
                out.write(imgBuf, 0, length);
                out.flush();

            }catch(IOException e){

            }finally {
                outputStream.close();
                fileInputStream.close();
                out.close();
            }
        }
    }

}
