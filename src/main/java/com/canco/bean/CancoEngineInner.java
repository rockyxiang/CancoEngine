package com.canco.bean;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

public class CancoEngineInner extends CancoEngineIdea {

  /**
   * 设置url
   */
  private String url;
  
  /**
   * 流程ID
   */
  private String procInstanceId ;

  /**
   * 全局url
   */
  private String domainUrl;

  /**
   * 处理时间
   */
  private Date dealTime;

  /**
   * 是否为起草节点
   */
  private boolean isStart = false;
  
  public String getProcInstanceId() {
    return procInstanceId;
  }
  
  public void setProcInstanceId(String procInstanceId) {
    this.procInstanceId = procInstanceId;
  }

  public boolean isStart() {
    return isStart;
  }

  public void setStart(boolean start) {
    isStart = start;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getUrl() {
    if (StringUtils.isEmpty(url)) {
      return "";
    }else{
      int lastIndexSign = url.length() - 2 ;
      url = "=".equals(url.substring(lastIndexSign))? url : url+"=";
    }
    if (domainUrl.lastIndexOf("\\/", domainUrl.length() - 1) != -1) {
      return domainUrl + url + getDataId() + "&taskId=" + getTaskId();
    } else {
      return domainUrl + "/" + url + getDataId() + "&taskId=" + getTaskId();
    }
  }

  public void setDomainUrl(String domainUrl) {
    this.domainUrl = domainUrl;
  }

  public Date getDealTime() {
    if( dealTime == null ){
      return new Date() ;
    }
    return dealTime;
  }

  public void setDealTime(Date dealTime) {
    this.dealTime = dealTime;
  }

}
