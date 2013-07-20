package com.canco.test;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.canco.bean.CancoEngineInner;
import com.canco.service.CancoEngineTaskService;

public class CancoEngineTaskServiceTest extends BaseAbstarctTest{
  
  @Autowired
  private CancoEngineTaskService cancoEngineTaskService ;
  
  public CancoEngineInner initCancoEngineInner(){
    CancoEngineInner cancoEngineInner = new CancoEngineInner();
    cancoEngineInner.setBusiType("1");
    cancoEngineInner.setDataId("1111111");
    cancoEngineInner.setDealUserId("1111");
    cancoEngineInner.setDeptId("2222");
    cancoEngineInner.setProcInstanceId("1223");
    cancoEngineInner.setUserId("1");
    cancoEngineInner.setProcInstanceId("1");
    cancoEngineInner.setDeptId("123");
    cancoEngineInner.setUrl("12344532");
    cancoEngineInner.setTaskId("234");
    return cancoEngineInner ;
  }
  
  @Test
  public void testCreateDoing(){
    cancoEngineTaskService.createDoing(initCancoEngineInner());
  }
  
  @Test
  public void testDoing2Done(){
    cancoEngineTaskService.doing2done(initCancoEngineInner());
  }
  
}
