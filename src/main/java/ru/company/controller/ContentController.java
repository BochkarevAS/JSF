package ru.company.controller;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import org.primefaces.event.FileUploadEvent;

@ManagedBean
@SessionScoped
public class ContentController implements Serializable {

    @ManagedProperty(value = "#{searchController}")
    private SearchController searchController;

    public SearchController getSearchController() {
        return searchController;
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    public void handleFileUpload(FileUploadEvent event) {
        searchController.getSelectedBook().setUploadedContent(event.getFile().getContents());
    }


    public boolean isShowContent() {
        if (searchController.getSelectedBook().getUploadedContent() != null) {
            return true;
        }

        return false;
    }

    public byte[] getUploadedContent() {
        return searchController.getSelectedBook().getUploadedContent();
    }
}



