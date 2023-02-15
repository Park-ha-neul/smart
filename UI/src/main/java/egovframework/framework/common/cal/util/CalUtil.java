/**
 * 0. Project  : baseProject
 *
 * 1. FileName : CalUtil.java
 * 2. Package : egovframework.framework.common.cal.util
 * 3. Comment :
 * 4. 작성자  : jjh
 * 5. 작성일  : 2013. 8. 26. 오후 1:45:02
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    jjh 	: 2013. 12. 23. :            : 신규 개발.
 */

package egovframework.framework.common.cal.util;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.ModelMap;

import egovframework.framework.common.cal.vo.CalVo;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.util.StringUtil;


public class CalUtil {

	private static Log log = LogFactory.getLog(CalUtil.class);

	/**
	 * <PRE>
	 * 1. MethodName : CalUtil
	 * 2. ClassName  : CalUtil
	 * 3. Comment   :
	 * 4. 작성자    : ntsys
	 * 5. 작성일    : 2013. 12. 23. 오전 11:18:22
	 * </PRE>
	 */
	public CalUtil() {
		// TODO Auto-generated constructor stub
	}

	public static DataMap createCalInfo(ModelMap model, DataMap dataMap){

		CalVo cnv=new CalVo();

		Calendar now = Calendar.getInstance();

		// 오늘 날짜 셋팅
		cnv.setCurYear(now.get(Calendar.YEAR));
		cnv.setCurMonth(now.get(Calendar.MONTH) + 1);
		cnv.setCurDay(now.get(Calendar.DAY_OF_MONTH));
		cnv.setCurWeek(now.get(Calendar.DAY_OF_WEEK));
		cnv.setCurLastDay(now.getActualMaximum(Calendar.DAY_OF_MONTH));


		// 설정 날짜 셋팅(설정값이 있으면 그것으로 셋팅)
		cnv.setViewYear(Integer.parseInt(StringUtil.nvl(dataMap.getString("cal_year"), String.valueOf(cnv.getCurYear()))));
		cnv.setViewMonth(Integer.parseInt(StringUtil.nvl(dataMap.getString("cal_month"), String.valueOf(cnv.getCurMonth()))));
//		cnv.setViewDay(Integer.parseInt(StringUtil.nvl(dataMap.getString("cal_day"), String.valueOf(cnv.getCurDay()))));

		// calendar 설정
//		now.set(cnv.getViewYear(), cnv.getViewMonth(), cnv.getViewDay());
		now.set(cnv.getViewYear(), cnv.getViewMonth() - 1, 1);

		cnv.setViewWeek(now.get(Calendar.DAY_OF_WEEK));
		cnv.setViewLastDay(now.getActualMaximum(Calendar.DAY_OF_MONTH));

		String calTable = createCalTable(cnv);

		model.addAttribute("calVo", cnv);
		model.addAttribute("calTable", calTable);

		return dataMap;
	}

	public static String createCalTable(CalVo cnv){

		StringBuffer rtnStr=new StringBuffer();
		int newLine = 0;
		int totalLine = 0;
		int curLine = 1;

		// 해당 월의 초기날짜 셋팅
		Calendar cal = Calendar.getInstance();
		// 마지막 날짜 셋팅
		cal.set(cnv.getViewYear(), cnv.getViewMonth() - 1, cnv.getViewLastDay());
		// 해당달의 총 주차
		totalLine = cal.get(Calendar.WEEK_OF_MONTH);

		// 첫날짜 셋팅
		cal.set(cnv.getViewYear(), cnv.getViewMonth() - 1, 1);

		rtnStr.append("<table>");
		rtnStr.append("	<caption>요일별 일정</caption>");
		rtnStr.append("	<thead>");
		rtnStr.append("		<tr>");
		rtnStr.append("			<th>일</th><th>월</th><th>화</th><th>수</th><th>목</th><th>금</th><th>토</th>");
		rtnStr.append("		</tr>");
		rtnStr.append("	</thead>");
		rtnStr.append("	<tbody>");
		rtnStr.append("		<tr>");

		// 1일이 어느 요일인지에 따라 빈칸 채우기
		for(int i = 1; i < cal.get(Calendar.DAY_OF_WEEK); i++){
			rtnStr.append("			<td></td>");
			newLine++;
		}

		// 1일 부터 마지막날까지 출력
		for(int i = 1; i <= cnv.getViewLastDay(); i++){
			String onDate = "";
			String satDate = "";
			String sunDate = "";

			// 오늘 날짜와 보여줄 달력의 날짜가 일치할경우 class 추가.
			if(cnv.getCurYear() == cnv.getViewYear() && cnv.getCurMonth() == cnv.getViewMonth() && cnv.getCurDay() == i){ onDate = "cal_on"; }
			// 토요일일 경우
			if(newLine == 6){ satDate = "cal_sat"; }
			// 일요일일 경우
			if(newLine == 7 || newLine == 0){ sunDate = "cal_sun"; }

			// 한줄을 다 채울경우 한줄 내림.
			if(newLine == 7){
				rtnStr.append("			</tr>");
				// 마지막 주차가 아닐경우
				if(totalLine != curLine){
					rtnStr.append("			<tr>");
					curLine++;
				}
				newLine = 0;
			}

			rtnStr.append("			<td class=\"" + onDate + " " + satDate + " " + sunDate + "\">");
			rtnStr.append("				<ul>");
			rtnStr.append("					<li>" + i + "</li>");
			rtnStr.append("				</ul>");
			rtnStr.append("			</td>");

			newLine++;
		}

		// 마지막주에 끝이 남을경우에 마지막 뒤에 빈칸 채워줌.
		if(newLine != 7){
			for(int i = newLine; i <= 7; i++){
				rtnStr.append("			<td></td>");
			}
			rtnStr.append("			</tr>");
		}
		rtnStr.append("	</tbody>");
		rtnStr.append("</table>");

		return rtnStr.toString();
	}

