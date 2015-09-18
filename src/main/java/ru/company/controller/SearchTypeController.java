package ru.company.controller;

import ru.company.enums.SearchType;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

@ManagedBean
@RequestScoped
public class SearchTypeController {

    private static Map<String, SearchType> searchList = new HashMap<>();

    public SearchTypeController() {
        ResourceBundle bundle = ResourceBundle.getBundle("messages.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());

        searchList.clear();
        searchList.put(bundle.getString("author_name"), SearchType.AUTHOR);
        searchList.put(bundle.getString("book_name"), SearchType.TITLE);
    }

    public Map<String, SearchType> getSearchList() {
        return searchList;
    }

}
