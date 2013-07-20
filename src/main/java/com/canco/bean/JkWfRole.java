package com.canco.bean;

/**
 * 流程角色
 * @author ROCKY.CHEN
 * @created 2013-07-13 18:00:08
 * @version 
 */
public class JkWfRole {
	/**
	 *  
	 */
	private String id;
	/**
	 *  
	 */
	private String roleFlag;
	/**
	 *  角色名称
	 */
	private String roleName;
	/**
	 *  角色描述
	 */
	private String roleDesc;

	/**
	 * 
	 *
	 * @param id
	 */
	public void setId(String id){
		this.id = id;
	}
	
    /**
     * 
     *
     * @return
     */	
    public String getId(){
    	return id;
    }
	
	/**
	 * 
	 *
	 * @param roleFlag
	 */
	public void setRoleFlag(String roleFlag){
		this.roleFlag = roleFlag;
	}
	
    /**
     * 
     *
     * @return
     */	
    public String getRoleFlag(){
    	return roleFlag;
    }
	
	/**
	 * 角色名称
	 *
	 * @param roleName
	 */
	public void setRoleName(String roleName){
		this.roleName = roleName;
	}
	
    /**
     * 角色名称
     *
     * @return
     */	
    public String getRoleName(){
    	return roleName;
    }
	
	/**
	 * 角色描述
	 *
	 * @param roleDesc
	 */
	public void setRoleDesc(String roleDesc){
		this.roleDesc = roleDesc;
	}
	
    /**
     * 角色描述
     *
     * @return
     */	
    public String getRoleDesc(){
    	return roleDesc;
    }
	
}