/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.empresa.contacto;


import sysfact.model.contacto.Contacto;
import sysfact.model.empresa.Empresa;
import sysfact.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class ContactoEmpresa extends Contacto{
    private int id;
    private Empresa empresa;
    private String data_criacao;
    
    
    public ContactoEmpresa(){}

    public ContactoEmpresa(Empresa empresa, String contacto, String email, String webSite) {
        super(contacto, email, webSite);
        this.empresa = empresa;
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

       
}
