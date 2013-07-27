package com.canco.ext;

import org.springframework.stereotype.Service;

@Service
public class CancoEngineJudgeImpl implements CancoEngineJudge {

	@Override
	public boolean isJudgeFlowCondition(String flowId,
			String processDefinitionKey) {
		return false;
	}

}
