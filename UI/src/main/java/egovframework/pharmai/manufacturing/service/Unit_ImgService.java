package egovframework.pharmai.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : Unit_ImgService.java
 * 3. Package  : egovframework.pharmai.manufacturing.service
 * 4. Comment  : 제형 공정 이미지 목록
 * 5. 작성자   : 김성민
 * 6. 작성일   : 2021. 8. 270. 오전 10:36:36
 * </PRE>
 */
public interface Unit_ImgService {

	void insertUnit_Img(DataMap param) throws Exception;

	List selectStp05List(DataMap param) throws Exception;

	List selectDistinctUnitList(DataMap param) throws Exception;

	List selectDistinctCppList(DataMap param) throws Exception;


}
