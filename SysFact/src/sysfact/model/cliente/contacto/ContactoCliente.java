/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.cliente.contacto;

import sysfact.model.cliente.Cliente;
import sysfact.model.contacto.Contacto;
import sysfact.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class ContactoCliente extends Contacto{
    private int id;
    private String responsavel;
    private Cliente cliente;
    private Usuario usuario;
    private String data_criacao;
    
    
    public ContactoCliente(){}

    public ContactoCliente(String responsavel, Cliente cliente, Usuario usuario, String contacto, String email, String webSite) {
        super(contacto, email, webSite);
        this.responsavel = responsavel;
        this.cliente = cliente;
        this.usuario = usuario;
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

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
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
        
}
