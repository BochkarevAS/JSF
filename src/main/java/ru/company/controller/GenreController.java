package ru.company.controller;

import ru.company.comporator.ListComparator;
import ru.company.db.DataSource;
import ru.company.entity.Genre;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.convert.Converter;
import java.io.Serializable;
import java.util.*;

@ManagedBean(eager = false)
@ApplicationScoped
public class GenreController implements Serializable, Converter {

    private List<Genre> allGeanrs;
    private List<SelectItem> selectItems = new ArrayList<>();
    private Map<Integer, Genre> genreMap = new HashMap<>();

    public GenreController() {
        allGeanrs = DataSource.getInstance().getAllGenres();
        Collections.sort(allGeanrs, ListComparator.getInstance());

        for (Genre genre : allGeanrs) {
            genreMap.put(genre.getId(), genre);
            selectItems.add(new SelectItem(genre, genre.getName()));
        }
    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public List<Genre> getAllGeanrs() {
        return allGeanrs;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return genreMap.get(Integer.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Genre) value).getId() + "";
    }
}
