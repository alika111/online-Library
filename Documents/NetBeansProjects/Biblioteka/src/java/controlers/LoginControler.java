/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlers;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author alex
 */
@RequestScoped
@Named
public class LoginControler {
    public String exit(){
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "exit";
    }
}
