/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.cliente.contacto;

import jminvsm.model.cliente.Cliente;
import jminvsm.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class ContactoCliente {
    private int id;
    private String responsavel;
    private Cliente cliente;
    private Usuario usuario;
    private String data_criacao;
    private String contacto_cli;
    private String email_cli;
    private String website_cli;
    
    
    public ContactoCliente(){}

    public ContactoCliente(String responsavel, Cliente cliente, String contacto, String email, String website) {
        this.responsavel = responsavel;
        this.cliente = cliente;
        this.contacto_cli = contacto;
        this.email_cli = email;
        this.website_cli = website;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public String getContacto_cli() {
        return contacto_cli;
    }

    public void setContacto_cli(String contacto_cli) {
        this.contacto_cli = contacto_cli;
    }

    public String getEmail_cli() {
        return email_cli;
    }

    public void setEmail_cli(String email_cli) {
        this.email_cli = email_cli;
    }

    public String getWebsite_cli() {
        return website_cli;
    }

    public void setWebsite_cli(String website_cli) {
        this.website_cli = website_cli;
    }

    
}
