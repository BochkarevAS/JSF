package ru.company.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

@ManagedBean
@SessionScoped
public class ImageController implements Serializable {

    private final int IMAGE_MAX_SIZE = 204800;

    @ManagedProperty(value = "#{searchController}")
    private SearchController searchController;

    public SearchController getSearchController() {
        return searchController;
    }

    public void setSearchController(SearchController searchController) {
        this.searchController = searchController;
    }

    public StreamedContent getDefaultImage() {
        return getStreamedContent(searchController.getSelectedBook().getImage());
    }

    public StreamedContent getUploadedImage() {
        return getStreamedContent(searchController.getSelectedBook().getUploadedImage());
    }

    public void handleFileUpload(FileUploadEvent event) {
        searchController.getSelectedBook().setUploadedImage(event.getFile().getContents());
    }

    private DefaultStreamedContent getStreamedContent(byte[] image) {

        if (image == null) {
            return null;
        }

        try (InputStream inputStream = new ByteArrayInputStream(image)) {

            return new DefaultStreamedContent(inputStream, "image/png");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getImageMaxSize() {
        return IMAGE_MAX_SIZE;
    }

}
