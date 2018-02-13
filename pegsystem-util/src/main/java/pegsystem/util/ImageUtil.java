package pegsystem.util;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


/**
 * 파일을 Base64 Encoding 처리.
 * Encoding된 String을 이미지로 변경
 * @author james
 */
public class ImageUtil {

	
	
	/**
	 * 파일 Full Name or 확장자.
	 * 
	 * @param fileName	
	 * @return
	 */
	public static boolean isImageFile ( String fileName)
	{
		
		String imageFileExt = "";
		if ( fileName.indexOf( ".") > 0)
		{
			imageFileExt = fileName.substring( fileName.lastIndexOf("."), fileName.length());
		}
		else
		{
			imageFileExt = fileName;
		}
		
		if ( "jpg".equalsIgnoreCase( imageFileExt) || "jpeg".equalsIgnoreCase( imageFileExt) 
			 ||"gif".equalsIgnoreCase( imageFileExt) || "png".equalsIgnoreCase( imageFileExt))
		{
			return true;
		}
		
		return false;
	}
	
    /**
     * Decode string to image
     * @param imageString The string to decode
     * @return decoded image
     */
	public static BufferedImage decodeToImage(String imageString) {

        BufferedImage image = null;
        byte[] imageByte;
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            imageByte = decoder.decodeBuffer(imageString);
            ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
            image = ImageIO.read(bis);
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    
    /**
     * 
     * Filt to Base64 Encoding
     * @param image
     * @param type
     * @return
     */
    public static String encodeToString(File image, String type) {
    	
    	BufferedImage originalImage = null;
		try {
			originalImage = ImageIO.read( image);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	return encodeToString( originalImage, type);
    }

    /**
     * Encode image to string
     * @param image The image to encode
     * @param type jpeg, bmp, ...
     * @return encoded string
     */
	public static String encodeToString(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            BASE64Encoder encoder = new BASE64Encoder();
            imageString = encoder.encode(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imageString;
    }

    public static void main (String args[]) throws IOException {
        /* Test image to string and string to image start */
        BufferedImage img = ImageIO.read(new File("files/img/TestImage.png"));
        BufferedImage newImg;
        String imgstr;
        imgstr = encodeToString(img, "png");
        System.out.println(imgstr);
        newImg = decodeToImage(imgstr);
        ImageIO.write(newImg, "png", new File("files/img/CopyOfTestImage.png"));
        /* Test image to string and string to image finish */
    }
}
