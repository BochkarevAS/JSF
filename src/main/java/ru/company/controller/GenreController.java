package ru.company.controller;

import ru.company.db.DataSource;
import ru.company.entity.Genre;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.convert.Converter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ManagedBean(eager = false)
@ApplicationScoped
public class GenreController implements Serializable, Converter {

    private List<Genre> allGeanrs;
    private List<SelectItem> selectItems = new ArrayList<>();
    private Map<Integer, Genre> genreMap;

    public GenreController() {
        genreMap = new HashMap<>();
        allGeanrs = DataSource.getInstance().getAllGeanrs();

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
