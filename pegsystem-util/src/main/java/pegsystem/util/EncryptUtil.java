package pegsystem.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 암.복호화 Util Class
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
public class EncryptUtil {

	private static final Logger logger = LoggerFactory.getLogger(EncryptUtil.class.getName());
	
	// MD5
	public static String getMD5(String str) {
		String rtnMD5 = null;
		logger.info("str : " + str);
		logger.info("str.getBytes() : " + Arrays.toString(str.getBytes()));
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");	// MessageDigest 인스턴스 생성
            md.update(str.getBytes());								// 해쉬값 업데이트
            byte byteData[] = md.digest();							// 해쉬값(다이제스트) 얻기
            logger.info("byteData[] : " + Arrays.toString(byteData));
            
            StringBuffer sb = new StringBuffer();
            //출력
            for(byte byteTmp : byteData) {
                sb.append(Integer.toString((byteTmp&0xff) + 0x100, 16).substring(1));
                /*
                int tmp1 = (byteTmp & 0xff);
                int tmp2 = ((byteTmp&0xff) + 0x100);
                
                System.out.println(byteTmp +" : "+ tmp1 +" : "+ tmp2 
                        +" : "+Integer.toString((byteTmp&0xff)+0x100, 16)
                        +" : "+(Integer.toString((byteTmp&0xff) + 0x100, 16).substring(1)));
                */
                
                // byte 타입의 범위 : -128~127 ( -2^7 ~ 2^7-1 )
                //-129 + 256 = 127
                // 0x100 = 256
            }
            
            rtnMD5 = sb.toString();
        } catch (Exception e) {
            e.printStackTrace(); 
        }
        return rtnMD5;
	}
	
	
	// SHA-256    
    public static String getSHA256(String str) {
        String rtnSHA = null;
        try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256"); 
            sh.update(str.getBytes()); 
            byte byteData[] = sh.digest();
            StringBuffer sb = new StringBuffer(); 
            
            for(int i = 0 ; i < byteData.length ; i++){
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            
            rtnSHA = sb.toString();
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace(); 
        }
        
        return rtnSHA;
    }
    
    
    // Type2. BigInteger를 이용하여 구현
    public static String getEncryptMD5(String a_origin) throws UnsupportedEncodingException{
        String encryptedMD5 = null;
                
        try {
        	MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(a_origin.getBytes());
            md.update(a_origin.getBytes(), 0, a_origin.getBytes().length);            
            byte byteData[] = md.digest();
            logger.info("byteData[]:"+(Arrays.toString(byteData)));
            
            encryptedMD5 = new BigInteger(1, byteData).toString(16); 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return encryptedMD5;
    }
        
    
    public static String getEncryptSHA256(String a_origin){
        String encryptedSHA256 = null;
        
        try {
        	MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(a_origin.getBytes(), 0, a_origin.length());
            encryptedSHA256 = new BigInteger(1, md.digest()).toString(16); 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        
        return encryptedSHA256;
    }
    
    // base64 인코딩
    public static String encodingBase64(byte[] target) {
    	byte[] encoded = Base64.encodeBase64(target);
    	return new String(encoded);
    }
    
    // base64 디코딩
    public static String decodingBase64(byte[] encoded) {
    	byte[] decoded = Base64.decodeBase64(encoded);
    	return new String(decoded);
    }
}
