package egovframework.pharmai.formulation.service;

import java.io.File;
import java.io.IOException;
import java.util.Base64;

import javax.annotation.Resource;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "file:src/main/webapp/WEB-INF/config/egovframework/springmvc/dispatcher-servlet.xml",
		"classpath*:egovframework/spring/context-*.xml" })
@WebAppConfiguration // WebApplicationContext를 생성할 수 있도록 하는 어노테이션
public class MolPredictionServiceTest {

	@Resource(name = "molPredictionService")
	private MolPredictionService molPredictionService;

	@Before
	public void initTest() {
		System.out.println("Init");
	}

	@After
	public void tearDown() {
		System.out.println("TearDown");
	}

	@Test
	public void testGetBase64String() {
		// TODO : 파일 path 정보를 받아서 base64 로 변환한다
		String sdfPath = "D:/pharmai-web/eGovFrameDev-3.9.0-64bit/workspace/PharmAI/src/main/webapp/home/data/t3q/uploads/pharmAi/formulation/PJT_000744/api1/sdf/Caffeine.sdf";
//		String sdfPath = "Caffeine.sdf";
		String base64String = molPredictionService.getBase64String(sdfPath);
		System.out.println("base64String : " + base64String);
	}

	@Test
	public void testGetBase64String2() throws IOException {
		// d드라이브의 work라는 폴더의 test.txt 파일을 읽어드린다.
//		String sdfPath = "D:/pharmai-web/eGovFrameDev-3.9.0-64bit/workspace/PharmAI/src/main/webapp/home/data/t3q/uploads/pharmAi/formulation/PJT_000744/api1/sdf/Caffeine.sdf";
		String sdfPath = "D:/pharmai-web/Caffeine.sdf";

		byte[] fileContent = FileUtils.readFileToByteArray(new File(sdfPath));
	    String encodedString = Base64.getEncoder().encodeToString(fileContent);
	    System.out.println(encodedString);
	}

}
