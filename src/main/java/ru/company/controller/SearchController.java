package ru.company.controller;

import ru.company.beans.Pager;
import ru.company.entity.Book;
import ru.company.db.DataSource;
import ru.company.enums.SearchType;

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
    private Pager<Book> pager = new Pager<>();
    private SearchType selectedSearchType = SearchType.TITLE;
    private transient int row = -1;

    public SearchController() {
        fillBooksAll();
    }

    private void submitValues(Character selectedLetter, int selectedPageNumber, int selectedGenreId) {
        this.selectedLetter = selectedLetter;
        this.selectedGenreId = selectedGenreId;
        pager.setSelectedPageNumber(selectedPageNumber);
    }

    public int getRow() {
        row++;
        return row;
    }

    public void fillBooksAll() {
        dataSource.getAllBooks(pager);
    }

    public String fillBooksByGenre() {
        row = -1;

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedGenreId = Integer.valueOf(params.get("genre_id"));

        submitValues(' ', 1, selectedGenreId);

        DataSource.getInstance().getBooksByGenre(selectedGenreId, pager);

        return "books";
    }

    public String fillBooksByLetter() {
        row = -1;

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        selectedLetter = params.get("letter").charAt(0);

        submitValues(selectedLetter, 1, -1);

        DataSource.getInstance().getBooksByLetter(selectedLetter, pager);

        return "books";
    }

    public String fillBooksBySearch() {
        row = -1;

        submitValues(' ', 1, -1);

        if (currentSearchString.trim().length() == 0) {
            fillBooksAll();
            return "books";
        }

        if (selectedSearchType == SearchType.AUTHOR) {
            DataSource.getInstance().getBooksByAuthor(currentSearchString, pager);
        } else if (selectedSearchType == SearchType.TITLE) {
            DataSource.getInstance().getBooksByName(currentSearchString, pager);
        }

        return "books";
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

    public void changeBooksCountOnPage(ValueChangeEvent e) {
        row = -1;
        cancelEdit();
        pager.setBooksCountOnPage(Integer.valueOf(e.getNewValue().toString()).intValue());
        pager.setSelectedPageNumber(1);
        DataSource.getInstance().runCurrentCriteria();
    }

    public void selectPage() {
        row = -1;
        cancelEdit();
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        pager.setSelectedPageNumber(Integer.valueOf(params.get("page_number")));
        DataSource.getInstance().runCurrentCriteria();
    }

    public boolean isEditMode() {
        return editMode;
    }

    public void showEdit() {
        row = -1;
        editMode = true;
    }

    public void cancelEdit(){
        editMode = false;
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

    public Pager<Book> getPager() {
        return pager;
    }

    public void setPager(Pager<Book> pager) {
        this.pager = pager;
    }
}


