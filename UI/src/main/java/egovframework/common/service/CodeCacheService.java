package egovframework.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import egovframework.framework.common.object.DataMap;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("codeCacheService")
public class CodeCacheService extends EgovAbstractServiceImpl {
	@Autowired
	public CodeCacheDao CodeCacheDao;
	private static DataMap cacheCode = new DataMap();

	/**
	 * <PRE>
	 * 1. MethodName : resetCodeList
	 * 2. ClassName  : CodeCacheService
	 * 3. Comment   :  공통코드 메모리 등록
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 21. 오후 5:20:50
	 * </PRE>
	 *
	 * @return void
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PostConstruct
	public void resetCodeList() throws Exception {
		if (cacheCode.isEmpty()) {
			synchronized (cacheCode) {
				if (cacheCode.isEmpty()) {
					// 공통코드 List 조회
					List<DataMap> groupIdMap = (ArrayList<DataMap>) CodeCacheDao.selectListCodeGroupId();
					// 공통코드상세 List 조회
					List<DataMap> dataMapList = (ArrayList<DataMap>) CodeCacheDao.selectListCode();
					cacheCode.clear();

					String groupId = "";
					DataMap dataMap = new DataMap();

					for (int i = 0; i < groupIdMap.size(); i++) {
						groupId = groupIdMap.get(i).getString("GROUP_ID");

						List<DataMap> list = new ArrayList<DataMap>();
						for (int j = 0; j < dataMapList.size(); j++) {
							dataMap = dataMapList.get(j);

							if (dataMap.getString("GROUP_ID").equals(groupId)) {
								list.add(dataMap);
							}
						}
						cacheCode.put(groupId, list);
					}
				}
			}
		}
	}

	/**
	 * <PRE>
	 * 1. MethodName : clear
	 * 2. ClassName  : CodeCacheService
	 * 3. Comment   : 공통 코드 clear
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 21. 오후 5:21:38
	 * </PRE>
	 *
	 * @return void
	 * @throws Exception
	 */
	public static void clear() throws Exception {
		cacheCode.clear();
	}

	/**
	 * <PRE>
	 * 1. MethodName : getCode
	 * 2. ClassName  : CodeCacheService
	 * 3. Comment   : 코드 그룹에 속한 상세 코드 List 반환
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 21. 오후 5:21:05
	 * </PRE>
	 *
	 * @return List<DataMap>
	 * @param groupId
	 * @return
	 * @throws Exception
	 */
	public static List<Object> getCode(String groupId) throws Exception {
		List<Object> returnList = cacheCode.getList(groupId);

		return returnList;
	}

	/**
	 * <PRE>
	 * 1. MethodName : getCode
	 * 2. ClassName  : CodeCacheService
	 * 3. Comment   :  코드 그룹에 속한 상세 특정 코드 List 반환
	 * 4. 작성자    : kimkm
	 * 5. 작성일    : 2020. 4. 23. 오후 3:27:50
	 * </PRE>
	 *   @return List<Object>
	 *   @param groupId
	 *   @param code
	 *   @return
	 *   @throws Exception
	 */
	public static List<Object> getCode(String groupId, String code) throws Exception {
		List<Object> returnList = cacheCode.getList(groupId);
		List<Object> returnVal = new ArrayList<Object>();

		for (int i = 0; i < returnList.size(); i++) {
			DataMap dataMap = new DataMap();
			dataMap = (DataMap) returnList.get(i);

			if (dataMap.getString("CODE").equals(code)) {
				returnVal.add(dataMap);
				break;
			}
		}
		return returnVal;
	}
}
