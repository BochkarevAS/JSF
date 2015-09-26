package ru.company.beans;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.util.ResourceBundle;

@ManagedBean
@SessionScoped
public class User {

    private String name;

    private String password;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {

        try {
            ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).login(name, password);

            return "books";

        } catch (Exception ex) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());
            FacesContext context = FacesContext.getCurrentInstance();
            FacesMessage message = new FacesMessage(bundle.getString("login_error"));
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            context.addMessage("login_form", message);
        }

        return "index";
    }

    public String logout() {
        String result = "/index.xhtml?faces-redirect=true";

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();

        try {
            request.logout();
        } catch (Exception e) {
           e.getStackTrace();
        }

        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        return result;
    }

}