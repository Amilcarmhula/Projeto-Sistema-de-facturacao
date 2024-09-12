/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.cliente;

import java.util.List;
import sysfact.model.cliente.contacto.ContactoCliente;
import sysfact.model.cliente.endereco.EnderecoCliente;
import sysfact.model.usuario.Usuario;



/**
 *
 * @author JM-Tecnologias
 */
public class Cliente {

    private int id;
    private String tipo;
    private String nome;
    private String apelido;
    private int nuit;
    private String data_criacao;
    private List<ContactoCliente> contactoCliente;
    private List<EnderecoCliente> enderecoCliente;
    private Usuario usuario;
    

    public Cliente() {
    }

    public Cliente(String tipo, String nome, String apelido, int nuit, List<ContactoCliente> contactoCliente, List<EnderecoCliente> enderecoCliente, Usuario usuario) {
        this.tipo = tipo;
        this.nome = nome;
        this.apelido = apelido;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
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

    public List<ContactoCliente> getContactoCliente() {
        return contactoCliente;
    }
    
    public String printContatos(){
        String contacto = "";
        if(contactoCliente != null){
            for (ContactoCliente c : contactoCliente) {
                contacto += c.getContacto() + ";   ";
            }
        }
        return contacto;
    }

    public void setContactoCliente(List<ContactoCliente> contactoCliente) {
        this.contactoCliente = contactoCliente;
    }

    public List<EnderecoCliente> getEnderecoCliente() {
        return enderecoCliente;
    }

    public void setEnderecoCliente(List<EnderecoCliente> enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
    }
    

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
