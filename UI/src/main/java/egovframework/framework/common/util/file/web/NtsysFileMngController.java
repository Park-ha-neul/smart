package egovframework.framework.common.util.file.web;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.admin.bbs.web.BbsMgtController;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.DateUtil;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;
import egovframework.framework.common.util.file.vo.NtsysFileVO;

/**
 * 파일 조회, 삭제, 다운로드 처리를 위한 컨트롤러 클래스
 * @author 공통서비스개발팀 이삼섭
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일	  수정자		   수정내용
 *  -------	--------	---------------------------
 *   2009.3.25  이삼섭		  최초 생성
 *
 * </pre>
 */
@Controller
public class NtsysFileMngController{

	@Resource(name = "NtsysFileMngService")
	private NtsysFileMngService fileService;

	/** ntsysFileMngService */
	@Resource(name = "NtsysFileMngService")
	private NtsysFileMngService ntsysFileMngService;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	private static Log log = LogFactory.getLog(NtsysFileMngController.class);

	/**
	 * 첨부파일에 대한 목록을 조회한다.
	 *
	 * @param fileVO
	 * @param atchFileId
	 * @param sessionVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping("/cmm/fms/selectFileInfs.do")
//	public String selectFileInfs(@ModelAttribute("searchVO") FileVO fileVO, Map<String, Object> commandMap, ModelMap model) throws Exception {
//	String atchFileId = (String)commandMap.get("param_atchFileId");
//
//	fileVO.setAtchFileId(atchFileId);
//	List<FileVO> result = fileService.selectFileInfs(fileVO);
//
//	model.addAttribute("fileList", result);
//	model.addAttribute("updateFlag", "N");
//	model.addAttribute("fileListCnt", result.size());
//	model.addAttribute("atchFileId", atchFileId);
//
//	return "egovframework/com/cmm/fms/EgovFileList";
//	}

	/**
	 * 첨부파일 변경을 위한 수정페이지로 이동한다.
	 *
	 * @param fileVO
	 * @param atchFileId
	 * @param sessionVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping("/cmm/fms/selectFileInfsForUpdate.do")
//	public String selectFileInfsForUpdate(@ModelAttribute("searchVO") FileVO fileVO, Map<String, Object> commandMap,
//		//SessionVO sessionVO,
//		ModelMap model) throws Exception {
//
//	String atchFileId = (String)commandMap.get("param_atchFileId");
//
//	fileVO.setAtchFileId(atchFileId);
//
//	List<FileVO> result = fileService.selectFileInfs(fileVO);
//
//	model.addAttribute("fileList", result);
//	model.addAttribute("updateFlag", "Y");
//	model.addAttribute("fileListCnt", result.size());
//	model.addAttribute("atchFileId", atchFileId);
//
//	return "egovframework/com/cmm/fms/EgovFileList";
//	}

	/**
	 * 첨부파일에 대한 삭제를 처리한다.(file_id)
	 *
	 * @param fileVO
	 * @param returnUrl
	 * @param sessionVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/common/file/deleteFileInfAjax.do")
	public @ResponseBody DataMap deleteFileInfAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setFileId(param.getString("file_id"));

		fvo = fileService.selectFileInf(fvo);

		//return 상태
		DataMap resultStats = new DataMap();
		DataMap resultJSON = new DataMap();

		boolean delYn = false;

		// d_type 설정이 안되어있으면 기본값
		if(StringUtil.nvl(param.getString("d_type")).equals("")){
			// 무조건 설정된 값으로
			param.put("d_type", EgovPropertiesUtil.getProperty("Globals.fileSaveType"));
		}

		// 원래 이미지, 확장자
		if(param.getString("d_type").equals("O")){
			delYn = ntsysFileMngUtil.deleteFile(fvo, "O");
		}
		// file_id, 원래 확장자
		else if(param.getString("d_type").equals("I")){
			delYn = ntsysFileMngUtil.deleteFile(fvo, "I");
		}
		// file_id, 변환 확장자
		else {
			delYn = ntsysFileMngUtil.deleteFile(fvo, "C");
		}

		// 정상 삭제시
		// 파일이 존재하지 않아 파일이 삭제가 안되더라도 삭제 성공을 리턴하여 첨부파일을 새로 넣을수 있게 만들어준다.
//		if(del_yn){
			fileService.deleteFileInf(param);
			resultStats.put("resultCode", "success");
			resultStats.put("resultMsg", "삭제에 성공하였습니다.");
			resultJSON.put("resultStats", resultStats);
//		} else {
//			resultStats.put("resultCode", "error");
//			resultStats.put("resultMsg", "삭제에 실패하였습니다.");
//			resultJSON.put("resultStats", resultStats);
//		}

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteOrignalFileInfAjax
	 * 2. ClassName  : NtsysFileMngController
	 * 3. Comment   : 첨부파일에 대한 삭제를 처리한다.(file_nm)
	 * 4. 작성자	: jjh
	 * 5. 작성일	: 2013. 12. 27. 오전 11:12:41
	 * </PRE>
	 *   @return void
	 *   @param request
	 *   @param response
	 *   @param model
	 *   @throws Exception
	 */
	@RequestMapping("/common/file/deleteOrignalFileInfAjax.do")
	public @ResponseBody DataMap deleteOrignalFileInfAjax(HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setFileId(param.getString("file_id"));

		fvo = fileService.selectFileInf(fvo);

		//return 상태
		DataMap resultStats = new DataMap();
		DataMap resultJSON = new DataMap();

		ntsysFileMngUtil.deleteFile(fvo, "O");

		// 정상 삭제시
//		if(){
			fileService.deleteFileInf(param);
			resultStats.put("resultCode", "success");
			resultStats.put("resultMsg", "삭제에 성공하였습니다.");
			resultJSON.put("resultStats", resultStats);
//		} else {
//			resultStats.put("resultCode", "error");
//			resultStats.put("resultMsg", "삭제에 실패하였습니다.");
//			resultJSON.put("resultStats", resultStats);
//		}

		return resultJSON;
	}

