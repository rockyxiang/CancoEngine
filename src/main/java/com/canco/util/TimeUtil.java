package com.canco.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class TimeUtil {
       /**
	     * 内置时间变量
	     */
	    private Calendar calendar = GregorianCalendar.getInstance();
	    
	    /**
	     * 当前时间
	     */
	    public TimeUtil() {
	        setTime(new java.util.Date());
	    }
	    /**
	     * 指定时间
	     * @param date
	     */
	    public TimeUtil(java.util.Date date) {
	        setTime(date);
	    }

	    /** *//**
	     * 指定时间
	     * @param timestamp
	     */
	    public TimeUtil(java.sql.Timestamp timestamp) {
	        setTimestamp(timestamp);
	    }
	    
	    /**
	     * 指定　年、月、日
	     * @param year　年：自然年份，如2008
	     * @param month 月：1～12，自然月份，如12月
	     * @param date 日：1～31，如1日
	     */
	    public TimeUtil(int year, int month, int date) {
	        this(year, month, date, 0, 0, 0);
	    }

	    /**
	     * 指定　年、月、日、时、分、秒
	     * @param year 年：自然年份，如2008
	     * @param month 月：1～12，自然月份，如12月
	     * @param date 日：1～31，如1日
	     * @param hrs 时：0～23，如0时
	     * @param min 分：0～59，如59分
	     * @param sec 秒：0～59，如59秒
	     */
	    public TimeUtil(int year, int month, int date, int hrs, int min, int sec) {
	        setTime(year, month, date, hrs, min, sec);
	    }

	    /**
	     * 指定　年、月、日、时、分、秒
	     * @param year 年：自然年份，如2008
	     * @param month 月：1～12，自然月份，如12月
	     * @param date 日：1～31，如1日
	     * @param hrs 时：0～23，如0时
	     * @param min 分：0～59，如59分
	     * @param sec 秒：0～59，如59秒
	     * @param millsec 毫秒：0～9999，如10毫秒
	     */
	    public TimeUtil(int year, int month, int date, int hrs, int min, int sec, int millsec) {
	        setTime(year, month, date, hrs, min, sec, millsec);
	    }  
	    /**
	     * 取年份
	     * @return
	     */
	    public int getYear() {
	        return calendar.get(Calendar.YEAR);
	    }

	    /**
	     * 设置年份
	     * @param year 年份
	     */
	    public void setYear(int year) {
	        calendar.set(Calendar.YEAR, year);
	    }

	    /**
	     * 取月份 1～12，自然月
	     * @return
	     */
	    public int getMonth() {
	        return calendar.get(Calendar.MONTH) + 1; // 月份要加1，因为java是从0~11
	    }

	    /**
	     * 设置月份 1～12，自然月
	     * @param month 自然月 1～12
	     */
	    public void setMonth(int month) {
	        calendar.set(Calendar.MONTH, month - 1); // 月份要-1，因为java是从0~11
	    }

	    /**
	     * 取月内天数 1～31
	     * @return
	     */
	    public int getDate() {
	        return calendar.get(Calendar.DATE);
	    }

	    /**
	     * 设置月内天数 1～31
	     * @param date 天数 1～31
	     */
	    public void setDate(int date) {
	        calendar.set(Calendar.DATE, date);
	    }

	    /**
	     * 取小时 0～23
	     * @return
	     */
	    public int getHour() {
	        return calendar.get(Calendar.HOUR_OF_DAY);
	    }

	    /**
	     * 设置小时 0～23
	     * @param hour 小时 0～23
	     */
	    public void setHour(int hour) {
	        calendar.set(Calendar.HOUR_OF_DAY, hour);
	    }

	    /**
	     * 取分钟 0～59
	     * @return
	     */
	    public int getMinute() {
	        return calendar.get(Calendar.MINUTE);
	    }

	    /** 
	     * 设置分钟 0～59
	     * @param minute 分钟 0～59
	     */
	    public void setMinute(int minute) {
	        calendar.set(Calendar.MINUTE, minute);
	    }

	    /** 
	     * 取秒 0～59
	     * @return
	     */
	    public int getSecond() {
	        return calendar.get(Calendar.SECOND);
	    }

	    /** 
	     * 设置秒 0～59
	     * @param second 秒 0～59
	     */
	    public void setSecond(int second) {
	        calendar.set(Calendar.SECOND, second);
	    }

	    /** 
	     * 取毫秒 0～9999
	     * @return
	     */
	    public int getMilliSecond() {
	        return calendar.get(Calendar.MILLISECOND);
	    }

	    /** 
	     * 设置毫秒
	     * @param millisecond 毫秒 0～9999
	     */
	    public void setMilliSecond(int millisecond) {
	        calendar.set(Calendar.MILLISECOND, millisecond);
	    }
	   


	    /**
	     * 增加时间 如加1天 add(TimeUtil.DATE, 1)
	     * @param field 字段：YEAR, MONTH, DATE=DATE_OF_MONTH, HOUR_OF_DAY, MINUTE, SECOND, MILLISECOND
	     * @param amount 增加量
	     *
	     * @see #YEAR
	     * @see #MONTH
	     * @see #DATE
	     * @see #DAY_OF_MONTH
	     * @see #HOUR_OF_DAY
	     * @see #MINUTE
	     * @see #SECOND
	     * @see #MILLISECOND
	     */
	    public void add(int field, int amount) {
	        calendar.add(field, amount);
	    }

	    /** 
	     * 保留年月日，清除时间部分
	     */
	    public void clearTime() {
	        calendar.clear(Calendar.HOUR_OF_DAY);
	        calendar.clear(Calendar.MINUTE);
	        calendar.clear(Calendar.SECOND);
	        calendar.clear(Calendar.MILLISECOND);
	    }    

	    /** 
	     * 指定　年、月、日
	     * @param year 年：自然年份，如2008
	     * @param month 月：1～12，自然月份，如12月
	     * @param date 日：1～31，如1日
	     */    
	    public void setTime(int year, int month, int date) {
	        setTime(year, month, date, 0, 0, 0);
	    }

	    /**
	     * 指定　年、月、日、时、分、秒
	     * @param year 年：自然年份，如2008
	     * @param month 月：1～12，自然月份，如12月
	     * @param date 日：1～31，如1日
	     * @param hrs 时：0～23，如0时
	     * @param min 分：0～59，如59分
	     * @param sec 秒：0～59，如59秒
	     */    
	    public void setTime(int year, int month, int date, int hrs, int min, int sec) {
	        calendar.clear();
	        month = month - 1;// 月份要减1，因为java是从0~11
	        calendar.set(year, month, date, hrs, min, sec);
	    }

	    /**
	     * 指定　年、月、日、时、分、秒、毫秒
	     * @param year 年：自然年份，如2008
	     * @param month 月：1～12，自然月份，如12月
	     * @param date 日：1～31，如1日
	     * @param hrs 时：0～23，如0时
	     * @param min 分：0～59，如59分
	     * @param sec 秒：0～59，如59秒
	     * @param millsec 毫秒：0～9999，如10毫秒
	     */    
	    public void setTime(int year, int month, int date, int hrs, int min, int sec, int millsec) {
	        calendar.clear();
	        month = month - 1;// 月份要减1
	        calendar.set(year, month, date, hrs, min, sec);
	        calendar.set(Calendar.MILLISECOND, millsec);
	    }

	    /**
	     * 指定时间
	     * @param date
	     */
	    public void setTime(java.util.Date date) {
	        calendar.setTime(date);
	    }

	    /**
	     * 指定时间
	     * @param timestamp
	     */
	    public void setTimestamp(java.sql.Timestamp timestamp) {
	        calendar.setTimeInMillis(timestamp.getTime());
	    }
	    
	    /**
	     * 获取 java.sql.Date
	     * @return
	     */
	    public java.sql.Date sqldateValue() {
	        return new java.sql.Date(calendar.getTime().getTime());
	    }
	    
	    /**
	     * 获取 java.sql.Time
	     * @return
	     */
	    public java.sql.Time sqltimeValue() {
	        return new java.sql.Time(calendar.getTime().getTime());
	    }
	    
	    /**
	     * 获取 java.sql.Timestamp
	     * @return
	     */
	    public java.sql.Timestamp timestampValue(){
	        return new java.sql.Timestamp(calendar.getTime().getTime());
	    }

    /**
     * 得到指定日期的当月的最后一天日期
     *
     * @param date
     * @return
     */
    public static final int getLastDayOfMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.roll(Calendar.DAY_OF_MONTH, -1);
        return cal.get(Calendar.DAY_OF_MONTH);
    }
 
	     /**
	     * 年[字段常量]，用于 add函数.
	      * 
	     * @see #MONTH
	     * @see #DATE
	     * @see #DAY_OF_MONTH
	     * @see #HOUR_OF_DAY
	     * @see #MINUTE
	     * @see #SECOND
	     * @see #MILLISECOND
	     * @see #add(int field, int amount)
	     */
	    public final static int YEAR = 1;

	    /**
	     * 月份[字段常量]，用于 add函数.
	     * 
	     * @see #YEAR
	     * @see #DATE
	     * @see #DAY_OF_MONTH
	     * @see #HOUR_OF_DAY
	     * @see #MINUTE
	     * @see #SECOND
	     * @see #MILLISECOND
	     * @see #add(int field, int amount)
	     */
	    public final static int MONTH = 2;

	    /**
	     * 天[字段常量]，用于 add函数. 与<code>DATE_OF_MONTH</code>等同
	     * 
	     * @see #YEAR
	     * @see #MONTH
	     * @see #DAY_OF_MONTH
	     * @see #HOUR_OF_DAY
	     * @see #MINUTE
	     * @see #SECOND
	     * @see #MILLISECOND
	     * @see #add(int field, int amount)
	     */
	    public final static int DATE = 5;

	    /**
	     * 天[字段常量]，用于 add函数 与<code>DATE</code>等同.
	     * @see #YEAR
	     * @see #MONTH
	     * @see #DAY_OF_MONTH
	     * @see #HOUR_OF_DAY
	     * @see #MINUTE
	     * @see #SECOND
	     * @see #MILLISECOND
	     * @see #add(int field, int amount)
	     */
	    public final static int DAY_OF_MONTH = 5;

	    /**
	     * 小时[字段常量]，0~23，用于 add函数
	     * 
	     * @see #YEAR
	     * @see #MONTH
	     * @see #DATE
	     * @see #HOUR_OF_DAY
	     * @see #MINUTE
	     * @see #SECOND
	     * @see #MILLISECOND
	     * @see #add(int field, int amount)
	     */
	    public final static int HOUR_OF_DAY = 11;

	    /**
	     * 分钟[字段常量]，用于 add函数
	     * 
	     * @see #YEAR
	     * @see #MONTH
	     * @see #DATE
	     * @see #DAY_OF_MONTH
	     * @see #HOUR_OF_DAY
	     * @see #SECOND
	     * @see #MILLISECOND
	     * @see #add(int field, int amount)
	     */
	    public final static int MINUTE = 12;

	    /**
	     * 秒[字段常量]，用于 add函数
	     * 
	     * @see #YEAR
	     * @see #MONTH
	     * @see #DATE
	     * @see #DAY_OF_MONTH
	     * @see #HOUR_OF_DAY
	     * @see #MINUTE
	     * @see #MILLISECOND
	     * @see #add(int field, int amount)
	     */
	    public final static int SECOND = 13;

	    /**
	     * 微秒[字段常量]，用于 add函数
	     * 
	     * @see #YEAR
	     * @see #MONTH
	     * @see #DATE
	     * @see #DAY_OF_MONTH
	     * @see #HOUR_OF_DAY
	     * @see #MINUTE
	     * @see #SECOND
	     * @see #add(int field, int amount)
	     */
	    public final static int MILLISECOND = 14;	
	    
	    /**
	     * 根据传入的Calendar获取周一的日期
	     * 
	     * @param calendar
	     * @return String
	     * @author: polly.chen
	     * @time :2011-4-21 下午12:54:59
	     */
	    public static String getFirstDayOfWeek(Calendar calendar){
	    	calendar = calendar==null?GregorianCalendar.getInstance():calendar;
	    	int curDay =  calendar.get(calendar.DAY_OF_WEEK);
	    	if(curDay==1){
	    		calendar.add(calendar.DATE, -6);
	    	}else
	    		calendar.add(calendar.DATE, 2-curDay);
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        return format.format(calendar.getTime());
	    }
	    
	    /**
	     * 获取当前的星期
	     * @return 大写的星期：比如 星期一，星期二
	     */
	    public static String getCurrentWeek(){
			Calendar calendar=GregorianCalendar.getInstance();
			String currentWeek="";
			switch (calendar.get(calendar.DAY_OF_WEEK)) {
				case 1:
					currentWeek="星期日";
					break;
				case 2:
					currentWeek="星期一";
					break;
				case 3:
					currentWeek="星期二";
					break;
				case 4:
					currentWeek="星期三";
					break;
				case 5:
					currentWeek="星期四";
					break;
				case 6:
					currentWeek="星期五";
					break;
				case 7:
					currentWeek="星期六";
					break;
				default:
					break;
			}
	    	return currentWeek;
	    }
	   
	    /**
	     * 根据传入的Calendar获取周日的日期
	     * 
	     * @param calendar
	     * @return String
	     * @author: polly.chen
	     * @time :2011-4-21 下午12:54:45
	     */
	    public static String getLastDayWeek(Calendar calendar){
	    	calendar = calendar==null?GregorianCalendar.getInstance():calendar;
	    	int curDay =  calendar.get(calendar.DAY_OF_WEEK);
	    	if(curDay!=1){
	    		calendar.add(calendar.DATE, 8-curDay);
	    	}
	    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	        return format.format(calendar.getTime());
	    }
	    
	    /**
	     * 获取字符串
	     * @param partten 格式
	     * @return
	     */
	    public String stringValue(String partten) {
	        SimpleDateFormat format = new SimpleDateFormat(partten);
	        return format.format(calendar.getTime());
	    }
	    
	    /**
	     * 获取 java.util.Date
	     * @return
	     */
	    public static java.util.Date dateValue(String datetime) {
	    	if(datetime==null || datetime.equals(""))
	    		return null;
	    	DateFormat df = DateFormat.getDateInstance();
	    	try {
	    		return  df.parse(datetime);
			} catch (ParseException e) {				
				e.printStackTrace();
				return null;
			}	        
	    }	         
		
		
	    /**
	     * 根据传入的天数，对当前时间进行加或减
	     * @param day 正数或者负数，用于到当前时间的加或减
	     * @return Calendar
	     * @author: polly.chen
	     * @time :2011-4-22 上午08:38:12 
	     */
	    public static Calendar getCalendar(int day){
	    	Calendar calendar = GregorianCalendar.getInstance() ;
	    	calendar.add(calendar.DATE, day);
	    	return calendar ;
	    }
	    
	    /**
	     * @Description: 获取当前的年份
	     * @return
	     * @throws ParseException
	     * @author Ban
	     * @mail   evan.ban@donfer.com.cn
	     * @date 2011-5-23 下午03:56:42
	     */
		public static String getTempYear(){
			Calendar calendar=Calendar.getInstance();
			int year=Calendar.getInstance().get(calendar.YEAR);
			return String.valueOf(year);
		}
		
		/**
		 * 获取当前的月份
		 * @return
		 */
		public static String getTempMonth(){
	    	Calendar cal = Calendar.getInstance();
	        int month = cal.get(Calendar.MONTH) + 1;
	        String returnMonth = String.valueOf(month);
	        return returnMonth.length() > 1 ? returnMonth : "0"+returnMonth;
		}
    /**
     * 得到当前时间的在月中的日期
     *
     * @return int year
     */
    public static final int getCurrentDayOfMonth() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }
		/**
		 * @Description: TODO
		 * @param resouce 给定的时间字符串
		 * @param format 给定的解析的格式
		 * @return 解析之后的dat额对象 如果解析出错则返回当前时间 new Date();
		 * @author Polly
		 * @mail   polly@donfer.com.cn
		 * @date 2011-7-25 下午08:39:08
		 */
		public static Date parserStringToDate(String resouce,String format){
			if("".equals(format)||format==null){
				format = "yyyy-MM-dd" ;
			}
			SimpleDateFormat sdf = new SimpleDateFormat (format);
			try {
				return sdf.parse(resouce) ;
			} catch (ParseException e) {
				e.printStackTrace();
				return new Date();
			}
		}
	
		
		/**
		 * @Description: TODO
		 * @param resouce 给定的时间字符串
		 * @return 解析之后的dat额对象 如果解析出错则返回当前时间 new Date();
		 * @author Polly
		 * @mail   polly@donfer.com.cn
		 * @date 2011-7-25 下午08:39:08
		 */
		public static Date parserStringToDate(String resouce){
			return parserStringToDate(resouce, null);
		}
		
		
		
		/**
		 * @Description: 把时间按照format 格式，格式化
		 * @param date 时间对象
		 * @param format  格式化字符串
		 * @return
		 * @author Polly
		 * @mail   polly@donfer.com.cn
		 * @date 2011-7-26 上午12:12:14
		 */
		public static String dateToString(Date date ,String format){
			SimpleDateFormat sdf = new SimpleDateFormat (format);
			return sdf.format(date);
		}
		/**
		 * @Description: 把当前时间格式化为yyyy-MM-dd HH:mm:ss格式
		 * @return String yyyy-MM-dd HH:mm:ss
		 * @author Polly
		 * @mail   polly@donfer.com.cn
		 * @date 2011-7-26 上午12:12:14
		 */
		public static String dateToString(){
			SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
			return sdf.format(new Date());
		}
		/**
		 * @Description: 获取当前的时间段（上午/下午）
		 * @return
		 * @author Ban
		 * @mail   evan.ban@donfer.com.cn
		 * @date 2011-7-13 下午02:08:21
		 */
		public static String noon(){
			GregorianCalendar timeRange=new GregorianCalendar();
			return timeRange.get(GregorianCalendar.AM_PM)==0?"上午":"下午";
		}
		/**
         * @Description: 把时间按照format 格式，格式化
         * @param date 时间对象
         * @return
         * @author Polly
         * @mail   polly@donfer.com.cn
         * @date 2011-7-26 上午12:12:14
         */
        public static String dateToString(Date date ){
        	if(date==null){
        		return null;
        	}
            SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
            return sdf.format(date);
        }
        
        /**
         * 将制定的时间对象转化为yyyy-MM-dd形式的字符串
         * @param date
         * @return
         */
        public static String dateToYMD(Date date ){
        	if(date==null){
        		return null;
        	}
        	SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");
        	return sdf.format(date);
        }
        
        /**
         * 将指定的时间转化为 ****年**月**日的格式
         * @param date
         * @return
         */
        public static String dateToYMDCN(Date date ){
        	SimpleDateFormat sdf = new SimpleDateFormat ("yyyy年MM月dd日");
        	return sdf.format(date);
        }
        
        /**
         * 将当前的时间对象转化为yyyy-MM-dd形式的字符串
         * @return
         */
        public static String dateToYMD(){
        	return dateToYMD(new Date());
        }
        
        /**
         * 将当前的时间转化为 ****年**月**日的格式
         * @param date
         * @return
         */
        public static String dateToYMDCN(){
        	return dateToYMDCN(new Date());
        }
        /**
         * 制作六位长度的日期字符
         * @Description: 六位长度的字符：110929
         * @return
         * @author Aspenn
         * @mail   aspenn.cai@donfer.com.cn
         * @date Sep 29, 2011 10:47:28 AM
         */
        public static String getsixcharformatdate(){
        	String y = TimeUtil.getTempYear().substring(2);
        	String m=new TimeUtil().getMonth()+"",d=new TimeUtil().getDate()+"";
        	int im = new TimeUtil().getMonth(),id=new TimeUtil().getDate();
    		if(new TimeUtil().getMonth()<10){
    			m = "0"+im;
    		}
    		if(new TimeUtil().getDate()<10){
    			d = "0"+id;
    		}
        	return y+m+d;
        }

        public static int getWeekOfYear(){
            GregorianCalendar calendar=new GregorianCalendar();
            return calendar.get(Calendar.WEEK_OF_YEAR);
        }
        
        
        /**
         * 获取指定日期的所在季度的第一个月
         * @param compareDate
         * @return
         */
        public static String getYearAndMonthByQuarterNow(String compareDate){
        	Calendar cal = Calendar.getInstance();
        	if(compareDate!=null){
            	Date date=dateValue(compareDate);
            	cal.setTime(date);
        	}
        	
            int month = cal.get(Calendar.MONTH) + 1;//基准月
            int year = cal.get(Calendar.YEAR);//基准年
            int q	=	(month-1)/3+1;//基准季度
            
            int a=year*4+q;//季度总数
            int newy=a/4;//年
            int newQ=a%4;//季度
            if(newQ==0){
            	newQ=4;
            	newy--;
            }
            int newM=newQ*3-2;
            String newD=newy+"-"+(newM<10?("0"+newM):newM);
        	return newD;
        }
        
        /**
         * 取得当前日期的往后m个季度的最后一个月的月份
         * 例如当前是2012-02，则取到3个季度后的月份为2012-12
         * @param m
         * @return
         */
        public static String getYearAndMonthByQuarter(int m,String compareDate){
        	Calendar cal = Calendar.getInstance();
        	if(compareDate!=null){
            	Date date=dateValue(compareDate);
            	cal.setTime(date);
        	}
        	
            int month = cal.get(Calendar.MONTH) + 1;//基准月
            int year = cal.get(Calendar.YEAR);//基准年
            int q	=	(month-1)/3+1;//基准季度
            
            int a=year*4+q+m;//季度总数
            int newy=a/4;//年
            int newQ=a%4;//季度
            if(newQ==0){
            	newQ=4;
            	newy--;
            }
            int newM=newQ*3;
            String newD=newy+"-"+(newM<10?("0"+newM):newM);
        	return newD;
        }
        
        
    	
    	/**
    	 * 获取当前季度
    	 * @return
    	 */
    	public static int getTempQuarter(){
        	Calendar cal = Calendar.getInstance();
            int month = cal.get(Calendar.MONTH) + 1;//基准月
            int q	=	(month-1)/3+1;//基准季度
            return q;
    	}
    	
    	
    	/**
    	 * 获取当月最后一天的日期
    	 * @return
    	 */
    	public static String getLastDateOfMonth(){
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.roll(Calendar.DAY_OF_MONTH, -1);
            return dateToYMD(cal.getTime());
    	}
    	
    	/**
    	 * 获取指定年指定月的最后一天的日期
    	 * @return
    	 */
    	public static String getLastDateOfMonth(String year,String month){
    		Calendar cal = Calendar.getInstance();  // 不加下面2行，就是取当前时间前一个月的第一天及最后一天
    		cal.set(Calendar.YEAR,Integer.valueOf(year));
    		cal.set(Calendar.MONTH, Integer.valueOf(month));  
    		cal.set(Calendar.DAY_OF_MONTH, 1);  
    		cal.add(Calendar.DAY_OF_MONTH, -1);  
    		Date lastDate = cal.getTime();    
    		return dateToYMD(lastDate);
    	}

	} 