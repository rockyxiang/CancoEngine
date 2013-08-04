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
     * 用户中文姓名
     */
    private String userName ;

    /**
     * 当前用户所在部门
     */
    private String deptId ;
    
    /**
     * 当前所在部门名称
     */
    private String deptName ;

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
    
    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

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
        if(StringUtils.isNotEmpty(getDealUserId())){
            variableMap.put("owner" ,  getDealUserId());
        }
        if(StringUtils.isNotEmpty(getUserId())){
            variableMap.put("userId" , getUserId());
            variableMap.put("userName", getUserName());
        }
        if(StringUtils.isNotEmpty(getDeptId())){
            variableMap.put("deptId" , getDeptId());
            variableMap.put("deptName", getDeptName());
        }
        if(StringUtils.isNotEmpty(getCondition())){
        	if(getCondition().contains(";")){
        		String []conditions = getCondition().split(";");
        		for(String cond : conditions){
        			String[] resultCondition = cond.split("\\:");
        			variableMap.put(resultCondition[0], resultCondition[1]);
        		}
        	}else{
        		variableMap.put("condition" , getCondition());
        	}
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
