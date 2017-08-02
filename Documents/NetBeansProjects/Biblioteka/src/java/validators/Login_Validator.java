/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package validators;

import java.util.ResourceBundle;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author alex
 */
@FacesValidator("validators.Login_Validator")
public class Login_Validator implements Validator{

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if(value.toString().length()<5){
            ResourceBundle rb=ResourceBundle.getBundle("messeges.messege",FacesContext.getCurrentInstance().getViewRoot().getLocale());
            FacesMessage fm=new FacesMessage(rb.getString("login_length"));
            fm.setSeverity(FacesMessage.SEVERITY_ERROR);
            throw new ValidatorException(fm);
        }
    }
    
}
