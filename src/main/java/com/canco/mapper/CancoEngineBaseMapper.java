package com.canco.mapper;

import java.util.List;

import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CancoEngineBaseMapper<T extends Object> {
	
	@Autowired
	private SqlSessionFactory sqlSessionFactory ;
	
	public void batchInsert(String statement ,List<T> inserts){
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH) ;
		for( T t : inserts ){
			sqlSession.insert(statement, t);
		}
		sqlSession.flushStatements();
		sqlSession.commit();
		sqlSession.close();
	}
	
	public void batchUpdate(String statement ,List<T> updates){
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH) ;
		for( T t : updates ){
			sqlSession.update(statement, t);
		}
		sqlSession.flushStatements();
		sqlSession.commit();
		sqlSession.close();
	}
	
	public void batchDelete(String statement ,List<T> dels){
		SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH) ;
		for( T t : dels ){
			sqlSession.delete(statement, t);
		}
		sqlSession.flushStatements();
		sqlSession.commit();
		sqlSession.close();
	}
	
	
}
