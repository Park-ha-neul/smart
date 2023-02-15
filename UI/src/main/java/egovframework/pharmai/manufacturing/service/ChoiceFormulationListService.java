package egovframework.pharmai.manufacturing.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : ChoiceFormulationListService.java
 * 3. Package  : egovframework.pharmai.manufacturing.service
 * 4. Comment  : 제형 선택 리스트
 * 5. 작성자   : 김성민
 * 6. 작성일   : 2021. 8. 10. 오전 10:56:36
 * </PRE>
 */
public interface ChoiceFormulationListService {

	int selectTotCntStep_06(DataMap param) throws Exception;

	List selectPageListChoiceFormulation(DataMap param) throws Exception;

	int selectCountChoiceFormulation(DataMap param) throws Exception;
}
