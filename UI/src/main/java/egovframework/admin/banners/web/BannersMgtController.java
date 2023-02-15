
package egovframework.admin.banners.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import egovframework.admin.banners.service.BannersMgtService;
import egovframework.admin.banners.vo.BannersImgFileVo;
import egovframework.admin.common.vo.UserInfoVo;
import egovframework.common.service.CodeCacheService;
import egovframework.common.service.CommonCodeService;
import egovframework.framework.common.constant.Const;
import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.util.pageNavigationUtil;
import egovframework.framework.common.util.EgovMessageSource;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.MessageUtil;
import egovframework.framework.common.util.RequestUtil;
import egovframework.framework.common.util.SessionUtil;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.TransReturnUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.service.NtsysFileMngService;
import egovframework.framework.common.util.file.vo.NtsysFileVO;


@Controller
public class BannersMgtController{

	private static Log log = LogFactory.getLog(BannersMgtController.class);

	/** egovMessageSource */
	@Resource(name="egovMessageSource")
    private EgovMessageSource egovMessageSource;

	/** bannersMgtService */
    @Resource(name = "bannersMgtService")
    private BannersMgtService bannersMgtService;

    /** NtsysFileMngService */
    @Resource(name="NtsysFileMngService")
    private NtsysFileMngService ntsysFileMngService;

    /** NtsysFileMngUtil */
    @Resource(name="NtsysFileMngUtil")
    private NtsysFileMngUtil ntsysFileMngUtil;

    /** CommonCodeService */
	@Resource(name = "commonCodeService")
	private CommonCodeService commonCodeService;


    /**
     * <PRE>
     * 1. MethodName : selectPageListBannersMgt
     * 2. ClassName  : BannersMgtController
     * 3. Comment   : 배너 목록
     * 4. 작성자    : 김진영
     * 5. 작성일    : 2018. 7. 23. 오전 9:59:33
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value = { "/admin/banners/selectPageListBannersMgt.do", "/admin/banners/{sys}/selectPageListBannersMgt.do" })
    public String selectPageListBannersMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	log.debug("####" + this.getClass().getName() + " START ####");

    	DataMap param = RequestUtil.getDataMap(request);

    	if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sch_sys_code", sys);
		}

		param.put("sys_group_id", Const.UPCODE_SYS_CODE);
		param.put("zone_group_id", Const.UPCODE_BANNER_ZONE);

    	/* ### Pasing 시작 ### */
    	int totCnt = bannersMgtService.selectTotCntBanners(param);
    	param.put("totalCount", totCnt);
    	param = pageNavigationUtil.createNavigationInfo(model, param);
        List resultList = bannersMgtService.selectPageListBanners(param);
        /* ### Pasing 끝 ### */

        model.addAttribute("totalCount", totCnt);
        model.addAttribute("resultList", resultList);
        model.addAttribute("param", param);

