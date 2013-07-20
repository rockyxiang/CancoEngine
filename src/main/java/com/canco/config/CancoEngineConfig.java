package com.canco.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * 读取配置
 */
@Configuration
@PropertySource("classpath:engine/cancoEngineConfig.properties")
public class CancoEngineConfig {

    @Autowired
    private Environment environment ;

    /**
     * 获取URL
     * @return
     */
    public String getCancoDomainUrl(){
        return environment.getProperty("canco.domain.url");
    }
    
    /**
     * 是否合并已办
     */
    public String getIsMegerDone(){
      return environment.getProperty("canco.done.meger");
    }

}
