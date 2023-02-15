/**
 *
 *
 * 1. FileName : pageNavigationUtil.java
 * 2. Package : egovframework.framework.common.util
 * 3. Comment :
 * 4. 작성자  : pjh
 * 5. 작성일  : 2013. 8. 26. 오후 1:45:02
 * 6. 변경이력 :
 *    이름     : 일자          : 근거자료   : 변경내용
 *    ------------------------------------------------------
 *    pjh : 2013. 8. 26. :            : 신규 개발.
 */

package egovframework.framework.common.page.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.ModelMap;

import egovframework.framework.common.constant.Const;
import egovframework.framework.common.object.DataMap;
import egovframework.framework.common.page.vo.pageNavigationVo;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : pageNavigationUtil.java
 * 3. Package  : egovframework.framework.common.util
 * 4. Comment  :
 * 5. 작성자   : pjh
 * 6. 작성일   : 2013. 8. 26. 오후 1:45:02
 * </PRE>
 */

 public class pageNavigationUtil {

	private static Log log = LogFactory.getLog(pageNavigationUtil.class);

	/**
	 * <PRE>
	 * 1. MethodName : pageNavigationUtil
	 * 2. ClassName  : pageNavigationUtil
	 * 3. Comment   :
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 8. 26. 오후 1:45:02
	 * </PRE>
	 */
	public pageNavigationUtil() {
		// TODO Auto-generated constructor stub
	}

	public static DataMap createNavigationInfo(ModelMap model, DataMap dataMap) {

		pageNavigationVo pnv = new pageNavigationVo();

		// 임의의 currentPage input name을 설정
		pnv.setPageInputName(dataMap.getString("pageInputName", "currentPage"));
		pnv.setPageCallFunction(dataMap.getString("pageCallFunction", "fnGoPage"));

		pnv.setTotalCount(dataMap.getInt("totalCount"));

		// 디폴트 한 페이지 당 컨텐츠 갯수
//		pnv.setRowPerPage(Integer.parseInt(dataMap.getString("rowPerPage", Const.DEF_ROW_PER_PAGE)));

		// selectBox 에서 선택된 한 페이지 당 컨텐츠 갯수
		pnv.setRowPerPage(Integer.parseInt(dataMap.getString("rowPerPage", dataMap.getString("sch_row_per_page", Const.DEF_ROW_PER_PAGE))));

		pnv.setCurrentPage(Integer.parseInt(dataMap.getString(pnv.getPageInputName(), Const.DEF_CURRENT_PAGE)));
		pnv.setNaviCount(Integer.parseInt(dataMap.getString("naviCount", Const.DEF_NAVI_COUNT_05)));

		int lastPage = pnv.getTotalCount() / pnv.getRowPerPage();
		int dummyPage = 0;
		if (pnv.getTotalCount() % pnv.getRowPerPage() > 0) { //나머지가 존재할경우 1페이지 추가
			dummyPage = 1;
		}
		pnv.setLastPage(lastPage + dummyPage);
		int plusPage = pnv.getCurrentPage() % pnv.getNaviCount() == 0 ? -1 * pnv.getNaviCount() + 1 : 1;
		pnv.setFirstPage(pnv.getCurrentPage() / pnv.getNaviCount() * pnv.getNaviCount() + plusPage);
		pnv.setCurrDataNo(pnv.getTotalCount() - ((pnv.getCurrentPage() - 1) * pnv.getRowPerPage()));

		model.addAttribute("pageNavigationVo", pnv);

		dataMap.put("limitStart", (pnv.getCurrentPage() - 1) * pnv.getRowPerPage());
		dataMap.put("limitCount", pnv.getRowPerPage());

		// mysql 경우 limit 이용시 끝에는 한페이지에 보여줄 게시물수만 있으면 된다.
		dataMap.put("limitEnd", pnv.getRowPerPage());

		String naviBar = createNavigationBar(pnv);
		// 페이지 관련 태그 스트링 등록
		model.addAttribute("navigationBar", naviBar);
		dataMap.put("navigationBar", naviBar);

		return dataMap;
	}

	public static String createNavigationBar(pageNavigationVo pnv) {

		StringBuffer rtnStr = new StringBuffer();
		int nextPage = 0;

//		<nav aria-label="Page navigation example">
//			<ul class="pagination">
//			<li class="page-item">
//			<a class="page-link" href="#" aria-label="Previous">
//			<span aria-hidden="true">&laquo;</span>
//			</a>
//			</li>
//			<li class="page-item"><a class="page-link" href="#">1</a></li>
//			<li class="page-item"><a class="page-link" href="#">2</a></li>
//			<li class="page-item"><a class="page-link" href="#">3</a></li>
//			<li class="page-item">
//			<a class="page-link" href="#" aria-label="Next">
//			<span aria-hidden="true">&raquo;</span>
//			</a>
//			</li>
//			</ul>
//			</nav>

		if (pnv.getTotalCount() > 0) {
			rtnStr.append("<nav aria-label=\"Page navigation\">");
			rtnStr.append("<ul class=\"pagination pagination-sm no-margin justify-content-center\">");

			// 기존 코드
			if (pnv.getFirstPage() + pnv.getNaviCount() > pnv.getLastPage()) {
				nextPage = pnv.getLastPage() + 1;
			} else {
				nextPage = pnv.getFirstPage() + pnv.getNaviCount();
			}
			// 수정
//			if (pnv.getLastPage() > 1) {
//				nextPage = pnv.getLastPage();
//			} else {
//				nextPage = pnv.getLastPage() + 1;
//			}

			rtnStr.append("<li class=\"page-item\"><a href=\"#\" class=\"page-link\" title=\"맨앞으로가기\" onclick=\"" + pnv.getPageCallFunction() + "('1'); return false;\">&laquo;</a></li>");

			if (pnv.getFirstPage() > pnv.getNaviCount()) {
				rtnStr.append("<li class=\"page-item\"><a href=\"#\" class=\"page-link\" title=\"앞으로가기\" onclick=\"" + pnv.getPageCallFunction() + "('" + (pnv.getFirstPage() - 1)
						+ "'); return false;\">&lt;</a></li>");
			}
			//			else
			//			{
			//				rtnStr.append("<li class=\"ui-state-default ui-state-disabled\"><span class=\"ui-icon ui-icon-seek-prev\"></span></li>");
			//			}

			for (int i = pnv.getFirstPage(); i < nextPage; i++) {
				if (pnv.getCurrentPage() == i) {
					rtnStr.append("<li class=\"active page-item\"><a href=\"#\" class=\"page-link\" onclick=\"" + pnv.getPageCallFunction() + "('" + i + "'); return false;\">" + i + "</a></li>");
				} else {
					rtnStr.append("<li class=\"page-item\"><a href=\"#\" class=\"page-link\" onclick=\"" + pnv.getPageCallFunction() + "('" + i + "'); return false;\">" + i + "</a></li>");
				}
			}

			if (pnv.getFirstPage() + pnv.getNaviCount() - 1 < pnv.getLastPage()) {
				rtnStr.append("<li class=\"page-item\"><a href=\"#\" class=\"page-link\" title=\"뒤로가기\" onclick=\"" + pnv.getPageCallFunction() + "('" + (pnv.getFirstPage() + pnv.getNaviCount())
						+ "'); return false;\">&gt;</a></li>");
			}
			/*else
			{
				out.print("<li class=\"ui-state-default ui-state-disabled\"><span class=\"ui-icon ui-icon-seek-next\"></span></li>");
			}*/

			rtnStr.append("<li class=\"page-item\"><a href=\"#\" class=\"page-link\" title=\"맨뒤로가기\" onclick=\"" + pnv.getPageCallFunction() + "('" + pnv.getLastPage()
					+ "'); return false;\">&raquo;</a></li>");
			rtnStr.append("</ul>");
			rtnStr.append("</nav>");

			rtnStr.append("<input type=\"hidden\" name=\"" + pnv.getPageInputName() + "\" id=\"" + pnv.getPageInputName() + "\" value=\"" + pnv.getCurrentPage() + "\"/>");
		}

		log.debug("#######rtnStr.toString():"+rtnStr.toString());
		return rtnStr.toString();
	}




	public static DataMap createNavigationInfo2(ModelMap model, DataMap dataMap) {

		pageNavigationVo pnv = new pageNavigationVo();

		// 임의의 currentPage input name을 설정
		pnv.setPageInputName(dataMap.getString("pageInputName", "currentPage"));
		pnv.setPageCallFunction(dataMap.getString("pageCallFunction", "fnGoPage"));

		pnv.setTotalCount(dataMap.getInt("totalCount"));
		pnv.setRowPerPage(Integer.parseInt(dataMap.getString("rowPerPage", Const.DEF_ROW_PER_PAGE)));
		pnv.setCurrentPage(Integer.parseInt(dataMap.getString(pnv.getPageInputName(), Const.DEF_CURRENT_PAGE)));
		pnv.setNaviCount(Integer.parseInt(dataMap.getString("naviCount", Const.DEF_NAVI_COUNT_05)));

		int lastPage = pnv.getTotalCount() / pnv.getRowPerPage();
		int dummyPage = 0;
		if (pnv.getTotalCount() % pnv.getRowPerPage() > 0) { //나머지가 존재할경우 1페이지 추가
			dummyPage = 1;
		}
		pnv.setLastPage(lastPage + dummyPage);
		int plusPage = pnv.getCurrentPage() % pnv.getNaviCount() == 0 ? -1 * pnv.getNaviCount() + 1 : 1;
		pnv.setFirstPage(pnv.getCurrentPage() / pnv.getNaviCount() * pnv.getNaviCount() + plusPage);
		pnv.setCurrDataNo(pnv.getTotalCount() - ((pnv.getCurrentPage() - 1) * pnv.getRowPerPage()));

		model.addAttribute("pageNavigationVo", pnv);

		dataMap.put("limitStart", (pnv.getCurrentPage() - 1) * pnv.getRowPerPage());
		dataMap.put("limitCount", pnv.getRowPerPage());

		// mysql 경우 limit 이용시 끝에는 한페이지에 보여줄 게시물수만 있으면 된다.
		dataMap.put("limitEnd", pnv.getRowPerPage());

		String naviBar = createNavigationBar2(pnv);
		// 페이지 관련 태그 스트링 등록
		model.addAttribute("navigationBar", naviBar);
		dataMap.put("navigationBar", naviBar);

		return dataMap;
	}

	public static String createNavigationBar2(pageNavigationVo pnv) {

		StringBuffer rtnStr = new StringBuffer();
		int nextPage = 0;

		if (pnv.getTotalCount() > 0) {

			rtnStr.append("<ul>");

			if (pnv.getFirstPage() + pnv.getNaviCount() > pnv.getLastPage()) {
				nextPage = pnv.getLastPage() + 1;
			} else {
				nextPage = pnv.getFirstPage() + pnv.getNaviCount();
			}


			//##### 맨앞으로 가기 버튼 생성 #################/
			rtnStr.append("<li class=\"arrow02 prev02\"><a href=\"#\" title=\"맨앞으로가기\" onclick=\"" + pnv.getPageCallFunction() + "('1'); return false;\">맨앞</a></li>");


			//##### 앞으로 가기 버튼 생성 #################/
			if (pnv.getFirstPage() > pnv.getNaviCount()) {
				rtnStr.append("<li class=\"arrow prev\"><a href=\"#\" title=\"앞으로가기\" onclick=\"" + pnv.getPageCallFunction() + "('" + (pnv.getFirstPage() - 1) + "'); return false;\">이전</a></li>");
			}


			for (int i = pnv.getFirstPage(); i < nextPage; i++) {
				if (pnv.getCurrentPage() == i) {
					rtnStr.append("<li class=\"on\"><a href=\"#\" onclick=\"" + pnv.getPageCallFunction() + "('" + i + "'); return false;\">" + i + "</a></li>");
				} else {
					rtnStr.append("<li><a href=\"#\" onclick=\"" + pnv.getPageCallFunction() + "('" + i + "'); return false;\">" + i + "</a></li>");
				}
			}

			//##### 뒤로 가기 버튼 생성 #################/
			if (pnv.getFirstPage() + pnv.getNaviCount() - 1 < pnv.getLastPage()) {
				rtnStr.append("<li class=\"arrow next\"><a href=\"#\" title=\"뒤로가기\" onclick=\"" + pnv.getPageCallFunction() + "('" + (pnv.getFirstPage() + pnv.getNaviCount()) + "'); return false;\">다음</a></li>");
			}

			//##### 맨뒤로 가기 버튼 생성 #################/
			rtnStr.append("<li class=\"arrow02 next02\"><a href=\"#\" title=\"맨뒤로가기\" onclick=\"" + pnv.getPageCallFunction() + "('" + pnv.getLastPage() + "'); return false;\">맨뒤</a></li>");


			rtnStr.append("</ul>");

			rtnStr.append("<input type=\"hidden\" name=\"" + pnv.getPageInputName() + "\" id=\"" + pnv.getPageInputName() + "\" value=\"" + pnv.getCurrentPage() + "\"/>");
		}

		return rtnStr.toString();
	}
}
