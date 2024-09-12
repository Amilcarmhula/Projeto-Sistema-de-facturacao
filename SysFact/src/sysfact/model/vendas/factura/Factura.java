/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.model.vendas.factura;

import sysfact.model.cliente.Cliente;
import sysfact.model.empresa.Empresa;
import sysfact.model.usuario.Usuario;


/**
 *
 * @author JM-Tecnologias
 */
public class Factura {

    private int id;
    private String tipo;
    private long numero;
    private boolean estado;
    private double total;
    private String data_criacao;
    private String data_vencimento;
    private Empresa empresa;
    private Cliente agente;
    private Usuario usuario;
    private boolean active_or_not;

    public Factura() {
    }

    public Factura(String tipo, long numero, boolean estado, double total, String data_vencimento, Empresa empresa, Cliente agente, Usuario usuario) {
        this.tipo = tipo;
        this.numero = numero;
        this.estado = estado;
        this.total = total;
        this.data_vencimento = data_vencimento;
        this.empresa = empresa;
        this.agente = agente;
        this.usuario = usuario;
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

    public long getNumero() {
        return numero;
    }

    public void setNumero(long numero) {
        this.numero = numero;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public String getData_vencimento() {
        return data_vencimento;
    }

    public void setData_vencimento(String data_vencimento) {
        this.data_vencimento = data_vencimento;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    public Cliente getAgente() {
        return agente;
    }

    public void setAgente(Cliente agente) {
        this.agente = agente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }

    
   
}
