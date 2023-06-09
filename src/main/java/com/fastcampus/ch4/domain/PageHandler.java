package com.fastcampus.ch4.domain;

public class PageHandler {
    int totalCnt;
    int pageSize;
    int naviSize = 10;
    int totalPage;
    int page;
    int beginPage;
    int endPage;
    boolean showPrev;
    boolean showNext;

    public PageHandler(int totalCnt, int page){
        this(totalCnt, page, 10);
    }

    public PageHandler(int totalCnt, int page, int pageSize){
        this.totalCnt = totalCnt;
        this.page = page;
        this.pageSize = pageSize;

        totalPage = (int)Math.ceil((double) totalCnt / (double) pageSize);
        beginPage = page / naviSize * naviSize +1;
        endPage = Math.min(beginPage + naviSize-1, totalPage); //둘 중에 작은 값을 endPage로 사용하여라
        showPrev = beginPage != 1;
        showNext = endPage != totalPage;
    }
    void print(){
        System.out.println("page ==>" + page);
        System.out.print(showPrev ? "[PREV]" : "");
        for (int i = beginPage; i <= endPage; i++) {
            System.out.print(i + " ");
        }
        System.out.println(showNext ? "[NEXT]" : "");
    }

    @Override
    public String toString() {
        return "PageHandler{" +
                "totalCnt=" + totalCnt +
                ", pageSize=" + pageSize +
                ", naviSize=" + naviSize +
                ", totalPage=" + totalPage +
                ", page=" + page +
                ", beginPage=" + beginPage +
                ", endPage=" + endPage +
                ", showPrev=" + showPrev +
                ", showNext=" + showNext +
                '}';
    }
}
