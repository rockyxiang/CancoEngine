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
		assertEquals(false, CancoEngineExpressionCodition.expressionResult("test>1", expressionParam));
	}
	
}
