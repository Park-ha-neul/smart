package egovframework.framework.common.util;

import egovframework.framework.security.seed.SeedCipher;

/**
 * <PRE>
 * 1. ClassName :
 * 2. FileName  : SeedScrtyUtil.java
 * 3. Package  : egovframework.framework.common.util
 * 4. Comment  : SEED 알고리즘 암복호화
 * 5. 작성자   : 박재현
 * 6. 작성일   : 2014. 10. 17. 오전 9:41:03
 * </PRE>
 */
public class SeedScrtyUtil {

	private static String key = "ourhome_sc1234556";

	/**
	 * <PRE>
	 * 1. MethodName : encryptText
	 * 2. ClassName  : SeedScrtyUtil
	 * 3. Comment   : 암호화
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2014. 10. 17. 오전 9:41:16
	 * </PRE>
	 *   @return String
	 *   @param orgnStr
	 *   @return
	 */
	public static String encryptText(String orgnStr) {

		SeedCipher seed = new SeedCipher();
		String encryptStr = "";

		try{

			encryptStr = Base64Utils.base64Encode(seed.encrypt(orgnStr, key.getBytes(), "UTF-8"));
		}
		catch(Exception e){
			System.out.println("######### 예외 발생32 ##########");
		}

		return encryptStr;

	}


	/**
	 * <PRE>
	 * 1. MethodName : decryptText
	 * 2. ClassName  : SeedScrtyUtil
	 * 3. Comment   : 복호화
	 * 4. 작성자    : 박재현
	 * 5. 작성일    : 2014. 10. 17. 오전 9:41:22
	 * </PRE>
	 *   @return String
	 *   @param encryptStr
	 *   @return
	 */
	public static String decryptText(String encryptStr){

		SeedCipher seed = new SeedCipher();
		String decryptStr = "";

		try{
			byte[] encryptbytes = Base64Utils.base64Decode(encryptStr);
			decryptStr = seed.decryptAsString(encryptbytes, key.getBytes(), "UTF-8");
		}
		catch(Exception e){
			System.out.println("######### 예외 발생33 ##########");
		}

		return decryptStr;
	}

}
