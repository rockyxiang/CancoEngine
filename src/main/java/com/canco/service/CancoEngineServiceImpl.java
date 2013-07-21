package com.canco.service;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.canco.bean.CancoEngineDeployment;
import com.canco.bean.CancoEngineInner;
import com.canco.bean.CancoEngineRuntime;
import com.canco.config.CancoEngineConfig;
import com.canco.ext.CancoEngineJudge;
import com.canco.util.CancoEngineParse;

/**
 * 引擎实现类
 */
@Service
public class CancoEngineServiceImpl extends CancoEngineBaseService implements
		CancoEngineService {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CancoEngineServiceImpl.class);

	@Autowired
	private CancoEngineTaskService cancoEngineTaskService;

	@Autowired
	private CancoEngineConfig cancoEngineConfig;

	@Autowired
	private CancoEngineJudge cancoEngineJudge;

	private CancoEngineInner parse2EngineBean(
			CancoEngineRuntime cancoEngineRuntime) {
		CancoEngineInner cancoEngineInner = (CancoEngineInner) cancoEngineRuntime;
		cancoEngineInner.setDomainUrl(cancoEngineConfig.getCancoDomainUrl());
		return cancoEngineInner;
	}

	private void validatorParam(CancoEngineRuntime cancoEngineRuntime) {
		StringBuffer sb = new StringBuffer();
		CancoEngineInner cancoEngineInner = parse2EngineBean(cancoEngineRuntime);
		if (!cancoEngineInner.isStart()) {
			if (StringUtils.isEmpty(cancoEngineRuntime.getTaskId())) {
				sb.append("taskId不能为空.").append("\n");
			}
		}
		if (StringUtils.isEmpty(cancoEngineRuntime.getBusiType())) {
			sb.append("busiType不能为空.").append("\n");
		}
		if (StringUtils.isEmpty(cancoEngineRuntime.getUserId())) {
			sb.append("userId不能为空.").append("\n");
		}
		if (StringUtils.isEmpty(cancoEngineRuntime.getDeptId())) {
			sb.append("deptId不能为空.").append("\n");
		}
		if (sb.length() > 0) {
			throw new IllegalArgumentException(sb.toString());
		}
	}

	@Transactional
	public String dealWorkFlow(CancoEngineRuntime cancoEngineRuntime) {
		String taskId = null;
		validatorParam(cancoEngineRuntime);
		CancoEngineInner cancoEngineInner = parse2EngineBean(cancoEngineRuntime);
		identityService.setAuthenticatedUserId(cancoEngineRuntime.getUserId());
		if (StringUtils.isEmpty(cancoEngineInner.getTaskId())) {
			runtimeService.startProcessInstanceByKey(
					cancoEngineInner.getBusiType(),
					cancoEngineInner.getDataId(),
					cancoEngineInner.getVariableMap());
			taskId = taskService.createTaskQuery()
					.taskAssignee(cancoEngineInner.getUserId()).singleResult()
					.getId();
			cancoEngineTaskService.createDrafter(cancoEngineInner);
		} else {
			taskId = cancoEngineInner.getTaskId();
			String processInstanceId = historyService
					.createHistoricTaskInstanceQuery().taskAssignee(taskId)
					.singleResult().getProcessInstanceId();
			taskService.addComment(taskId, processInstanceId,
					cancoEngineInner.getAllMsg());
			taskService.complete(taskId, cancoEngineInner.getVariableMap());
			if (cancoEngineInner.isStart()) {
				cancoEngineTaskService.drafter2Create(cancoEngineInner);
			} else {
				cancoEngineTaskService.doing2done(cancoEngineInner);
			}
			createDoing(cancoEngineInner, processInstanceId);
		}
		return taskId;
	}

	private void createDoing(CancoEngineInner cancoEngineInner,
			String processInstanceId) {
		List<Task> tasks = taskService.createTaskQuery()
				.processInstanceId(processInstanceId).list();
		if (tasks != null && tasks.size() > 0) {
			if (tasks.size() == 1) {
				Task task = tasks.get(0);
				cancoEngineInner.setTaskId(task.getId());
				cancoEngineInner.setDealUserId(task.getAssignee());
				cancoEngineTaskService.createDoing(cancoEngineInner);
			} else {
				List<CancoEngineInner> cancoEngineInners = new ArrayList<CancoEngineInner>();
				for (Task task : tasks) {
					try {
						CancoEngineInner inner = (CancoEngineInner) BeanUtils
								.cloneBean(cancoEngineInner);
						inner.setTaskId(task.getId());
						inner.setDealUserId(task.getAssignee());
						cancoEngineInners.add(inner);
					} catch (IllegalAccessException e) {
						LOGGER.error("IllegalAccessException:{}", e);
					} catch (InstantiationException e) {
						LOGGER.error("InstantiationException:{}", e);
					} catch (InvocationTargetException e) {
						LOGGER.error("InvocationTargetException:{}", e);
					} catch (NoSuchMethodException e) {
						LOGGER.error("NoSuchMethodException:{}", e);
					}
				}
				cancoEngineTaskService.createDoings(cancoEngineInners);
			}
		}
	}

	@Override
	public String pageInitParam(String taskId, String busiType) {
		String resultStr = getFormKeyByBusiTypeOrTaskId(taskId, busiType);
		List<Comment> comments = getComments(taskId);
		if (comments != null) {
			resultStr += ",\"comments\":[";
			for (Comment comment : comments) {
				resultStr += "{\"userId\":\"" + comment.getUserId() + "\"";
				// TODO 这里需要将时间修改为字符串
				resultStr += ",\"time\":\"" + comment.getTime() + "\"";
				resultStr += "," + comment.getFullMessage() + "},";
			}
			resultStr = resultStr.substring(0, resultStr.length() - 2);
			resultStr += "]";
		}
		return resultStr;
	}

	@Override
	public String nextInfos(String taskId) {
		String processDefintionId = historyService.createHistoricTaskInstanceQuery().taskId(taskId)
				.singleResult().getProcessDefinitionId();
		List<Map<String, String>> taskInfos = nextTaskInfos(taskId);
		for (int i = 0, size = taskInfos.size(); i < size; i++) {
			Map<String, String> taskInfo = taskInfos.get(i);
			if (!cancoEngineJudge.isJudgeFlowCondition(taskInfo.get("flowId"),processDefintionId)) {
				taskInfos.remove(i);
			}
		}
		return CancoEngineParse.list2Json(taskInfos);
	}

	@Override
	public void deployWorkFlow(String busiType, InputStream inputStream) {
		repositoryService.createDeployment().addZipInputStream(new ZipInputStream(inputStream)).deploy();
	}

	@Override
	public List<CancoEngineDeployment> searchCancoEngineDeploymentsByBusiType(String busiType) {
		List<CancoEngineDeployment> cancoEngineDeployments = new ArrayList<CancoEngineDeployment>();
		List<ProcessDefinition> processDefinitions = null ;
		if(StringUtils.isNotEmpty(busiType)){
			processDefinitions = repositoryService.createProcessDefinitionQuery().processDefinitionKey(busiType).desc().list();
		}else{
			processDefinitions = repositoryService.createProcessDefinitionQuery().desc().list();
		}
		for(ProcessDefinition processDefinition : processDefinitions){
			CancoEngineDeployment cancoEngineDeployment = new CancoEngineDeployment();
			cancoEngineDeployment.setDeployId(processDefinition.getId());
			cancoEngineDeployment.setProcessBpmnImageName(processDefinition.getDiagramResourceName());
			cancoEngineDeployment.setProcessBpmnName(processDefinition.getResourceName());
			cancoEngineDeployment.setProcessDefinitionName(processDefinition.getName());
			cancoEngineDeployments.add(cancoEngineDeployment);
		}
		return cancoEngineDeployments;
	}
}
