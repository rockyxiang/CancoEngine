package com.canco.bean;

import java.util.Date;

/**
 * 待办 / 已办 /我创建的
 * 
 * @author ROCKY.CHEN
 * @created 2013-07-13 18:15:50
 * @version
 */
public class CancoEngineTask {

	/**
	 *  
	 */
	private String id;
	/**
	 * 业务类型
	 */
	private String busiType;
	/**
	 * 任务ID
	 */
	private String taskId;
	/**
	 * 流程实例ID
	 */
	private String procInstId;
	/**
	 *  
	 */
	private String url;
	/**
	 * 当前处理人
	 */
	private Integer curUser;
	/**
	 * 提交人
	 */
	private Integer submitUser;
	/**
	 * 提交人所在部门
	 */
	private Integer submitDept;
	/**
	 * 业务主键
	 */
	private String applyId;

	/**
	 * 0 待办 1 已办
	 */
	private String taskState;
	/**
	 * 创建人ID
	 */
	private Integer createUser;

	/**
	 * 处理时间
	 */
	private Date curTime;

	public Date getCurTime() {
		return curTime;
	}

	public void setCurTime(Date curTime) {
		this.curTime = curTime;
	}

	public String getProcInstId() {
		return procInstId;
	}

	public void setProcInstId(String procInstId) {
		this.procInstId = procInstId;
	}

	/**
	 * 
	 * 
	 * @param id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getId() {
		return id;
	}

	/**
	 * 业务类型
	 * 
	 * @param busiType
	 */
	public void setBusiType(String busiType) {
		this.busiType = busiType;
	}

	/**
	 * 业务类型
	 * 
	 * @return
	 */
	public String getBusiType() {
		return busiType;
	}

	/**
	 * 任务ID
	 * 
	 * @param taskId
	 */
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	/**
	 * 任务ID
	 * 
	 * @return
	 */
	public String getTaskId() {
		return taskId;
	}

	/**
	 * 
	 * 
	 * @param url
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * 当前处理人
	 * 
	 * @param curUser
	 */
	public void setCurUser(Integer curUser) {
		this.curUser = curUser;
	}

	/**
	 * 当前处理人
	 * 
	 * @return
	 */
	public Integer getCurUser() {
		return curUser;
	}

	/**
	 * 提交人
	 * 
	 * @param submitUser
	 */
	public void setSubmitUser(Integer submitUser) {
		this.submitUser = submitUser;
	}

	/**
	 * 提交人
	 * 
	 * @return
	 */
	public Integer getSubmitUser() {
		return submitUser;
	}

	/**
	 * 提交人所在部门
	 * 
	 * @param submitDept
	 */
	public void setSubmitDept(Integer submitDept) {
		this.submitDept = submitDept;
	}

	/**
	 * 提交人所在部门
	 * 
	 * @return
	 */
	public Integer getSubmitDept() {
		return submitDept;
	}

	/**
	 * 业务主键
	 * 
	 * @param applyId
	 */
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	/**
	 * 业务主键
	 * 
	 * @return
	 */
	public String getApplyId() {
		return applyId;
	}

	/**
	 * 0 待办 1 已办
	 * 
	 * @param taskState
	 */
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}

	/**
	 * 0 待办 1 已办
	 * 
	 * @return
	 */
	public String getTaskState() {
		return taskState;
	}

	/**
	 * 创建人ID
	 * 
	 * @param createUser
	 */
	public void setCreateUser(Integer createUser) {
		this.createUser = createUser;
	}

	/**
	 * 创建人ID
	 * 
	 * @return
	 */
	public Integer getCreateUser() {
		return createUser;
	}

}