package com.canco.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.canco.bean.CancoEngineDeployment;
import com.canco.bean.CancoEngineRuntime;

/**
 *
 * User: rocky
 * Date: 13-7-7
 * Time: 下午2:18
 *
 */
public interface CancoEngineService {
	
	public static enum RESOURCE_TYPE {
		XML("xml") , IMAGE("image") ;
		
		String value = null ;
		private RESOURCE_TYPE(String value){
			this.value = value ;
		}
		
		public String toString(){
			return value ;
		}
	}

    /**
     * 提交一下步处理
     * @param cancoEngineRuntime
     * @return
     */
    public String dealWorkFlow(CancoEngineRuntime cancoEngineRuntime) ;

    /**
     * 页面初始化所需要的参数(工具条，意见，当前环节)
     * @param taskId 任务ID
     * @param busiType 业务类型
     * @return
     */
    public String  pageInitParam(String taskId , String busiType);

    /**
     * 获取下个环节所有的参数
     * @param taskId
     * @return
     */
    public String  nextInfos(String taskId , Map<String,Object> clientMap) ;
    
    /**
     * 部署流程
     * @param busiType
     * @param inputStream
     */
    public void deployWorkFlow(InputStream inputStream) ;
    
    /**
     * 通过流程类型查询出所有部署信息。如流程类型为空，则查询所有
     * @param busiType 流程KEY 
     * @return
     */
    public List<CancoEngineDeployment> searchCancoEngineDeploymentsByBusiType(String busiType);
    
    /**
     * 获取流文件或流图片输出流
     * @param deploymentId 部署ID
     * @param resourceType xml或者image
     * @return
     */
    public InputStream resourceInputStream(String deploymentId , RESOURCE_TYPE resourceType) ;
    
    /**
     * 流程跟踪
     * @param taskId 任务ID
     * @return [{"taskName(环节名称)":"起草","taskId(当前任务ID)":"123","assinger":"张三[123]","startTime":"","endTime":""}]
     */
    public String followText(String taskId) ;
    
    /**
     * 图形化流程跟踪
     * @param taskId 任务ID
     * @return [{"taskName(环节名称)":"起草","taskId(当前任务ID)":"123","assingers":"张三[123],李四[124]"
     * 							,"width":100,"height":200,"x":10,"y":100,"startTime":"","endTime":""}]
     */
    public String followImage(String taskId);
    
}
