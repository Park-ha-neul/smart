package egovframework.admin.stdTerm.service;

import java.util.List;


import egovframework.framework.common.object.DataMap;

public interface StdTermMgtService {

	int selectTotCntStdTermMgt(DataMap param) throws Exception;

	List selectPageListStdTermMgt(DataMap param) throws Exception;

	void insertstdTermMgt(DataMap param, List attachFileList)throws Exception;

	List selectDistinctFileName() throws Exception;

	String selectTopFile() throws Exception;

}
