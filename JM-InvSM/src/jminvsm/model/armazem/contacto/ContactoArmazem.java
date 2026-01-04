/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.armazem.contacto;

import jminvsm.model.armazem.Armazem;
import jminvsm.model.contacto.Contacto;
import jminvsm.model.usuario.Usuario;

/**
 *
 * @author JM-Tecnologias
 */
public class ContactoArmazem {

    private int id;
    private String responsavel_arm;
    private Armazem armazem;
    private String contacto_arm;
    private String email_arm;

    public ContactoArmazem() {
    }

    public ContactoArmazem(String responsavel, Armazem armazem, String contacto, String email) {
        this.responsavel_arm = responsavel;
        this.armazem = armazem;
        this.contacto_arm = contacto;
        this.email_arm = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResponsavel_arm() {
        return responsavel_arm;
    }

    public void setResponsavel_arm(String responsavel_arm) {
        this.responsavel_arm = responsavel_arm;
    }

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
    }

    public String getContacto_arm() {
        return contacto_arm;
    }

    public void setContacto_arm(String contacto_arm) {
        this.contacto_arm = contacto_arm;
    }

    public String getEmail_arm() {
        return email_arm;
    }

    public void setEmail_arm(String email_arm) {
        this.email_arm = email_arm;
    }
}
