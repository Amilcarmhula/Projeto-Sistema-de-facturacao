/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.fornecedor;

import java.util.List;
import jminvsm.model.fornecedor.contacto.ContactoFornecedor;
import jminvsm.model.fornecedor.endereco.EnderecoFornecedor;
import jminvsm.model.usuario.Usuario;



/**
 *
 * @author JM-Tecnologias
 */
public class Fornecedor {

    private int id;
    private String tipo_forn;
    private String razaosocial_forn;
    private int nuit_forn;
    private String data_criacao;
    private List<ContactoFornecedor> contactoFornecedor;
    private List<EnderecoFornecedor> enderecoFornecedor;
    

    public Fornecedor() {
    }

    public Fornecedor(String tipo, String razaosocial, int nuit, List<ContactoFornecedor> contactoFornecedor, List<EnderecoFornecedor> enderecoFornecedor) {
        this.tipo_forn = tipo;
        this.razaosocial_forn = razaosocial;
        this.nuit_forn = nuit;
        this.contactoFornecedor = contactoFornecedor;
        this.enderecoFornecedor = enderecoFornecedor;
    }

    
    public String getTipo_forn() {
        return tipo_forn;
    }

    public void setTipo_forn(String tipo_forn) {
        this.tipo_forn = tipo_forn;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRazaosocial_forn() {
        return razaosocial_forn;
    }

    public void setRazaosocial_forn(String razaosocial_forn) {
        this.razaosocial_forn = razaosocial_forn;
    }

    public int getNuit_forn() {
        return nuit_forn;
    }

    public void setNuit_forn(int nuit_forn) {
        this.nuit_forn = nuit_forn;
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
                contacto += c.getContacto_forn() + ";   ";
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
   
}
