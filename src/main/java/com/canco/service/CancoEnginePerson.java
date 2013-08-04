package com.canco.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

/**
 * 
 *  人员接口
 *
 */
//@Service(value="cancoEnginePerson")
public interface CancoEnginePerson {
	
	/**
	 * 通过部门ID和角色ID获取人员信息
	 * @param roleId
	 * @param deptId
	 * @return Map key userId  value: userName 
	 */
	public List<Map<String,String>> searchPersonByRoleIdAndDeptId(String roleId , String deptId) ;
	
	/**
	 *  通过角色ID获取人员信息
	 * @param roleId
	 * @return Map key userId  value: userName
	 */
	public List<Map<String,String>> searchPersonByRoleId(String roleId);
	
}
