package pegsystem.util;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;


/**
 * 썸네일생성 Util Class
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
public class ThumbnailUtil {
	
	public static final int SAME = -1;
	public static final int RATIO = 0;
	
	/**
	 * <b>File to Thumbnail File</b>
	 * @discription 원본 이미지를 썸네일 File로 생성한다.
	 * 
	 * @param src 원본 이미지 파일
	 * @param dest 썸네일 이미지 파일
	 * @param width 변경 width
	 * @param height 변경 height
	 * @throws IOException
	 */
	public static void makeThumbnail(File src, File dest, int width, int height) throws IOException{
		FileInputStream srcIs = null;
		
		try {
			srcIs = new FileInputStream(src);
			ThumbnailUtil.makeThumbnail(srcIs, dest, width, height);
		} finally{
			if (srcIs != null){
				try {
					srcIs.close();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
	}

	/**
	 * <b>InputStream to Thumbnail File</b>
	 * @discription 원본 이미지 File의 Stream 을 썸네일 File로 생성한다.
	 * 
	 * @param src 원본 이미지 파일의 Stream
	 * @param dest 썸네일 이미지 파일
	 * @param width 변경 width
	 * @param height 변경 height
	 * @throws IOException
	 */
	public static void makeThumbnail(InputStream src, File dest, int width, int height) throws IOException {
		
		//원본 이미지 정보 할당
		BufferedImage srcImg = ImageIO.read(src);
		
		//원본 이미지 폭과 높이 설정
		int srcWidth = srcImg.getWidth();
		int srcHeight = srcImg.getHeight();
		
		/***************************************
		 * 썸네일 이미지의 폭과 높이 구하기 - s 
		 ***************************************/
		int destWidth = -1;
		int destHeight = -1;
		
		if(width == SAME){
			destWidth = srcWidth;
		}else if (width > 0){
			destWidth = width;
		}
		
		if(height == SAME){
			destHeight = srcHeight;
		}else if (width > 0){
			destHeight = height;
		}
		
		if(width == RATIO && height == RATIO){
			destWidth = srcWidth;
			destHeight = srcHeight;
		}else if (width == RATIO){
			double ratio = ((double)destHeight) / ((double)destHeight);
			destWidth = (int)((double)srcWidth * ratio);
		}else if (height == RATIO){
			double ratio = ((double)destWidth) / ((double)destWidth);
			destHeight = (int)((double)srcHeight * ratio);
		}
		/***************************************/
		
		//새로 생성할 이미지 정보 할당
		BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_3BYTE_BGR);
		
		//썸네일 이미지 그리기
		Graphics2D g = destImg.createGraphics();
		g.drawImage(srcImg, 0, 0, destWidth, destHeight, null);
		
		//이미지 생성
		ImageIO.write(destImg, "png", dest);
	}
}