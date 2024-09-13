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
    private List<ContactoFornecedor> contactoCliente;
    private List<EnderecoFornecedor> enderecoCliente;
    private Usuario usuario;
    

    public Fornecedor() {
    }

    public Fornecedor(String tipo, String razaosocial, int nuit, List<ContactoFornecedor> contactoCliente, List<EnderecoFornecedor> enderecoCliente, Usuario usuario) {
        this.tipo = tipo;
        this.razaosocial = razaosocial;
        this.nuit = nuit;
        this.contactoCliente = contactoCliente;
        this.enderecoCliente = enderecoCliente;
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

    public List<ContactoFornecedor> getContactoCliente() {
        return contactoCliente;
    }
    
    public String printContatos(){
        String contacto = "";
        if(contactoCliente != null){
            for (ContactoFornecedor c : contactoCliente) {
                contacto += c.getContacto() + ";   ";
            }
        }
        return contacto;
    }

    public void setContactoCliente(List<ContactoFornecedor> contactoCliente) {
        this.contactoCliente = contactoCliente;
    }

    public List<EnderecoFornecedor> getEnderecoCliente() {
        return enderecoCliente;
    }

    public void setEnderecoCliente(List<EnderecoFornecedor> enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
    }
    

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
