package egovframework.framework.common.util;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * 메시지 리소스 사용을 위한 MessageSource 인터페이스 및 ReloadableResourceBundleMessageSource 클래스의 구현체
 * @author 공통서비스 개발팀 이문준
 * @since 2009.06.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.03.11  이문준          최초 생성
 *
 * </pre>
 */

public class EgovMessageSource extends ReloadableResourceBundleMessageSource implements MessageSource {

	private ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource;

	/**
	 * getReloadableResourceBundleMessageSource() 
	 * @param reloadableResourceBundleMessageSource - resource MessageSource
	 * @return ReloadableResourceBundleMessageSource
	 */	
	public void setReloadableResourceBundleMessageSource(ReloadableResourceBundleMessageSource reloadableResourceBundleMessageSource) {
		this.reloadableResourceBundleMessageSource = reloadableResourceBundleMessageSource;
	}
	
	/**
	 * getReloadableResourceBundleMessageSource() 
	 * @return ReloadableResourceBundleMessageSource
	 */	
	public ReloadableResourceBundleMessageSource getReloadableResourceBundleMessageSource() {
		return reloadableResourceBundleMessageSource;
	}
	
	/**
	 * 정의된 메세지 조회
	 * @param code - 메세지 코드
	 * @return String
	 */	
	public String getMessage(String code) {
		try{
			return getReloadableResourceBundleMessageSource().getMessage(code, null, Locale.KOREAN);
		}
		catch(NoSuchMessageException e){
			return "######### 예외 발생12 ##########";	    	
		}		
	}
	
	/**
	 * 정의된 메세지 조회
	 * @param code - 메세지 코드
	 * @return String
	 */	
	public String getMessage(String code, String[] args) {
		try{
			return getReloadableResourceBundleMessageSource().getMessage(code, args, Locale.KOREAN);
		}
		catch(NoSuchMessageException e){
			return "######### 예외 발생13 ##########";	
		}
	}
	
	/**
	 * <PRE>
	 * 1. MethodName : getMessage
	 * 2. ClassName  : EgovMessageSource
	 * 3. Comment   : locale 정보를 통해 해당 언어에 맞는 메세지 정보를 가져온다.
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 10. 22. 오후 3:56:34
	 * </PRE>
	 *   @return String
	 *   @param code
	 *   @param locale
	 *   @return
	 */
	public String getMessage(String code, String locale) {
		return getMessage(code, null, locale);
	}
	
	/**
	 * <PRE>
	 * 1. MethodName : getMessage
	 * 2. ClassName  : EgovMessageSource
	 * 3. Comment   : locale 정보를 통해 해당 언어에 맞는 메세지 정보를 가져온다.
	 * 4. 작성자    : 조정현
	 * 5. 작성일    : 2014. 10. 22. 오후 3:56:36
	 * </PRE>
	 *   @return String
	 *   @param code
	 *   @param args
	 *   @param locale
	 *   @return
	 */
	public String getMessage(String code, String[] args, String locale) {
		String rtnMessage = "";
		
		try{
			if(locale.equals("KO")){
				rtnMessage = getReloadableResourceBundleMessageSource().getMessage(code, args, Locale.KOREAN);
			}
			else if(locale.equals("CN")){
				rtnMessage = getReloadableResourceBundleMessageSource().getMessage(code, args, Locale.CHINESE);
			}
			
			if(rtnMessage == null || rtnMessage.equals("")){
				rtnMessage = getReloadableResourceBundleMessageSource().getMessage(code, args, Locale.KOREAN);			
			}
		}
		catch(NoSuchMessageException e){
			rtnMessage = "######### 예외 발생14 ##########";	
		}
		
		return rtnMessage;
	}
	
	
}