	public static DataMap createCalInfoScdl(ModelMap model, DataMap dataMap, List<Map<String, Object>> list){

		CalVo cnv=new CalVo();

		Calendar now = Calendar.getInstance();

		// 오늘 날짜 셋팅
		cnv.setCurYear(now.get(Calendar.YEAR));
		cnv.setCurMonth(now.get(Calendar.MONTH) + 1);
		cnv.setCurDay(now.get(Calendar.DAY_OF_MONTH));
		cnv.setCurWeek(now.get(Calendar.DAY_OF_WEEK));
		cnv.setCurLastDay(now.getActualMaximum(Calendar.DAY_OF_MONTH));


		// 설정 날짜 셋팅(설정값이 있으면 그것으로 셋팅)
		cnv.setViewYear(Integer.parseInt(StringUtil.nvl(dataMap.getString("cal_year"), String.valueOf(cnv.getCurYear()))));
		cnv.setViewMonth(Integer.parseInt(StringUtil.nvl(dataMap.getString("cal_month"), String.valueOf(cnv.getCurMonth()))));
//		cnv.setViewDay(Integer.parseInt(StringUtil.nvl(dataMap.getString("cal_day"), String.valueOf(cnv.getCurDay()))));

		// calendar 설정
//		now.set(cnv.getViewYear(), cnv.getViewMonth(), cnv.getViewDay());
		now.set(cnv.getViewYear(), cnv.getViewMonth() - 1, 1);

		cnv.setViewWeek(now.get(Calendar.DAY_OF_WEEK));
		cnv.setViewLastDay(now.getActualMaximum(Calendar.DAY_OF_MONTH));

		String calTable = createCalTableScdl(cnv,dataMap,list);

		model.addAttribute("calVo", cnv);
		model.addAttribute("calTable", calTable);

		return dataMap;
	}

