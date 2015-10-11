package ru.company.controller;

import ru.company.comporator.ListComparator;
import ru.company.db.DataSource;
import ru.company.entity.Author;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.*;

@ManagedBean(eager = false)
@ApplicationScoped
public class AuthorController implements Serializable, Converter {

    private List<SelectItem> selectItems = new ArrayList<>();
    private Map<Integer, Author> authorMap = new HashMap<>();
    private List<Author> authors;

    public AuthorController() {
        authors = DataSource.getInstance().getAllAuthors();
        Collections.sort(authors, ListComparator.getInstance());

        for (Author author : authors) {
            authorMap.put(author.getId(), author);
            selectItems.add(new SelectItem(author, author.getFio()));
        }
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return authorMap.get(Integer.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Author) value).getId() + "";
    }
}
