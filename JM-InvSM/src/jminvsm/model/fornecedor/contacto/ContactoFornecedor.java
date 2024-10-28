/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.fornecedor.contacto;

import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class ContactoFornecedor{
    private int id; //
    private String responsavel_forn;
    private Fornecedor fornecedor;
    private Usuario usuario;
    private String data_criacao;
    private String contacto_forn;
    private String email_forn;
    private String website_forn;
    
    
    public ContactoFornecedor(){}

    public ContactoFornecedor(String responsavel, Fornecedor fornecedor, Usuario usuario, String contacto, String email, String website) {
        this.responsavel_forn = responsavel;
        this.fornecedor = fornecedor;
        this.usuario = usuario;
        this.contacto_forn = contacto;
        this.email_forn = email;
        this.website_forn = website;
    }

   

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResponsavel_forn() {
        return responsavel_forn;
    }

    public void setResponsavel_forn(String responsavel_forn) {
        this.responsavel_forn = responsavel_forn;
    }

    public Fornecedor getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(Fornecedor fornecedor) {
        this.fornecedor = fornecedor;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public String getContacto_forn() {
        return contacto_forn;
    }

    public void setContacto_forn(String contacto_forn) {
        this.contacto_forn = contacto_forn;
    }

    public String getEmail_forn() {
        return email_forn;
    }

    public void setEmail_forn(String email_forn) {
        this.email_forn = email_forn;
    }

    public String getWebsite_forn() {
        return website_forn;
    }

    public void setWebsite_forn(String website_forn) {
        this.website_forn = website_forn;
    }

 
        
}
