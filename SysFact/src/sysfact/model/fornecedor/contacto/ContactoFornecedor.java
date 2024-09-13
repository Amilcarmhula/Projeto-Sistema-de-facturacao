/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.fornecedor.contacto;

import sysfact.model.contacto.Contacto;
import sysfact.model.fornecedor.Fornecedor;
import sysfact.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class ContactoFornecedor extends Contacto{
    private int id;
    private String responsavel;
    private Fornecedor fornecedor;
    private Usuario usuario;
    private String data_criacao;
    
    
    public ContactoFornecedor(){}

    public ContactoFornecedor(String responsavel, Fornecedor fornecedor, Usuario usuario, String contacto, String email, String webSite) {
        super(contacto, email, webSite);
        this.responsavel = responsavel;
        this.fornecedor = fornecedor;
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
        
}
