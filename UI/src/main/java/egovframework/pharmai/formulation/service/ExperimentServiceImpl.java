package egovframework.pharmai.formulation.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import egovframework.framework.common.dao.CommonMybatisDao;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.EgovPropertiesUtil;
import egovframework.framework.common.util.EgovWebUtil;
import egovframework.framework.common.util.StringUtil;
import egovframework.framework.common.util.SysUtil;
import egovframework.framework.common.util.file.NtsysFileMngUtil;
import egovframework.framework.common.util.file.vo.NtsysFileVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;

@Service("experimentService")
public class ExperimentServiceImpl extends EgovAbstractServiceImpl implements ExperimentService{

	/** commonDao */
	@Resource(name="commonMybatisDao")
	private CommonMybatisDao commonMybatisDao;

	@Resource(name="NtsysFileMngUtil")
	private NtsysFileMngUtil ntsysFileMngUtil;

	@Resource(name = "experimentService")
	private ExperimentService experimentService;

	public void insertFormulaStp_05(DataMap param) throws Exception{
		
		System.out.println("save 먼저 해주는지 check");

		//header
		List exprHeader = param.getList("exprHeader");
		for(int i = 0; i< exprHeader.size(); i++) {
			param.put("c"+(i+1), exprHeader.get(i));
		}
		commonMybatisDao.insert("pharmai.formulation.insertFormulaStp_05", param);

		//experimentData
		int exprLength = Integer.parseInt(param.getString("exprLength"));
		for(int i = 0; i< exprLength; i++) {
			List exprList = param.getList("exprVal_"+i);
			for(int j = 0; j< exprList.size(); j++) {
				param.put("c"+(j+1), exprList.get(j));
			}
			commonMybatisDao.insert("pharmai.formulation.insertFormulaStp_05", param);
		}

		List exprDataList = commonMybatisDao.selectList("pharmai.formulation.selectListFormulaStp_05", param);

		File file = null;
		BufferedWriter bufferWriter = null;

		String filePath = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation") + param.getString("prjct_id") + "/api6/csv/";
		String fileName = "api6_experiment.csv";

		File saveFolder = new File(EgovWebUtil.filePathBlackList(filePath));

		//파일 경로에 폴더가 없으면 폴더생성
		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}

		file = new File(filePath + fileName);

		bufferWriter = new BufferedWriter(new FileWriter(file));

		DataMap data = new DataMap();
		int selectStp3TotYn = experimentService.selectStp3TotYn(param);

		for(int i = 0; i< exprDataList.size(); i++) {
			data = (DataMap) exprDataList.get(i);
			for(int j = 1 + selectStp3TotYn; j <= 13; j++) {
				String currentData = data.getString("C" + j);
				if(!"".equals(currentData)) {
					bufferWriter.append(currentData);
					String nextData = data.getString("C" + (j+1));
					if(!"".equals(nextData)) {
						bufferWriter.append(",");
					}
				}
			}

			bufferWriter.newLine();
		}


		bufferWriter.flush();
		bufferWriter.close();

		String doc_id = StringUtil.nvl(param.getString("doc_id"), SysUtil.getDocId());

		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setFileId(SysUtil.getFileId());
		fvo.setDocId(doc_id);
		fvo.setFileRmk("");
		fvo.setFileNm(fileName);
		fvo.setFileRltvPath(EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation") + param.getString("prjct_id") + "/api6/csv/");//상대경로
		fvo.setFileAsltPath(filePath); //절대경료
		fvo.setFileSize(file.length());
		fvo.setSsUserId(param.getString("ss_user_no"));
		fvo.setContentType("application/csv");
		fvo.setFileExtNm("csv");

		commonMybatisDao.insert("common.file.insertAttchFile", fvo);

	   	//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharmai.formulation.updateMstStep", param);

	}

	public List selectListFormulaStp_03(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectListFormulaStp_03", param);
	}

	public List selectListFormulaStp_04(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectListFormulaStp_04", param);
	}

