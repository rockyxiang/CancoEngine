package com.canco.service;

import com.canco.bean.CancoEngineRuntime;

import java.util.Map;

/**
 *
 * User: rocky
 * Date: 13-7-7
 * Time: 下午2:18
 *
 */
public interface CancoEngineService {

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
    public String  nextInfos(String taskId) ;

}
