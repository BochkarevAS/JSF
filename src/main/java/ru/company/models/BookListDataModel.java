package ru.company.models;

import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;
import ru.company.beans.Pager;
import ru.company.db.DataSource;

import ru.company.entity.Book;
import java.util.List;
import java.util.Map;

public class BookListDataModel extends LazyDataModel<Book> {

    private List<Book> bookList;
    private DataSource dataSource = DataSource.getInstance();
    private Pager pager = Pager.getInstance();

    public BookListDataModel() {

    }

    @Override
    public Book getRowData(String rowKey) {

        for(Book book : bookList) {
            if(book.getId() == Integer.valueOf(rowKey).intValue())
                return book;
        }

        return null;
    }

    @Override
    public Object getRowKey(Book book) {
        return book.getId();
    }


    @Override
    public List<Book> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {

        pager.setFrom(first);
        pager.setTo(pageSize);

        dataSource.populateList();

        this.setRowCount(pager.getTotalBooksCount());

        return pager.getList();

    }
}

