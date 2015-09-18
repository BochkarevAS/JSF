package ru.company.controller;

import ru.company.beans.Book;
import ru.company.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.sql.*;
import java.util.*;

@ManagedBean(eager = true)
@SessionScoped
public class SearchController implements Serializable {

    private String searchString;
    private SearchType searchType;
    private ArrayList<Book> currentBookList;
    private static Map<String, SearchType> searchList = new HashMap<String, SearchType>();
    private int totalBooksCount;
    private int booksOnPage = 1;
    private long selectedPageNumber = 1;
    private List<Integer> pageNumbers = new ArrayList<>();
    private String currentSql;
    private int selectedGenreId;
    private char selectedLetter;
    private boolean editMode;
    private String currentSearchString;
    private SearchType selectedSearchType = SearchType.TITLE;
    private boolean pageSelected;

    public SearchController() {
        fillBooksAll();
    }

    public Character[] getRussianLetters() {
        Character[] letters = new Character[33];
        letters[0] = 'А';
        letters[1] = 'Б';
        letters[2] = 'В';
        letters[3] = 'Г';
        letters[4] = 'Д';
        letters[5] = 'Е';
        letters[6] = 'Ё';
        letters[7] = 'Ж';
        letters[8] = 'З';
        letters[9] = 'И';
        letters[10] = 'Й';
        letters[11] = 'К';
        letters[12] = 'Л';
        letters[13] = 'М';
        letters[14] = 'Н';
        letters[15] = 'О';
        letters[16] = 'П';
        letters[17] = 'Р';
        letters[18] = 'С';
        letters[19] = 'Т';
        letters[20] = 'У';
        letters[21] = 'Ф';
        letters[22] = 'Х';
        letters[23] = 'Ц';
        letters[24] = 'Ч';
        letters[25] = 'Ш';
        letters[26] = 'Щ';
        letters[27] = 'Ъ';
        letters[28] = 'Ы';
        letters[29] = 'Ь';
        letters[30] = 'Э';
        letters[31] = 'Ю';
        letters[32] = 'Я';

        return letters;
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost/library", "root", "");
        return connection;
    }