	public static String createCalTableScdl(CalVo cnv,DataMap dataMap, List<Map<String, Object>> list){

		StringBuffer rtnStr=new StringBuffer();
		int newLine = 0;
		int totalLine = 0;
		int curLine = 1;

		// 해당 월의 초기날짜 셋팅
		Calendar cal = Calendar.getInstance();
		// 마지막 날짜 셋팅
		cal.set(cnv.getViewYear(), cnv.getViewMonth() - 1, cnv.getViewLastDay());
		// 해당달의 총 주차
		totalLine = cal.get(Calendar.WEEK_OF_MONTH);
		// 첫날짜 셋팅
		cal.set(cnv.getViewYear(), cnv.getViewMonth() - 1, 1);

		//오늘 날짜 년 월 생성
		/*SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String retStr = dateFormat.format(cal.getTime());*/

		rtnStr.append("<table class=\"cal\">");
		rtnStr.append("	<caption>요일별 일정</caption>");
		rtnStr.append("	<thead>");
		rtnStr.append("		<tr>");
		rtnStr.append("			<th class=\"sun\">일</th><th>월</th><th>화</th><th>수</th><th>목</th><th>금</th><th class=\"sat\">토</th>");
		rtnStr.append("		</tr>");
		rtnStr.append("	</thead>");
		rtnStr.append("	<tbody>");
		rtnStr.append("		<tr>");

		// 1일이 어느 요일인지에 따라 빈칸 채우기
		for(int i = 1; i < cal.get(Calendar.DAY_OF_WEEK); i++){
			rtnStr.append("			<td></td>");
			newLine++;
		}

		// 날짜 선택하여 작성 할수있는지에 대한 여부 확인
		boolean ssSchCodeOk = false;
		boolean chkTap = false;
		String ssSchCode = dataMap.getString("ss_user_sch_code");
		if(ssSchCode.equals("10") || ssSchCode.equals("20") || ssSchCode.equals("30") || ssSchCode.equals("70")){
			ssSchCodeOk = true;
		}
		if(dataMap.getString("mc").equals("R040020020") && (ssSchCode.equals("10") || ssSchCode.equals("20") || ssSchCode.equals("30")|| ssSchCode.equals("70"))){
			chkTap = true;
		}else{
			if(ssSchCode.equals("10") && dataMap.getString("mc").equals("R040020010010")){
				chkTap = true;
			}
			if(ssSchCode.equals("20") && dataMap.getString("mc").equals("R040020010020")){
				chkTap = true;
			}
			if(ssSchCode.equals("30") && dataMap.getString("mc").equals("R040020010030")){
				chkTap = true;
			}
			if(ssSchCode.equals("70") && dataMap.getString("mc").equals("R040020010040")){
				chkTap = true;
			}
		}

		// 1일 부터 마지막날까지 출력
		for(int i = 1; i <= cnv.getViewLastDay(); i++){
			String onDate = "";
			String satDate = "";
			String sunDate = "";

			// 오늘 날짜와 보여줄 달력의 날짜가 일치할경우 class 추가.
			if(cnv.getCurYear() == cnv.getViewYear() && cnv.getCurMonth() == cnv.getViewMonth() && cnv.getCurDay() == i){ onDate = "on"; }
			// 토요일일 경우
			if(newLine == 6){ satDate = "sat"; }
			// 일요일일 경우
			if(newLine == 7 || newLine == 0){ sunDate = "sun"; }

			// 한줄을 다 채울경우 한줄 내림.
			if(newLine == 7){
				rtnStr.append("			</tr>");
				// 마지막 주차가 아닐경우
				if(totalLine != curLine){
					rtnStr.append("			<tr>");
					curLine++;
				}
				newLine = 0;
			}

			//오늘날짜 파라메터 생성
			String day = "";
			if(i < 10){
				day = "0"+i;
			}else{
				day = ""+i;
			}

			rtnStr.append("			<td class=\"" + onDate + " " + satDate + " " + sunDate + "\">");
			rtnStr.append("				<ul  class=\"plan_list\">");

			//소속기관에 맟게 날짜 클릭시 등록 가능여부 분기
			if(ssSchCodeOk){
				if(chkTap){
					rtnStr.append("					<li><a href=\"#\" class=\"" + onDate + " " + satDate + " " + sunDate + "\" onclick=\"goWorkscdlInsetFormCal(\'"+day+"\');\">" + i + "</a></li>");
				}else{
					rtnStr.append("					<li class=\"" + onDate + " " + satDate + " " + sunDate + "\">" + i + "</li>");
				}
			}else{
				rtnStr.append("					<li class=\"" + onDate + " " + satDate + " " + sunDate + "\">" + i + "</li>");
			}

			//###조회한 리스트에서 해당 날짜의 데이터 값만 표시해준다###
			int flag = 0; //전체 리스트 갯수 합
			int flag1 = 0; //보여지는 리스트 갯수 합
			String defaultMc = "";
			for (int j = 0; j < list.size(); j++) {

				String sDate = (String) list.get(j).get("START_YMD");
				int sDateInt = Integer.parseInt(sDate.substring(6, 8));

				String eDate = (String) list.get(j).get("END_YMD");
				int eDateInt = Integer.parseInt(eDate.substring(6, 8));

				String content = (String) list.get(j).get("TTL");
				String gubun = (String) list.get(j).get("WORK_SCDL_DIV_CODE");
				String workSeq = list.get(j).get("WORK_SCDL_SEQ").toString();
				String mc = (String) list.get(j).get("MC");
				defaultMc = mc;

				try {
					if(dataMap.getString("flag").equals("main")){
						content = StringUtil.getReSizeRemoveDot(content, 5);
					}else{
						content = StringUtil.getReSizeRemoveDot(content, 7);
					}
				} catch (Exception e) {
					log.error("######### 예외 발생3 ##########");
				}
				/*if(i == sDateInt){*/
					if(flag1 < 3){
						if(gubun.equals("U")){
							if(sDateInt <= i && eDateInt >= i){
								rtnStr.append("					<li class=\"per\"><a href=\"#\" class=\""+ onDate + " " + satDate + " " + sunDate +"\" onclick=\"goWorkscdlDetail(\'"+ workSeq+"\',\'"+day+"\',\'"+mc+"\');\">" + content + "</a></li>");
								flag++;
								flag1++;
							}
						}else if(gubun.equals("A")){
							if(i == eDateInt){
								rtnStr.append("					<li class=\"all\"><a href=\"#\" class=\""+ onDate + " " + satDate + " " + sunDate +"\" onclick=\"goWorkscdlDetail(\'"+ workSeq+"\',\'"+day+"\',\'"+mc+"\');\">" + content + "</a></li>");
								flag++;
								flag1++;
							}else if(sDateInt <= i && eDateInt >= i){
								flag++;
							}
						}
					}else {
						if(sDateInt <= i && eDateInt >= i){
							flag++;
						}
					}
				/*}*/


				/*if(i == sDateInt){
					if(flag < 3){
						if(sDateInt == eDateInt){
							if(gubun.equals("U")){
								rtnStr.append("					<li class=\"per\"><a href=\"#\" class=\""+ on_date + " " + sat_date + " " + sun_date +"\" onclick=\"goWorkscdlDetail(\'"+ workSeq+"\',\'"+day+"\',\'"+mc+"\');\">" + content + "</a></li>");
								flag++;
							}else if(gubun.equals("A")){
								rtnStr.append("					<li class=\"all\"><a href=\"#\" class=\""+ on_date + " " + sat_date + " " + sun_date +"\" onclick=\"goWorkscdlDetail(\'"+ workSeq+"\',\'"+day+"\',\'"+mc+"\');\">" + content + "</a></li>");
								flag++;
							}
						}else{
							if(gubun.equals("U")){
								rtnStr.append("					<li class=\"per\"><a href=\"#\" class=\""+ on_date + " " + sat_date + " " + sun_date +"\" onclick=\"goWorkscdlDetail(\'"+ workSeq+"\',\'"+day+"\',\'"+mc+"\');\">" + content + "</a></li>");
								flag++;
							}else if(gubun.equals("A")){
								rtnStr.append("					<li class=\"all\"><a href=\"#\" class=\""+ on_date + " " + sat_date + " " + sun_date +"\" onclick=\"goWorkscdlDetail(\'"+ workSeq+"\',\'"+day+"\',\'"+mc+"\');\">" + content + "</a></li>");
								flag++;
							}

						}
					}else{
				    	flag++;
				    }
				}
				if(i == eDateInt){
					if(flag < 3){
						if(sDateInt != eDateInt){
							if(gubun.equals("U")){
								rtnStr.append("					<li class=\"per\"><a href=\"#\" class=\""+ on_date + " " + sat_date + " " + sun_date +"\" onclick=\"goWorkscdlDetail(\'"+ workSeq+"\',\'"+day+"\',\'"+mc+"\');\">" + content + "</a></li>");
								flag++;
							}else if(gubun.equals("A")){
								rtnStr.append("					<li class=\"all\"><a href=\"#\" class=\""+ on_date + " " + sat_date + " " + sun_date +"\" onclick=\"goWorkscdlDetail(\'"+ workSeq+"\',\'"+day+"\',\'"+mc+"\');\">" + content + "</a></li>");
								flag++;
							}
						}
					}else{
				    	flag++;
				    }
				}*/

			}
			//#########################################
			if(flag > 3){
				rtnStr.append("				</ul>");
				rtnStr.append(" <div class=\"icon_more\"><a href=\"#\" onclick=\"goWorkscdlDetail(\'all\',\'"+day+"\',\'"+defaultMc+"\');\"><img src=\"/common/seoul_images/icon_more.png\" alt=\"더보기\" /> </a> </div>");
			}else{
				rtnStr.append("				</ul>");
			}
			rtnStr.append("			</td>");

			newLine++;
		}

		// 마지막주에 끝이 남을경우에 마지막 뒤에 빈칸 채워줌.
		if(newLine != 7){
			for(int i = newLine; i <= 6; i++){
				rtnStr.append("			<td></td>");
			}
			rtnStr.append("			</tr>");
		}
		rtnStr.append("	</tbody>");
		rtnStr.append("</table>");

		return rtnStr.toString();
	}

}
