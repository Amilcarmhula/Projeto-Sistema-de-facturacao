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
    private int nib;
    private long nuib;
    private Empresa fk_id_empresa;
    private boolean active_or_not;

    public ContaBancaria() {
    }

    public ContaBancaria(int id, String nome_banco, long numero, int nib, long nuib, Empresa fk_id_empresa, boolean active_or_not) {
        this.id = id;
        this.nome_banco = nome_banco;
        this.numero = numero;
        this.nib = nib;
        this.nuib = nuib;
        this.fk_id_empresa = fk_id_empresa;
        this.active_or_not = active_or_not;
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

    public int getNib() {
        return nib;
    }

    public void setNib(int nib) {
        this.nib = nib;
    }

    public long getNuib() {
        return nuib;
    }

    public void setNuib(long nuib) {
        this.nuib = nuib;
    }

    public Empresa getFk_id_empresa() {
        return fk_id_empresa;
    }

    public void setFk_id_empresa(Empresa fk_id_empresa) {
        this.fk_id_empresa = fk_id_empresa;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }
    
    
}
