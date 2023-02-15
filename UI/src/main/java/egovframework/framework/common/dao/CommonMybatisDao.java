package egovframework.framework.common.dao;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository("commonMybatisDao")
public class CommonMybatisDao extends EgovAbstractMapper{	

	@Resource(name = "sqlSession")	//context-mapper.xml 의 기본 bean id
	public void setSqlSessionFactory(SqlSessionFactory sqlSession) {
		super.setSqlSessionFactory(sqlSession);
	}
}
