package ru.company.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.util.ResourceBundle;

@FacesValidator("ru.company.validator.LoginValidator")
public class LoginValidator implements Validator {

    public void validate(FacesContext facesContext, UIComponent uiComponent, Object value) throws ValidatorException {

        ResourceBundle bundle = ResourceBundle.getBundle("messages.messages", FacesContext.getCurrentInstance().getViewRoot().getLocale());

        try {

            if (!Character.isLetter(value.toString().charAt(0))) {
                throw new IllegalArgumentException(bundle.getString("first_letter_error"));
            }

            if (value.toString().length() < 5) {
                throw new IllegalArgumentException(bundle.getString("login_length_error"));
            }

        } catch (IllegalArgumentException e) {
            FacesMessage message = new FacesMessage(e.getMessage());
            message.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(message);
        }
    }
}