	public List selectListFormulaStp_05(DataMap param) throws Exception {
		return (List) commonMybatisDao.selectList("pharmai.formulation.selectListFormulaStp_05", param);
	}

	public int selectStp5ColumnCount() throws Exception {
		return (Integer) commonMybatisDao.selectOne("pharmai.formulation.selectStp5ColumnCount");
	}

	public int selectStp3TotYn(DataMap param) throws Exception {
		return (Integer) commonMybatisDao.selectOne("pharmai.formulation.selectStp3TotYn", param);
	}

	public void formulTemporarySave(DataMap param) throws Exception {

		//header
		List exprHeader = param.getList("exprHeader");
		for(int i = 0; i< exprHeader.size(); i++) {
			param.put("c"+(i+1), exprHeader.get(i));
		}
		commonMybatisDao.insert("pharmai.formulation.insertFormulaStp_05", param);

		//experimentData
		int exprLength = Integer.parseInt(param.getString("exprLength"));
		for(int i = 0; i< exprLength; i++) {
			List exprList = param.getList("exprVal_"+i);
			for(int j = 0; j< exprList.size(); j++) {
				param.put("c"+(j+1), exprList.get(j));
			}
			commonMybatisDao.insert("pharmai.formulation.insertFormulaStp_05", param);
		}

		// 중간저장 후 next 버튼 클릭 시 csv가 생성되지 않는 문제 해결
		List exprDataList = commonMybatisDao.selectList("pharmai.formulation.selectListFormulaStp_05", param);

		File file = null;
		BufferedWriter bufferWriter = null;

		String filePath = EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation") + param.getString("prjct_id") + "/api6/csv/";
		String fileName = "api6_experiment.csv";

		File saveFolder = new File(EgovWebUtil.filePathBlackList(filePath));

		//파일 경로에 폴더가 없으면 폴더생성
		if (!saveFolder.exists() || saveFolder.isFile()) {
			saveFolder.mkdirs();
		}

		file = new File(filePath + fileName);

		bufferWriter = new BufferedWriter(new FileWriter(file));

		DataMap data = new DataMap();
		int selectStp3TotYn = experimentService.selectStp3TotYn(param);

		for(int i = 0; i< exprDataList.size(); i++) {
			data = (DataMap) exprDataList.get(i);
			for(int j = 1 + selectStp3TotYn; j <= 13; j++) {
				String currentData = data.getString("C" + j);
				if(!"".equals(currentData)) {
					bufferWriter.append(currentData);
					String nextData = data.getString("C" + (j+1));
					if(!"".equals(nextData)) {
						bufferWriter.append(",");
					}
				}
			}

			bufferWriter.newLine();
		}


		bufferWriter.flush();
		bufferWriter.close();

		String doc_id = StringUtil.nvl(param.getString("doc_id"), SysUtil.getDocId());

		NtsysFileVO fvo = new NtsysFileVO();
		fvo.setFileId(SysUtil.getFileId());
		fvo.setDocId(doc_id);
		fvo.setFileRmk("");
		fvo.setFileNm(fileName);
		fvo.setFileRltvPath(EgovPropertiesUtil.getProperty("Globals.fileWebrootPath.formulation") + param.getString("prjct_id") + "/api6/csv/");//상대경로
		fvo.setFileAsltPath(filePath); //절대경료
		fvo.setFileSize(file.length());
		fvo.setSsUserId(param.getString("ss_user_no"));
		fvo.setContentType("application/csv");
		fvo.setFileExtNm("csv");

		commonMybatisDao.insert("common.file.insertAttchFile", fvo);
		
	 	//INSERT시 CHANGE
    	String step = param.getString("status");
    	int plusCnt = Integer.parseInt(step.substring(step.length()-1, step.length()));
    	param.put("step_cd", "0" + plusCnt);

    	commonMybatisDao.update("pharmai.formulation.updateMstStep", param);


	}


}
