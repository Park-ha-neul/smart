package egovframework.admin.qr.service;

import java.awt.image.BufferedImage;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.DateUtil;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.QrcodeUtil;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.dao.NtsysFileManageDao;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : QrCodeMgtServiceImpl.java
 * 3. Package  : egovframework.admin.qr.service
 * 4. Comment  :
 * 5. 작성자   : mk
 * 6. 작성일   : 2018. 7. 13. 오전 11:49:32
 * </PRE>
 */
@Service("qrCodeMgtService")
public class QrCodeMgtServiceImpl extends EgovAbstractServiceImpl implements QrCodeMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	/** 파일관련 */
    @Resource(name = "NtsysFileManageDao")
    private NtsysFileManageDao ntsysFileManageDao;

    @Resource(name="NtsysFileMngUtil")
    private NtsysFileMngUtil ntsysFileMngUtil;

    /** qrcode */
	@Resource(name = "qrCodeUtil")
	private QrcodeUtil qrCodeUtil;


	/**
	 * <PRE>
	 * 1. MethodName : selectTotCntQrCodeMgt
	 * 2. ClassName  : QrCodeMgtServiceImpl
	 * 3. Comment   : qr 코드 총 개수
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오전 11:52:28
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotCntQrCodeMgt(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.qrCode.selectTotCntQrCodeMgt", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListQrCodeMgt
	 * 2. ClassName  : QrCodeMgtServiceImpl
	 * 3. Comment   : qr 코드 리스트
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오전 11:52:44
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListQrCodeMgt(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.qrCode.selectPageListQrCodeMgt", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectQrCodeMgt
	 * 2. ClassName  : QrCodeMgtServiceImpl
	 * 3. Comment   : qr 코드 상세
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오전 11:54:12
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectQrCodeMgt(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.qrCode.selectQrCodeMgt", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertQrCodeMgt
	 * 2. ClassName  : QrCodeMgtServiceImpl
	 * 3. Comment   : qr 코드 등록
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오전 11:57:19
	 * </PRE>
	 *   @param param
	 *   @param fileList
	 *   @throws Exception
	 */
	public void insertQrCodeMgt(DataMap param, List fileList) throws Exception {

		String common_dir = "qr/" + DateUtil.getToday("yyyyMMdd") + "/";
		String code_dir = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath") + common_dir;
    	String code_nm = SysUtil.getFileId();
    	// QR_IMAGE_DOC_ID 셋팅
    	String qr_image_doc_id = StringUtil.nvl(param.getString("qr_image_doc_id"), SysUtil.getDocId());
		param.put("qr_image_doc_id", qr_image_doc_id );
    	// IMAGE_DOC_ID 셋팅
		String image_doc_id = StringUtil.nvl(param.getString("image_doc_id"), SysUtil.getDocId());
		param.put("image_doc_id", image_doc_id );

    	// 저장할 경로를 설정
    	param.put("code_dir", code_dir);
    	// 저장이름 설정
    	param.put("code_nm", code_nm);

		if(!fileList.isEmpty()){

			// ########### 배경 이미지 Upload 처리 시작 #############
			NtsysFileVO _reNtsysFile = null;
			for(int i=0; i < fileList.size(); i++){
				MultipartFile mfile = (MultipartFile)fileList.get(i);
				if(!mfile.isEmpty()){
					/*
					 * parseFileInf
					 * 1:파일객체
					 * 2:문서아이디
					 * 3:서브폴더명
					 * 4:사용자번호
					 * 5:Web Root Yn
					 */
					// 파일을 서버에 물리적으로 저장하고
					_reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, image_doc_id, "qr/" , param.getString("ss_user_no"), "Y");
					// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
					commonMybatisDao.insert("common.file.insertAttchFile", _reNtsysFile);
				}
			}
			// ########### 배경 이미지 File 처리 종료 ############

	    	//배경이미지가 있을 경우 배경이미지의 사이즈를 설정
			MultipartFile mf = (MultipartFile)fileList.get(0);
			BufferedImage image = ImageIO.read(mf.getInputStream());

			param.put("code_width_margin", image.getWidth());
			param.put("code_height_margin", image.getHeight());
		}

    	// 확정된 이미지를 첨부파일 경로에 저장한다.
    	NtsysFileVO fvo = qrCodeUtil.makeQRcode(param, fileList);

    	if(fvo != null){
//    		########### Upload File 처리 시작 #############

    		// 파일 정보 생성(추가적인 정보 입력)
    		fvo.setFileId(code_nm);
    		fvo.setDocId(qr_image_doc_id);
    		fvo.setFileRmk("qr code");
    		fvo.setFileAsltPath(code_dir);
    		fvo.setFileRltvPath(EgovPropertiesUtil.getProperty("Globals.fileWebrootURL") + common_dir);
    		fvo.setSsUserId(param.getString("ss_user_id"));

//    		 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
    		ntsysFileManageDao.insertFileInf(fvo);

//    		########### Upload File 처리 종료 ############

    		commonMybatisDao.insert("admin.qrCode.insertQrCodeMgt", param);
    	}
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateQrCodeMgt
	 * 2. ClassName  : QrCodeMgtServiceImpl
	 * 3. Comment   : qr 코드 수정
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오전 11:57:24
	 * </PRE>
	 *   @param param
	 *   @param fileList
	 *   @throws Exception
	 */
	public void updateQrCodeMgt(DataMap param, List fileList, String imgExistYn) throws Exception {

		String common_dir = "qr/" + DateUtil.getToday("yyyyMMdd") + "/";
		String code_dir = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath") + common_dir;
    	String code_nm = SysUtil.getFileId();
    	// QR_IMAGE_DOC_ID 셋팅
    	String qr_image_doc_id = StringUtil.nvl(param.getString("qr_image_doc_id"), SysUtil.getDocId());
    	param.put("qr_image_doc_id", qr_image_doc_id );
    	// IMAGE_DOC_ID 셋팅
		String image_doc_id = StringUtil.nvl(param.getString("image_doc_id"), SysUtil.getDocId());
		param.put("image_doc_id", image_doc_id );

    	// 저장할 경로를 설정
    	param.put("code_dir", code_dir);
    	// 저장이름 설정
    	param.put("code_nm", code_nm);

		if(!fileList.isEmpty()){
			//파일리스트에 파일이 존재하고 서버에 등록된 배경이미지가 존재 하지 않으면 배경이미지를 서버에 업로드하고 DB에 저장한다.
			if(imgExistYn.equals("N")){
				// ########### 배경 이미지 Upload 처리 시작 #############
				NtsysFileVO _reNtsysFile = null;
				for(int i=0; i < fileList.size(); i++){
					MultipartFile mfile = (MultipartFile)fileList.get(i);
					if(!mfile.isEmpty()){
						/*
						 * parseFileInf
						 * 1:파일객체
						 * 2:문서아이디
						 * 3:서브폴더명
						 * 4:사용자번호
						 * 5:Web Root Yn
						 */
						// 파일을 서버에 물리적으로 저장하고
						_reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, image_doc_id, "qr/" , param.getString("ss_user_no"), "Y");
						// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
						commonMybatisDao.insert("common.file.insertAttchFile", _reNtsysFile);
					}
				}
				// ########### 배경 이미지 File 처리 종료 ############
			}

			//배경이미지가 있을 경우 배경이미지의 사이즈를 설정
			MultipartFile mf = (MultipartFile)fileList.get(0);
			BufferedImage image = ImageIO.read(mf.getInputStream());

			param.put("code_width_margin", image.getWidth());
			param.put("code_height_margin", image.getHeight());
		}

    	// 확정된 이미지를 첨부파일 경로에 저장한다.
    	NtsysFileVO fvo = qrCodeUtil.makeQRcode(param, fileList);

    	if(fvo != null){

    		// 기존 qr코드 이미지  삭제
    		NtsysFileVO dfvo = new NtsysFileVO();
    		dfvo.setDocId(param.getString("qr_image_doc_id"));
    		List<NtsysFileVO> dFile = (List<NtsysFileVO>)commonMybatisDao.list("common.file.selectAttchFiles", dfvo);
    		ntsysFileMngUtil.deleteFile(dFile, "I");
    		//qr코드 이미지 삭제 doc_id 셋팅
    		param.put("doc_id", param.getString("qr_image_doc_id"));
    		commonMybatisDao.delete("common.file.deleteAttchFiles", param);

//    		########### Upload File 처리 시작 #############

    		// 파일 정보 생성(추가적인 정보 입력)
    		fvo.setFileId(code_nm);
    		fvo.setDocId(qr_image_doc_id);
    		fvo.setFileRmk("qr code");
    		fvo.setFileAsltPath(code_dir);
    		fvo.setFileRltvPath(EgovPropertiesUtil.getProperty("Globals.fileWebrootURL") + common_dir); //상대경로
    		fvo.setSsUserId(param.getString("ss_user_id"));

//    		 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
    		ntsysFileManageDao.insertFileInf(fvo);

//    		########### Upload File 처리 종료 ############

    		commonMybatisDao.update("admin.qrCode.updateQrCodeMgt", param);
    	}
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteQrCodeMgt
	 * 2. ClassName  : QrCodeMgtServiceImpl
	 * 3. Comment   : qr 코드 삭제
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 7. 13. 오전 11:57:29
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteQrCodeMgt(DataMap param) throws Exception {

		//qr코드 이미지  삭제
		NtsysFileVO dfvo = new NtsysFileVO();
		dfvo.setDocId(param.getString("qr_image_doc_id"));
		List<NtsysFileVO> dFile = (List)commonMybatisDao.selectList("common.file.selectAttchFiles", dfvo);
		ntsysFileMngUtil.deleteFile(dFile, "I");
		//qr코드 이미지 삭제 doc_id 셋팅
		param.put("doc_id", param.getString("qr_image_doc_id"));
		commonMybatisDao.delete("common.file.deleteAttchFiles", param);

		// 배경 이미지 삭제
		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setDocId(param.getString("image_doc_id"));
		List<NtsysFileVO> dImgFile = (List)commonMybatisDao.selectList("common.file.selectAttchFiles", fvo);
		ntsysFileMngUtil.deleteFile(dImgFile, "I");
		param.put("doc_id", param.getString("image_doc_id"));
		commonMybatisDao.delete("common.file.deleteAttchFiles", param);

		commonMybatisDao.delete("admin.qrCode.deleteQrCodeMgt", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectImgExistYn
	 * 2. ClassName  : QrCodeMgtServiceImpl
	 * 3. Comment   : 상세정보에 배경이미지 존재 유무 확인
	 * 4. 작성자    : mk
	 * 5. 작성일    : 2018. 8. 11. 오후 4:40:15
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectImgExistYn(DataMap param) throws Exception {
		return (DataMap) commonMybatisDao.selectOne("admin.qrCode.selectImgExistYn", param);
	}

}

