/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.armazem.endereco;

import sysfact.model.armazem.Armazem;
import sysfact.model.endereco.Endereco;
import sysfact.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoArmazem extends Endereco {

    private int id;
    private String data_criacao;
    private Armazem armazem;
    private Usuario usuario;

    public EnderecoArmazem() {
    }

    public EnderecoArmazem(Armazem fornecedor, Usuario usuario, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero, String tipo) {
        super(provincia, cidade, bairro, avenida, rua, codigoPostal, numero, tipo);
        this.armazem = fornecedor;
        this.usuario = usuario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
