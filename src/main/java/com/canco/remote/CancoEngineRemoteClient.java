package com.canco.remote;

import org.springframework.stereotype.Service;

/**
 * 
 * 供业务系统实现
 * @author rocky
 *
 */
@Service(value="cancoEngineRemoteClient")
public interface CancoEngineRemoteClient {
	
	/**
	 * json格式字符串 业务主键为dataId 其它内容均为在流程运行参数 有的是客户端提供
	 * @param engineVariable
	 */
	public void dealBusi(String engineVariable) ;
	
}
