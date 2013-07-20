package com.canco.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.canco.bean.JkWfTask;
import com.canco.pagination.Page;

@Repository
@Transactional
public interface JkWfTaskMapper {

  void insertJkWfTask(JkWfTask jkWfTask);

  void deleteJkWfTaskById(String id);

  void updateJkWfTask(JkWfTask jkWfTask);
  
  void updateDoing2Done(Map<String,Object> param) ;
  
  void deleteJkWfTaskByCurUserAndLcIdAndTaskState(Map<String,String> parameterMap) ;

  List<JkWfTask> searchJkWfTaskByParams(@Param("map") Map<String, String> map, @Param("page") Page page);

}
