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
    private String data_vencimento;
    private boolean estado;
    private double total;
    private Empresa fk_id_empresa;
    private Cliente fk_id_cliente;
    private Usuario fk_id_usuario;
    private boolean active_or_not;

    public Factura() {
    }

    public Factura(int id, String tipo, long numero, String data_vencimento, boolean estado, double total, Empresa fk_id_empresa, Cliente fk_id_cliente, Usuario fk_id_usuario, boolean active_or_not) {
        this.id = id;
        this.tipo = tipo;
        this.numero = numero;
        this.data_vencimento = data_vencimento;
        this.estado = estado;
        this.total = total;
        this.fk_id_empresa = fk_id_empresa;
        this.fk_id_cliente = fk_id_cliente;
        this.fk_id_usuario = fk_id_usuario;
        this.active_or_not = active_or_not;
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

    public String getData_vencimento() {
        return data_vencimento;
    }

    public void setData_vencimento(String data_vencimento) {
        this.data_vencimento = data_vencimento;
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

    public Empresa getFk_id_empresa() {
        return fk_id_empresa;
    }

    public void setFk_id_empresa(Empresa fk_id_empresa) {
        this.fk_id_empresa = fk_id_empresa;
    }

    public Cliente getFk_id_cliente() {
        return fk_id_cliente;
    }

    public void setFk_id_cliente(Cliente fk_id_cliente) {
        this.fk_id_cliente = fk_id_cliente;
    }

    public Usuario getFk_id_usuario() {
        return fk_id_usuario;
    }

    public void setFk_id_usuario(Usuario fk_id_usuario) {
        this.fk_id_usuario = fk_id_usuario;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }
    
    
}
