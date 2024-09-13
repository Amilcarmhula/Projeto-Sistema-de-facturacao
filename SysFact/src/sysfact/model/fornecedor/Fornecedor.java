/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.fornecedor;

import java.util.List;
import sysfact.model.fornecedor.contacto.ContactoFornecedor;
import sysfact.model.fornecedor.endereco.EnderecoFornecedor;
import sysfact.model.usuario.Usuario;



/**
 *
 * @author JM-Tecnologias
 */
public class Fornecedor {

    private int id;
    private String tipo;
    private String razaosocial;
    private int nuit;
    private String data_criacao;
    private List<ContactoFornecedor> contactoFornecedor;
    private List<EnderecoFornecedor> enderecoFornecedor;
    private Usuario usuario;
    

    public Fornecedor() {
    }

    public Fornecedor(String tipo, String razaosocial, int nuit, List<ContactoFornecedor> contactoFornecedor, List<EnderecoFornecedor> enderecoFornecedor, Usuario usuario) {
        this.tipo = tipo;
        this.razaosocial = razaosocial;
        this.nuit = nuit;
        this.contactoFornecedor = contactoFornecedor;
        this.enderecoFornecedor = enderecoFornecedor;
        this.usuario = usuario;
    }

    
    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRazaosocial() {
        return razaosocial;
    }

    public void setRazaosocial(String razaosocial) {
        this.razaosocial = razaosocial;
    }

    public int getNuit() {
        return nuit;
    }

    public void setNuit(int nuit) {
        this.nuit = nuit;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public List<ContactoFornecedor> getContactoFornecedor() {
        return contactoFornecedor;
    }
    
    public String printContatos(){
        String contacto = "";
        if(contactoFornecedor != null){
            for (ContactoFornecedor c : contactoFornecedor) {
                contacto += c.getContacto() + ";   ";
            }
        }
        return contacto;
    }

    public void setContactoFornecedor(List<ContactoFornecedor> contactoFornecedor) {
        this.contactoFornecedor = contactoFornecedor;
    }

    public List<EnderecoFornecedor> getEnderecoFornecedor() {
        return enderecoFornecedor;
    }

    public void setEnderecoFornecedor(List<EnderecoFornecedor> enderecoFornecedor) {
        this.enderecoFornecedor = enderecoFornecedor;
    }
    

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