        return "admin/banners/selectPageListBannersMgt";
    }


    /**
     * <PRE>
     * 1. MethodName : selectBannersMgt
     * 2. ClassName  : BannersMgtController
     * 3. Comment   : 배너 상세 조회
     * 4. 작성자    : 김진영
     * 5. 작성일    : 2018. 7. 23. 오전 10:00:46
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value = { "/admin/banners/selectBannersMgt.do", "/admin/banners/{sys}/selectBannersMgt.do" })
    public String selectBannersMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	log.debug("####" + this.getClass().getName() + " START ####");

    	DataMap param = RequestUtil.getDataMap(request);

    	if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		param.put("sys_group_id", Const.UPCODE_SYS_CODE);
		param.put("zone_group_id", Const.UPCODE_BANNER_ZONE);

    	DataMap resultMap = bannersMgtService.selectBanners(param);

    	//이미지 파일 정보 조회
    	BannersImgFileVo ifvo = new BannersImgFileVo();
    	List<BannersImgFileVo> fileList = null;

		ifvo.setDocId(resultMap.getString("WEB_IMAGE_DOC_ID"));
		fileList = bannersMgtService.selectBannersImgs(ifvo);
		model.addAttribute("webImageList", fileList);

		ifvo.setDocId(resultMap.getString("MOBILE_IMAGE_DOC_ID"));
		fileList = bannersMgtService.selectBannersImgs(ifvo);
		model.addAttribute("mobileImageList", fileList);

    	model.addAttribute("resultMap", resultMap);
        model.addAttribute("param", param);

        return "admin/banners/selectBannersMgt";
    }


    /**
     * <PRE>
     * 1. MethodName : insertFormBannersMgt
     * 2. ClassName  : BannersMgtController
     * 3. Comment   : 배너 등록 폼 이동
     * 4. 작성자    : 김진영
     * 5. 작성일    : 2018. 7. 23. 오전 10:01:16
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value = { "/admin/banners/insertFormBannersMgt.do", "/admin/banners/{sys}/insertFormBannersMgt.do" })
    public String insertFormBannersMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	log.debug("####" + this.getClass().getName() + " START ####");

    	DataMap param = RequestUtil.getDataMap(request);

    	if (sys != null) {
			param.put("sys_mapping_code", sys);
			param.put("sys_code", sys);
		}
		// 배너 구역 코드 조회
		List zoneComboStr = CodeCacheService.getCode(Const.UPCODE_BANNER_ZONE);
		model.addAttribute("zoneComboStr", zoneComboStr);

        model.addAttribute("param", param);

        return "admin/banners/insertFormBannersMgt";
    }


    /**
     * <PRE>
     * 1. MethodName : insertBannersImageAjax
     * 2. ClassName  : BannersMgtController
     * 3. Comment   : 배너입력
     * 4. 작성자    : 김진영
     * 5. 작성일    : 2018. 7. 27. 오전 10:10:46
     * </PRE>
     *   @return void
     *   @param request
     *   @param response
     *   @param model
     *   @throws Exception
     */
    @RequestMapping(value = { "/admin/banners/insertBannersImageAjax.do", "/admin/banners/{sys}/insertBannersImageAjax.do" })
	public @ResponseBody DataMap insertBannersImageAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		DataMap param = RequestUtil.getDataMap(request);

		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		List filelistW = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request, "upload_0");
		List filelistM = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request, "upload_1");

		//return 상태
		DataMap resultJSON = new DataMap();
		DataMap resultStats = new DataMap();

		// 파일 확장자 체크
		String msg1 = ntsysFileMngUtil.checkFileExt(filelistW);
		String msg2 = ntsysFileMngUtil.checkFileExt(filelistM);

		if (msg1 != "") {
			resultStats.put("resultCode", "error");
			resultStats.put("resultMsg", egovMessageSource.getMessage("error.file.not.access", new String[] { msg1 }));
			resultJSON.put("resultStats", resultStats);
		} else if (msg2 != "") {
			resultStats.put("resultCode", "error");
			resultStats.put("resultMsg", egovMessageSource.getMessage("error.file.not.access", new String[] { msg2 }));
			resultJSON.put("resultStats", resultStats);
		} else {
			bannersMgtService.insertBanners(param, filelistW, filelistM);

			MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));
			resultStats.put("resultCode", "ok");
			resultStats.put("resultMsg", "");
			resultJSON.put("resultStats", resultStats);//리턴값 : 선택된 메뉴 정보
		}

		return resultJSON;
	}

    /**
     * <PRE>
     * 1. MethodName : updateFormBannersMgt
     * 2. ClassName  : BannersMgtController
     * 3. Comment   : 배너 수정 폼 이동
     * 4. 작성자    : 김진영
     * 5. 작성일    : 2018. 7. 23. 오전 10:02:58
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value = { "/admin/banners/updateFormBannersMgt.do", "/admin/banners/{sys}/updateFormBannersMgt.do" })
    public String updateFormBannersMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	log.debug("####" + this.getClass().getName() + " START ####");

    	DataMap param = RequestUtil.getDataMap(request);

    	if (sys != null) {
			param.put("sys_mapping_code", sys);
		}

		// 배너 구역 코드 조회
		List zoneComboStr = CodeCacheService.getCode(Const.UPCODE_BANNER_ZONE);
		model.addAttribute("zoneComboStr", zoneComboStr);

		param.put("sys_group_id", Const.UPCODE_SYS_CODE);
		param.put("zone_group_id", Const.UPCODE_BANNER_ZONE);
		DataMap resultMap = bannersMgtService.selectBanners(param);

		//이미지 파일 정보 조회
    	BannersImgFileVo ifvo = new BannersImgFileVo();
    	List<BannersImgFileVo> fileList = null;

		ifvo.setDocId(resultMap.getString("WEB_IMAGE_DOC_ID"));
		fileList = bannersMgtService.selectBannersImgs(ifvo);
		model.addAttribute("webImageList", fileList);

		ifvo.setDocId(resultMap.getString("MOBILE_IMAGE_DOC_ID"));
		fileList = bannersMgtService.selectBannersImgs(ifvo);
		model.addAttribute("mobileImageList", fileList);

    	model.addAttribute("resultMap", resultMap);
        model.addAttribute("param", param);

        return "admin/banners/updateFormBannersMgt";
    }



    /**
     * <PRE>
     * 1. MethodName : updateBannersImageAjax
     * 2. ClassName  : BannersMgtController
     * 3. Comment   : 배너 수정
     * 4. 작성자    : 김진영
     * 5. 작성일    : 2018. 7. 27. 오전 10:11:28
     * </PRE>
     *   @return void
     *   @param request
     *   @param response
     *   @param model
     *   @throws Exception
     */
    @RequestMapping(value = { "/admin/banners/updateBannersImageAjax.do", "/admin/banners/{sys}/updateBannersImageAjax.do" })
	public @ResponseBody DataMap updateBannersImageAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

		// 시스템별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/banners/" + sys;
		} else {
			returnUrl = "/admin/banners";
		}

		DataMap param = RequestUtil.getDataMap(request);
		UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);
		param.put("ss_user_no", userInfoVo.getUserNo());

		List filelistW = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request, "upload_0");
    	List filelistM = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request, "upload_1");

    	//return 상태
    	DataMap resultJSON = new DataMap();
		DataMap resultStats = new DataMap();

		// 파일 확장자 체크
		String msg1 = ntsysFileMngUtil.checkFileExt(filelistW);
		String msg2 = ntsysFileMngUtil.checkFileExt(filelistM);

		if (msg1 != "") {
			resultStats.put("resultCode", "error");
			resultStats.put("resultMsg", egovMessageSource.getMessage("error.file.not.access", new String[] { msg1 }));
			resultJSON.put("resultStats", resultStats);
		} else if (msg2 != "") {
			resultStats.put("resultCode", "error");
			resultStats.put("resultMsg", egovMessageSource.getMessage("error.file.not.access", new String[] { msg2 }));
			resultJSON.put("resultStats", resultStats);
		} else {
			bannersMgtService.updateBanners(param, filelistW, filelistM);

			MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.insert"));
			resultStats.put("resultCode", "ok");
			resultStats.put("resultMsg", "");
			resultJSON.put("resultStats", resultStats);//리턴값 : 선택된 메뉴 정보
		}

		DataMap resultParam = new DataMap();

		resultParam.put("returnUrl", returnUrl + "/selectBannersMgt.do");
		String returnParam = SysUtil.createUrlParam(param);
		resultParam.put("returnParam", returnParam);
		resultJSON.put("resultParam", resultParam);

		return resultJSON;
	}


    /**
     * <PRE>
     * 1. MethodName : deleteBannersMgt
     * 2. ClassName  : BannersMgtController
     * 3. Comment   : 배너 삭제
     * 4. 작성자    : 안소영
     * 5. 작성일    : 2020. 6. 17. 오전 10:02:51
     * </PRE>
     *   @return String
     *   @param request
     *   @param response
     *   @param model
     *   @return
     *   @throws Exception
     */
    @RequestMapping(value = { "/admin/banners/deleteBannersMgt.do", "/admin/banners/{sys}/deleteBannersMgt.do" })
    public String deleteBannersMgt(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	UserInfoVo userInfoVo = SessionUtil.getSessionUserInfoVo(request);

    	//시스템별 returnUrl 설정
		String returnUrl = "";
		if (sys != null) {
			returnUrl = "/admin/banners/"+ sys;
        } else {
        	returnUrl = "/admin/banners";
        }

		DataMap param = RequestUtil.getDataMap(request);

		param.put("ss_user_no", userInfoVo.getUserNo());
		model.addAttribute("param", param);

		bannersMgtService.deleteBanners(param);

		MessageUtil.setMessage(request, egovMessageSource.getMessage("succ.data.delete"));

		return TransReturnUtil.returnPage(Globals.METHOD_GET, returnUrl + "/selectPageListBannersMgt.do", null, model);

    }


    /**
     * <PRE>
     * 1. MethodName : deleteBannersFileAjax
     * 2. ClassName  : BannersMgtController
     * 3. Comment   : 배너 이미지 삭제
     * 4. 작성자    : 김진영
     * 5. 작성일    : 2018. 8. 9. 오후 8:25:40
     * </PRE>
     *   @return void
     *   @param request
     *   @param response
     *   @param model
     *   @throws Exception
     */
    @RequestMapping(value = { "/admin/banners/deleteBannersFileAjax.do", "/admin/banners/{sys}/deleteBannersFileAjax.do" })
    public @ResponseBody DataMap deleteBannersFileAjax(@PathVariable(name = "sys", required = false) String sys, HttpServletRequest request, HttpServletResponse response, ModelMap model) throws Exception {

    	DataMap param = RequestUtil.getDataMap(request);

    	NtsysFileVO fvo = new NtsysFileVO();
    	fvo.setFileId(param.getString("file_id"));

    	fvo = bannersMgtService.selectBannersImg(fvo);

    	//return 상태
    	DataMap resultStats = new DataMap();
    	DataMap resultJSON = new DataMap();

    	boolean delYn = false;

		// d_type 설정이 안되어있으면 기본값
		if (StringUtil.nvl(param.getString("d_type")).equals("")) {
			// 무조건 설정된 값으로
			param.put("d_type", EgovPropertiesUtil.getProperty("Globals.fileSaveType"));
		}

		// 원래 이미지, 확장자
		if (param.getString("d_type").equals("O")) {
			delYn = ntsysFileMngUtil.deleteFile(fvo, "O");
		}
		// file_id, 원래 확장자
		else if (param.getString("d_type").equals("I")) {
			delYn = ntsysFileMngUtil.deleteFile(fvo, "I");
		}
		// file_id, 변환 확장자
		else {
			delYn = ntsysFileMngUtil.deleteFile(fvo, "C");
		}

		bannersMgtService.deleteBannersImg(param);

		resultStats.put("resultCode", "success");
		resultStats.put("resultMsg", "삭제에 성공하였습니다.");
		resultJSON.put("resultStats", resultStats);

		return resultJSON;
    }

}
