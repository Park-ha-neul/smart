package egovframework.framework.common.util;

import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class StringUtil {

	private static Log log = LogFactory.getLog(StringUtil.class);

	/**
	*
	* @param src
	* @param defaultValue
	* @return String
	*/
	public static String nvl(String src, String defaultValue) {
		return (src == null || "".equals(src.trim())) ? defaultValue : src;
	}

	/**
	*
	* @param src
	* @return String
	*/
	public static String nvl(String src) {
		return nvl(src, "");
	}

	/**
	 * 문자 길이 자르기
	 *
	 * @param contents
	 * @param length
	 * @return String
	 * @throws LMailException
	 */
	public static String getReSize(String contents, int length) throws Exception {

		if (contents != null && contents.length() > length) {
			contents = contents.substring(0, length) + "...";
		}

		return contents;
	}

	/**
	 * 문자 길이 자르기 (말줄임표 없음)
	 *
	 * @param contents
	 * @param length
	 * @return String
	 * @throws LMailException
	 */
	public static String getReSizeRemoveDot(String contents, int length) throws Exception {

		if (contents != null && contents.length() > length) {
			contents = contents.substring(0, length);
		}

		return contents;
	}

	public static String toEng(String str) {
		if (str == null)
			return null;
		try {
			return new String(str.getBytes("KSC5601"), "8859_1");
		} catch (UnsupportedEncodingException e) {

			return str;
		}
	}

	public static String toKor(String input) {

		String str = "";
		try {
			str = new String(input.getBytes("8859_1"), "KSC5601");
		} catch (UnsupportedEncodingException e) {
//			System.out.println("######### 예외 발생 ##########");
			log.error("######### 예외 발생 ##########");

		}

		return str;
	}

	/**
	 * 개행문자 처리
	 *
	 * @param strOriData (개행처리할 문자열)
	 * @return String (개행처리된 문자열)
	 */
	public static String getHtmlValue(String strOriData) {
		StringBuffer buffer = new StringBuffer();

		for (int i = 0; i < strOriData.length(); i++) {
			//System.out.println("$$$$$:"+strOriData.charAt(i)+":"+String.valueOf(strOriData.charAt(i)).hashCode());
			if (String.valueOf(strOriData.charAt(i)).hashCode() == 10 || String.valueOf(strOriData.charAt(i)).hashCode() == 13) {
				buffer.append("<br/>");
			} else {
				buffer.append(String.valueOf(strOriData.charAt(i)));
			}
		}
		return buffer.toString();
	}

	/**
	 * 전자결재를 위한 개행문자 처리 & 특수문자 처리
	 *
	 * @param strOriData (개행처리할 문자열)
	 * @return String (개행처리된 문자열)
	 */
	public static String newLineForEapprov(String strOriData) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < strOriData.length(); i++) {

			int intCode = String.valueOf(strOriData.charAt(i)).hashCode();
			if (intCode == 10 || intCode == 13) {
				buffer.append("\\\\r\\\\n");
				i++;
			} else if (intCode == 34) // ["]
			{
				buffer.append("\\\"");
			} else if (intCode == 39 || intCode == 44) // [']
			{
				buffer.append("\\\'");
			} else {
				buffer.append(String.valueOf(strOriData.charAt(i)));
			}
		}
		String strReturn = buffer.toString();

		return strReturn;
	}

	/**
	 * <PRE>
	 * 1. MethodName : toHtmlFormatDataMap
	 * 2. ClassName  : StringUtil
	 * 3. Comment   : dataMap 에서 사용하는 htmlTagFilterRestore 를 반대로 적용한것
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 8. 12. 오전 11:25:08
	 * </PRE>
	 *   @return String
	 *   @param src
	 *   @return
	 */
	public static String toHtmlFormatDataMap(String src) {

		if (src != null) {
			src = src.replaceAll("&", "&amp;");
			src = src.replaceAll("<", "&lt;");
			src = src.replaceAll(">", "&gt;");
			src = src.replaceAll("\"", "&quot;");
			src = src.replaceAll("\'", "&#039;");
		}
		return src;
	}

	/**
	 * HTHML태그들을 브라우져에 표현되도록 인코딩하는 기능
	 * @return String
	 */
	public static String toHtmlFormat(String src) {

		if (src != null) {
			src = src.replaceAll("&", "&amp;");
			src = src.replaceAll("<", "&lt;");
			src = src.replaceAll(">", "&gt;");
			src = src.replaceAll("\n", "<br/>");
			src = src.replaceAll(" ", "&nbsp;");
			/* 2010.03.02 by pjh
			src = src.replaceAll("·","&#8228;");
			src = src.replaceAll("「","&#65378;");
			src = src.replaceAll("」","&#65379;");
			src = src.replaceAll("ㆍ","&#9702;");
			src = src.replaceAll("⇒","&#8680;");
			src = src.replaceAll("●","&#8226;");
			src = src.replaceAll("▶","&#983803;");
			src = src.replaceAll("①","&#983729;");
			src = src.replaceAll("②","&#983730;");
			src = src.replaceAll("③","&#983731;");
			src = src.replaceAll("④","&#983732;");
			src = src.replaceAll("⑤","&#983733;");
			src = src.replaceAll("⑥","&#983734;");
			src = src.replaceAll("⑦","&#983735;");
			src = src.replaceAll("⑧","&#983736;");
			src = src.replaceAll("⑨","&#983737;");
			//src = src.replaceAll("○","&#61549;");
			src = src.replaceAll("\"","&#034;");
			src = src.replaceAll("\'","&#039;");
			*/
		}
		return src;
	}

	/**
	 * HTHML태그들을 브라우져에 표현되도록 인코딩하는 기능
	 * @return String
	 */
	public static String toHtmlFormat_AZ(String src) {
		src = src.replaceAll("&", "&amp;");
		src = src.replaceAll("<", "&lt;");
		src = src.replaceAll(">", "&gt;");
		src = src.replaceAll("\n", "<br/>");
		src = src.replaceAll(" ", "&nbsp;");
		src = src.replaceAll("·", "&#8228;");
		src = src.replaceAll("「", "&#65378;");
		src = src.replaceAll("」", "&#65379;");
		src = src.replaceAll("ㆍ", "&#9702;");
		src = src.replaceAll("⇒", "&#8680;");
		src = src.replaceAll("●", "&#8226;");
		src = src.replaceAll("▶", "&#983803;");
		src = src.replaceAll("①", "&#983729;");
		src = src.replaceAll("②", "&#983730;");
		src = src.replaceAll("③", "&#983731;");
		src = src.replaceAll("④", "&#983732;");
		src = src.replaceAll("⑤", "&#983733;");
		src = src.replaceAll("⑥", "&#983734;");
		src = src.replaceAll("⑦", "&#983735;");
		src = src.replaceAll("⑧", "&#983736;");
		src = src.replaceAll("⑨", "&#983737;");
		//src = src.replaceAll("○","&#61549;");
		src = src.replaceAll("\"", "&#034;");
		src = src.replaceAll("\'", "&#039;");
		return src;
	}

	/**
	 * HTHML태그들을 브라우져에 표현되도록 인코딩하는 기능
	 * (한글에디터에서 사용)
	 */
	public static String toHtmlFormat_hwp(String src) {
		src = src.replaceAll("&amp;", "&");
		src = src.replaceAll("&lt;", "<");
		src = src.replaceAll("&gt;", ">");
		src = src.replaceAll("<br/>", "\n");
		src = src.replaceAll("&nbsp;", " ");
		src = src.replaceAll("&#8228;", "·");
		src = src.replaceAll("&#65378;", "「");
		src = src.replaceAll("&#65379;", "」");
		src = src.replaceAll("&#9702;", "ㆍ");
		src = src.replaceAll("&#8680;", "⇒");
		src = src.replaceAll("&#8226;", "●");
		src = src.replaceAll("&#983803;", "▶");
		src = src.replaceAll("&#983729;", "①");
		src = src.replaceAll("&#983730;", "②");
		src = src.replaceAll("&#983731;", "③");
		src = src.replaceAll("&#983732;", "④");
		src = src.replaceAll("&#983733;", "⑤");
		src = src.replaceAll("&#983734;", "⑥");
		src = src.replaceAll("&#983735;", "⑦");
		src = src.replaceAll("&#983736;", "⑧");
		src = src.replaceAll("&#983737;", "⑨");
		src = src.replaceAll("&#61549;", "○");
		src = src.replaceAll("\"", "&#034;");
		src = src.replaceAll("\'", "&#039;");
		return src;
	}

	/**
	 * HTHML태그들을 브라우져에 표현되도록 인코딩하는 기능
	 * (한글에디터에서 사용)
	 */
	public static String toHtmlFormat_forLawNm(String src) {
		src = src.replaceAll("\"", "&#034;");
		src = src.replaceAll("\'", "&#039;");
		return src;
	}

	/**
	 * HTHML태그들을 브라우져에 표현되도록 인코딩하는 기능(상세조회화면)
	 * 특수문자 다시 처리(안맞는 부분 있는 듯함)
	 * @return String
	 */
	public static String toHtmlFormat2(String src) {
		//특수문자 처리 후
		src = src.replaceAll("&#8228;", "·");
		src = src.replaceAll("&#65378;", "「");
		src = src.replaceAll("&#65379;", "」");
		src = src.replaceAll("&#9702;", "ㆍ");
		src = src.replaceAll("&#8680;", "⇒");
		src = src.replaceAll("&#8226;", "●");
		src = src.replaceAll("&#983803;", "▶");
		src = src.replaceAll("&#983729;", "①");
		src = src.replaceAll("&#983730;", "②");
		src = src.replaceAll("&#983731;", "③");
		src = src.replaceAll("&#983732;", "④");
		src = src.replaceAll("&#983733;", "⑤");
		src = src.replaceAll("&#983734;", "⑥");
		src = src.replaceAll("&#983735;", "⑦");
		src = src.replaceAll("&#983736;", "⑧");
		src = src.replaceAll("&#983737;", "⑨");
		src = src.replaceAll("&#61549;", "○");
		src = src.replaceAll("&#8718;", "·");

		//html태그처리 해야함
		src = src.replaceAll("&", "&amp;");
		src = src.replaceAll("<", "&lt;");
		src = src.replaceAll(">", "&gt;");
		src = src.replaceAll("\n", "<br/>");
		src = src.replaceAll(" ", "&nbsp;");
		return src;
	}

	/**
	 * HTHML태그들을 브라우져에 표현되도록 인코딩하는 기능(상세조회화면)
	 * 특수문자 다시 처리(안맞는 부분 있는 듯함)
	 * @return String
	 */
	public static String toHtmlFormat3(String src) {
		//특수문자 처리 후
		src = src.replaceAll("&#8228;", "·");
		src = src.replaceAll("&#65378;", "「");
		src = src.replaceAll("&#65379;", "」");
		src = src.replaceAll("&#9702;", "ㆍ");
		src = src.replaceAll("&#8680;", "⇒");
		src = src.replaceAll("&#8226;", "●");
		src = src.replaceAll("&#983803;", "▶");
		src = src.replaceAll("&#983729;", "①");
		src = src.replaceAll("&#983730;", "②");
		src = src.replaceAll("&#983731;", "③");
		src = src.replaceAll("&#983732;", "④");
		src = src.replaceAll("&#983733;", "⑤");
		src = src.replaceAll("&#983734;", "⑥");
		src = src.replaceAll("&#983735;", "⑦");
		src = src.replaceAll("&#983736;", "⑧");
		src = src.replaceAll("&#983737;", "⑨");
		src = src.replaceAll("&#61549;", "○");
		src = src.replaceAll("&#8718;", "·");

		return src;
	}

	/**
	 * HTML태그들을 복원시키는 기능
	 * @return String
	 */
	public static String toRawFormat(String src) {
		src = src.replaceAll("&amp;", "&");
		src = src.replaceAll("&lt;", "<");
		src = src.replaceAll("&gt;", ">");
		src = src.replaceAll("<br/>", "\n");
		src = src.replaceAll("&nbsp;", " ");
		src = src.replaceAll("&#8228;", "·");
		src = src.replaceAll("&#65378;", "「");
		src = src.replaceAll("&#65379;", "」");
		src = src.replaceAll("&#9702;", "ㆍ");
		src = src.replaceAll("&#8680;", "⇒");
		src = src.replaceAll("&#8226;", "●");
		src = src.replaceAll("&#983803;", "▶");
		src = src.replaceAll("&#983729;", "①");
		src = src.replaceAll("&#983730;", "②");
		src = src.replaceAll("&#983731;", "③");
		src = src.replaceAll("&#983732;", "④");
		src = src.replaceAll("&#983733;", "⑤");
		src = src.replaceAll("&#983734;", "⑥");
		src = src.replaceAll("&#983735;", "⑦");
		src = src.replaceAll("&#983736;", "⑧");
		src = src.replaceAll("&#983737;", "⑨");
		src = src.replaceAll("&#61549;", "○");
		src = src.replaceAll("&#034;", "\"");
		src = src.replaceAll("&#039;", "\'");
		return src;
	}

	public static String toRawFormat2(String src) {
		src = src.replaceAll("&amp;", "&");
		src = src.replaceAll("&lt;", "<");
		src = src.replaceAll("&gt;", ">");
		src = src.replaceAll("&quot;", "\"");
		src = src.replaceAll("&#039;", "\'");
		return src;
	}

	public static String toHtmlFormatBrSpace(String src) {
		src = src.replaceAll("\n", "<br/>");
//		src = src.replaceAll(" ", "&nbsp;");
		return src;
	}

	/**
	 *<pre>엔터를 <BR>태그로 변환한다. young^^ </pre>
	 *@param inStr                변환할 문자열
	 *@return String
	 *@exception none
	 */
	public static String putsBR(String inStr) {
		if (inStr == null) {
			return "";
		}
		int length = inStr.length();
		StringBuffer buffer = new StringBuffer();

		String comp, comp2, comp3 = "";
		String befComp = "";

		int lot = 0;
		for (int i = 0; i < length; ++i) {
			comp = inStr.substring(i, i + 1);

			if ("<".compareTo(comp) == 0) {
				if (i < length - 8) {
					comp3 = inStr.substring(i, i + 6);
					if ("<TABLE".compareTo(comp3.toUpperCase()) == 0) {
						comp2 = inStr.substring(i, inStr.length());
						lot = comp2.toUpperCase().indexOf("</TABLE>");
						buffer.append(inStr.substring(i, i + lot + 8));
						i = i + lot + 8;
					} else {
						buffer.append(comp);
					}
				} else {
					buffer.append(comp);
				}

			} else {
				if ("\r".compareTo(comp) == 0) {
					if (i < length - 1) {
						comp = inStr.substring(++i, i + 1);
						if ("\n".compareTo(comp) == 0) {
							buffer.append("<BR/>");
						} else {
							buffer.append("<BR/>");
						}
					} else
						buffer.append("<BR/>");
				} else if ("\n".compareTo(comp) == 0) {
					boolean check = true;
					if (i > 9) {
						comp2 = inStr.substring(i - 9, i - 1);

						if ("</TABLE>".compareTo(comp2.toUpperCase()) == 0) {
							check = false;
						}
					}

					if (check) {
						if ("\r".compareTo(befComp) != 0)
							buffer.append("<BR/>");
					}
				}

				befComp = comp;
				buffer.append(comp);
			}
		}
		return buffer.toString();
	}

	/**
	 * 엔터제거 기능
	 * @return String
	 */
	public static String convertHtmlTags(String s) {
		s = s.replaceAll("<[^>]*>", ""); //정규식 태그삭제
		s = s.replaceAll("\r\n", "<br/>"); //엔터제거
		s = s.replaceAll("\n", "<br/>"); //엔터제거
		return s;
	}

	/**
	 * 화면에 표시되는 script, embed, iframe태그를 무효화 시키는 기능
	 * @return String
	 */
	public static String toSafeHtmlFormat(String src) {
		StringBuffer sb = new StringBuffer(src);
		String srcLower = src.toLowerCase();
		int appendix = 0;

		for (int i = 0; i < srcLower.length(); i++) {
			int begin = srcLower.indexOf("<", i);
			int end = 0;

			if (-1 != begin) {
				end = srcLower.indexOf(">", i + 1);
				if (-1 != end) {
					String temp = srcLower.substring(begin, end + 1);
					if (-1 != temp.indexOf("script") || -1 != temp.indexOf("embed") || -1 != temp.indexOf("iframe")) {
						sb.insert(begin + appendix + 1, "!--");
						appendix = appendix + 3;

						sb.insert(end + appendix, "--");
						appendix = appendix + 2;
					}
					i = end;
				}
			}
		}

		return sb.toString();
	}

	/**
	 * 대상문자열(strTarget)에서 지정문자열(strSearch)이 검색된 횟수를,
	 * 지정문자열이 없으면 0 을 반환한다.
	 *
	 * @param strTarget 대상문자열
	 * @param strSearch 검색할 문자열
	 * @return 지정문자열이 검색되었으면 검색된 횟수를, 검색되지 않았으면 0 을 반환한다.
	 * @exception 	LException
	 */
	public static int search(String strTarget, String strSearch) throws Exception {
		int result = 0;
		try {

//			String strCheck = new String(strTarget);
			String strCheck = strTarget;
			for (int i = 0; i < strTarget.length();) {
				int loc = strCheck.indexOf(strSearch);
				if (loc == -1) {
					break;
				} else {
					result++;
					i = loc + strSearch.length();
					strCheck = strCheck.substring(i);
				}
			}

		} catch (Exception e) {
//			System.out.println("######### 예외 발생 ##########");
			log.error("######### 예외 발생 ##########");

		}
		return result;
	}

	/*
	 * 특수문자를 제거한다
	 */
	public static String specialCharRemove(String str) {

		String strImsi = "";

		String[] filterWord = { " ", "\\`", "\\,", "\\.", "\\?", "\\~", "\\!", "\\@", "\\#", "\\$", "\\%", "\\^", "\\&", "\\*", "\\(", "\\)", "\\-", "\\_", "\\+", "\\=", "\\|",
				"\\\\", "\\}", "\\]", "\\{", "\\[", "\\\"", "\\'", "\\:", "\\;", "\\<", "\\>", "\\.", "\\?", "\\/" };

		for (int i = 0; i < filterWord.length; i++) {

			strImsi = str.replaceAll(filterWord[i], "");
			str = strImsi;
		}

		return str;
	}

	/*
	 * 쿼리관련 특수문자를 제거한다
	 */
	public static String queryCharRemove(String str) {

		String strImsi = "";

		String[] filterWord = { "\\`", "\\'", "\\/", "\\\\", "\\;", "\\:", "\\-", "\\+", "\\=", "\\’" };

		for (int i = 0; i < filterWord.length; i++) {

			strImsi = str.replaceAll(filterWord[i], "");
			str = strImsi;
		}

		return str;
	}

	/*
	 * Byte[] 를 String 으로 변환한다
	 */
	public static String convByteToString(byte[] byteSrc, String addStringValue) {

		//byte 를 String 에 담기
		String stringSrc = "";
		for (int i = 0; i < byteSrc.length; i++) {

			if (i != 0) {
				stringSrc += addStringValue;
			}

			stringSrc += Integer.toString(byteSrc[i]);
		}

		return stringSrc;
	}

	/*
	 * String 을 Byte[] 로 변환한다
	 */
	public static byte[] convStringToByte(String stringSrc, String cutStringValue) {

		String[] byteSrc = stringSrc.split(cutStringValue);

		byte[] byteArray = new byte[byteSrc.length];

		for (int i = 0; i < byteSrc.length; i++) {

			byteArray[i] = (byte) Integer.parseInt(byteSrc[i]);
		}

		return byteArray;
	}


	/**
	 * <PRE>
	 * 1. MethodName : removeTag
	 * 2. ClassName  : StringUtil
	 * 3. Comment   : HTML TAG 제거
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 10. 31. 오후 4:12:41
	 * </PRE>
	 *   @return String
	 *   @param str
	 *   @param rmTag
	 *   @return
	 */
	public static String removeTag(String str, String rmTag) {

		int lt = str.toUpperCase().indexOf(rmTag.toUpperCase());

		if (lt != -1) {

			int gt = str.indexOf(">", lt);
			if ((gt != -1)) {

				str = str.substring(0, lt) + str.substring(gt + 1);

				// 재귀 호출
				str = removeTag(str, rmTag);
			}
		}
		return str;
	}

	/**
	 * <PRE>
	 * 1. MethodName : changeWidthTag
	 * 2. ClassName  : StringUtil
	 * 3. Comment   : Contents 에서 Img 태그를 조회하여 Img 태그의 width 를 100% 로 변경
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 10. 31. 오후 4:11:13
	 * </PRE>
	 *   @return String
	 *   @param str
	 *   @return
	 */
	public static String changeWidthTag(String str) {

		int lt = str.toUpperCase().indexOf("<IMG");
		String frontData = "";
		String endData = "";
		String imgData = "";

		//System.out.println("str:"+str);

		if (lt != -1) {

			int gt = str.indexOf(">", lt);

			if ((gt != -1)) {

				frontData = str.substring(0, lt);
				endData = str.substring(gt + 1);
				imgData = "<#" + str.substring(lt + 2, str.indexOf(">", lt) + 1);

				int imgFrontNo = imgData.toUpperCase().indexOf("WIDTH=\"");

				if (imgFrontNo != -1) {

					int imgEndNo = imgData.indexOf("\"", imgFrontNo + 7);

					//System.out.println("widthFrontNo:"+imgFrontNo);
					//System.out.println("widthEndNo:"+imgEndNo);

					String imgFrontData = imgData.substring(0, imgFrontNo);
					String imgEndData = imgData.substring(imgEndNo + 1);
					String widthData = imgData.substring(imgFrontNo, imgEndNo + 1);

					//System.out.println(imgFrontData);
					//System.out.println(imgEndData);
					//System.out.println(widthData);

					int widthFrontNo = widthData.toUpperCase().indexOf("\"");

					if (widthFrontNo != -1) {

						int widthEndNo = widthData.indexOf("\"", widthFrontNo + 1);

						//System.out.println(widthEndNo);

						String widthFrontData = widthData.substring(0, widthFrontNo + 1);
						String widthEndData = widthData.substring(widthEndNo);
						String sizeData = "100%";

						widthData = widthFrontData + sizeData + widthEndData;

						//System.out.println(widthFrontData);
						//System.out.println(widthEndData);
						//System.out.println(sizeData);
						//System.out.println(widthData);
					}

					imgData = imgFrontData + widthData + imgEndData;
					//System.out.println(imgData);
				} else {
					imgData = "<#" + str.substring(lt + 2, str.indexOf(">", lt) - 1) + " width=\"100%\" />";
				}

				str = frontData + imgData + endData;
				//System.out.println(str);

				// 재귀 호출
				str = changeWidthTag(str);
			}
		} else {

			str = str.replaceAll("#mg", "img");
			str = str.replaceAll("#MG", "IMG");
		}
		return str;
	}

	/**
	 * <PRE>
	 * 1. MethodName : imgTagExtract
	 * 2. ClassName  : StringUtil
	 * 3. Comment   : 실제 이미지 URL 만 추출
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 10. 31. 오후 4:11:20
	 * </PRE>
	 *   @return String
	 *   @param str
	 *   @return
	 */
	public static String imgTagExtract(String str) {

		int lt = str.toUpperCase().indexOf("<IMG");

		//System.out.println("before str:"+str);

		if (lt != -1) {

			int gt = str.indexOf(">", lt);

			if ((gt != -1)) {

				str = str.substring(lt, gt + 1);
				//System.out.println("\n>>>>>>>>>>before src lt : "+ str);
				lt = str.indexOf("src=\"");
				str = str.substring(lt + 5);
				//System.out.println("\n>>>>>>>>>>after src lt : "+ Integer.toString(lt) + "\n str : " + str);
				if (lt != -1) {
					gt = str.indexOf("\"");
					//System.out.println("\n>>>>>>>>>>after src gt : "+ Integer.toString(gt));
					if (gt != -1) {
						str = str.substring(0, gt);
					}
				}
			}
		} else {

			str = str.replaceAll("#mg", "img");
			str = str.replaceAll("#MG", "IMG");
		}

		//System.out.println("\n>>>>>>>>>>after str: "+str);

		return str;
	}

	/**
	 * <PRE>
	 * 1. MethodName : changeHeightTag
	 * 2. ClassName  : StringUtil
	 * 3. Comment   :  Contents 에서 Img 태그를 조회하여 Img 태그의 height 를 auto 로 변경
	 * 4. 작성자    : pjh
	 * 5. 작성일    : 2013. 10. 31. 오후 4:11:33
	 * </PRE>
	 *   @return String
	 *   @param str
	 *   @return
	 */
	public static String changeHeightTag(String str) {

		int lt = str.toUpperCase().indexOf("<IMG");
		String frontData = "";
		String endData = "";
		String imgData = "";

		//System.out.println("str:"+str);

		if (lt != -1) {

			int gt = str.indexOf(">", lt);

			if ((gt != -1)) {

				frontData = str.substring(0, lt);
				endData = str.substring(gt + 1);
				imgData = "<#" + str.substring(lt + 2, str.indexOf(">", lt) + 1);

				int imgFrontNo = imgData.toUpperCase().indexOf("HEIGHT=\"");

				if (imgFrontNo != -1) {

					int imgEndNo = imgData.indexOf("\"", imgFrontNo + 8);

					//System.out.println("widthFrontNo:"+imgFrontNo);
					//System.out.println("widthEndNo:"+imgEndNo);

					String imgFrontData = imgData.substring(0, imgFrontNo);
					String imgEndData = imgData.substring(imgEndNo + 1);
					String widthData = imgData.substring(imgFrontNo, imgEndNo + 1);

					//System.out.println("imgFrontData:"+imgFrontData);
					//System.out.println("widthData"+widthData);
					//System.out.println("imgEndData:"+imgEndData);

					int widthFrontNo = widthData.toUpperCase().indexOf("\"");

					if (widthFrontNo != -1) {

						int widthEndNo = widthData.indexOf("\"", widthFrontNo + 1);

						//System.out.println(widthEndNo);

						String widthFrontData = widthData.substring(0, widthFrontNo + 1);
						String widthEndData = widthData.substring(widthEndNo);
						String sizeData = "auto";

						widthData = widthFrontData + sizeData + widthEndData;

						//System.out.println(widthFrontData);
						//System.out.println(widthEndData);
						//System.out.println(sizeData);
						//System.out.println(widthData);
					}

					imgData = imgFrontData + widthData + imgEndData;
					//System.out.println(imgData);
				} else {
					imgData = "<#" + str.substring(lt + 2, str.indexOf(">", lt) - 1) + " height=\"auto\" />";
				}

				str = frontData + imgData + endData;
				//System.out.println(str);

				/*
				System.out.println(frontData);
				System.out.println(endData);
				System.out.println(imgData);
				*/

				// 재귀 호출
				str = changeHeightTag(str);
			}
		} else {

			str = str.replaceAll("#mg", "img");
			str = str.replaceAll("#MG", "IMG");
		}
		return str;
	}

	/**
     * <p>오라클의 decode 함수와 동일한 기능을 가진 메서드이다.
     * <code>sourStr</code>과 <code>compareStr</code>의 값이 같으면
     * <code>returStr</code>을 반환하며, 다르면  <code>defaultStr</code>을 반환한다.
     * </p>
     *
     * <pre>
     * StringUtil.decode(null, null, "foo", "bar")= "foo"
     * StringUtil.decode("", null, "foo", "bar") = "bar"
     * StringUtil.decode(null, "", "foo", "bar") = "bar"
     * StringUtil.decode("하이", "하이", null, "bar") = null
     * StringUtil.decode("하이", "이  ", "foo", null) = null
     * StringUtil.decode("하이", "하이", "foo", "bar") = "foo"
     * StringUtil.decode("하이", "이  ", "foo", "bar") = "bar"
     * </pre>
     *
     * @param sourceStr 비교할 문자열
     * @param compareStr 비교 대상 문자열
     * @param returnStr sourceStr와 compareStr의 값이 같을 때 반환할 문자열
     * @param defaultStr sourceStr와 compareStr의 값이 다를 때 반환할 문자열
     * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며,
     *         <br/>다르면 defaultStr을 반환한다.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr, String defaultStr) {
        if (sourceStr == null && compareStr == null) {
            return returnStr;
        }

        if (sourceStr == null && compareStr != null) {
            return defaultStr;
        }

        if (sourceStr.trim().equals(compareStr)) {
            return returnStr;
        }

        return defaultStr;
    }

    /**
     * <p>오라클의 decode 함수와 동일한 기능을 가진 메서드이다.
     * <code>sourStr</code>과 <code>compareStr</code>의 값이 같으면
     * <code>returStr</code>을 반환하며, 다르면  <code>sourceStr</code>을 반환한다.
     * </p>
     *
     * <pre>
     * StringUtil.decode(null, null, "foo") = "foo"
     * StringUtil.decode("", null, "foo") = ""
     * StringUtil.decode(null, "", "foo") = null
     * StringUtil.decode("하이", "하", "foo") = "foo"
     * StringUtil.decode("하이", "하이 ", "foo") = "하이"
     * StringUtil.decode("하이", "바이", "foo") = "하이"
     * </pre>
     *
     * @param sourceStr 비교할 문자열
     * @param compareStr 비교 대상 문자열
     * @param returnStr sourceStr와 compareStr의 값이 같을 때 반환할 문자열
     * @return sourceStr과 compareStr의 값이 동일(equal)할 때 returnStr을 반환하며,
     *         <br/>다르면 sourceStr을 반환한다.
     */
    public static String decode(String sourceStr, String compareStr, String returnStr) {
        return decode(sourceStr, compareStr, returnStr, sourceStr);
    }

	/**
	 * <PRE>
	 * 1. MethodName : splitList
	 * 2. ClassName  : SysUtil
	 * 3. Comment   : 문자열을 해당 문자로 잘라서 리스트로 변환
	 * 4. 작성자    : ntsys
	 * 5. 작성일    : 2013. 10. 30. 오후 8:38:14
	 * </PRE>
	 *   @return List
	 *   @param str
	 *   @param split
	 *   @return
	 */
	public static List splitList(String str, String splitStr)
	{
		List returnList = new ArrayList();

		returnList = Arrays.asList(str.split(splitStr, str.split(splitStr).length+1));
		return returnList;
	}

	/**
     * <p>{@link String#toUpperCase()}를 이용하여 대문자로 변환한다.</p>
     *
     * <pre>
     * StringUtil.upperCase(null)  = null
     * StringUtil.upperCase("")    = ""
     * StringUtil.upperCase("aBc") = "ABC"
     * </pre>
     *
     * @param str 대문자로 변환되어야 할 문자열
     * @return 대문자로 변환된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String upperCase(String str) {
        if (str == null) {
            return null;
        }

        return str.toUpperCase();
    }

    /**
     * <p>입력된 String의 앞쪽에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
     *
     * <pre>
     * StringUtil.stripStart(null, *)          = null
     * StringUtil.stripStart("", *)            = ""
     * StringUtil.stripStart("abc", "")        = "abc"
     * StringUtil.stripStart("abc", null)      = "abc"
     * StringUtil.stripStart("  abc", null)    = "abc"
     * StringUtil.stripStart("abc  ", null)    = "abc  "
     * StringUtil.stripStart(" abc ", null)    = "abc "
     * StringUtil.stripStart("yxabc  ", "xyz") = "abc  "
     * </pre>
     *
     * @param str 지정된 문자가 제거되어야 할 문자열
     * @param stripChars 제거대상 문자열
     * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String stripStart(String str, String stripChars) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        int start = 0;
        if (stripChars == null) {
            while ((start != strLen) && Character.isWhitespace(str.charAt(start))) {
                start++;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((start != strLen) && (stripChars.indexOf(str.charAt(start)) != -1)) {
                start++;
            }
        }

        return str.substring(start);
    }


    /**
     * <p>입력된 String의 뒤쪽에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
     *
     * <pre>
     * StringUtil.stripEnd(null, *)          = null
     * StringUtil.stripEnd("", *)            = ""
     * StringUtil.stripEnd("abc", "")        = "abc"
     * StringUtil.stripEnd("abc", null)      = "abc"
     * StringUtil.stripEnd("  abc", null)    = "  abc"
     * StringUtil.stripEnd("abc  ", null)    = "abc"
     * StringUtil.stripEnd(" abc ", null)    = " abc"
     * StringUtil.stripEnd("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str 지정된 문자가 제거되어야 할 문자열
     * @param stripChars 제거대상 문자열
     * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String stripEnd(String str, String stripChars) {
        int end;
        if (str == null || (end = str.length()) == 0) {
            return str;
        }

        if (stripChars == null) {
            while ((end != 0) && Character.isWhitespace(str.charAt(end - 1))) {
                end--;
            }
        } else if (stripChars.length() == 0) {
            return str;
        } else {
            while ((end != 0) && (stripChars.indexOf(str.charAt(end - 1)) != -1)) {
                end--;
            }
        }

        return str.substring(0, end);
    }

    /**
     * <p>입력된 String의 앞, 뒤에서 두번째 인자로 전달된 문자(stripChars)를 모두 제거한다.</p>
     *
     * <pre>
     * StringUtil.strip(null, *)          = null
     * StringUtil.strip("", *)            = ""
     * StringUtil.strip("abc", null)      = "abc"
     * StringUtil.strip("  abc", null)    = "abc"
     * StringUtil.strip("abc  ", null)    = "abc"
     * StringUtil.strip(" abc ", null)    = "abc"
     * StringUtil.strip("  abcyx", "xyz") = "  abc"
     * </pre>
     *
     * @param str 지정된 문자가 제거되어야 할 문자열
     * @param stripChars 제거대상 문자열
     * @return 지정된 문자가 제거된 문자열, null이 입력되면 <code>null</code> 리턴
     */
    public static String strip(String str, String stripChars) {
	if (isEmpty(str)) {
	    return str;
	}

	String srcStr = str;
	srcStr = stripStart(srcStr, stripChars);

	return stripEnd(srcStr, stripChars);
    }

    /**
     * <p>
     * String이 비었거나("") 혹은 null 인지 검증한다.
     * </p>
     *
     * <pre>
     *  StringUtil.isEmpty(null)      = true
     *  StringUtil.isEmpty("")        = true
     *  StringUtil.isEmpty(" ")       = false
     *  StringUtil.isEmpty("bob")     = false
     *  StringUtil.isEmpty("  bob  ") = false
     * </pre>
     *
     * @param str - 체크 대상 스트링오브젝트이며 null을 허용함
     * @return <code>true</code> - 입력받은 String 이 빈 문자열 또는 null인 경우
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <PRE>
     * 1. MethodName : isNullOrEmpty
     * 2. ClassName  : StringUtil
     * 3. Comment   :
     * 4. 작성자    : 문 형 순
     * 5. 작성일    : 2018. 7. 17. 오전 11:24:04
     * </PRE>
     *   @return boolean
     *   @param str
     *   @return
     */
    public static boolean isNullOrEmpty(String str) {
        int strLen;
        if(str == null || (strLen = str.length()) == 0)
            return true;

        for(int i = 0; i < strLen; i++)
            if(!Character.isWhitespace(str.charAt(i)))
                return false;

        return true;
	}

    /**
     * <PRE>
     * 1. MethodName : setPoint
     * 2. ClassName  : StringUtil
     * 3. Comment   : 검색결과 리스트 색상 칠하기
     * 4. 작성자    : pjh
     * 5. 작성일    : 2014. 8. 22. 오후 1:34:51
     * </PRE>
     *   @return String
     *   @param src
     *   @param schTxt
     *   @param colorCode
     *   @return
     */
    public static String setTextColor(String src, HashMap schTxtMap, String colorCode) {

    	Set key = schTxtMap.keySet();

//    	System.out.println("schTxtMap"+key.size());
    	log.debug("schTxtMap"+key.size());

        for (Iterator iterator = key.iterator(); iterator.hasNext();) {

            String keyName = (String) iterator.next();
            String value = (String) schTxtMap.get(keyName);

            log.debug("keyName"+keyName);
            log.debug("value"+value);
//            System.out.println("keyName"+keyName);
//            System.out.println("value"+value);

            src = src.replaceAll(value, "<span style=\"color:"+colorCode+"; font-weight:bold\">"+value+"</span>");

        }

		return src;
	}


    public static String removeHtmlTag(String srcStr){

    	String rtnStr = null;

    	if(srcStr != null && !srcStr.equals("")){

    		rtnStr = srcStr.replaceAll("<(/)?([a-zA-Z]*)(\\s[a-zA-Z]*=[^>]*)?(\\s)*(/)?>", "");
    	}

    	return rtnStr;
    }


    /**
     * <PRE>
     * 1. MethodName : setComma
     * 2. ClassName  : StringUtil
     * 3. Comment   : 3자리 마다 콤마 찍기
     * 4. 작성자    : 박재현
     * 5. 작성일    : 2015. 9. 16. 오후 5:23:40
     * </PRE>
     *   @return String
     *   @param num
     *   @return
     */
    public static String setComma(String num) {

        //Null 체크
        if(num == null || num.isEmpty()) return "0";

        //숫자형태가 아닌 문자열일경우 디폴트 0으로 반환
        String numberExpr = "^[-+]?(0|[1-9][0-9]*)(\\.[0-9]+)?([eE][-+]?[0-9]+)?$";
        boolean isNumber = num.matches(numberExpr);
        if (!isNumber) return "0";

        String strResult = num; //출력할 결과를 저장할 변수
        Pattern p = Pattern.compile("(^[+-]?\\d+)(\\d{3})"); //정규표현식
        Matcher regexMatcher = p.matcher(num);

        int cnt = 0;
        while(regexMatcher.find()) {
            strResult = regexMatcher.replaceAll("$1,$2"); //치환 : 그룹1 + "," + 그룹2

            //System.out.println("과정("+ (++cnt) +"):"+strResult);

            //치환된 문자열로 다시 matcher객체 얻기
            //regexMatcher = p.matcher(strResult);
            regexMatcher.reset(strResult);
        }
        return strResult;
    }

    public static long removeComma(String num){
    	//Null 체크
        if(num == null || num.isEmpty()) return 0;

        return Long.parseLong(num.replaceAll(",", ""));
    }


    public static String castMoneyType(int amount) {

		java.text.DecimalFormat decFormat = new java.text.DecimalFormat("###,###,###,###");

		return decFormat.format(amount);
	}

	public static String castMoneyType(double amount) {

		java.text.DecimalFormat decFormat = new java.text.DecimalFormat("###,###,###,###");

		return decFormat.format(amount);
	}

	public static String castMoneyType(String amount) {

		java.text.DecimalFormat decFormat = new java.text.DecimalFormat("###,###,###,###");
		if(amount == null || amount.isEmpty()) return "";
		amount = amount.replaceAll(",", "");
		return decFormat.format(Double.parseDouble(amount));
	}


	public static String castDateFormat(String oriDate, String oriFormat, String changeFormat) throws Exception{
		Date tempDate;

		SimpleDateFormat oriDateFormat = new SimpleDateFormat(oriFormat);
		SimpleDateFormat changeDateFormat = new SimpleDateFormat(changeFormat);

		tempDate = oriDateFormat.parse(oriDate);

		return changeDateFormat.format(tempDate);
	}

	// 자리수에 맞게 '0'로 채우기
	public static String getFillZero(int val, int len) {
		String retStr = "";
		StringBuffer str = new StringBuffer();

		for (int i = 0; i < len; i++)
			str.append("0");

		NumberFormat form = NumberFormat.getPercentInstance();
		((java.text.DecimalFormat) form).applyPattern(new String(str));
		retStr = form.format(val);

		return retStr;
	}
}
