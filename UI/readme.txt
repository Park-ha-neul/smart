기존과 변경점.

1. tiles 적용
> tiles 폴더 생성후 설정
- 로그인메뉴(.login), 기본 메뉴 화면, 메뉴없는 화면(.noMenu)
- 컨트롤러 단에서 view string 적을때 각각 다르게 적어주면됨.

> dispatcher-servlet.xml view 설정 변경
- <!-- Tiles 뷰 리졸버 -->
	<bean id="tielsViewResolver"
		class="org.springframework.web.servlet.view.UrlBasedViewResolver">
		<property name="viewClass"
			value="org.springframework.web.servlet.view.tiles3.TilesView" />
		<property name="order" value="1" />
	</bean>
	<!-- Tiles 설정 파일 -->
	<bean id="tilesConfigurer"
		class="org.springframework.web.servlet.view.tiles3.TilesConfigurer">
		<property name="definitions">
			<list>
				<value>/WEB-INF/tiles/tiles-layout.xml</value>
			</list>
		</property>
	</bean>

	<bean class="org.springframework.web.servlet.view.UrlBasedViewResolver" p:order="2"
		p:viewClass="org.springframework.web.servlet.view.JstlView"
		p:prefix="/WEB-INF/jsp/egovframework/" p:suffix=".jsp"/>\

> 각각의 상세 페이지는 body안쪽으로 붙기때문에 head, body, html 태그는 삭제한다.
> .noMenu, .login등 각각의 컨트롤러 단에서 설정한값 셋팅

1.1 상단메뉴, 좌측메뉴 정보 .do호출에서 인터셉터(CommonInterceptor.java)에서 데이터 조회하는것으로 변경

1.2 common/inc 에있는것들 jsp 안쪽으로 이동(보안상 소스코드가 보이기 때문에 변경)

2. @ResponseBody 소스 변경
> 기존에는 response 객체에 일일이 쓰기함
> dispatcher-servlet.xml
	<bean class="org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter">
		<property name="messageConverters">
			<list>
				<bean class="org.springframework.http.converter.json.MappingJackson2HttpMessageConverter">
					<property name="supportedMediaTypes">
						<list>
							<value>application/json; charset=UTF-8</value>
						</list>
					</property>
				</bean>
			</list>
		</property>
	</bean>
추가후
@ResponseBody void => @ResponseBody DataMap 로 변경(json return인경우; 일반 string인경우는 기존 방식 고수)
==
DataMap resultJSON = new DataMap();

//return 상태
DataMap resultStats = new DataMap();
resultStats.put("resultCode", "success");
resultStats.put("resultMsg", egovMessageSource.getMessage("succ.data.update"));
resultJSON.put("resultStats", resultStats);
return resultJSON;
==
사용함.

3. jstl 사용
4. webjars 사용
> lib 형태로 maven에서 관리하도록 변경(pom.xml)
설정은
dispatcher-servlet.xml
<mvc:resources mapping="/webjars/**" location="classpath:/META-INF/resources/webjars" />
기존 common에 존재하는 js, css 정리함

5. servlet 3.0 으로 변경
6. maven -> gradle로 변경

7. xss injection 사용(기존에 사용하는 RequestUtil htmlTagFilter 사용안함)
사용 : compile group: 'com.navercorp.lucy', name: 'lucy-xss-servlet', version: '2.0.1'
web.xml
//===============================================================================================================
<!-- multipart filter 적용 -->
<filter>
	<filter-name>multipartFilter</filter-name>
	<filter-class>org.springframework.web.multipart.support.MultipartFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>multipartFilter</filter-name>
	<url-pattern>*.do</url-pattern>
</filter-mapping>
<!-- 네이버 xss 필터 적용 -->
<filter>
	<filter-name>xssEscapeServletFilter</filter-name>
	<filter-class>com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter</filter-class>
</filter>
<filter-mapping>
	<filter-name>xssEscapeServletFilter</filter-name>
	<url-pattern>*.do</url-pattern>
</filter-mapping>
<!-- 네이버 xss 필터 적용 -->
> xss 필터 적용을 하는데 multipart 타입의 폼 양식은 적용이 되지않아서 위에 multipart 관련 filter 추가

context-common.xml
<bean id="filterMultipartResolver" class="org.springframework.web.multipart.commons.CommonsMultipartResolver" >
	<property name="maxUploadSize" value="600000000" />
	<property name="maxInMemorySize" value="100000000" />
