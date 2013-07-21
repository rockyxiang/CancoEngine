package com.canco.bean;

/**
 * 流程意见
 * User: rocky
 * Date: 13-7-7
 * Time: 下午5:07
 */
public class CancoEngineIdea extends CancoEngineRuntime{

    /**
     * 处理意见
     */
    private String ideaContent ;

    /**
     * 结论性意见
     */
    private String ideaCondition ;
    
    /**
     * 获取所有拼接后的意见
     */
    public String getAllMsg(){
      return "\"ideaContent\":\""+ ideaContent + "\"," +
      		 "\"ideaCondition\":\""+ideaCondition+"\"," +
      		 "\"userName\":\""+getUserName()+"\"," +
      		 "\"deptId\":\""+getDeptId()+"\","+
      		 "\"deptName\":\""+getDeptName()+"\"";
    }
    
    public String getIdeaContent() {
        return ideaContent;
    }

    public void setIdeaContent(String ideaContent) {
        this.ideaContent = ideaContent;
    }

    public String getIdeaCondition() {
        return ideaCondition;
    }

    public void setIdeaCondition(String ideaCondition) {
        this.ideaCondition = ideaCondition;
    }


}
