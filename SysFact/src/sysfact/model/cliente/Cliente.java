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
    private List<ContactoCliente> contactosCliente;
    private List<EnderecoCliente> enderecosCliente;
    private ContactoCliente contactoCliente;
    private EnderecoCliente enderecoCliente;
    private Usuario usuario;
    

    public Cliente() {
    }

    public Cliente(String tipo, String nome, String apelido, int nuit, List<ContactoCliente> contactosCliente, List<EnderecoCliente> enderecosCliente, Usuario usuario) {
        this.tipo = tipo;
        this.nome = nome;
        this.apelido = apelido;
        this.nuit = nuit;
        this.contactosCliente = contactosCliente;
        this.enderecosCliente = enderecosCliente;
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

    public List<ContactoCliente> getContactosCliente() {
        return contactosCliente;
    }

    public void setContactosCliente(List<ContactoCliente> contactosCliente) {
        this.contactosCliente = contactosCliente;
    }

    public List<EnderecoCliente> getEnderecosCliente() {
        return enderecosCliente;
    }

    public void setEnderecosCliente(List<EnderecoCliente> enderecosCliente) {
        this.enderecosCliente = enderecosCliente;
    }

    public ContactoCliente getContactoCliente() {
        return contactoCliente;
    }

    public void setContactoCliente(ContactoCliente contactoCliente) {
        this.contactoCliente = contactoCliente;
    }

    public EnderecoCliente getEnderecoCliente() {
        return enderecoCliente;
    }

    public void setEnderecoCliente(EnderecoCliente enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
    }

    
    
    public String printContatos(){
        String contacto = "";
        if(contactosCliente != null){
            for (ContactoCliente c : contactosCliente) {
                contacto += c.getContacto() + "; ";
            }
        }
        return contacto;
    }
    
    public String printEnderecos(){
        String endereco = "";
        if(enderecoCliente != null){
            endereco = enderecoCliente.getAvenida()+", "+enderecoCliente.getRua()+
                    ", "+enderecoCliente.getNumero()+"; "+enderecoCliente.getBairro()
                    +", "+enderecoCliente.getCidade()+", "+enderecoCliente.getProvincia()
                    +"-"+enderecoCliente.getCodigoPostal();
        }
        return endereco;
    }

   
    

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
