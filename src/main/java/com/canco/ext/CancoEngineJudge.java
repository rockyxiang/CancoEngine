package com.canco.ext;

import org.springframework.stereotype.Service;

@Service
public interface CancoEngineJudge {
	
	/**
	 * 判断当前路径是否显示
	 * @param flowId 路径定义ID
	 * @param processDefinitionKey 
	 * @return
	 */
	public boolean isJudgeFlowCondition( String flowId , String processDefinitionKey ) ;

}
