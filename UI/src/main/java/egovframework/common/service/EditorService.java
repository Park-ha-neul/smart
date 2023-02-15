package egovframework.common.service;

import java.util.List;

import egovframework.framework.common.object.DataMap;

public interface EditorService {

	void insertEditorFile(DataMap param, List fileList) throws Exception;
}
