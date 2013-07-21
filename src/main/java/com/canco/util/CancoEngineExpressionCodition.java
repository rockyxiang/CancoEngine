package com.canco.util;

import java.util.Map;

import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 * 表达式工具类
 * @author rocky.chen
 */
public final class CancoEngineExpressionCodition {
	
	private static final JexlEngine jexl = new JexlEngine();
	
	/**
	 * 解析表达式并返回结果
	 * @param expression 表达式
	 * @param expressionParam 参数值
	 * @return true/false
	 */
	public static boolean expressionResult(String expression , Map<String,Object> expressionParam){
		Expression expressionCa = jexl.createExpression(expression);
		JexlContext context = new MapContext(expressionParam);
		return (Boolean)expressionCa.evaluate(context);
	}
	
}
