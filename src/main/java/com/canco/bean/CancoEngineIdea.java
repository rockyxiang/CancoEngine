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
     * 意见流水号
     */
    private String ideaNo ;
    
    /**
     * 行办会议流水号
     */
    private String bankMessageNo ;
    
    /**
     * 获取所有拼接后的意见
     */
    public String getAllMsg(){
      return ideaCondition + "~(WSM)~" + ideaContent ;
    }
    
    public String getIdeaNo() {
        return ideaNo;
    }

    public void setIdeaNo(String ideaNo) {
        this.ideaNo = ideaNo;
    }

    public String getBankMessageNo() {
        return bankMessageNo;
    }

    public void setBankMessageNo(String bankMessageNo) {
        this.bankMessageNo = bankMessageNo;
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
