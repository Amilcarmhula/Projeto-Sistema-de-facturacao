/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

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
    private int fk_id_empresa;
    private int fk_id_agente;
    private int fk_id_usuario;
    private boolean active_or_not;

    public Factura() {
    }

    public Factura(String tipo, long numero, boolean estado, double total, String data_vencimento, Empresa fk_id_empresa, Agente fk_id_agente, Usuario fk_id_usuario) {
        this.tipo = tipo;
        this.numero = numero;
        this.estado = estado;
        this.total = total;
        this.data_vencimento = data_vencimento;
        this.fk_id_empresa = fk_id_empresa.getId();
        this.fk_id_agente = fk_id_agente.getId();
        this.fk_id_usuario = fk_id_usuario.getId();
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

    public int getFk_id_empresa() {
        return fk_id_empresa;
    }

    public void setFk_id_empresa(int fk_id_empresa) {
        this.fk_id_empresa = fk_id_empresa;
    }

    public int getFk_id_agente() {
        return fk_id_agente;
    }

    public void setFk_id_cliente(int fk_id_agente) {
        this.fk_id_agente = fk_id_agente;
    }

    public int getFk_id_usuario() {
        return fk_id_usuario;
    }

    public void setFk_id_usuario(int fk_id_usuario) {
        this.fk_id_usuario = fk_id_usuario;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }

   
}
