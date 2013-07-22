package com.canco.test;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestCase;

import org.junit.Test;

import com.canco.util.CancoEngineExpressionCodition;

public class CancoEngineUtilsTest extends TestCase{
	
	@Test
	public void testExpressionResult(){
		Map<String,Object> expressionParam = new HashMap<String, Object>();
		expressionParam.put("test", new Integer(0));
		assertEquals(false, CancoEngineExpressionCodition.expressionResult("test>1 || test<2", expressionParam));
	}
	
	@Test
	public void testJsonParse(){
		final String str = "{" + "\"taskKey\":\"\",\"url\":\"11111\",\"save\":{\"isdisplay\":false},\"del\":{\"isdisplay\":true},\"flowgz\":{\"isdisplay\":false},\"deal\":{\"require\":false,\"isdisplay\":false},\"taskInfo\":{\"type\":\"0/1(AB角)/2(会签)\",\"isJudgeCondition\":false,\"users\":{\"isSelected\":true,\"isMultiple\":false,\"expressions\":\"xxx.xxx('1',deptParam)\"},\"ideaconfig\":[{\"type\":\"0\",\"selectText\":[{\"content\":\"同意\"},{\"content\":\"不同意\"}]}]}"+"}";
		
	}
	
}
