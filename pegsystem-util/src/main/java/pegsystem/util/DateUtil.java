package pegsystem.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 날짜 Util Class
 * @author	pegsystem
 * @since	2018.01.31
 * @version 1.0
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자      수정내용
 *  --------	--------    ---------------------------
 *   2018.01.31  박진우      최초 생성
 *   0000.00.00  홍길동	     XXXXXXXXXXXXXXX
 *
 * </pre>
 */
public class DateUtil {
	
	/**
	 * 현재 시간을 구해서 리턴
	 * @return
	 */
	public static String getCurrentTime() {
		long time = System.currentTimeMillis(); 
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy/mm/dd hh:mm:ss");
		String retVal = dayTime.format(new Date(time));
		
		return retVal;
	}
	
	
	/**
	 * 현재 년월일을 구해서 리턴(20170208)
	 * @return
	 */
	public static String getDate() {
		Calendar cal = Calendar.getInstance();
		int y = cal.get(Calendar.YEAR);
		int m = cal.get(Calendar.MONTH) + 1;
		int d = cal.get(Calendar.DATE);
		
		String year = String.valueOf(y);
		String month = String.valueOf(m);
		String date = String.valueOf(d);
		
		if(m < 10) month = "0" + String.valueOf(m);
		if(d < 10) date = "0" + String.valueOf(d);
		
		return year + month + date;
	}
	
	
	/**
	 * 만 나이 계산
	 * @param birthYear
	 * @param birthMonth
	 * @param birthDay
	 * @return
	 */
	public static int getAge(int birthYear, int birthMonth, int birthDay) {
		Calendar cal = Calendar.getInstance();
		int currentYear  = cal.get(Calendar.YEAR);
		int currentMonth = cal.get(Calendar.MONTH) + 1;
		int currentDay   = cal.get(Calendar.DAY_OF_MONTH);

		int age = currentYear - birthYear;
		if(birthMonth * 100 + birthDay > currentMonth * 100 + currentDay) age--;		// 생일 안 지난 경우 -1
		
		return age;
	 }
}
