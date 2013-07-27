package com.canco.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.canco.bean.CancoEngineTask;
import com.canco.pagination.Page;

@Repository
@Transactional
public interface JkWfTaskMapper {

  void insertJkWfTask(CancoEngineTask jkWfTask);

  void deleteJkWfTaskById(String id);

  void updateJkWfTask(CancoEngineTask jkWfTask);
  
  void updateDoing2Done(Map<String,Object> param) ;
  
  void deleteJkWfTaskByCurUserAndLcIdAndTaskState(Map<String,String> parameterMap) ;

  List<CancoEngineTask> searchJkWfTaskByParams(@Param("map") Map<String, String> map, @Param("page") Page page);

}
