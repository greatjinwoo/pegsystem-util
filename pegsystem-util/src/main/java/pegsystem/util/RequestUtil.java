package pegsystem.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Request정보 Util Class
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
public class RequestUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

	/**
	 * request(client)의 IP 주소를 반환한다.
	 * 
	 * @param request
	 * @return
	 */
	public static String getClientIpAddress(HttpServletRequest request) {
		String clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
		if(clientIp == null || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) clientIp = request.getHeader("X-Forwarded-For");
		if(clientIp == null || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) clientIp = request.getHeader("REMOTE_ADDR");
		if(clientIp == null || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) clientIp = request.getHeader("Proxy-Client-IP");
		if(clientIp == null || clientIp.length() == 0 || clientIp.toLowerCase().equals("unknown")) clientIp = request.getRemoteAddr();
		
		return clientIp;
	}
	

	/**
	 * 요청 서버의 주소를 리턴
	 * @param request
	 * @return
	 */
	public static String getRequestServer(HttpServletRequest request) {
		String protocol = "HTTP";
		if(request.isSecure()) protocol = "HTTPS";
		String serverName = request.getServerName();
		int port = request.getServerPort();
		String contextPath = request.getContextPath();
		String retVal = protocol + "://" + serverName + ":" + port + contextPath;
		return retVal;
	}
	

	/**
	 * 모바일에서 접속했는지 확인.
	 * 
	 * @param request
	 * @return
	 */
	public static boolean checkMobile(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent").toLowerCase().replaceAll(" ", "");
		if(userAgent.indexOf("mobile") != -1) return true;
		else return false;
	}
	
	/**
	 * 네이버 api를 이용한 shortcut url 만들기
	 * @param text
	 * @return
	 */
	public static StringBuffer shortcutURL(String address) {
		// 라이프온
		String clientId		= "tmWJOLwKAKznjNyxBPkc";	//애플리케이션 클라이언트 아이디값";
		String clientSecret	= "18u4D5k9mP";				//애플리케이션 클라이언트 시크릿값";
		
		// 
//		String clientId		= "EE4Z_xwIR4euBudEIONu";//애플리케이션 클라이언트 아이디값";
//		String clientSecret	= "Dqrv2sF5c7";//애플리케이션 클라이언트 시크릿값";
		StringBuffer response = null;
		try {
			String apiURL = "https://openapi.naver.com/v1/util/shorturl";
			URL url = new URL(apiURL);
			HttpURLConnection con = (HttpURLConnection)url.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("X-Naver-Client-Id", clientId);
			con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
	         
			// post request
			String postParams = "url=" + address;
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.writeBytes(postParams);
			wr.flush();
			wr.close();
			int responseCode = con.getResponseCode();
			BufferedReader br;
			if(responseCode == 200) { // 정상 호출
				br = new BufferedReader(new InputStreamReader(con.getInputStream()));
			} else {  // 에러 발생
				br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
			}
			String inputLine;
			response = new StringBuffer();
			while((inputLine = br.readLine()) != null) {
				response.append(inputLine);
			}
			br.close();
	         
			logger.info("################################ shotcut URL");
			logger.info(response.toString());
			logger.info("################################ shotcut URL");
		} catch (Exception e) {
			logger.info(e.getMessage());
		}
	     
		return response;
	 }
}