</bean>
> bean id 값 변경 > filterMultipartResolver

허나 controller 단에서
List fileList = ntsysFileMngUtil.getFiles((MultipartHttpServletRequest)request);
변환시 에러 발생
그래서 HttpServletRequest request => MultipartHttpServletRequest request 변경해야 함; 단, multipart 폼 양식으로만 넘길경우만.
아닌경우는 HttpServletRequest request 그대로 사용; 안하는경우 에러 발생
//===============================================================================================================
>> servlet filter로 적용을 하려 했으나 multipart 경우에 소스에서 정상적으로 작동을 하지 않아서 RequestUtil > xssServletFilter 함수로 생성하여 getDataMap 에서 사용하게끔 변경(RequestUtil.java)
>> 그래서 기존에 선언된 filter 부분 모두 제거(web.xml) 하고 context-common.xml filterMultipartResolver 명도 원복(context-common.xml)
>> controller 단에 MultipartHttpServletRequest 변경했던것도 모두 HttpServletRequest 로 원복.(MultipartHttpServletRequest  로 선언된 controller method들)
>> ajax에서 호출을 하고 javascript 에서 원복 데이터를 가져올때는 common.js 에 있는 unescape(str) 함수 사용

8.JSTL 사용
> JSTL 에서 자바 util 사용하기 위해 tld 선언

9.중복로그인 방지 사용
- globals.properties
> Globals.duplLogin = N
- LoginController
> 로그인
if(EgovPropertiesUtil.getProperty("Globals.duplLogin").equals("N")) {
	// 중복로그인 방지를 위한 소스(아이디 기반으로 처리함)
	EgovHttpSessionBindingListener listener = new EgovHttpSessionBindingListener();
	request.getSession().setAttribute(userInfoVo.getId(), listener);
}
> 로그아웃
request.getSession().invalidate();

10.bootstrap4 사용
> fontawesome 5.X 사용

11. Globals.jsp.root 추가하여 jsp 단 페이지는 추후 다른 폴더를 추가하여 다른 레이아웃 구성 할수 있음.(삭제함)
> tiles 적용으로 tiles에서 경로 설정한다.

12. 폰트 noto-sans 적용

13. datetimepicker사용시 테이블 안에서는 해당 사용하는 td 에 클래스 datetimepicker-td 추가

14. adminLte3 적용; 사용자 설정 테마 설정 적용(사용자테이블(tbw_user) thema_option 컬럼 추가; 현재 mysql쪽에는 추가 못함(접근이 되지 않아서))
 설정 기본값 : Globals.default.theme = {"c3":"","t4":"navbar-primary","c4":"","t5":"primary","c5":"","c6":"","t1":"navbar-primary","c1":"","t2":"accent-primary","c2":"","t3":"sidebar-dark-primary"}
 현재 테마타입은 모두 동일한 색상으로 변경하도록 함

 15. DataMap extends HashMap => LinkedHashMap으로 변경
 > 넘어오는 파라미 순서대로 받기 위하여

 16. PMD 적용 수정
 > egov pmd rule 적용하여 1차 수정건 (egovinspectionrules-3.8 적용: blocker 건만 일부 수정; 추후 확인하여 수정 요망[현재 egov것사용하고 있는 파일들은 수정못함])
 > 주요 수정내용 자바 변수 관련 camelCase방식으로 선언해야 하는데 하지 않음(주요 변환파일 >  파일관련 VO 객체 및 쿼리 변수)
 > 추후 egov 관련 파일 사용한것 버전업 된것으로 업데이트 필요함.

==================================================================================================================
gradle 빌드 방법

1. window-preference-gradle > gradle home : D:\eGovFrameDev-3.9.0-64bit\bin\gradle
2. run configurations - gradle project - add

gradle tasks 탭
gradle tasks : build 입력
working directory : 해당 프로젝트 선택

project settings 탭
override project settings 체크
gradle user home : D:\eGovFrameDev-3.9.0-64bit\bin\gradle

java home 탭
D:\eGovFrameDev-3.9.0-64bit\bin\Java\x64\jdk1.8.0_171

arguments 탭
-Pprofile=dev

3. 실행하면
target/libs에 war파일로 나옴

==================================================================================================================
mysql 에서 사용자테이블(tbw_user) thema_option 컬럼 추가하지 못함
