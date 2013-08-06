package com.canco.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public final class CancoEngineParse {
  
  public static enum PARSE_ENUM{
    TASK_KEY, URL;
    
    public String getValue(){
      switch (this) {
	      case TASK_KEY:
	        return "taskKey";
	      case URL:
	        return "url";
      }
      return "";
    }
  }
  
  public static enum PARSE_INNER{
	  TASK_INFO("taskInfo") ,TYPE("type") ,IS_JUDGE_CONDITION("isJudgeCondition") ,IS_SELECTED("isSelected") 
	  ,IS_MULTIPLE("isMultiple") ,EXPRESSIONS("expressions") ,USERS("users"),ROLE_ID("roleId");
	  
	  private String value ;
	  
	  private PARSE_INNER(String value){
		  this.value = value;
	  }
	  
	  public String toString(){
		  return value ;
	  }
  }
  
  public static String parseConfig( final String config ,PARSE_ENUM parseEnum) {
    JSONObject json = JSON.parseObject(config);
    return  json.getString(parseEnum.getValue()) ;
  }
  
  public static String list2Json(List<?> paserList) {
	return JSON.toJSONString(paserList, true);
  }
  
  public static String map2Json(Map<String,Object> parseMap) {
	  return JSON.toJSONString(parseMap);
  }
  
  public static Map<String,Object> parseTaskInfo(final String config){
	JSONObject json = JSON.parseObject(config);
	JSONObject taskInfo = (JSONObject)json.get(PARSE_INNER.TASK_INFO.toString());
	if(taskInfo != null){
		Map<String,Object> resultMap = new HashMap<String, Object>();
		resultMap.put(PARSE_INNER.TYPE.toString(), taskInfo.getString(PARSE_INNER.TYPE.toString()));
		resultMap.put(PARSE_INNER.IS_JUDGE_CONDITION.toString(), taskInfo.getBoolean(PARSE_INNER.IS_JUDGE_CONDITION.toString()));
		JSONObject users = (JSONObject)taskInfo.get(PARSE_INNER.USERS.toString());
		resultMap.put(PARSE_INNER.IS_SELECTED.toString(), users.getBoolean(PARSE_INNER.IS_SELECTED.toString()));
		resultMap.put(PARSE_INNER.IS_MULTIPLE.toString(), users.getBoolean(PARSE_INNER.IS_MULTIPLE.toString()));
		resultMap.put(PARSE_INNER.EXPRESSIONS.toString(), users.getString(PARSE_INNER.EXPRESSIONS.toString()));
		resultMap.put(PARSE_INNER.ROLE_ID.toString(), users.getString(PARSE_INNER.ROLE_ID.toString()));
		return resultMap ;
	}
	return null ;
  }
  
  public static void main(String[] args) {
    final String str = "{" + "\"taskKey\":\"\",\"url\":\"11111\",\"save\":{\"isdisplay\":false},\"del\":{\"isdisplay\":true},\"flowgz\":{\"isdisplay\":false},\"deal\":{\"require\":false,\"isdisplay\":false},\"taskInfo\":{\"type\":\"0/1(AB角)/2(会签)\",\"isJudgeCondition\":false,\"users\":{\"isSelected\":true,\"roleId\":\"\",\"isMultiple\":false,\"expressions\":\"xxx.xxx('1',deptParam)\"}},\"ideaconfig\":[{\"type\":\"0\",\"selectText\":[{\"content\":\"同意\"},{\"content\":\"不同意\"}]}]"+"}";
    System.out.println(parseTaskInfo(str));
  }

}