	/**
	 * <PRE>
	 * 1. MethodName : saveFileAjax
	 * 2. ClassName  : NtsysFileMngController
	 * 3. Comment   : 첨부파일을 tb_file에 저장 정상저장시 doc_id 리턴(첨부파일 미리 업로드시 사용할수 있다)
	 * 4. 작성자	: jjh
	 * 5. 작성일	: 2014. 1. 2. 오후 3:59:05
	 * </PRE>
	 *   @return void
	 *   @param commandMap
	 *   @param request
	 *   @param response
	 *   @throws Exception
	 */
	@RequestMapping(value = "/common/file/saveFileAjax.do")
	public @ResponseBody DataMap saveFileAjax(Map<String, Object> commandMap, HttpServletRequest request, HttpServletResponse response, String ssUserNo) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);
		String resultCode = "ok";
		String resultMsg = "";

		String docId = SysUtil.getDocId();

//		########### Upload File 처리 시작 #############

		// 파일업로드 path 설정.
		String pathKey = param.getString("file_path");

		// HttpServletRequest를 MultipartHttpServletRequest로 캐스팅(Casting)한 다음 MultipartHttpServletRequest API를 이용하여 첨부된 파일에 접근
		MultipartHttpServletRequest multiRequest 	= (MultipartHttpServletRequest)request;

		List filelist = multiRequest.getFiles("upload");
		NtsysFileVO reNtsysFile = null;
		if(!filelist.isEmpty()){

			for(int i=0; i < filelist.size(); i++){
				MultipartFile mfile = (MultipartFile)filelist.get(i);
				if(!mfile.isEmpty()){

					// 파일 사이즈 체크(체크 할경우)
					if(!param.getString("max_size").equals("")){
						// 사이즈 체크
						if(mfile.getSize() <= param.getLong("max_size")){
							// 파일을 서버에 물리적으로 저장하고
							reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, docId, pathKey, ssUserNo);
							// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
							ntsysFileMngService.insertFileInf(reNtsysFile);
							resultCode = "ok";
						} else {
							resultCode = "error";
							resultMsg = "첨부파일의 사이즈가 큽니다.(최대 : " + SysUtil.byteCalculation(param.getString("max_size")) + ")";
							docId = "";
							break;
						}
					} else {
						// 파일을 서버에 물리적으로 저장하고
						// 해당 jsp 페이지에서 d_type을 선언해준다(값이 없을시 C로 적용됨)
						reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, docId, pathKey, ssUserNo);
						// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
						ntsysFileMngService.insertFileInf(reNtsysFile);
						resultCode = "ok";
					}
				}
			}
		} else {
			// file 이 없을경우
			resultCode = "ok";
			resultMsg = "파일이 없습니다.";
			docId = "";
		}
//		########### Upload File 처리 종료 #############

		DataMap resultJSON = new DataMap();

		//return 상태
		DataMap resultStats = new DataMap();
		resultStats.put("resultCode", resultCode);
		resultStats.put("resultMsg", resultMsg);
		resultStats.put("doc_id", docId);
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
	}

	/**
	 * 이미지 첨부파일에 대한 목록을 조회한다.
	 *
	 * @param fileVO
	 * @param atchFileId
	 * @param sessionVO
	 * @param model
	 * @return
	 * @throws Exception
	 */
//	@RequestMapping("/cmm/fms/selectImageFileInfs.do")
//	public String selectImageFileInfs(@ModelAttribute("searchVO") FileVO fileVO, Map<String, Object> commandMap,
//		//SessionVO sessionVO,
//		ModelMap model) throws Exception {
//
//	String atchFileId = (String)commandMap.get("atchFileId");
//
//	fileVO.setAtchFileId(atchFileId);
//	List<FileVO> result = fileService.selectImageFileList(fileVO);
//
//	model.addAttribute("fileList", result);
//
//	return "egovframework/com/cmm/fms/EgovImgFileList";
//	}
}
