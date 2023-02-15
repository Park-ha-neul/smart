package egovframework.admin.stdTerm.service;

import java.io.FileInputStream;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.text.SimpleDateFormat;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("stdTermMgtService")
public class StdTermMgtServiceImpl extends EgovAbstractServiceImpl implements StdTermMgtService {

	/** commonDao */
	@Resource(name="commonMybatisDao")
    private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;



	public int selectTotCntStdTermMgt(DataMap param) throws Exception {

		return (int)commonMybatisDao.selectOne("admin.stdTerm.selectTotCntStdTermMgt", param);
	}


	public List selectPageListStdTermMgt(DataMap param) throws Exception {

		return (List) commonMybatisDao.selectList("admin.stdTerm.selectPageListStdTermMgt", param);
	}

	public void insertstdTermMgt(DataMap param, List attachFileList) throws Exception {

		// 글 ID 셋팅
		String doc_id = StringUtil.nvl(param.getString("atch_doc_id"), SysUtil.getDocId());
		param.put("atch_doc_id", doc_id );

		// ########### Upload File 처리 시작 #############
				NtsysFileVO reNtsysFile = null;
				reNtsysFile = null;
				if(!attachFileList.isEmpty()){

					for(int i=0; i < attachFileList.size(); i++){
						MultipartFile mfile = (MultipartFile)attachFileList.get(i);
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
							reNtsysFile	= ntsysFileMngUtil.parseFileInf(mfile, doc_id, "stdTerm/", param.getString("ss_user_no"), "Y");
							// 파일이 생성되고나면 생성된 첨부파일 정보를 DB에 넣는다.
							commonMybatisDao.insert("common.file.insertAttchFile", reNtsysFile);
						}
					}
				}


			// ########### Upload File 처리 종료 ############
			System.out.println(reNtsysFile.getFileRltvPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm());
			FileInputStream file = new FileInputStream(reNtsysFile.getFileAsltPath() + reNtsysFile.getFileId() + "." + reNtsysFile.getFileExtNm());
			Date date = new Date();
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

			if(reNtsysFile.getFileExtNm().equals("xls")){ //xls 확장자일때

				HSSFWorkbook workbook =  new HSSFWorkbook(file);
				int rowIndex = 0;

				HSSFSheet sheet = workbook.getSheetAt(0);
				int rows = sheet.getPhysicalNumberOfRows();

				for(rowIndex = 2; rowIndex < rows; rowIndex++){
					HSSFRow row = sheet.getRow(rowIndex);
					if(row != null){
						DataMap dataMap = new DataMap();
						dataMap.put("termCode", row.getCell(1).getStringCellValue());
						dataMap.put("termName", row.getCell(2).getStringCellValue());
						dataMap.put("termEngName", row.getCell(3).getStringCellValue());
						dataMap.put("engAbbreviation", row.getCell(4).getStringCellValue());
						dataMap.put("registDt", format.parse(row.getCell(5).getStringCellValue()));
						dataMap.put("useYn", row.getCell(6).getStringCellValue().equals("사용중") ? "Y" : "N");
						dataMap.put("fileId", reNtsysFile.getFileId());
						dataMap.put("fileName", reNtsysFile.getFileNm());
						dataMap.put("uploadDt", date);

						commonMybatisDao.insert("admin.stdTerm.insertstdTermMgt", dataMap);

					}
				}
			}else{ //xlsx 확장자 일때
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				int rowindex = 0;
				XSSFSheet sheet = workbook.getSheetAt(0);

				int rows = sheet.getPhysicalNumberOfRows();
				for(rowindex = 2; rowindex < rows; rowindex++){
					XSSFRow row = sheet.getRow(rowindex);
					if(row != null){
						DataMap dataMap = new DataMap();
						dataMap.put("termCode", row.getCell(1).getStringCellValue());
						dataMap.put("termName", row.getCell(2).getStringCellValue());
						dataMap.put("termEngName", row.getCell(3).getStringCellValue());
						dataMap.put("engAbbreviation", row.getCell(4).getStringCellValue());
						dataMap.put("registDt", format.parse(row.getCell(5).getStringCellValue()));
						dataMap.put("useYn", row.getCell(6).getStringCellValue().equals("사용중") ? "Y" : "N");
						dataMap.put("fileId", reNtsysFile.getFileId());
						dataMap.put("fileName", reNtsysFile.getFileNm());
						dataMap.put("uploadDt", date);

						commonMybatisDao.insert("admin.stdTerm.insertstdTermMgt", dataMap);

					}

				}

			}
		}

	public List selectDistinctFileName() throws Exception {
		// TODO Auto-generated method stub
		return (List) commonMybatisDao.selectList("admin.stdTerm.selectDistinctFileName");
	}

	public String selectTopFile() throws Exception {
		// TODO Auto-generated method stub
		return commonMybatisDao.selectOne("admin.stdTerm.selectTopFile");
	}



}
