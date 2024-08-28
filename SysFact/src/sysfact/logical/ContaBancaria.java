/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class ContaBancaria {

    private int id;
    private String nome_banco;
    private long numero;
    private long nib;
    private long nuib;
    private int fk_id_empresa;
    private int fk_id_usuario;
    private String data_criacao;
    private boolean active_or_not;

    public ContaBancaria() {
    }

    public ContaBancaria(String nome_banco, long numero, int nib, long nuib, Empresa fk_id_empresa, Usuario fk_id_usuario) {
        this.nome_banco = nome_banco;
        this.numero = numero;
        this.nib = nib;
        this.nuib = nuib;
        this.fk_id_empresa = fk_id_empresa.getId();
        this.fk_id_usuario = fk_id_usuario.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome_banco() {
        return nome_banco;
    }

    public void setNome_banco(String nome_banco) {
        this.nome_banco = nome_banco;
    }

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public long getNib() {
        return nib;
    }

    public void setNib(long nib) {
        this.nib = nib;
    }

    public long getNuib() {
        return nuib;
    }

    public void setNuib(long nuib) {
        this.nuib = nuib;
    }

    public int getFk_id_empresa() {
        return fk_id_empresa;
    }

    public void setFk_id_empresa(int fk_id_empresa) {
        this.fk_id_empresa = fk_id_empresa;
    }

    public int getFk_id_usuario() {
        return fk_id_usuario;
    }

    public void setFk_id_usuario(int fk_id_usuario) {
        this.fk_id_usuario = fk_id_usuario;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }

    
}
