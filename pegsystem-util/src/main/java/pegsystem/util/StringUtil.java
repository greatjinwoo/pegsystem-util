package pegsystem.util;

import java.io.UnsupportedEncodingException;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 문자열 Util Class
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
public class StringUtil {

	private static final Logger logger = LoggerFactory.getLogger(StringUtil.class);
	
	
	
	/**
	 * String의 마지막 문자열의 ascii값 + 1 해서 반환
	 * 문자열이 한자리일 경우에는 그대로 반환
	 * 
	 * @param str
	 * @return
	 */
	public static String characterIncrease(String str) {
		if(str.length() > 1) {
			String temp = str.substring(0, str.length() - 1);	// 마지막 문자열을 제외한 값
			String last = str.substring(str.length() - 1);		// 마지막 문자열
			int asc = last.charAt(0);							// 마지막 문자열의 ascii 값
			last = Character.toString((char)(asc + 1));
			
			return temp + last;
		}
		
		return str;
	}
	
	
	/**
	 * parameter의 인코딩을 확인한다.
	 * @param str
	 */
	public static void encodingCheck(String str) {
		String[] charSet = {"utf-8", "euc-kr", "ksc5601","iso-8859-1", "x-windows-949"};
		for(int i=0; i<charSet.length; i++) {
			for(int j=0; j<charSet.length; j++) {
				try {
					logger.info("[" + charSet[i] +"," + charSet[j] + "] = " + new String(str.getBytes(charSet[i]), charSet[j]));
				} catch(UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * 랜덤 문자열(숫자+문자) 생성해서 리턴
	 * @return
	 */
	public static String makeRandomString(int size) {
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		
		for(int i=0; i<size; i++) {
			if(random.nextBoolean()) sb.append((char)((int)(random.nextInt(26))+97));
			else sb.append((random.nextInt(10)));
		}
		
		return sb.toString();
	}
	
	
	/**
	 * 정해진 문자열의 길이만큼 0을 붙여 리턴
	 * 
	 * @param temp
	 * @param length
	 * @return
	 */
	public static String lpad(int temp, int length) {
		String seq = String.valueOf(temp);
		int targetLen = length - seq.length();
		if(targetLen > 0) {
			for(int i=0; i<targetLen; i++) {
				seq = "0" + seq;
			}
		}
		
		return seq;
	}
	
	
	/**
	 * 공백문자 모두 제거
	 * @param str
	 * @return
	 */
	public static String trimAll(String str) {
		if(str != null) {
			str = str.replaceAll(" ", "");
			str = str.replaceAll("(^\\p{Z}+|\\p{Z}+$)", "");
		}
		return str;
	}
		
}
