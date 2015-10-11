package ru.company.controller;

import ru.company.comporator.ListComparator;
import ru.company.db.DataSource;
import ru.company.entity.Publisher;

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
public class PublisherController implements Serializable, Converter {

    private List<SelectItem> selectItems = new ArrayList<>();
    private Map<Integer, Publisher> map = new HashMap<>();
    private List<Publisher> list;

    public PublisherController() {
        list = DataSource.getInstance().getAllPublishers();
        Collections.sort(list, ListComparator.getInstance());

        for (Publisher publisher : list) {
            map.put(publisher.getId(), publisher);
            selectItems.add(new SelectItem(publisher, publisher.getName()));
        }

    }

    public List<SelectItem> getSelectItems() {
        return selectItems;
    }

    public List<Publisher> getPublisherList() {
        return list;
    }

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        return map.get(Integer.valueOf(value));
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        return ((Publisher)value).getId() + "";
    }
}

