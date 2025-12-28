/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.model.armazem;

import java.util.List;
import jminvsm.model.armazem.contacto.ContactoArmazem;
import jminvsm.model.armazem.endereco.EnderecoArmazem;



/**
 *
 * @author JM-Tecnologias
 */
public class Armazem {

    private int id;
    private String tipo;
    private String nome_arm;
    private String descricao_arm;
    private boolean estado;
    private List<ContactoArmazem> contactoArmazem;
    private List<EnderecoArmazem> enderecoArmazem;
    

    public Armazem() {
    }

    public Armazem(String tipo, String nome, String descricao, List<ContactoArmazem> contactoArmazem, List<EnderecoArmazem> enderecoArmazem) {
        this.tipo = tipo;
        this.nome_arm = nome;
        this.descricao_arm = descricao;
        this.contactoArmazem = contactoArmazem;
        this.enderecoArmazem = enderecoArmazem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome_arm() {
        return nome_arm;
    }

    public void setNome_arm(String nome_arm) {
        this.nome_arm = nome_arm;
    }

    public String getDescricao_arm() {
        return descricao_arm;
    }

    public void setDescricao_arm(String descricao_arm) {
        this.descricao_arm = descricao_arm;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String printContatos(){
        String contacto = "";
        if(contactoArmazem != null){
            for (ContactoArmazem c : contactoArmazem) {
                contacto += c.getContacto_arm() + ";   ";
            }
        }
        return contacto;
    }

    public List<ContactoArmazem> getContactoArmazem() {
        return contactoArmazem;
    }

    public void setContactoArmazem(List<ContactoArmazem> contactoArmazem) {
        this.contactoArmazem = contactoArmazem;
    }

    public List<EnderecoArmazem> getEnderecoArmazem() {
        return enderecoArmazem;
    }

    public void setEnderecoArmazem(List<EnderecoArmazem> enderecoArmazem) {
        this.enderecoArmazem = enderecoArmazem;
    }

}
