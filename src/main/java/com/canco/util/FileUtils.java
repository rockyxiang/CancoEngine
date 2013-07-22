package com.canco.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.canco.config.CancoEngineConfig;

/**
 * WEB文件下载
 * @Title: FileUtils.java
 * @Package com.donfer.util
 * @Description: 文件下载相关处理
 * @author Rocky
 * @mail rocky.chen@donfer.com.cn
 * @date 2011-3-18 上午09:39:23
 * @version v1.0
 */
public class FileUtils {
	
	private static Logger logger = LoggerFactory.getLogger(FileUtils.class) ;
	
	private static final CancoEngineConfig fileConfig = (CancoEngineConfig)SpringUtils.getBean("cancoEngineConfig");
	
	/**
	 * 设置下载头部信息
	 * 
	 * @param response HttpServletResponse响应 
	 * @param exportFileName 输出的文件名称，如：a.xls
	 * @param headerType 设置http头部下载类型
	 * @throws UnsupportedEncodingException 文件转码异常 
	 * 
	 */
	public static void setResponseHeader(HttpServletResponse response,
			String headerType, String exportFileName) throws UnsupportedEncodingException {
		if(StringUtils.isBlank(headerType)){
			headerType = HtmlHeadConstants.HTML_HEAD_HTML_HTML;
		}
		logger.debug("this is filename:{}" , exportFileName);
		response.setContentType(headerType);
		response.setHeader("Content-Disposition", "attachment;filename=\""+new String(exportFileName.getBytes("gbk"),"iso-8859-1")+"\"");
		response.setHeader("Cache-Control", "must-revalidate,post-check=0,pre-check=0");
		response.setHeader("Pragma", "public");
		response.setDateHeader("Expires", (System.currentTimeMillis()+1000));
	}
	
	public static void download(HttpServletResponse response , InputStream inputStream ) throws IOException{
		OutputStream out = response.getOutputStream();
		IOUtils.copy(inputStream, out);
		out.flush();
		out.close();
	}
	
	/**
     * 根据文件名称获取文件后缀,如.zip
     * @param fileName
     * @return
     */
    public static String getSuffixFromFileName(String fileName){
    	if(StringUtils.isEmpty(fileName)  || fileName.indexOf(".")==-1){
    		return null;
    	}
    	String[] parts=fileName.split("\\.");
    	return parts[parts.length-1];
    }
	
}
