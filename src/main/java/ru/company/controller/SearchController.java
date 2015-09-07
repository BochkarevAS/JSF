package ru.company.controller;

import ru.company.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ManagedBean
@SessionScoped
public class SearchController {

    private SearchType searchType;

    private static Map<String, SearchType> searchTypeMap = new HashMap<String, SearchType>();

    public SearchController() {
        ResourceBundle resource = ResourceBundle.getBundle("messages.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
        searchTypeMap.put(resource.getString("author_name"), SearchType.AUTHOR);
        searchTypeMap.put(resource.getString("book_name"), SearchType.TITLE);
    }

    public SearchType getSearchType() {
        return searchType;
    }

    public Map<String, SearchType> getSearchTypeMap() {
        return searchTypeMap;
    }

}


