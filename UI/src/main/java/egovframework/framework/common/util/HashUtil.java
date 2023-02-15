package egovframework.framework.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {
	/**
	 * <PRE>
	 * 1. MethodName : getHashCode
	 * 2. ClassName  : HashUtil
	 * 3. Comment   : 해쉬코드 받기
	 * 4. 작성자    : JJH
	 * 5. 작성일    : 2018. 9. 12. 오후 12:33:23
	 * </PRE>
	 *   @return String
	 *   @param orgnStr
	 *   @return
	 */
	public static String getHashCode(String orgnStr) {
		
		String rtnSHA = "";
		
		try{
			MessageDigest sh = MessageDigest.getInstance("SHA-256");
			sh.update(orgnStr.getBytes());
			byte byteData[] = sh.digest();
			StringBuffer sb = new StringBuffer();
			
			for(int i = 0 ; i < byteData.length ; i++){
				sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
			}
			rtnSHA = sb.toString();
			
		}catch(NoSuchAlgorithmException e){
			e.printStackTrace();
			rtnSHA = null;
		}
		return rtnSHA;
	}

}
