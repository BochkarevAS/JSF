package ru.company.controller;

import ru.company.db.DataSource;
import ru.company.entity.Author;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorController implements Serializable, Converter  {

    private List<SelectItem> selectItems = new ArrayList<SelectItem>();;
    private Map<Integer, Author> authorMap;

    public AuthorController() {
        authorMap = new HashMap<>();

        for (Author author : DataSource.getInstance().getAllAuthors()) {
            authorMap.put(author.getId(), author);
            selectItems.add(new SelectItem(author, author.getFio()));
        }
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
