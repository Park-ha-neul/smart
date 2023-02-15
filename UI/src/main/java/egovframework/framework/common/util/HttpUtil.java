package egovframework.framework.common.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class HttpUtil {

	private static Log log = LogFactory.getLog(HttpUtil.class);
	/*
     * url 요청결과를 string 으로 변환
     */
    public static String getHttpRtnData(String urlData, String encodeType){

        StringBuffer rtnData = new StringBuffer();
        BufferedReader in = null;

        try{

            URL url = new URL(urlData);
            URLConnection conn = url.openConnection();

            in = new BufferedReader(new InputStreamReader(conn.getInputStream(), encodeType));

            String line = null;
            while ((line = in.readLine()) != null) {
                rtnData.append(line);
            }

        }
        catch(Exception e){
//            System.out.println("######### 예외 발생26 ##########");
            log.error("######### 예외 발생26 ##########");
        }
        finally{
        	try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
//				System.out.println("######### 예외 발생27 ##########");
				log.error("######### 예외 발생27 ##########");
			}
        }
        return rtnData.toString();
    }

    //String 값을 Map 형태로 변환
    public static Map jsonToMap(String str){

        Map states = JSONObject.fromObject(str);

        return states;
    }

	public static Map getXmlToMap(String url, String encodeType) {

		Map<String, Object> jsonInMap = null;

		String xmlData = getHttpRtnData(url, encodeType);

		XMLSerializer xmlSerializer = new XMLSerializer();
		JSON json = xmlSerializer.read(xmlData);

		ObjectMapper mapper = new ObjectMapper();

		try {

			jsonInMap = mapper.readValue(json.toString(), new TypeReference<Map<String, Object>>() {
			});
			List<String> keys = new ArrayList<String>(jsonInMap.keySet());

			for (String key : keys) {
//				System.out.println(key + ":" + jsonInMap.get(key));
				log.debug(key + ":" + jsonInMap.get(key));
			}

		} catch (JsonGenerationException e) {
//			System.out.println("######### 예외 발생28 ##########");
			log.error("######### 예외 발생28 ##########");
		} catch (JsonMappingException e) {
//			System.out.println("######### 예외 발생29 ##########");
			log.error("######### 예외 발생29 ##########");
		} catch (IOException e) {
//			System.out.println("######### 예외 발생30 ##########");
			log.error("######### 예외 발생30 ##########");
		}

		return jsonInMap;

	}

}
