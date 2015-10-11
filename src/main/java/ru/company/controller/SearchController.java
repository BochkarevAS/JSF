package ru.company.controller;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.model.LazyDataModel;
import ru.company.beans.Pager;
import ru.company.entity.Book;
import ru.company.db.DataSource;
import ru.company.enums.SearchType;
import ru.company.models.BookListDataModel;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.*;

@ManagedBean(eager = true)
@SessionScoped
public class SearchController implements Serializable {

    private DataSource dataSource = DataSource.getInstance();
    private boolean editMode;
    private int selectedGenreId;
    private char selectedLetter;
    private String currentSearchString;
    private Pager pager = Pager.getInstance();
    private SearchType selectedSearchType = SearchType.TITLE;
    private LazyDataModel<Book> bookListModel;
    private Book selectedBook;
    private DataTable dataTable;

    public SearchController() {
        bookListModel = new BookListDataModel();
        fillBooksAll();
    }

    private void submitValues(Character selectedLetter, int selectedGenreId) {
        this.selectedLetter = selectedLetter;
        this.selectedGenreId = selectedGenreId;
    }

    public void fillBooksAll() {
        dataSource.getAllBooks();
    }

    public void fillBooksByGenre() {
        cancelEdit();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedGenreId = Integer.valueOf(params.get("genre_id"));

        submitValues(' ', selectedGenreId);
        dataSource.getBooksByGenre(selectedGenreId);
    }

    public void fillBooksByLetter() {
        cancelEdit();

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter").charAt(0);

        submitValues(selectedLetter, -1);

        dataSource.getBooksByLetter(selectedLetter);

    }

    public void fillBooksBySearch() {
        cancelEdit();

        submitValues(' ',  -1);

        if (currentSearchString.trim().length() == 0) {
            fillBooksAll();
        }

        if (selectedSearchType == SearchType.AUTHOR) {
            dataSource.getBooksByAuthor(currentSearchString);
        } else if (selectedSearchType == SearchType.TITLE) {
            dataSource.getBooksByName(currentSearchString);
        }
    }

    private int calcSelectedPage() {
        int page = dataTable.getPage();

        int leftBound = pager.getTo() * (page - 1);
        int rightBound = pager.getTo() * page;

        boolean goPrevPage = pager.getTotalBooksCount() > leftBound & pager.getTotalBooksCount() <= rightBound;

        if (goPrevPage) page--;

        return (page > 0) ? page * pager.getTo() : 0;
    }

    public Character[] getRussianLetters() {
        Character[] letters = new Character[]{'А', 'Б', 'В', 'Г', 'Д', 'Е', 'Ё', 'Ж', 'З', 'И', 'Й', 'К', 'Л', 'М', 'Н', 'О', 'П', 'Р', 'С', 'Т', 'У', 'Ф', 'Х', 'Ц', 'Ч', 'Ш', 'Щ', 'Ъ', 'Ы', 'Ь', 'Э', 'Ю', 'Я',};
        return letters;
    }

    public void searchStringChanged(ValueChangeEvent e) {
        currentSearchString = e.getNewValue().toString();
    }

    public void searchTypeChanged(ValueChangeEvent e) {
        selectedSearchType = (SearchType) e.getNewValue();
    }

    public void updateBook() {

        dataSource.updateBook(selectedBook);
        showEdit();
        dataSource.populateList();

        RequestContext.getCurrentInstance().execute("PF('dlgEditBook').hide();");

        ResourceBundle bundle = ResourceBundle.getBundle("messages.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("updated")));

        dataTable.setFirst(calcSelectedPage());

    }

    public void deleteBook() {
        dataSource.deleteBook(selectedBook);
        dataSource.populateList();

      //  RequestContext.getCurrentInstance().execute("PF('dlgDeleteBook').hide();");
        ResourceBundle bundle = ResourceBundle.getBundle("messages.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(bundle.getString("deleted")));

        dataTable.setFirst(calcSelectedPage());

    }

    public boolean isEditMode() {
        return editMode;
    }

    public void cancelEdit(){
        editMode = false;
        RequestContext.getCurrentInstance().execute("PF('dlgEditBook').hide();");
    }

    public void showEdit() {
        editMode = true;
        RequestContext.getCurrentInstance().execute("PF('dlgEditBook').show();");
    }

    public String getSearchString() {
        return currentSearchString;
    }

    public void setSearchString(String searchString) {
        this.currentSearchString = searchString;
    }

    public SearchType getSearchType() {
        return selectedSearchType;
    }

    public void setSearchType(SearchType selectedSearchType) {
        this.selectedSearchType = selectedSearchType;
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

    public Pager getPager() {
        return pager;
    }

    public LazyDataModel<Book> getBookListModel() {
        return bookListModel;
    }

    public void setSelectedBook(Book selectedBook) {
        this.selectedBook = selectedBook;
    }

    public Book getSelectedBook() {
        return selectedBook;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }
}


