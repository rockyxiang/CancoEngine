package com.canco.bean;

/**
 * 
 * 部署内容
 *
 */
public class CancoEngineDeployment {
	/**
	 * 部署ID
	 */
	private String deployId ;
	
	/**
	 * 流程定义ID
	 */
	private String processDefinitionKey ;
	
	/**
	 * 流程名称
	 */
	private String processDefinitionName ;
	
	/**
	 * 流程XML名称
	 */
	private String processBpmnName ;
	
	/**
	 * 流程图名称
	 */
	private String processBpmnImageName ;
	
	/**
	 * 流程部署版本号
	 */
	private int version ;
	
	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getDeployId() {
		return deployId;
	}

	public void setDeployId(String deployId) {
		this.deployId = deployId;
	}

	public String getProcessDefinitionKey() {
		return processDefinitionKey;
	}

	public void setProcessDefinitionKey(String processDefinitionKey) {
		this.processDefinitionKey = processDefinitionKey;
	}

	public String getProcessDefinitionName() {
		return processDefinitionName;
	}

	public void setProcessDefinitionName(String processDefinitionName) {
		this.processDefinitionName = processDefinitionName;
	}

	public String getProcessBpmnName() {
		return processBpmnName;
	}

	public void setProcessBpmnName(String processBpmnName) {
		this.processBpmnName = processBpmnName;
	}

	public String getProcessBpmnImageName() {
		return processBpmnImageName;
	}

	public void setProcessBpmnImageName(String processBpmnImageName) {
		this.processBpmnImageName = processBpmnImageName;
	}
	
	
}
