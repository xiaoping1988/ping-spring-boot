package com.mybatis.ping.spring.boot.utils;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Pattern;

/**
 * 日期工具类
 * @author liujiangping
 *
 */
public class DateUtils {
	private static Logger log = LoggerFactory.getLogger(DateUtils.class);

	/**
	 * 获得昨天日期
	 * @param pattern
	 * @return
	 */
	public static String getYesterday(String pattern){
		Calendar   cal   =   Calendar.getInstance();
		cal.add(Calendar.DATE,   -1);
		String yesterday = new SimpleDateFormat( pattern).format(cal.getTime());
		return yesterday;
	}
	/**
	 * 获得当天日期
	 * @param pattern
	 * @return
	 */
	public static String getCurrentday(String pattern){
		Calendar   cal   =   Calendar.getInstance();
		String currentday = new SimpleDateFormat( pattern).format(cal.getTime());
		return currentday;
	}
	/**
	 * 获得上一月月份
	 * @param pattern
	 * @return
	 */
	public static String getLastMonth(String pattern){
		return getLastMonth(getCurrentday("yyyy-MM-dd"),pattern);
	}

	/**
	 * 获取当月月份
	 * @param pattern
	 * @return
	 */
	public static String getMonth(String pattern){
		return getMonth(getCurrentday("yyyy-MM-dd"),pattern);
	}

	/**
	 * 获得某个日期所在月的上一月月份
	 * @param pattern
	 * @return
	 */
	public static String getLastMonth(String date,String pattern){
		Calendar   cal   =   Calendar.getInstance();
		cal.setTime(parseStrToDate(date));
		cal.add(Calendar.MONTH,   -1);
		String lastMonth = new SimpleDateFormat( pattern).format(cal.getTime());
		return lastMonth;
	}

	/**
	 * 获取某个日期所在月的当月月份
	 * @param pattern
	 * @return
	 */
	public static String getMonth(String date,String pattern){
		return formatDate(parseStrToDate(date),pattern);
	}

