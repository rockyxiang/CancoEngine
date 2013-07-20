package com.canco.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.canco.bean.CancoEngineInner;

/**
 * 待办，已办service
 * User: rocky
 * Date: 13-7-9
 * Time: 下午9:39
 *
 */
@Service
public interface CancoEngineTaskService {
  
    public void createDoings(List<CancoEngineInner> cancoEngineInners);

    public void createDoing(CancoEngineInner cancoEngineInner) ;

    public void doing2done(CancoEngineInner cancoEngineInner);

    public void createDrafter(CancoEngineInner cancoEngineInner);

    public void drafter2Create(CancoEngineInner cancoEngineInner);

}