    private void fillBooksBySQL(String sql) {
        StringBuilder sqlBuilder = new StringBuilder(sql);

        currentSql = sql;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet result = null;

            if (!pageSelected) {
                result = statement.executeQuery(sql);
                result.last();

                totalBooksCount = result.getRow();
                fillPageNumbers(totalBooksCount, booksOnPage);
            }

            if (totalBooksCount > booksOnPage) {
                sqlBuilder.append(" limit ").append(selectedPageNumber * booksOnPage - booksOnPage).append(",").append(booksOnPage);
            }

            result = statement.executeQuery(sqlBuilder.toString());

            currentBookList = new ArrayList<Book>();

            while (result.next()) {
                Book book = new Book();
                book.setId(result.getLong("id"));
                book.setName(result.getString("name"));
                book.setGenre(result.getString("genre"));
                book.setIsbn(result.getString("isbn"));
                book.setAuthor(result.getString("author"));
                book.setPageCount(result.getInt("page_count"));
                book.setPublishDate(result.getInt("publish_year"));
                book.setPublisher(result.getString("publisher"));
//              book.setImage(result.getBytes("image"));
//              book.setContent(result.getBytes("content"));
                book.setDescr(result.getString("descr"));
                currentBookList.add(book);
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void submitValues(Character selectedLetter, long selectedPageNumber, int selectedGenreId, boolean requestFromPager) {
        this.selectedLetter = selectedLetter;
        this.selectedPageNumber = selectedPageNumber;
        this.selectedGenreId = selectedGenreId;
        this.pageSelected = requestFromPager;
    }

    private void fillBooksAll() {

        fillBooksBySQL("select b.id,b.name,b.isbn,b.page_count,b.publish_year, p.name as publisher, b.descr, "
                + "a.fio as author, g.name as genre, b.image from book b inner join author a on b.author_id=a.id "
                + "inner join genre g on b.genre_id=g.id inner join publisher p on b.publisher_id=p.id order by b.name");
    }

    public String fillBooksByGenre() {
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();

        submitValues(' ', 1, Integer.valueOf(params.get("genre_id")), false);

        fillBooksBySQL("select b.id,b.name,b.isbn,b.page_count,b.publish_year, p.name as publisher, a.fio as author, g.name as genre, b.descr, b.image from book b "
                + "inner join author a on b.author_id=a.id "
                + "inner join genre g on b.genre_id=g.id "
                + "inner join publisher p on b.publisher_id=p.id "
                + "where genre_id=" + selectedGenreId + " order by b.name ");

        return "books";
    }

    public String fillBooksByLetter() {

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter").charAt(0);

        submitValues(selectedLetter, 1, -1, false);

        fillBooksBySQL("select b.id,b.name,b.isbn,b.page_count,b.publish_year, p.name as publisher, a.fio as author, g.name as genre, b.descr, b.image from book b "
                + "inner join author a on b.author_id=a.id "
                + "inner join genre g on b.genre_id=g.id "
                + "inner join publisher p on b.publisher_id=p.id "
                + "where substr(b.name,1,1)='" + selectedLetter + "' order by b.name ");

        return "books";
    }

    public String fillBooksBySearch() {

        submitValues(' ', 1, -1, false);

        if (searchString.trim().length() == 0) {
            fillBooksAll();
            return "books";
        }

        StringBuilder sql = new StringBuilder("select b.descr, b.id,b.name,b.isbn,b.page_count,b.publish_year, p.name as publisher, a.fio as author, g.name as genre, b.image from book b "
                + "inner join author a on b.author_id=a.id "
                + "inner join genre g on b.genre_id=g.id "
                + "inner join publisher p on b.publisher_id=p.id ");

        if (searchType == SearchType.AUTHOR) {
            sql.append("where lower(a.fio) like '%" + searchString.toLowerCase() + "%' order by b.name ");

        } else if (searchType == SearchType.TITLE) {
            sql.append("where lower(b.name) like '%" + searchString.toLowerCase() + "%' order by b.name ");
        }

        fillBooksBySQL(sql.toString());

        return "books";
    }

    public void searchStringChanged(ValueChangeEvent e) {
        currentSearchString = e.getNewValue().toString();
    }

    public void searchTypeChanged(ValueChangeEvent e) {
        selectedSearchType = (SearchType) e.getNewValue();
    }

    public void changeBooksCountOnPage(ValueChangeEvent e) {
        cancelEdit();
        pageSelected = false;
        booksOnPage = Integer.valueOf(e.getNewValue().toString()).intValue();
        selectedPageNumber = 1;
        fillBooksBySQL(currentSql);
    }

    public void selectPage() {
        cancelEdit();
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedPageNumber = Integer.valueOf(params.get("page_number"));
        pageSelected = true;
        fillBooksBySQL(currentSql);
    }

    public byte[] getImage(int id) {
        byte[] image = null;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT image FROM book WHERE id=" + id)) {

            while (result.next()) {
                image = result.getBytes("image");
            }

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return image;
    }

    public byte[] getContent(int id) {

        byte[] content = null;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet result = statement.executeQuery("SELECT content FROM book WHERE id=" + id)) {

            while (result.next()) {
                content = result.getBytes("content");
            }

        } catch (Exception e) {
           e.getStackTrace();
        }

        return content;
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void showEdit() {
        editMode = true;
    }

    public void cancelEdit(){
        editMode = false;
        for (Book book : currentBookList) {
            book.setEdit(false);
        }
    }

    public String updateBooks() {

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement("UPDATE book SET name=?, isbn=?, page_count=?, publish_year=?, descr=? WHERE id=?")) {

            for (Book book : currentBookList) {
               // if (!book.isEdit()) continue;
                statement.setString(1, book.getName());
                statement.setString(2, book.getIsbn());
//              statement.setString(3, book.getAuthor());
                statement.setInt(3, book.getPageCount());
                statement.setInt(4, book.getPublishDate());
//              statement.setString(6, book.getPublisher());
                statement.setString(5, book.getDescr());
                statement.setLong(6, book.getId());
                statement.addBatch();
            }
            statement.executeBatch();

        } catch (Exception e) {
            e.getStackTrace();
        }

        cancelEdit();

        return "books";
    }

    private void fillPageNumbers(int totalBooksCount, int booksCountOnPage) {
        int pageCount = 0;

        if (booksCountOnPage > 0) {
            pageCount = (totalBooksCount % booksCountOnPage == 0) ? (totalBooksCount / booksCountOnPage) : (int) ((totalBooksCount / booksCountOnPage) + 1);
        }

        pageNumbers.clear();
        for (int i = 1; i <= pageCount; i++) {
            pageNumbers.add(i);
        }

    }

    public List<Integer> getPageNumbers() {
        return pageNumbers;
    }

    public void setPageNumbers(List<Integer> pageNumbers) {
        this.pageNumbers = pageNumbers;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public void setSearchType(SearchType searchType) {
        this.searchType = searchType;
    }

    public Map<String, SearchType> getSearchList() {
        return searchList;
    }

    public ArrayList<Book> getCurrentBookList() {
        return currentBookList;
    }

    public void setTotalBooksCount(int booksCount) {
        this.totalBooksCount = booksCount;
    }

    public long getTotalBooksCount() {
        return totalBooksCount;
    }

    public int getSelectedGenreId() {
        return selectedGenreId;
    }

    public void setSelectedGenreId(int selectedGenreId) {
        this.selectedGenreId = selectedGenreId;
    }

    public char getSelectedLetter() {
        return selectedLetter;
    }

    public void setSelectedLetter(char selectedLetter) {
        this.selectedLetter = selectedLetter;
    }

    public int getBooksOnPage() {
        return booksOnPage;
    }

    public void setBooksOnPage(int booksOnPage) {
        this.booksOnPage = booksOnPage;
    }

    public void setSelectedPageNumber(long selectedPageNumber) {
        this.selectedPageNumber = selectedPageNumber;
    }

    public long getSelectedPageNumber() {
        return selectedPageNumber;
    }
}


