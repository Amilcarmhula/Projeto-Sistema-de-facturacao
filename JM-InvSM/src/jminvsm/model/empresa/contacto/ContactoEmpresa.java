/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.empresa.contacto;


import jminvsm.model.empresa.Empresa;


/**
 *
 * @author JM-Tecnologias
 */
public class ContactoEmpresa{
    private int id;
    private Empresa empresa;
    private String data_criacao;
    private String contacto_emp;
    private String email_emp;
    private String website_emp;
    
    
    public ContactoEmpresa(){}

    public ContactoEmpresa(Empresa empresa, String contacto, String email, String website) {
        this.empresa = empresa;
        this.contacto_emp = contacto;
        this.email_emp = email;
        this.website_emp = website;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public String getContacto_emp() {
        return contacto_emp;
    }

    public void setContacto_emp(String contacto_emp) {
        this.contacto_emp = contacto_emp;
    }

    public String getEmail_emp() {
        return email_emp;
    }

    public void setEmail_emp(String email_emp) {
        this.email_emp = email_emp;
    }

    public String getWebsite_emp() {
        return website_emp;
    }

    public void setWebsite_emp(String website_emp) {
        this.website_emp = website_emp;
    }

       
}
