/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.contacto;


/**
 *
 * @author JM-Tecnologias
 */
public class Contacto {
    
    private String contacto;
    private String email;
    private String website;
    

    
    public Contacto(){}

    public Contacto( String contacto, String email, String webSite) {

        this.contacto = contacto;
        this.email = email;
        this.website = webSite;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
    
    
}
