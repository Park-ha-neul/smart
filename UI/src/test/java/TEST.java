import egovframework.framework.common.util.Base64Utils;
import egovframework.framework.security.seed.SeedCipher;

public class TEST {

	private static String key = "ourhome_sc1234556";

	public static void main(String[] args) {

		SeedCipher seed = new SeedCipher();
		String decryptStr = "";

		try{
			byte[] encryptbytes = Base64Utils.base64Decode("b7hniPOOY0EfHJXBDkacx91ysH4h0TnkTXJpdRwJOhw=");
			decryptStr = seed.decryptAsString(encryptbytes, key.getBytes(), "UTF-8");
			System.out.println(decryptStr);
		}
		catch(Exception e){
			System.out.println("######### 예외 발생33 ##########");
		}
	}



}
