package com.canco.bean;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 流程运行时bean
 * User: rocky
 * Date: 13-7-7
 * Time: 下午2:24
 *
 */
public class CancoEngineRuntime {

    /**
     * 当前用户
     */
    private String userId ;

    /**
     * 当前用户所在部门
     */
    private String deptId ;

    /**
     * 下个环节处理人
     */
    private String dealUserId ;

    /**
     * 流程运行时，所需的参数
     */
    private Map<String,Object> variableMap ;

    /**
     * 任务ID
     */
    private String taskId ;

    /**
     * 下个环节处理判断条件,如存在多个的条件结果如下：key:value;key1:value1
     */
    private String condition ;

    /**
     * 业务主键
     */
    private String dataId ;

    /**
     * 业务类型
     */
    private String busiType ;
    
    public String getBusiType() {
      return busiType;
    }
    
    public void setBusiType(String busiType) {
      this.busiType = busiType;
    }

    public String getDataId() {
        return dataId;
    }

    public void setDataId(String dataId) {
        this.dataId = dataId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDealUserId() {
        return dealUserId;
    }

    public void setDealUserId(String dealUserId) {
        this.dealUserId = dealUserId;
    }

    public Map<String, Object> getVariableMap() {
        if (variableMap == null){
            setVariableMap(new HashMap<String, Object>());
        }
        if(StringUtils.isEmpty(getDealUserId())){
            variableMap.put("owner" ,  getDealUserId());
        }
        if(StringUtils.isEmpty(getUserId())){
            variableMap.put("userId" , getUserId());
        }
        if(StringUtils.isEmpty(getDeptId())){
            variableMap.put("deptId" , getDeptId());
        }
        if(StringUtils.isEmpty(getCondition())){
            variableMap.put("condition" , getCondition());
        }
        return variableMap;
    }

    public void setVariableMap(Map<String, Object> variableMap) {
        this.variableMap = variableMap;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

}
