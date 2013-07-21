package com.canco.util;

import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public final class CancoEngineParse {
  
  public static enum PARSE_ENUM{
    TASK_KEY,
    URL;
    
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
  
  public static enum TASK_TYPE{
    
  }
  
  public static String parseConfig( final String config ,PARSE_ENUM parseEnum) {
    JSONObject json = JSON.parseObject(config);
    return  json.getString(parseEnum.getValue()) ;
  }
  
  public static String list2Json(List<?> paserList) {
	return JSON.toJSONString(paserList, true);
  }
  
  public static void main(String[] args) {
    final String str = "{" + "\"taskKey\":\"\",\"url\":\"11111\",\"save\":{\"isdisplay\":false},\"del\":{\"isdisplay\":true},\"flowgz\":{\"isdisplay\":false},\"deal\":{\"require\":false,\"isdisplay\":false},{\"taskInfo\":{\"type\":\"0/1(AB角)/2(会签)\",\"isJudgeCondition\":false,\"users\":{\"isSelected\":true,\"isMultiple\":false,\"expressions\":\"obj.execute('1',deptParam)\"}},\"ideaconfig\":[{\"type\":\"0\",\"selectText\":[{\"content\":\"同意\"},{\"content\":\"不同意\"}]}]}"+"}";
    System.out.println(parseConfig(str , PARSE_ENUM.URL));
  }

}
