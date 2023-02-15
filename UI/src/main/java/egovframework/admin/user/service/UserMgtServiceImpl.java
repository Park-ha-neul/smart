package egovframework.admin.user.service;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.egovutil.EgovFileScrty;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.CookieUtil;
import egovframework.framework.common.util.HashUtil;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.cryptography.EgovEnvCryptoService;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : UserServiceImpl.java
 * 3. Package  : egovframework.admin.user.web.service
 * 4. Comment  : 사용자 관리
 * 5. 작성자   : 함경호
 * 6. 작성일   : 2015. 9. 1. 오후 3:34:30
 * </PRE>
 */
@Service("userMgtService")
public class UserMgtServiceImpl extends EgovAbstractServiceImpl implements UserMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	/** 암호화서비스 */
	@Resource(name = "egovEnvCryptoService")
	EgovEnvCryptoService cryptoService;

	/**
	 * <PRE>
	 * 1. MethodName : selectUserTotCnt
	 * 2. ClassName  : UserServiceImpl
	 * 3. Comment   : 사용자 총 개수 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:30
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int selectTotCntUser(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("admin.user.selectTotCntUser", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListUser
	 * 2. ClassName  : UserServiceImpl
	 * 3. Comment   : 사용자 페이지 리스트 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:29
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListUser(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.user.selectPageListUser", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertUser
	 * 2. ClassName  : UserServiceImpl
	 * 3. Comment   : 사용자 등록
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:28
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void insertUser(DataMap param) throws Exception {
    	int userNo = (Integer) commonMybatisDao.selectOne("admin.user.selectNumberUser", "");

    	//사용자 정보삽입
    	param.put("user_no", userNo);
		param.put("password", EgovFileScrty.encryptPassword(param.getString("password"), param.getString("user_id")));
		commonMybatisDao.insert("admin.user.insertUser", param);

		//사용자부가정보삽입
		if (!param.getString("cttpc").equals(""))
			param.put("cttpc", cryptoService.encrypt(param.getString("cttpc")));
		if (!param.getString("email").equals(""))
			param.put("email", cryptoService.encrypt(param.getString("email")));
		commonMybatisDao.insert("admin.user.insertUserInfo", param);

		List authList = param.getList("author_id");
		if(authList != null){
			DataMap authMap = new DataMap();
			authMap.put("user_no", userNo);
			authMap.put("ss_user_no", param.getString("ss_user_no"));

			for(int i = 0; i < authList.size(); i++){
				authMap.put("author_id", authList.get(i));
				commonMybatisDao.insert("admin.user.insertAuthUser", authMap);
			}
		}

	}

	/**
	 * <PRE>
	 * 1. MethodName : selectUser
	 * 2. ClassName  : UserServiceImpl
	 * 3. Comment   : 사용자 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:28
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectUser(DataMap param) throws Exception {
		DataMap dataMap = commonMybatisDao.selectOne("admin.user.selectUser", param);

		if (!dataMap.getString("CTTPC").equals(""))
			dataMap.put("CTTPC", cryptoService.decrypt(dataMap.getString("CTTPC")));
		if (!dataMap.getString("EMAIL").equals(""))
			dataMap.put("EMAIL", cryptoService.decrypt(dataMap.getString("EMAIL")));

		return dataMap;
	}

	/**
	 * <PRE>
	 * 1. MethodName : updateUser
	 * 2. ClassName  : UserServiceImpl
	 * 3. Comment   : 사용자 수정
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:27
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void updateUser(DataMap param) throws Exception {
		// 사용자 정보
		if (!param.getString("password").equals("")) {
			param.put("password", EgovFileScrty.encryptPassword(param.getString("password"), param.getString("user_id")));
		}

		commonMybatisDao.update("admin.user.updateUser", param);

		//사용자 부가 정보
		String resultMap = commonMybatisDao.selectOne("admin.user.selectUserInfoExistYn", param);

		if (!param.getString("cttpc").equals(""))
			param.put("cttpc", cryptoService.encrypt(param.getString("cttpc")));
		if (!param.getString("email").equals(""))
			param.put("email", cryptoService.encrypt(param.getString("email")));

		if(resultMap.equals("Y")){
			commonMybatisDao.update("admin.user.updateUserInfo", param);
		}else{
			commonMybatisDao.insert("admin.user.insertUserInfo", param);
		}

		commonMybatisDao.delete("admin.user.deleteAuthUser", param);

		List authList = param.getList("author_id");
		if(authList != null){
			//int userNoSeq = (Integer) commonMybatisDao.selectOne("admin.user.selectAuthNumberUser", "");
			DataMap authMap = new DataMap();
			authMap.put("user_no", param.getString("user_no"));
			authMap.put("ss_user_no", param.getString("ss_user_no"));

			for(int i = 0; i < authList.size(); i++){
				authMap.put("author_id", authList.get(i));
				//authMap.put("user_no_seq",  userNoSeq);
				commonMybatisDao.insert("admin.user.insertAuthUser", authMap);
			}
		}

	}


	/**
	 * <PRE>
	 * 1. MethodName : updateUserInfo
	 * 2. ClassName  : UserMgtServiceImpl
	 * 3. Comment   : 모달창 사용자정보 변경
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2016. 6. 3. 오후 3:06:12
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void updateUserInfo(HttpServletRequest request, HttpServletResponse response, DataMap param) throws Exception {
		//비밀번호 변경
		if(!param.getString("modal_password").equals("")){
			// 비밀번호 암호화
			param.put("modal_password",EgovFileScrty.encryptPassword(param.getString("modal_password"), param.getString("modal_user_id")));
			commonMybatisDao.update("admin.user.updatePwd", param);
			// 비밀번호 변경이 되기 때문에 자동로그인 해제
			CookieUtil.removeCookie(request, response, "autoLoginId");
			CookieUtil.removeCookie(request, response, "autoLoginPw");
		}
		if (!param.getString("modal_cttpc").equals(""))
			param.put("cttpc", cryptoService.encrypt(param.getString("modal_cttpc")));
		if (!param.getString("modal_email").equals(""))
			param.put("email", cryptoService.encrypt(param.getString("modal_email")));
		//회원정보 변경
		param.put("cttpc_se_code", param.getString("modal_cttpc_se_code"));
		commonMybatisDao.update("admin.user.updateUserInfo", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : deleteUser
	 * 2. ClassName  : UserServiceImpl
	 * 3. Comment   : 사용자 삭제
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:25
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void deleteUser(DataMap param) throws Exception {
		commonMybatisDao.delete("admin.user.deleteAuthUser", param);
    	commonMybatisDao.delete("admin.user.deleteUser", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectIdExistYn
	 * 2. ClassName  : UserServiceImpl
	 * 3. Comment   : 사용자 아이디 중복 검사
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 3:34:23
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public String selectIdExistYn(DataMap param) throws Exception {
    	return (String) commonMybatisDao.selectOne("admin.user.selectIdExistYn", param);
    }

	/**
	 * <PRE>
	 * 1. MethodName : selectAuthorList
	 * 2. ClassName  : UserServiceImpl
	 * 3. Comment   : 권한 리스트 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 1. 오후 5:55:08
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectListAuthor(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.user.selectListAuthor", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListAuthorUser
	 * 2. ClassName  : UserServiceImpl
	 * 3. Comment   : 사용자 권한 리스트 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 9. 2. 오전 9:21:42
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectListAuthorUser(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.user.selectListAuthorUser", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectPageListAllUser
	 * 2. ClassName  : UserServiceImpl
	 * 3. Comment   : 사용자 리스트 전체 조회
	 * 4. 작성자    : 함경호
	 * 5. 작성일    : 2015. 11. 3. 오전 9:33:18
	 * </PRE>
	 *   @return List
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectPageListAllUser(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("admin.user.selectPageListAllUser", param);
	}
	/**
	 * <PRE>
	 * 1. MethodName : initlUserPassword
	 * 2. ClassName  : UserMgtServiceImpl
	 * 3. Comment   : 비밀번호 초기화
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 7. 오후 3:09:47
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	@Override
	public void initlUserPassword(DataMap param) throws Exception {
		commonMybatisDao.selectList("admin.user.initlUserPassword", param);
	}

}
