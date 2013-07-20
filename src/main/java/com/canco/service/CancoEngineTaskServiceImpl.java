package com.canco.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.canco.bean.CancoEngineInner;
import com.canco.bean.JkWfTask;
import com.canco.config.CancoEngineConfig;
import com.canco.mapper.CancoEngineBaseMapper;
import com.canco.mapper.JkWfTaskMapper;
import com.canco.util.KeyUtils;

@Service
public class CancoEngineTaskServiceImpl implements CancoEngineTaskService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(CancoEngineServiceImpl.class);
  
  /**
   * 合并待办的状态值
   */
  private static final String MEGER = "1" ;
  
  private enum TASK_STATE {
    DOING,
    DONE,
    DRAFTE,
    CREATE ;
    
    public String getValue(){
      switch (this) {
        case DOING:
          return "0";
        case DONE:
          return "1";
        case DRAFTE:
          return "2";
        case CREATE:
          return "3";
      }
      return "";
    }
  }
  
  @Autowired
  private JkWfTaskMapper jkWfTaskMapper ;
  
  @Autowired
  private CancoEngineConfig cancoEngineConfig ;
  
  @Autowired
  private CancoEngineBaseMapper<JkWfTask> baseMapper;

  public void createDoing(CancoEngineInner cancoEngineInner) {
    LOGGER.debug("创建待办，待办ID为：{}" , cancoEngineInner.getTaskId());
    JkWfTask jkWfTask = engine2JkWfTask(cancoEngineInner) ;
    jkWfTask.setTaskState(TASK_STATE.CREATE.getValue());
    jkWfTaskMapper.insertJkWfTask(jkWfTask);
  }

  public void doing2done(CancoEngineInner cancoEngineInner) {
    if(MEGER.equals(cancoEngineConfig.getIsMegerDone())){
      Map<String,String> parameterMap = new HashMap<String, String>();
      parameterMap.put("lcId", cancoEngineInner.getProcInstanceId());
      parameterMap.put("curUser", cancoEngineInner.getUserId());
      parameterMap.put("taskState", TASK_STATE.DONE.getValue());
      jkWfTaskMapper.deleteJkWfTaskByCurUserAndLcIdAndTaskState(parameterMap);
    }
    JkWfTask jkWfTask = new JkWfTask() ;
    if(StringUtils.isNotEmpty(cancoEngineInner.getDealUserId())){
      jkWfTask.setCurUser(Integer.parseInt(cancoEngineInner.getDealUserId()));
      jkWfTask.setCurTime(cancoEngineInner.getDealTime());
    }
    jkWfTask.setTaskState(TASK_STATE.DONE.getValue());
    jkWfTask.setTaskId(cancoEngineInner.getTaskId());
    jkWfTaskMapper.updateJkWfTask(jkWfTask);
  }

  public void createDrafter(CancoEngineInner cancoEngineInner) {
    LOGGER.debug("创建我的草稿，任务ID为：{}" , cancoEngineInner.getTaskId());
    JkWfTask jkWfTask = engine2JkWfTask(cancoEngineInner) ;
    jkWfTask.setTaskState(TASK_STATE.DRAFTE.getValue());
    jkWfTaskMapper.insertJkWfTask(jkWfTask);
  }

  public void drafter2Create(CancoEngineInner cancoEngineInner) {
    JkWfTask jkWfTask = new JkWfTask();
    jkWfTask.setTaskId(cancoEngineInner.getTaskId());
    jkWfTask.setTaskState(TASK_STATE.CREATE.getValue());
    jkWfTask.setCurTime(cancoEngineInner.getDealTime());
    if( StringUtils.isNotEmpty(cancoEngineInner.getUserId())){
      jkWfTask.setCurUser(Integer.parseInt(cancoEngineInner.getUserId()));
    }
  }
  
  private JkWfTask engine2JkWfTask(CancoEngineInner cancoEngineInner){
    JkWfTask jkWfTask = new JkWfTask();
    jkWfTask.setId(KeyUtils.nextUUID());
    jkWfTask.setApplyId(cancoEngineInner.getDataId());
    if(StringUtils.isNotEmpty(cancoEngineInner.getBusiType())){
      jkWfTask.setBusiType(Integer.parseInt(cancoEngineInner.getBusiType()));
    }
    if(cancoEngineInner.isStart()){
      if(StringUtils.isNotEmpty(cancoEngineInner.getUserId())){
        jkWfTask.setCreateUser(Integer.parseInt(cancoEngineInner.getUserId()));
      }
    }
    if(StringUtils.isNotEmpty(cancoEngineInner.getDealUserId())){
      jkWfTask.setCurUser(Integer.parseInt(cancoEngineInner.getDealUserId()));
    }
    if(StringUtils.isNotEmpty(cancoEngineInner.getUserId())){
      jkWfTask.setSubmitUser(Integer.parseInt(cancoEngineInner.getUserId()));
      jkWfTask.setSubmitDept(Integer.parseInt(cancoEngineInner.getDeptId()));
    }
    jkWfTask.setLcId(cancoEngineInner.getProcInstanceId());
    jkWfTask.setTaskId(cancoEngineInner.getTaskId());
    //TODO 此处代码将移至engine代码中 放在此处只是为了临时测试用
//    cancoEngineInner.setDomainUrl(cancoEngineConfig.getCancoDomainUrl());
    jkWfTask.setUrl(cancoEngineInner.getUrl());
    return jkWfTask;
  }

  @Override
  public void createDoings(List<CancoEngineInner> cancoEngineInners) {
    if(cancoEngineInners != null && cancoEngineInners.size() > 0 ){
      List<JkWfTask> jkWfTasks = new ArrayList<JkWfTask>();
      for( CancoEngineInner cancoEngineInner : cancoEngineInners ){
        JkWfTask jkWfTask = engine2JkWfTask(cancoEngineInner) ;
        jkWfTask.setTaskState(TASK_STATE.CREATE.getValue());
        jkWfTasks.add(jkWfTask);
      }
      baseMapper.batchInsert("insertJkWfTask", jkWfTasks);
    }
  }
  
}
