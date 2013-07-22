package com.canco.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.bpmn.behavior.ExclusiveGatewayActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.NoneEndEventActivityBehavior;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.delegate.ActivityBehavior;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Attachment;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class CancoEngineBaseService {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(CancoEngineBaseService.class);

  @Autowired
  protected TaskService taskService;

  @Autowired
  protected HistoryService historyService;

  @Autowired
  protected FormService formService;

  @Autowired
  protected RuntimeService runtimeService;

  @Autowired
  protected IdentityService identityService;

  @Autowired
  protected RepositoryService repositoryService;

  /**
   * 驳回流程
   * 
   * @param taskId
   *          当前任务ID
   * @param activityId
   *          驳回节点ID
   * @param variables
   *          流程存储参数
   * @throws Exception
   */
  public void backProcess(String taskId, String activityId, Map<String, Object> variables) throws RuntimeException {
    if (StringUtils.isEmpty(activityId)) {
      throw new RuntimeException("驳回目标节点ID为空！");
    }

    // 查找所有并行任务节点，同时驳回
    List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(taskId).getId(), findTaskById(taskId).getTaskDefinitionKey());
    for (Task task : taskList) {
      commitProcess(task.getId(), variables, activityId);
    }
  }

  /**
   * 取回流程
   * 
   * @param taskId
   *          当前任务ID
   * @param activityId
   *          取回节点ID
   * @throws Exception
   */
  public void callBackProcess(String taskId, String activityId) throws RuntimeException {
    if (StringUtils.isEmpty(activityId)) {
      throw new RuntimeException("目标节点ID为空！");
    }

    // 查找所有并行任务节点，同时取回
    List<Task> taskList = findTaskListByKey(findProcessInstanceByTaskId(taskId).getId(), findTaskById(taskId).getTaskDefinitionKey());
    for (Task task : taskList) {
      commitProcess(task.getId(), null, activityId);
    }
  }

  /**
   * 清空指定活动节点流向
   * 
   * @param activityImpl
   *          活动节点
   * @return 节点流向集合
   */
  private List<PvmTransition> clearTransition(ActivityImpl activityImpl) {
    // 存储当前节点所有流向临时变量
    List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
    // 获取当前节点所有流向，存储到临时变量，然后清空
    List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
    for (PvmTransition pvmTransition : pvmTransitionList) {
      oriPvmTransitionList.add(pvmTransition);
    }
    pvmTransitionList.clear();

    return oriPvmTransitionList;
  }

  /**
   * @param taskId
   *          当前任务ID
   * @param variables
   *          流程变量
   * @param activityId
   *          流程转向执行任务节点ID<br>
   *          此参数为空，默认为提交操作
   * @throws Exception
   */
  private void commitProcess(String taskId, Map<String, Object> variables, String activityId) throws RuntimeException {
    if (variables == null) {
      variables = new HashMap<String, Object>();
    }
    // 跳转节点为空，默认提交操作
    if (StringUtils.isEmpty(activityId)) {
      taskService.complete(taskId, variables);
    } else {// 流程转向操作
      turnTransition(taskId, activityId, variables);
    }
  }

  /**
   * 中止流程(特权人直接审批通过等)
   * 
   * @param taskId
   */
  public void endProcess(String taskId) throws RuntimeException {
    ActivityImpl endActivity = findActivitiImpl(taskId, "end");
    commitProcess(taskId, null, endActivity.getId());
  }

  /**
   * 根据流入任务集合，查询最近一次的流入任务节点
   * 
   * @param processInstance
   *          流程实例
   * @param tempList
   *          流入任务集合
   * @return
   */
  private ActivityImpl filterNewestActivity(ProcessInstance processInstance, List<ActivityImpl> tempList) {
    while (tempList.size() > 0) {
      ActivityImpl activity_1 = tempList.get(0);
      HistoricActivityInstance activityInstance_1 = findHistoricUserTask(processInstance, activity_1.getId());
      if (activityInstance_1 == null) {
        tempList.remove(activity_1);
        continue;
      }

      if (tempList.size() > 1) {
        ActivityImpl activity_2 = tempList.get(1);
        HistoricActivityInstance activityInstance_2 = findHistoricUserTask(processInstance, activity_2.getId());
        if (activityInstance_2 == null) {
          tempList.remove(activity_2);
          continue;
        }

        if (activityInstance_1.getEndTime().before(activityInstance_2.getEndTime())) {
          tempList.remove(activity_1);
        } else {
          tempList.remove(activity_2);
        }
      } else {
        break;
      }
    }
    if (tempList.size() > 0) {
      return tempList.get(0);
    }
    return null;
  }

  /**
   * 根据任务ID和节点ID获取活动节点 <br>
   * 
   * @param taskId
   *          任务ID
   * @param activityId
   *          活动节点ID <br>
   *          如果为null或""，则默认查询当前活动节点 <br>
   *          如果为"end"，则查询结束节点 <br>
   * 
   * @return
   * @throws Exception
   */
  private ActivityImpl findActivitiImpl(String taskId, String activityId) throws RuntimeException {
    // 取得流程定义
    ProcessDefinitionEntity processDefinition = findProcessDefinitionEntityByTaskId(taskId);

    // 获取当前活动节点ID
    if (StringUtils.isEmpty(activityId)) {
      activityId = findTaskById(taskId).getTaskDefinitionKey();
    }

    // 根据流程定义，获取该流程实例的结束节点
    if (activityId.toUpperCase().equals("END")) {
      for (ActivityImpl activityImpl : processDefinition.getActivities()) {
        List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
        if (pvmTransitionList.isEmpty()) {
          return activityImpl;
        }
      }
    }

    // 根据节点ID，获取对应的活动节点
    ActivityImpl activityImpl = ((ProcessDefinitionImpl) processDefinition).findActivity(activityId);

    return activityImpl;
  }

  /**
   * 根据当前任务ID，查询可以驳回的任务节点
   * 
   * @param taskId
   *          当前任务ID
   */
  public List<ActivityImpl> findBackAvtivity(String taskId) throws RuntimeException {
    List<ActivityImpl> rtnList = iteratorBackActivity(taskId, findActivitiImpl(taskId, null), new ArrayList<ActivityImpl>(), new ArrayList<ActivityImpl>());
    return reverList(rtnList);
  }

  /**
   * 查询指定任务节点的最新记录
   * 
   * @param processInstance
   *          流程实例
   * @param activityId
   * @return
   */
  private HistoricActivityInstance findHistoricUserTask(ProcessInstance processInstance, String activityId) {
    HistoricActivityInstance rtnVal = null;
    // 查询当前流程实例审批结束的历史节点
    List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().activityType("userTask")
            .processInstanceId(processInstance.getId()).activityId(activityId).finished().orderByHistoricActivityInstanceEndTime().desc().list();
    if (historicActivityInstances.size() > 0) {
      rtnVal = historicActivityInstances.get(0);
    }

    return rtnVal;
  }

  /**
   * 根据当前节点，查询输出流向是否为并行终点，如果为并行终点，则拼装对应的并行起点ID
   * 
   * @param activityImpl
   *          当前节点
   * @return
   */
  private String findParallelGatewayId(ActivityImpl activityImpl) {
    List<PvmTransition> incomingTransitions = activityImpl.getOutgoingTransitions();
    for (PvmTransition pvmTransition : incomingTransitions) {
      TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
      activityImpl = transitionImpl.getDestination();
      String type = (String) activityImpl.getProperty("type");
      if ("parallelGateway".equals(type)) {// 并行路线
        String gatewayId = activityImpl.getId();
        String gatewayType = gatewayId.substring(gatewayId.lastIndexOf("_") + 1);
        if ("END".equals(gatewayType.toUpperCase())) {
          return gatewayId.substring(0, gatewayId.lastIndexOf("_")) + "_start";
        }
      }
    }
    return null;
  }

  /**
   * 根据任务ID获取流程定义
   * 
   * @param taskId
   *          任务ID
   * @return
   * @throws Exception
   */
  public ProcessDefinitionEntity findProcessDefinitionEntityByTaskId(String taskId) throws RuntimeException {
    // 取得流程定义
    ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
            .getDeployedProcessDefinition(findTaskById(taskId).getProcessDefinitionId());

    if (processDefinition == null) {
      throw new RuntimeException("流程定义未找到!");
    }

    return processDefinition;
  }

  /**
   * 根据任务ID获取对应的流程实例
   * 
   * @param taskId
   *          任务ID
   * @return
   * @throws Exception
   */
  public ProcessInstance findProcessInstanceByTaskId(String taskId) throws RuntimeException {
    // 找到流程实例
    ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(findTaskById(taskId).getProcessInstanceId()).singleResult();
    if (processInstance == null) {
      throw new RuntimeException("流程实例未找到!");
    }
    return processInstance;
  }

  /**
   * 根据任务ID获得任务实例
   * 
   * @param taskId
   *          任务ID
   * @return
   * @throws Exception
   */
  private HistoricTaskInstanceEntity findTaskById(String taskId) throws RuntimeException {
    HistoricTaskInstanceEntity task = (HistoricTaskInstanceEntity) historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
    if (task == null) {
      throw new RuntimeException("任务实例未找到!");
    }
    return task;
  }

  /**
   * 根据流程实例ID和任务key值查询所有同级任务集合
   * 
   * @param processInstanceId
   * @param key
   * @return
   */
  private List<Task> findTaskListByKey(String processInstanceId, String key) {
    return taskService.createTaskQuery().processInstanceId(processInstanceId).taskDefinitionKey(key).list();
  }

  /**
   * 迭代循环流程树结构，查询当前节点可驳回的任务节点
   * 
   * @param taskId
   *          当前任务ID
   * @param currActivity
   *          当前活动节点
   * @param rtnList
   *          存储回退节点集合
   * @param tempList
   *          临时存储节点集合（存储一次迭代过程中的同级userTask节点）
   * @return 回退节点集合
   */
  private List<ActivityImpl> iteratorBackActivity(String taskId, ActivityImpl currActivity, List<ActivityImpl> rtnList, List<ActivityImpl> tempList)
          throws RuntimeException {
    // 查询流程定义，生成流程树结构
    ProcessInstance processInstance = findProcessInstanceByTaskId(taskId);

    // 当前节点的流入来源
    List<PvmTransition> incomingTransitions = currActivity.getIncomingTransitions();
    // 条件分支节点集合，userTask节点遍历完毕，迭代遍历此集合，查询条件分支对应的userTask节点
    List<ActivityImpl> exclusiveGateways = new ArrayList<ActivityImpl>();
    // 并行节点集合，userTask节点遍历完毕，迭代遍历此集合，查询并行节点对应的userTask节点
    List<ActivityImpl> parallelGateways = new ArrayList<ActivityImpl>();
    // 遍历当前节点所有流入路径
    for (PvmTransition pvmTransition : incomingTransitions) {
      TransitionImpl transitionImpl = (TransitionImpl) pvmTransition;
      ActivityImpl activityImpl = transitionImpl.getSource();
      String type = (String) activityImpl.getProperty("type");
      /**
       * 并行节点配置要求：<br>
       * 必须成对出现，且要求分别配置节点ID为:XXX_start(开始)，XXX_end(结束)
       */
      if ("parallelGateway".equals(type)) {// 并行路线
        String gatewayId = activityImpl.getId();
        String gatewayType = gatewayId.substring(gatewayId.lastIndexOf("_") + 1);
        if ("START".equals(gatewayType.toUpperCase())) {// 并行起点，停止递归
          return rtnList;
        } else {// 并行终点，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
          parallelGateways.add(activityImpl);
        }
      } else if ("startEvent".equals(type)) {// 开始节点，停止递归
        return rtnList;
      } else if ("userTask".equals(type)) {// 用户任务
        tempList.add(activityImpl);
      } else if ("exclusiveGateway".equals(type)) {// 分支路线，临时存储此节点，本次循环结束，迭代集合，查询对应的userTask节点
        currActivity = transitionImpl.getSource();
        exclusiveGateways.add(currActivity);
      }
    }

    /**
     * 迭代条件分支集合，查询对应的userTask节点
     */
    for (ActivityImpl activityImpl : exclusiveGateways) {
      iteratorBackActivity(taskId, activityImpl, rtnList, tempList);
    }

    /**
     * 迭代并行集合，查询对应的userTask节点
     */
    for (ActivityImpl activityImpl : parallelGateways) {
      iteratorBackActivity(taskId, activityImpl, rtnList, tempList);
    }

    /**
     * 根据同级userTask集合，过滤最近发生的节点
     */
    currActivity = filterNewestActivity(processInstance, tempList);
    if (currActivity != null) {
      // 查询当前节点的流向是否为并行终点，并获取并行起点ID
      String id = findParallelGatewayId(currActivity);
      if (StringUtils.isEmpty(id)) {// 并行起点ID为空，此节点流向不是并行终点，符合驳回条件，存储此节点
        rtnList.add(currActivity);
      } else {// 根据并行起点ID查询当前节点，然后迭代查询其对应的userTask任务节点
        currActivity = findActivitiImpl(taskId, id);
      }

      // 清空本次迭代临时集合
      tempList.clear();
      // 执行下次迭代
      iteratorBackActivity(taskId, currActivity, rtnList, tempList);
    }
    return rtnList;
  }

  /**
   * 还原指定活动节点流向
   * 
   * @param activityImpl
   *          活动节点
   * @param oriPvmTransitionList
   *          原有节点流向集合
   */
  private void restoreTransition(ActivityImpl activityImpl, List<PvmTransition> oriPvmTransitionList) {
    // 清空现有流向
    List<PvmTransition> pvmTransitionList = activityImpl.getOutgoingTransitions();
    pvmTransitionList.clear();
    // 还原以前流向
    for (PvmTransition pvmTransition : oriPvmTransitionList) {
      pvmTransitionList.add(pvmTransition);
    }
  }

  /**
   * 反向排序list集合，便于驳回节点按顺序显示
   * 
   * @param list
   * @return
   */
  private List<ActivityImpl> reverList(List<ActivityImpl> list) {
    List<ActivityImpl> rtnList = new ArrayList<ActivityImpl>();
    // 由于迭代出现重复数据，排除重复
    for (int i = list.size(); i > 0; i--) {
      if (!rtnList.contains(list.get(i - 1)))
        rtnList.add(list.get(i - 1));
    }
    return rtnList;
  }

  /**
   * 转办流程
   * 
   * @param taskId
   *          当前任务节点ID
   * @param userCode
   *          被转办人Code
   */
  public void transferAssignee(String taskId, String userCode) {
    taskService.delegateTask(taskId, userCode);
  }

  /**
   * 流程转向操作
   * 
   * @param taskId
   *          当前任务ID
   * @param activityId
   *          目标节点任务ID
   * @param variables
   *          流程变量
   * @throws Exception
   */
  public void turnTransition(String taskId, String activityId, Map<String, Object> variables) throws RuntimeException {
    // 当前节点
    ActivityImpl currActivity = findActivitiImpl(taskId, null);
    // 清空当前流向
    List<PvmTransition> oriPvmTransitionList = clearTransition(currActivity);

    // 创建新流向
    TransitionImpl newTransition = currActivity.createOutgoingTransition();
    // 目标节点
    ActivityImpl pointActivity = findActivitiImpl(taskId, activityId);
    // 设置新流向的目标节点
    newTransition.setDestination(pointActivity);
    // 执行转向任务
    taskService.complete(taskId, variables);
    // 删除目标节点新流入
    pointActivity.getIncomingTransitions().remove(newTransition);

    // 还原以前流向
    restoreTransition(currActivity, oriPvmTransitionList);
  }

  public boolean isDone(String taskId) throws RuntimeException {
    if (taskId == null && "".equals(taskId)) {
      return false;
    }
    return historyService.createHistoricTaskInstanceQuery().taskId(taskId).finished().singleResult() == null ? false : true;
  }

  public boolean isPopedomByTaskId(String taskId, String userId) throws RuntimeException {
    return historyService.createHistoricTaskInstanceQuery().taskId(taskId).taskAssignee(userId).singleResult() == null ? false : true;
  }

  protected ProcessDefinitionEntity findProcessDefinitionEntityByDeployId(String deployId) throws RuntimeException {
    ProcessDefinition definition = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).singleResult();
    return (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService).getDeployedProcessDefinition(definition.getId());
  }

  protected List<Map<String, String>> searchNextActivitiesByTaskKey(String taskKey, ProcessDefinitionEntity entity) {
    List<Map<String, String>> results = new ArrayList<Map<String, String>>();
    List<ActivityImpl> activityImpls = entity.getActivities();
    for (ActivityImpl activityImpl : activityImpls) {
      if (activityImpl.getId().equals(taskKey)) {
        List<PvmTransition> pvms = activityImpl.getOutgoingTransitions();
        for (PvmTransition pvm : pvms) {
          ActivityImpl pvmActivity = (ActivityImpl) pvm.getDestination();
          List<PvmTransition> realPvms = new ArrayList<PvmTransition>();
          ActivityBehavior activityBehavior = pvmActivity.getActivityBehavior();
          if (activityBehavior instanceof ExclusiveGatewayActivityBehavior) {
            realPvms = pvmActivity.getOutgoingTransitions();
            for (PvmTransition realPvm : realPvms) {
              ActivityImpl realActivty = (ActivityImpl) realPvm.getDestination();
              taskDefinitionMap(realPvm.getDestination(), results, realActivty.getActivityBehavior(), realPvm.getId());
            }
          } else {
            taskDefinitionMap(pvm.getDestination(), results, activityBehavior, pvm.getId());
          }
        }
        return results;
      }
    }
    return null;
  }

  private void taskDefinitionMap(PvmActivity pvm, List<Map<String, String>> results, ActivityBehavior activityBehavior, String flowId) {
    Map<String, String> resultMap = new HashMap<String, String>();
    resultMap.put(WORK_FLOW_ELMENTS.TASK_KEY.toString(), pvm.getId());
    resultMap.put(WORK_FLOW_ELMENTS.TASK_NAME.toString(), pvm.getProperty("name").toString());
    boolean isSelectPerson = (activityBehavior instanceof NoneEndEventActivityBehavior) ? false : true;
    resultMap.put(WORK_FLOW_ELMENTS.IS_SELECTED_PERSON.toString(), String.valueOf(isSelectPerson));
    resultMap.put(WORK_FLOW_ELMENTS.FLOW_ID.toString(), flowId);
    results.add(resultMap);
  }

  public List<Map<String, String>> nextTaskInfos(String taskId) {
    String taskKey = taskService.createTaskQuery().taskId(taskId).singleResult().getTaskDefinitionKey();
    ProcessDefinitionEntity entity = findProcessDefinitionEntityByTaskId(taskId);
    return searchNextActivitiesByTaskKey(taskKey, entity);
  }

  public List<Map<String, String>> nextTaskInfos(String deployId, String taskKey) {
    ProcessDefinitionEntity entity = findProcessDefinitionEntityByDeployId(deployId);
    return searchNextActivitiesByTaskKey(taskKey, entity);
  }

  public Map<String, Object> searchProcessVariableByTaskId(String taskId) {
    Map<String, Object> resultMap = new HashMap<String, Object>();
    if (taskId != null && !"".equals(taskId)) {
      resultMap.put("taskId", taskId);
      List<HistoricVariableInstance> variables = historyService.createHistoricVariableInstanceQuery().taskId(taskId).list();
      if (variables != null) {
        for (HistoricVariableInstance variable : variables) {
          resultMap.put(variable.getVariableName(), variable.getValue());
        }
      }
    }
    return resultMap;
  }

  protected List<HistoricTaskInstance> searchCurrentTasksByTaskId(String taskId) {
    String instanceId = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult().getProcessInstanceId();
    return historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceId).unfinished().list();
  }

  protected String searchStartUserByTaskId(String taskId) {
    String instanceId = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult().getProcessInstanceId();
    String userId = historyService.createHistoricProcessInstanceQuery().processInstanceId(instanceId).singleResult().getStartUserId();
    return userId;
  }

  protected String searchProcessInstanceIdByTaskId(String taskId) {
    return historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult().getProcessInstanceId();
  }

  protected String searchAssignerByTaskIdAndTaskKey(String taskId, String taskKey) {
    String procInstanceId = searchProcessInstanceIdByTaskId(taskId);
    List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(procInstanceId)
            .taskDefinitionKey(taskKey).orderByHistoricTaskInstanceStartTime().desc().listPage(0, 1);
    if (historicTaskInstances != null) {
      return historicTaskInstances.get(0).getAssignee();
    }
    return null;
  }

  protected String searchTaskIdByBussinessKeyAndUserId(String bussinessKey, String userId) {
    HistoricProcessInstance instance = historyService.createHistoricProcessInstanceQuery().processInstanceBusinessKey(bussinessKey).singleResult();
    if (instance == null) {
      return null;
    }
    String instanceId = instance.getId();
    List<HistoricTaskInstance> hisTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(instanceId).taskAssignee(userId)
            .orderByHistoricTaskInstanceStartTime().desc().listPage(0, 1);
    if (hisTaskInstances != null && hisTaskInstances.size() > 0) {
      return hisTaskInstances.get(0).getId();
    }
    return null;
  }

  protected List<Map<String, Object>> searchCurrentInfoMapByTaskId(String taskId) {
    HistoricTaskInstance historicTaskInstance = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
    String processInstanceId = historicTaskInstance.getProcessInstanceId();
    boolean isFinished = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).finished().count() > 0 ? true : false;
    StringBuffer usersBuffer = new StringBuffer();
    List<HistoricTaskInstance> historicTaskInstances = null;
    String endId = null;
    if (!isFinished) {
      historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(historicTaskInstance.getProcessInstanceId()).unfinished()
              .list();
    } else {
      endId = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult().getEndActivityId();
    }
    ProcessDefinitionEntity entity = findProcessDefinitionEntityByTaskId(taskId);

    List<Map<String, Object>> resultLists = new ArrayList<Map<String, Object>>();
    List<ActivityImpl> activityImpls = entity.getActivities();
    for (ActivityImpl activityImpl : activityImpls) {
      if (isFinished) {
        if (activityImpl.getActivityBehavior() instanceof NoneEndEventActivityBehavior) {
          if (endId != null && activityImpl.getId().equals(endId)) {
            Map<String, Object> activityMap = new HashMap<String, Object>();
            initFollowImageMap(activityMap, activityImpl);
            resultLists.add(activityMap);
            break;
          }
        }
      } else {
        for (int i = 0, size = historicTaskInstances.size(); i < size; i++) {
          HistoricTaskInstance tempInstance = null;
          if (i != 0) {
            tempInstance = historicTaskInstances.get(i - 1);
          }
          HistoricTaskInstance instance = historicTaskInstances.get(i);
          if (activityImpl.getId().equals(instance.getTaskDefinitionKey())) {
            if (tempInstance != null && !activityImpl.getId().equals(tempInstance.getTaskDefinitionKey())) {
              usersBuffer = new StringBuffer(0);
            }
            Map<String, Object> activityMap = new HashMap<String, Object>();
            initFollowImageMap(activityMap, activityImpl);
            usersBuffer.append(instance.getAssignee()).append(",");
            activityMap.put("users", usersBuffer.substring(0, usersBuffer.length() - 1).toString());
            resultLists.add(activityMap);
          }
        }
      }
    }
    return resultLists;
  }

  protected Map<String, String> searchHistoryVarsByTaskId(String taskId) {
    String instanceId = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult().getProcessInstanceId();
    List<HistoricVariableInstance> historicVariableInstances = historyService.createHistoricVariableInstanceQuery().processInstanceId(instanceId).list();
    Map<String, String> returnMap = new HashMap<String, String>();
    for (HistoricVariableInstance historicVariableInstance : historicVariableInstances) {
      returnMap.put(historicVariableInstance.getVariableName(), historicVariableInstance.getValue().toString());
    }
    return returnMap;
  }

  private void initFollowImageMap(Map<String, Object> activityMap, ActivityImpl activityImpl) {
    activityMap.put("width", activityImpl.getWidth());
    activityMap.put("height", activityImpl.getHeight());
    activityMap.put("x", activityImpl.getX());
    activityMap.put("y", activityImpl.getY());
    activityMap.put("taskName", activityImpl.getProperties().get("name"));
  }

  protected enum WORK_FLOW_ELMENTS {
    START_TIME("startTime"), END_TIME("endTime"), TASK_KEY("taskKey"), TASK_NAME("taskName") ,
    FLOW_ID("flowId") , IS_SELECTED_PERSON("isSelectPerson");

    private WORK_FLOW_ELMENTS(String mapKey) {
      this.mapKey = mapKey;
    }

    public String toString() {
      return mapKey;
    }

    private String mapKey;
  }
  
  protected List<Comment> getComments(String taskId){
     if(StringUtils.isEmpty(taskId)){
       return null;
     }
	 HistoricTaskInstance taskInstance =  historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
	 return taskService.getProcessInstanceComments(taskInstance.getProcessInstanceId());
  } 
  
  /**
   * 获取formkey中配置的文件内容
   * @param taskId 
   * @param busiType
   * @return 获取配置内容
   */
  protected String getFormKeyByBusiTypeOrTaskId(String taskId , String busiType){
    if(StringUtils.isEmpty(taskId) && StringUtils.isEmpty(busiType)){
      return null;
    }
    String formKey = null ;
    if(StringUtils.isNotEmpty(taskId)){
      formKey = formService.getTaskFormData(taskId).getFormKey();
      HistoricTaskInstance taskInstance =  historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
      if(taskInstance.getEndTime() == null){
          formKey += getTaskState("false");
        }else{
          formKey += getTaskState("true");
        }
    }else{
      formKey = formService.getStartFormKey(busiType);
      formKey += getTaskState("false");
    }
    return formKey ;
  }
  
  private String getTaskState(String isFinish){
    return  ",\"finished\":"+isFinish;
  }

}
