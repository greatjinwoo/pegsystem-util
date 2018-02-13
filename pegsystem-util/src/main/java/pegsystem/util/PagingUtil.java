package pegsystem.util;


/**
 * 패이징처리 Util Class
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
public class PagingUtil {

	private int pageSize; 		// 게시 글 수
    private int firstPageNum;	// 첫 번째 페이지 번호
    private int prevPageNum;	// 이전 페이지 번호
    private int startPageNum;	// 시작 페이지 (페이징 네비 기준)
    private int pageNum;		// 페이지 번호
    private int endPageNum;		// 끝 페이지 (페이징 네비 기준)
    private int nextPageNum;	// 다음 페이지 번호
    private int finalPageNum;	// 마지막 페이지 번호
    private int totalCount;		// 게시 글 전체 수
    
    
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public int getFirstPageNum() {
		return firstPageNum;
	}
	public void setFirstPageNum(int firstPageNum) {
		this.firstPageNum = firstPageNum;
	}
	public int getPrevPageNum() {
		return prevPageNum;
	}
	public void setPrevPageNum(int prevPageNum) {
		this.prevPageNum = prevPageNum;
	}
	public int getStartPageNum() {
		return startPageNum;
	}
	public void setStartPageNum(int startPageNum) {
		this.startPageNum = startPageNum;
	}
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getEndPageNum() {
		return endPageNum;
	}
	public void setEndPageNum(int endPageNum) {
		this.endPageNum = endPageNum;
	}
	public int getNextPageNum() {
		return nextPageNum;
	}
	public void setNextPageNum(int nextPageNum) {
		this.nextPageNum = nextPageNum;
	}
	public int getFinalPageNum() {
		return finalPageNum;
	}
	public void setFinalPageNum(int finalPageNum) {
		this.finalPageNum = finalPageNum;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		this.makePaging();
	}


    /**
     * 페이징 생성
     */
    private void makePaging() {
    	
        if (this.totalCount == 0) return;				// 게시 글 전체 수가 없는 경우
        if (this.pageNum == 0) this.setPageNum(1); 		// 기본 값 설정
        if (this.pageSize == 0) this.setPageSize(20); 	// 기본 값 설정

        int finalPage = (totalCount + (pageSize - 1)) / pageSize;	// 마지막 페이지
        if (this.pageNum > finalPage) this.setPageNum(finalPage);	// 기본 값 설정

        if (this.pageNum < 0 || this.pageNum > finalPage) this.pageNum = 1;	// 현재 페이지 유효성 체크

        boolean isNumwFirst = pageNum == 1 ? true : false;					// 시작 페이지 (전체)
        boolean isNumwFinal = pageNum == finalPage ? true : false;			// 마지막 페이지 (전체)

        int startPage = ((pageNum - 1) / 10) * 10 + 1;	// 시작 페이지 (페이징 네비 기준)
        int endPage = startPage + 10 - 1;				// 끝 페이지 (페이징 네비 기준)

        if (endPage > finalPage) endPage = finalPage; 	// [마지막 페이지 (페이징 네비 기준) > 마지막 페이지] 보다 큰 경우

        this.setFirstPageNum(1); // 첫 번째 페이지 번호

        if (isNumwFirst) this.setPrevPageNum(1);							// 이전 페이지 번호
        else this.setPrevPageNum(((pageNum - 1) < 1 ? 1 : (pageNum - 1)));	// 이전 페이지 번호

        this.setStartPageNum(startPage);// 시작 페이지 (페이징 네비 기준)
        this.setEndPageNum(endPage); 	// 끝 페이지 (페이징 네비 기준)

        if (isNumwFinal) this.setNextPageNum(finalPage);									// 다음 페이지 번호
        else this.setNextPageNum(((pageNum + 1) > finalPage ? finalPage : (pageNum + 1)));	// 다음 페이지 번호

        this.setFinalPageNum(finalPage);	// 마지막 페이지 번호
    }
    
    
	@Override
	public String toString() {
		return "PagingHelper [pageSize=" + pageSize + ", firstPageNum="
				+ firstPageNum + ", prevPageNum=" + prevPageNum + ", startPageNum="
				+ startPageNum + ", pageNum=" + pageNum + ", endPageNum="
				+ endPageNum + ", nextPageNum=" + nextPageNum + ", finalPageNum="
				+ finalPageNum + ", totalCount=" + totalCount + "]";
	}
	    
}
