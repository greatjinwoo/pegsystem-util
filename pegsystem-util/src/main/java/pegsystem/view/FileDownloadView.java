package pegsystem.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.view.AbstractView;


/**
 * 첨부파일 download view
 * map에 있는 파일 객체의 정보를 가지고 download 수행
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
public class FileDownloadView extends AbstractView {

	private static final Logger logger = LoggerFactory.getLogger(FileDownloadView.class);
	
	
	
	public FileDownloadView() {
		setContentType("application/download; charset=utf-8");
	}

	
	
	@Override
	protected void renderMergedOutputModel(Map<String, Object> model
										 , HttpServletRequest request, HttpServletResponse response) throws Exception {
		File file = (File) model.get("downloadFile");
		String orgName = (String) model.get("orgName");
		
        logger.info("DownloadView --> file.getPath() : " + file.getPath());
        logger.info("DownloadView --> file.getName() : " + file.getName());
        logger.info("DownloadView --> orgName : " + orgName);
         
        response.setContentType(getContentType());
        response.setContentLength((int)file.length());
        
        // 브라우저에 따른 한글 처리
        String userAgent = request.getHeader("User-Agent");
        boolean ie = userAgent.indexOf("MSIE") > -1;
        if(ie)orgName = URLEncoder.encode(orgName, "utf-8");
        else orgName = new String(orgName.getBytes("utf-8"), "iso-8859-1");
 
        response.setHeader("Content-Disposition", "attachment; filename=\"" + orgName + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");
         
        OutputStream out = response.getOutputStream();
        FileInputStream fis = null;
         
        try {
            fis = new FileInputStream(file);
            FileCopyUtils.copy(fis, out);
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(fis != null) {
                try {
                    fis.close();
                } catch(Exception e) {
                	e.printStackTrace();
                }
            }
        }// try end;
         
        out.flush();
	}

}
