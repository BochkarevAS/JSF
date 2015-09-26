package ru.company.beans;

import java.util.ArrayList;
import java.util.List;

public class Pager<T> {

    private int selectedPageNumber = 1;
    private int booksCountOnPage = 1;
    private int totalBooksCount;

    private List<Integer> pageNumbers = new ArrayList<>();
    private List<T> list;

    public int getFrom() {
        return selectedPageNumber * booksCountOnPage - booksCountOnPage;
    }

    public int getTo() {
        return booksCountOnPage;
    }

    public List<Integer> getPageNumbers() {

        int pageCount = 0;

        if (booksCountOnPage > 0) {
            pageCount = (totalBooksCount % booksCountOnPage == 0) ? (totalBooksCount / booksCountOnPage) : (int) ((totalBooksCount / booksCountOnPage) + 1);
        }

        pageNumbers.clear();

        for (int i = 1; i <= pageCount; i++) {
            pageNumbers.add(i);
        }

        return pageNumbers;
    }

    public int getBooksCountOnPage() {
        return booksCountOnPage;
    }

    public void setBooksCountOnPage(int booksCountOnPage) {
        this.booksCountOnPage = booksCountOnPage;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setTotalBooksCount(int totalBooksCount) {
        this.totalBooksCount = totalBooksCount;
    }

    public int getTotalBooksCount() {
        return totalBooksCount;
    }

    public void setSelectedPageNumber(int selectedPageNumber) {
        this.selectedPageNumber = selectedPageNumber;
    }

    public int getSelectedPageNumber() {
        return selectedPageNumber;
    }

}

