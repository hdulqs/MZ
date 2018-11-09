package com.fh.util.date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
   
public class DateUtil {
     
    public static final String PATTERN_STANDARD08W = "yyyyMMdd";
    public static final String PATTERN_STANDARD12W = "yyyyMMddHHmm";
    public static final String PATTERN_STANDARD14W = "yyyyMMddHHmmss";
    public static final String PATTERN_STANDARD17W = "yyyyMMddHHmmssSSS";
     
    public static final String PATTERN_STANDARD10H = "yyyy-MM-dd";
    public static final String PATTERN_STANDARD16H = "yyyy-MM-dd HH:mm";
    public static final String PATTERN_STANDARD19H = "yyyy-MM-dd HH:mm:ss";
     
    public static final String PATTERN_STANDARD10X = "yyyy/MM/dd";
    public static final String PATTERN_STANDARD16X = "yyyy/MM/dd HH:mm";
    public static final String PATTERN_STANDARD19X = "yyyy/MM/dd HH:mm:ss";
 
    /**
     * @Title: date2String
     * @Description: 日期格式的时间转化成字符串格式的时间
     * @author YFB
     * @param date
     * @param pattern
     * @return
     */ 
    public static String date2String(Date date, String pattern) {
        if (date == null) {
            throw new java.lang.IllegalArgumentException("timestamp null illegal");
        }
        pattern = (pattern == null || pattern.equals(""))?PATTERN_STANDARD19H:pattern;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
     
    /**
     * @Title: string2Date
     * @Description: 字符串格式的时间转化成日期格式的时间
     * @author YFB
     * @param strDate
     * @param pattern
     * @return
     */ 
    public static Date string2Date(String strDate, String pattern) {
        if (strDate == null || strDate.equals("")) {
            throw new RuntimeException("strDate is null");
        }
        pattern = (pattern == null || pattern.equals(""))?PATTERN_STANDARD19H:pattern;
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Date date = null;
        try {
            date = sdf.parse(strDate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return date;
    }
     
    /**
     * @Title: getCurrentTime
     * @Description: 取得当前系统时间
     * @author YFB
     * @param format 格式 17位(yyyyMMddHHmmssSSS) (14位:yyyyMMddHHmmss) (12位:yyyyMMddHHmm) (8位:yyyyMMdd)
     * @return
     */ 
    public static String getCurrentTime(String format) {
        SimpleDateFormat formatDate = new SimpleDateFormat(format);
        String date = formatDate.format(new Date());
        return date;
    }
     
    /**
     * @Title: getWantDate
     * @Description: 获取想要的时间格式
     * @author YFB
     * @param dateStr
     * @param wantFormat
     * @return
     */ 
    public static String getWantDate(String dateStr,String wantFormat){
        if(!"".equals(dateStr)&&dateStr!=null){
            String pattern = PATTERN_STANDARD14W;
            int len = dateStr.length();
            switch(len){
                case 8:pattern = PATTERN_STANDARD08W;break;
                case 12:pattern = PATTERN_STANDARD12W;break;
                case 14:pattern = PATTERN_STANDARD14W;break;
                case 17:pattern = PATTERN_STANDARD17W;break;
                case 10:pattern = (dateStr.contains("-"))?PATTERN_STANDARD10H:PATTERN_STANDARD10X;break;
                case 16:pattern = (dateStr.contains("-"))?PATTERN_STANDARD16H:PATTERN_STANDARD16X;break;
                case 19:pattern = (dateStr.contains("-"))?PATTERN_STANDARD19H:PATTERN_STANDARD19X;break;
                default:pattern = PATTERN_STANDARD14W;break;
            }
            SimpleDateFormat sdf = new SimpleDateFormat(wantFormat);
            try {
                SimpleDateFormat sdfStr = new SimpleDateFormat(pattern);
                Date date = sdfStr.parse(dateStr);
                dateStr = sdf.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dateStr;
    }
     
    /**
     * @Title: getAfterTime
     * @Description: 获取该时间的几分钟之后的时间
     * @author YFB
     * @param dateStr
     * @param minute
     * @return
     */ 
    public static String getAfterTime(String dateStr,int minute){
        String returnStr = "";
        try {
            String pattern = PATTERN_STANDARD14W;
            int len = dateStr.length();
            switch(len){
                case 8:pattern = PATTERN_STANDARD08W;break;
                case 10:pattern = PATTERN_STANDARD10H;break;
                case 12:pattern = PATTERN_STANDARD12W;break;
                case 14:pattern = PATTERN_STANDARD14W;break;
                case 16:pattern = PATTERN_STANDARD16H;break;
                case 17:pattern = PATTERN_STANDARD17W;break;
                case 19:pattern = PATTERN_STANDARD19H;break;
                default:pattern = PATTERN_STANDARD14W;break;
            }
            SimpleDateFormat formatDate = new SimpleDateFormat(pattern);
            Date date = null;
            date = formatDate.parse(dateStr);
            Date afterDate = new Date(date.getTime()+(60000*minute));
            returnStr = formatDate.format(afterDate);
        } catch (Exception e) {
            returnStr = dateStr;
            e.printStackTrace();
        }
        return returnStr;
    }
     
    /**
     * @Title: getBeforeTime
     * @Description: 获取该时间的几分钟之前的时间
     * @author YFB
     * @param dateStr
     * @param minute
     * @return
     */ 
    public static String getBeforeTime(String dateStr,int minute){
        String returnStr = "";
        try {
            String pattern = PATTERN_STANDARD14W;
            int len = dateStr.length();
            switch(len){
                case 8:pattern = PATTERN_STANDARD08W;break;
                case 10:pattern = PATTERN_STANDARD10H;break;
                case 12:pattern = PATTERN_STANDARD12W;break;
                case 14:pattern = PATTERN_STANDARD14W;break;
                case 16:pattern = PATTERN_STANDARD16H;break;
                case 17:pattern = PATTERN_STANDARD17W;break;
                case 19:pattern = PATTERN_STANDARD19H;break;
                default:pattern = PATTERN_STANDARD14W;break;
            }
            SimpleDateFormat formatDate = new SimpleDateFormat(pattern);
            Date date = null;
            date = formatDate.parse(dateStr);
            Date afterDate = new Date(date.getTime()-(60000*minute));
            returnStr = formatDate.format(afterDate);
        } catch (Exception e) {
            returnStr = dateStr;
            e.printStackTrace();
        }
        return returnStr;
    }
     
    public static void main(String[] args){
        System.out.println(DateUtil.getWantDate("2011-01-01 23:59:23", "yyyyMMdd"));
    }
    
	/**
	 * 时间
	 * @return
	 */
	public  static String getDate(){
		String time="";
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY); //时
		int minute = c.get(Calendar.MINUTE);//分
		switch (hour) {
		case 0:
			if ((minute >=0 && minute < 30)) {
				time="0030";
			} else {
				time="0100";
			}
			break;
		case 1:
			if ((minute >=0 && minute < 30)) {
				time="0130";
			} else {
				time="0200";
			}
			break;
		case 2:
			if ((minute >=0 && minute < 30)) {
				time="02:30";
			} else {
				time="03:00";
			}
			break;
		case 3:
			if ((minute >=0 && minute < 30)) {
				time="03:30";
			} else {
				time="04:00";
			}
			break;
		case 4:
			if ((minute >=0 && minute < 30)) {
				time="04:30";
			} else {
				time="05:00";
			}
			break;
		case 5:
			if ((minute >=0 && minute < 30)) {
				time="05:30";
			} else {
				time="06:00";
			}
			break;
		case 6:
			if ((minute >=0 && minute < 30)) {
				time="06:30";
			} else {
				time="07:00";
			}
			break;
		case 7:
			if ((minute >=0 && minute < 30)) {
				time="07:30";
			} else {
				time="08:00";
			}
			break;
		case 8:
			if ((minute >=0 && minute < 30)) {
				time="08:30";
			} else {
				time="09:00";
			}
			break;
		case 9:
			if ((minute >=0 && minute < 30)) {
				time="09:30";
			} else {
				time="10:00";
			}
			break;
		case 10:
			if ((minute >=0 && minute < 30)) {
				time="10:30";
			} else {
				time="11:00";
			}
			break;
		case 11:
			if ((minute >=0 && minute < 30)) {
				time="11:30";
			} else {
				time="12:00";
			}
			break;
		case 12:
			if ((minute >=0 && minute < 30)) {
				time="12:30";
			} else {
				time="13:00";
			}
			break;
		case 13:
			if ((minute >=0 && minute < 30)) {
				time="13:30";
			} else {
				time="14:00";
			}
			break;
		case 14:
			if ((minute >=0 && minute < 30)) {
				time="14:30";
			} else {
				time="15:00";
			}
			break;
		case 15:
			if ((minute >=0 && minute < 30)) {
				time="15:30";
			} else {
				time="16:00";
			}
			break;
		case 16:
			if ((minute >=0 && minute < 30)) {
				time="16:30";
			} else {
				time="17:00";
			}
			break;
		case 17:
			if ((minute >=0 && minute < 30)) {
				time="17:30";
			} else {
				time="18:00";
			}
			break;
		case 18:
			if ((minute >=0 && minute < 30)) {
				time="18:30";
			} else {
				time="19:00";
			}
			break;
		case 19:
			if ((minute >=0 && minute < 30)) {
				time="19:30";
			} else {
				time="20:00";
			}
			break;
		case 20:
			if ((minute >=0 && minute < 30)) {
				time="20:30";
			} else {
				time="21:00";
			}
			break;
		case 21:
			if ((minute >=0 && minute < 30)) {
				time="21:30";
			} else {
				time="22:00";
			}
			break;
		case 22:
			if ((minute >=0 && minute < 30)) {
				time="22:30";
			} else {
				time="23:00";
			}
			break;
		case 23:
			if ((minute >=0 && minute < 30)) {
				time="23:30";
			} else {
				time="00:00";
			}
			break;

		default:
			break;
		}
		return time;
	}
}