	/**
	 * 获得N个月之后的日期
	 * @param pattern
	 * @param add 正数时可获得add个月之后的日期，负数时获得add个月之前的日期
	 * @return
	 */
	public static String getAddMonth(String pattern, int add){
		Calendar   cal   =   Calendar.getInstance();
		cal.add(Calendar.MONTH,   add);
		String lastMonth = new SimpleDateFormat( pattern).format(cal.getTime());
		return lastMonth;
	}
	/**
	 * 获得指定月份N月之后或之前的月份
	 * @param month
	 * @param pattern
	 * @param add
	 * @return
	 */
	public static String getAddMonth(String month,String pattern, int add){
		String result = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
			Date date = sdf.parse(month);

			Calendar   cal   =   Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.MONTH,   add);
			result = new SimpleDateFormat( pattern).format(cal.getTime());
		}catch(Exception e){
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return result;
	}
	/**
	 * 获得N天之后或之前的日期,今天日期不算在内
	 * @param pattern
	 * @param add 正数时可获得add天之后的日期，负数时获得add天之前的日期
	 * @return
	 */
	public static String getAddDate(String pattern, int add){
		Calendar   cal   =   Calendar.getInstance();
		cal.add(Calendar.DATE,   add);
		String date = new SimpleDateFormat( pattern).format(cal.getTime());
		return date;
	}
	/**
	 * 获得指定日期N天之后或之前的日期
	 * @param dateString 指定日期，格式 yyyyMMdd
	 * @param pattern
	 * @param add
	 * @return
	 */
	public static String getAddDate(String dateString, String pattern, int add){
		String result = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);  
			Date date = sdf.parse(dateString);

			Calendar   cal   =   Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE,   add);
			result = new SimpleDateFormat( pattern).format(cal.getTime());
		}catch(Exception e){
			log.error(ExceptionUtils.getStackTrace(e));
		}
		
		return result;
	}
	
	/**
	 * 获得指定日期月份的最后一天
	 * @param dateString 指定日期
	 * @param dateStringPattern 指定日期的格式
	 * @param pattern 返回日期的格式
	 * @return
	 */
	public static String getLastDayOfMonth(String dateString, String dateStringPattern, String pattern){
		String result = "";
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(dateStringPattern);  
			Date date = sdf.parse(dateString);

			Calendar   cal   =   Calendar.getInstance();
			cal.setTime(date);
			cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));  
			result = new SimpleDateFormat( pattern).format(cal.getTime());
		}catch(Exception e){
			log.error(ExceptionUtils.getStackTrace(e));
		}
		
		return result;
	}
	/**
	 * 格式化日期
	 * @param date 要格式化的日期
	 * @param pattern 格式
	 * @return
	 */
	public static String formatDate(Date date,String pattern){
		return new SimpleDateFormat(pattern).format(date);
	}
	/**
	 * 将字符串转化为日期
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date parseString(String date,String pattern) throws ParseException{
		return new SimpleDateFormat(pattern).parse(date);
	}

	/**
	 * 得到上一季度 季份   格式 年份+Q1 Q2 Q3 Q4  对应四个季度,比如2017Q1
	 * @return
	 */
	public static String getLastQuarter() {
		return getLastQuarter(getCurrentday("yyyy-MM-dd"));
	}

	/**
	 * 得到季度 季份   格式 年份+Q1 Q2 Q3 Q4  对应四个季度,比如2017Q1
	 * @return
	 */
	public static String getQuarter() {
		return getQuarter(getCurrentday("yyyy-MM-dd"));
	}

	/**
	 * 得到季度 季份   格式 年份+Q1 Q2 Q3 Q4  对应四个季度,比如2017Q1
	 * @return
	 */
	public static String getQuarter(String date) {
		String currentMonth = new SimpleDateFormat("yyyyMM").format(parseStrToDate(date));
		String quarter = "";
		String year = currentMonth.substring(0, 4);
		String month = currentMonth.substring(4);
		int mon = Integer.parseInt(month);
		if(mon>=1&&mon<=3){
			quarter = "Q1";
		}else if(mon>=4&&mon<=6){
			quarter = "Q2";
		}else if(mon>=7&&mon<=9){
			quarter = "Q3";
		}else{//上一季是当年第三季
			quarter = "Q4";
		}
		return year+quarter;
	}

	/**
	 * 得到上一季度 季份   格式 年份+Q1 Q2 Q3 Q4  对应四个季度,比如2017Q1
	 * @return
	 */
	public static String getLastQuarter(String date) {
		String currentMonth = new SimpleDateFormat("yyyyMM").format(parseStrToDate(date));
		String quarter = "";
		String year = currentMonth.substring(0, 4);
		String month = currentMonth.substring(4);
		int mon = Integer.parseInt(month);
		if(mon>=1&&mon<=3){//上一季是去年第四季
			year = getLastYear(date);
			quarter = "Q4";
		}else if(mon>=4&&mon<=6){//上一季是当年第一季
			quarter = "Q1";
		}else if(mon>=7&&mon<=9){//上一季是当年第二季
			quarter = "Q2";
		}else{//上一季是当年第三季
			quarter = "Q3";
		}
		return year+quarter;
	}

	/**
	 * 获取当年年份
	 * @return
	 */
	public static String getYear(String date){
		return formatDate(parseStrToDate(date),"yyyy");
	}

	/**
	 * 获取当年年份
	 * @return
	 */
	public static String getYear(){
		return getYear(getCurrentday("yyyy-MM-dd"));
	}

	/**
	 * 得到去年年份
	 * @return
	 */
	public static String getLastYear() {
		return getLastYear(getCurrentday("yyyy-MM-dd"));
	}

	/**
	 * 得到去年年份
	 * @return
	 */
	public static String getLastYear(String date) {
		Calendar   cal   =   Calendar.getInstance();
		cal.setTime(parseStrToDate(date));
		cal.add(Calendar.YEAR,   -1);
		String lastYear = new SimpleDateFormat("yyyy").format(cal.getTime());
		return lastYear;
	}

	/**
	 * 得到指定月份的下一月
	 * @param month
	 * @param pattern
	 * @return
	 */
	public static String getNextMonth(String month, String pattern) {
		int yearindex = pattern.indexOf("yyyy");
		String year = month.substring(yearindex, yearindex+4);
		int monindex = pattern.indexOf("MM");
		String mon = month.substring(monindex, monindex+2);
		int mon_num = Integer.parseInt(mon);
		int next_mon = (mon_num==12)?1:(mon_num+1);
		int year_num = Integer.parseInt(year);
		int next_year = (mon_num==12)?(year_num+1):year_num;
		pattern = pattern.replaceAll("yyyy", String.valueOf(next_year)).replaceAll("MM", (next_mon<10)?("0"+next_mon):String.valueOf(next_mon));
		return pattern;
	}
	/**
	 * 得到某年某周的最后一天
	 * @param year 年份
	 * @param week 周数
	 * @return
	 */
	public static Date getLastDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);
		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);
		return getLastDayOfWeek(cal.getTime());
	}
	/**
	 * 取得某个日期所在周的最后一天
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}
	/**
	 * 得到某年某周的第一天
	 * @param year 年份
	 * @param week 周数
	 * @return
	 */
	public static Date getFirstDayOfWeek(int year, int week) {
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);
		Calendar cal = (GregorianCalendar) c.clone();
		cal.add(Calendar.DATE, week * 7);
		return getFirstDayOfWeek(cal.getTime());
	}
	/**
	 * 取得某个日期所在周的第一天
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date) {
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek()); // Monday
		return c.getTime();
	}

	/**
	 * 将某个日期转化为周几
	 * <br>1 周日</br>
	 * <br>2 周一</br>
	 * <br>3 周二</br>
	 * <br>4 周三</br>
	 * <br>5 周四</br>
	 * <br>6 周五</br>
	 * <br>7 周六</br>
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static int getDayOfWeek(String date,String pattern) throws ParseException {
		Calendar c = new GregorianCalendar();
		c.setTime(parseString(date,pattern));
		return c.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * 将某个日期转化为星期几
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static String getDayOfWeek_CN(String date,String pattern) throws ParseException {
		Calendar c = new GregorianCalendar();
		c.setTime(parseString(date,pattern));
		int i = c.get(Calendar.DAY_OF_WEEK);
		String dayOfWeek = "星期一";
		switch (i) {
			case 1:
				dayOfWeek = "星期日";
				break;
			case 2:
				dayOfWeek = "星期一";
				break;
			case 3:
				dayOfWeek = "星期二";
				break;
			case 4:
				dayOfWeek = "星期三";
				break;
			case 5:
				dayOfWeek = "星期四";
				break;
			case 6:
				dayOfWeek = "星期五";
				break;
			case 7:
				dayOfWeek = "星期六";
				break;
		}
		return dayOfWeek;
	}

	/**
	 * 将某个日期转化为星期几
	 * @param date
	 * @param pattern
	 * @return
	 * @throws ParseException
	 */
	public static String getDayOfWeek_EN(String date,String pattern) throws ParseException {
		Calendar c = new GregorianCalendar();
		c.setTime(parseString(date,pattern));
		int i = c.get(Calendar.DAY_OF_WEEK);
		String dayOfWeek = "SUNDAY";
		switch (i) {
			case 1:
				dayOfWeek = "SUNDAY";
				break;
			case 2:
				dayOfWeek = "MONDAY";
				break;
			case 3:
				dayOfWeek = "TUESDAY";
				break;
			case 4:
				dayOfWeek = "WEDNESDAY";
				break;
			case 5:
				dayOfWeek = "THURSDAY";
				break;
			case 6:
				dayOfWeek = "FRIDAY";
				break;
			case 7:
				dayOfWeek = "SATURDAY";
				break;
		}
		return dayOfWeek;
	}

	/**
	 * 获取某个日期所在周的周日日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getSundayOfWeek(String date,String pattern) throws ParseException {
		int dayOfWeek = getDayOfWeek(date, pattern);
		return getAddDate(date,pattern,(dayOfWeek==1?1:8)-dayOfWeek);
	}

	/**
	 * 获取某个日期所在周的周一日期
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String getMondayOfWeek(String date,String pattern) throws ParseException {
		int dayOfWeek = getDayOfWeek(date, pattern);
		return getAddDate(date,pattern,(dayOfWeek==1?-5:2)-dayOfWeek);
	}

	/**
	 * 获取当周的日期区间
	 * @return 返回的是周一~周日,比如"2017-05-01~2017-05-07"
	 */
	public static String getWeek(String pattern) throws ParseException {
		return getWeek(getCurrentday(pattern),pattern);
	}

	/**
	 * 获取上周的日期区间
	 * @return 返回的是周一~周日,比如"2017-05-01~2017-05-07"
	 */
	public static String getLastWeek(String pattern) throws ParseException {
		return getLastWeek(getCurrentday(pattern),pattern);
	}

	/**
	 * 获取某个日期所在周的日期区间
	 * @return 返回的是周一~周日,比如"2017-05-01~2017-05-07"
	 */
	public static String getWeek(String date,String pattern) throws ParseException {
		return getMondayOfWeek(date,pattern)+"~"+getSundayOfWeek(date,pattern);
	}

	/**
	 * 获取某个日期所在周上周的日期区间
	 * @return 返回的是周一~周日,比如"2017-05-01~2017-05-07"
	 */
	public static String getLastWeek(String date,String pattern) throws ParseException {
		String addDate = getAddDate(date,pattern, -7);
		return getMondayOfWeek(addDate,pattern)+"~"+getSundayOfWeek(addDate,pattern);
	}

	/**
	 * 将存在某个时间段内的周日期转化成日期区间
	 * @param weekdate 周日期  比如201409  2014年第9周
	 * @param startdate 起始日期 
	 * @param enddate 结束日期
	 * @param fmt 日期格式   起始和结束日期格式必须与此相符
	 * @return  返回格式20141001~20141007
	 */
	public static String transformWeekToDate(String weekdate,String startdate,String enddate,String fmt){
		try {
			String regex = "[0-9]{6}";
			if(!Pattern.matches(regex, weekdate)){
				log.info("周日期不是六位格式！");
				return weekdate;
			}
			
			String year = weekdate.substring(0, 4);
			String week = weekdate.substring(4, 6);
			Date tDate = getLastDayOfWeek(Integer.parseInt(year),0);
			String strTdate = formatDate(tDate, "yyyyMMdd");
			Integer iweek = Integer.parseInt(week);
			if (!strTdate.endsWith("0101")){//isSub参数控制周是否减1，如果遇到20121001-20121017这样特别的日期，就不进行减1操作
				iweek = iweek-1;
			}
			Date _startDate = parseString(startdate, fmt);
			Date _endDate = parseString(enddate, fmt);
			Date start =getFirstDayOfWeek(Integer.parseInt(year),iweek);
			Date end = getLastDayOfWeek(Integer.parseInt(year),iweek);
			if(_startDate.getTime() > start.getTime()){
					start = _startDate;
			}
			if(_endDate.getTime() < end.getTime()){
					end = _endDate;
			}
			if (start.getTime()>end.getTime()){
				start = getFirstDayOfWeek(Integer.parseInt(year),Integer.parseInt(week));
				end = getLastDayOfWeek(Integer.parseInt(year),Integer.parseInt(week));
			}
			String startWeek = DateUtils.formatDate(start, "yyyyMMdd");
			String endWeek = DateUtils.formatDate(end, "yyyyMMdd");
			return startWeek+"~"+endWeek;
		} catch (ParseException e) {
			log.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}
	
	/**
	 * 字符串转换为日期,支持转换格式 
	 * <br>yyyyMMdd
	 * <br>yyyy-MM-dd 
	 * <br>yyyy/MM/dd 
	 * <br>yyyy\MM\dd 
	 * <br>yyyy-MM-dd HH:mm:ss
	 * <br>yyyy/MM/dd HH:mm:ss
	 * <br>yyyyMMdd HH:mm:ss 
	 * @param date 传入的日期
	 * @return
	 */
	public static Date parseStrToDate(String date){
		if(null==date||date.trim().equals("")){
			return null;
		}
		date = date.trim();
		try {
			String reg1 = "^\\d{4}-\\d{2}-\\d{2}$";//yyyy-MM-dd
			if(Pattern.matches(reg1, date)){
				return new SimpleDateFormat("yyyy-MM-dd").parse(date);
			}
			String reg2 = "^\\d{4}/\\d{2}/\\d{2}$";//yyyy/MM/dd
			if(Pattern.matches(reg2, date)){
				return new SimpleDateFormat("yyyy/MM/dd").parse(date);
			}
			String reg3 = "^\\d{4}\\\\d{2}\\\\d{2}$";//yyyy\MM\dd
			if(Pattern.matches(reg3, date)){
				return new SimpleDateFormat("yyyy\\MM\\dd").parse(date);
			}
			String reg4 = "^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$";//yyyy-MM-dd HH:mm:ss
			if(Pattern.matches(reg4, date)){
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
			}
			String reg5 = "^\\d{4}/\\d{2}/\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$";//yyyy/MM/dd HH:mm:ss
			if(Pattern.matches(reg5, date)){
				return new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(date);
			}
			String reg6 = "^\\d{4}\\d{2}\\d{2}\\s\\d{2}:\\d{2}:\\d{2}$";//yyyyMMdd HH:mm:ss
			if(Pattern.matches(reg6, date)){
				return new SimpleDateFormat("yyyyMMdd HH:mm:ss").parse(date);
			}
			String reg7 = "^\\d{4}\\d{2}\\d{2}$";//yyyyMMdd
			if(Pattern.matches(reg7, date)){
				return new SimpleDateFormat("yyyyMMdd").parse(date);
			}
		} catch (ParseException e) {
			log.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
	public static void main(String[] args) throws ParseException {
		System.out.println(getMonth("yyyyMM"));
		System.out.println(getLastMonth("yyyyMM"));
		System.out.println(getMonth("2016-04-04","yyyyMM"));
		System.out.println(getLastMonth("2016-01-04","yyyyMM"));
	}
}
