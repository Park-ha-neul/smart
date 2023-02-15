package egovframework.admin.common.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.constant.Globals;
import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.egovutil.EgovFileScrty;
import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : LoginServiceImpl.java
 * 3. Package  : egovframework.admin.common.service
 * 4. Comment  : 관리자 로그인
 * 5. 작성자   : kimkm
 * 6. 작성일   : 2020. 4. 1. 오후 5:14:12
 * </PRE>
 */
@Service("loginService")
public class LoginServiceImpl extends EgovAbstractServiceImpl implements LoginService {

	/** commonDao */
	@Resource(name = "commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;
	
    public int registerCheckUserId(DataMap param) throws Exception {
        return (Integer) commonMybatisDao.selectOne("login.registerCheckUserId", param);
    }
	
	/**
	 * <PRE>
	 * 1. MethodName : registerUserAuthor
	 * 2. ClassName  : LoginServiceImpl
	 * 3. Comment   : 회원가입 권한 생성
	 * 4. 작성자    : 허웅
	 * 5. 작성일    : 2022. 4. 15
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public void registerUserAuthor(DataMap param) throws Exception {
		commonMybatisDao.insert("login.registerUserAuthor", param);
	}
	
	/**
	 * <PRE>
	 * 1. MethodName : registerUser
	 * 2. ClassName  : LoginServiceImpl
	 * 3. Comment   : 회원가입
	 * 4. 작성자    : 허웅
	 * 5. 작성일    : 2022. 4. 15
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public void registerUser(DataMap param) throws Exception {
		String enpassword = EgovFileScrty.encryptPassword(param.getString("r_password"), param.getString("r_user_id"));
		param.put("pwd", enpassword);
		commonMybatisDao.insert("login.registerUser", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : checkIfUserExists
	 * 2. ClassName  : LoginServiceImpl
	 * 3. Comment   : 사용자 존재 여부 체크
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 1. 오후 5:19:46
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public String checkIfUserExists(DataMap param) throws Exception {
		return commonMybatisDao.selectOne("login.checkIfUserExists", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectUserInfo
	 * 2. ClassName  : LoginServiceImpl
	 * 3. Comment   : 사용자 정보 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 1. 오후 5:20:27
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public DataMap selectUserInfo(DataMap param) throws Exception {
		param.put("system_env", Globals.SYSTEM_ENV);
		// 비밀번호 암호화
		String enpassword = EgovFileScrty.encryptPassword(param.getString("pwd"), param.getString("id"));
		param.put("pwd", enpassword);

		return (DataMap) commonMybatisDao.selectOne("login.selectUserInfo", param);
	}

	/**
	 *
	 * <PRE>
	 * 1. MethodName : updateLoginFailCount
	 * 2. ClassName  : LoginServiceImpl
	 * 3. Comment   : 로그인실패 카운트 갱신
	 * 4. 작성자    : 이정훈
	 * 5. 작성일    : 2020. 1. 9. 오후 1:32:29
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public int updateLoginFailCount(DataMap param) throws Exception {
		return commonMybatisDao.update("login.updateLoginFailCount", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertAdminEventLog
	 * 2. ClassName  : LoginServiceImpl
	 * 3. Comment   : 관리자 로그 삽입
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 1. 오후 5:20:52
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void insertAdminEventLog(DataMap param) throws Exception {
		commonMybatisDao.insert("common.insertAdminEventLog", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : insertErrorEventLog
	 * 2. ClassName  : LoginServiceImpl
	 * 3. Comment   : 에러 로그 삽입
	 * 4. 작성자    : Ahn So Young
	 * 5. 작성일    : 2020. 6. 5. 오후 1:6:31
	 * </PRE>
	 *   @param param
	 *   @throws Exception
	 */
	public void insertErrorEventLog(DataMap param) throws Exception {
		commonMybatisDao.insert("common.insertErrorEventLog", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectListUserAuthId
	 * 2. ClassName  : LoginServiceImpl
	 * 3. Comment   : 권한 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 7. 오후 5:44:31
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public List selectListUserAuthId(DataMap param) throws Exception {
        return (List) commonMybatisDao.selectList("login.selectListUserAuthId", param);
    }

	/**
	 * <PRE>
	 * 1. MethodName : initlUserPasswordCheck
	 * 2. ClassName  : LoginServiceImpl
	 * 3. Comment   : 비밀번호 초기화 여부 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 5. 7. 오후 5:44:45
	 * </PRE>
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	@Override
	public DataMap initlUserPasswordCheck(DataMap param) throws Exception {
		// TODO Auto-generated method stub
		return (DataMap) commonMybatisDao.selectOne("login.initlUserPasswordCheck", param);
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectAccessYn
	 * 2. ClassName  : LoginServiceImpl
	 * 3. Comment   : 접근 ip 조회
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 6. 12. 오후 4:23:34
	 * </PRE>
	 *   @param dataParam
	 *   @return
	 *   @throws Exception
	 */
	@Override
	public boolean selectAccessYn(DataMap dataParam) throws Exception {
		// TODO Auto-generated method stub
		String flagYn = commonMybatisDao.selectOne("common.selectAccessYn", dataParam);

		if (flagYn.equals("Y")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : selectAuthorMenuByUrl
	 * 2. ClassName  : LoginService
	 * 3. Comment   : 권한에 따른 초기 url
	 * 4. 작성자    : KALEL
	 * 5. 작성일    : 2021. 3. 24. 오전 11:06:19
	 * </PRE>
	 *   @return String
	 *   @param param
	 *   @return
	 *   @throws Exception
	 */
	public String selectAuthorMenuByUrl(DataMap param) throws Exception {
		return (String)commonMybatisDao.selectOne("common.selectAuthorMenuByUrl",param);
	}

}
