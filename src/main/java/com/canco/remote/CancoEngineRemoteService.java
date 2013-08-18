package com.canco.remote;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.canco.util.CancoEngineParse;
import com.canco.util.SpringUtils;


public class CancoEngineRemoteService implements ExecutionListener{
	
	private static final long serialVersionUID = 1L;
	
  private static final Logger LOGGER = LoggerFactory.getLogger(CancoEngineRemoteService.class);

  @Override
  public void notify(DelegateExecution delegateExecution) throws Exception {
    Map<String,Object> resultMap = new HashMap<String, Object>();
    resultMap.put("dataId", delegateExecution.getProcessBusinessKey());
    String []busiType = delegateExecution.getProcessDefinitionId().split(":");
    resultMap.put("busiType", busiType[0]);
    LOGGER.info("==============CancoEngineRemoteService   execution start ==============");
    LOGGER.info("processDefinitionKey : {} , taskDefinitionKey : {} " , delegateExecution.getProcessDefinitionId() , delegateExecution.getCurrentActivityId());
    CancoEngineRemoteClient cancoEngineRemoteClient = (CancoEngineRemoteClient)SpringUtils.getBean("cancoEngineRemoteClient");
    cancoEngineRemoteClient.dealBusi(CancoEngineParse.map2Json(resultMap));
    LOGGER.info("==============CancoEngineRemoteService   execution end ==============");
  }

}
