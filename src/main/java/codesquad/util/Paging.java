package codesquad.util;

import java.util.ArrayList;
import java.util.List;

public class Paging {
    private static final int PAGE_RANGE = 5;

    private int currentPage;
    private int totalPage;

    public Paging(int currentPage, int totalPage) {
        this.currentPage = currentPage;
        this.totalPage = totalPage;
    }

    public Integer getNextBtn(){
        int nextPage = ((this.currentPage / PAGE_RANGE) + 1) * PAGE_RANGE;
        return nextPage > this.totalPage ? null : nextPage;
    }

    public Integer getPreBtn() {
        int prePage =  ((this.currentPage / PAGE_RANGE) * PAGE_RANGE) - 1;
        return prePage == -1 ? null : prePage;
    }

    public List<Integer> getPages(){
        int startPage = (this.currentPage / PAGE_RANGE) * PAGE_RANGE;
        int endPage = (((this.currentPage / PAGE_RANGE) + 1) * PAGE_RANGE) - 1;
        endPage = totalPage < endPage ? totalPage : endPage;

        List<Integer> pages = new ArrayList<>();
        for(int i = startPage; i <= endPage; i++){
            pages.add(i);
        }
        return pages;
    }
}
