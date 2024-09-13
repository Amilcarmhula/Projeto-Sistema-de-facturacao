/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.armazem.contacto;

import sysfact.model.armazem.Armazem;
import sysfact.model.contacto.Contacto;
import sysfact.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class ContactoArmazem extends Contacto{
    private int id;
    private String responsavel;
    private Armazem armazem;
    private Usuario usuario;
    private String data_criacao;
    
    
    public ContactoArmazem(){}

    public ContactoArmazem(String responsavel, Armazem fornecedor, Usuario usuario, String contacto, String email, String webSite) {
        super(contacto, email, webSite);
        this.responsavel = responsavel;
        this.armazem = fornecedor;
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

    public Armazem getArmazem() {
        return armazem;
    }

    public void setArmazem(Armazem armazem) {
        this.armazem = armazem;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
        
}
