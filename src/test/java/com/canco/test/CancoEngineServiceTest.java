package com.canco.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.canco.bean.CancoEngineDeployment;
import com.canco.bean.CancoEngineIdea;
import com.canco.service.CancoEngineService;
import com.canco.service.CancoEngineService.RESOURCE_TYPE;

public class CancoEngineServiceTest extends BaseAbstarctTest{
	
	@Autowired
	private CancoEngineService cancoEngineService ;
	
	private CancoEngineIdea getCancoEngineIdea(){
		CancoEngineIdea cancoEngineIdea = new CancoEngineIdea();
		cancoEngineIdea.setBusiType("bgxhply");
		cancoEngineIdea.setDataId("1111");
		cancoEngineIdea.setUserId("1");
		cancoEngineIdea.setUserName("张三");
		return cancoEngineIdea;
	}
	
	@Test
	public void deployWorkFlowAndSearchDeploymentsTest(){
		try {
			int deploySize = cancoEngineService.searchCancoEngineDeploymentsByBusiType("bgxhply").size();
			InputStream inputStream = new FileInputStream(new File("/Users/rocky/study/CancoEngine/src/main/resources/engine/deployments/bgxhply/bgxhply.zip"));
			cancoEngineService.deployWorkFlow(inputStream);
			Assert.assertEquals(deploySize+1, cancoEngineService.searchCancoEngineDeploymentsByBusiType("bgxhply").size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void pageInitParamTest(){
		System.out.println(cancoEngineService.pageInitParam("", "bgxhply"));
	}
	
	@Test
	public void imageInputStreamTest(){
		OutputStream outputStream;
		try {
			outputStream = new FileOutputStream(new File("/Users/rocky/study/CancoEngine/src/main/resources/engine/deployments/bgxhply/a.png"));
			IOUtils.copy(cancoEngineService.resourceInputStream("928e620a-f472-11e2-9340-28cfe919b5d7", RESOURCE_TYPE.IMAGE),outputStream);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
