/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.fornecedor.endereco;

import sysfact.model.endereco.Endereco;
import sysfact.model.fornecedor.Fornecedor;
import sysfact.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class EnderecoFornecedor extends Endereco {

    private int id;
    private String data_criacao;
    private Fornecedor fornecedor;
    private Usuario usuario;

    public EnderecoFornecedor() {
    }

    public EnderecoFornecedor(Fornecedor fornecedor, Usuario usuario, String provincia, String cidade, String bairro, String avenida, String rua, int codigoPostal, int numero, String tipo) {
        super(provincia, cidade, bairro, avenida, rua, codigoPostal, numero, tipo);
        this.fornecedor = fornecedor;
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
