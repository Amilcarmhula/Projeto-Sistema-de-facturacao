/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class Pagamento {

    private int id;
    private String data_pagamento;
    private long referencia_numero_fatura;
    private double valor;
    private int fk_id_metodo_pagamento;
    private String data_criacao;
    private int fk_id_factura;
    private int fk_id_usuario;
    private boolean active_or_not;

    public Pagamento() {
    }

    public Pagamento(String data_pagamento, long referencia_numero_fatura, double valor, MetodoPagamento fk_id_metodo_pagamento, Factura fk_id_factura, Usuario fk_id_usuario) {
        this.data_pagamento = data_pagamento;
        this.referencia_numero_fatura = referencia_numero_fatura;
        this.valor = valor;
        this.fk_id_metodo_pagamento = fk_id_metodo_pagamento.getId();
        this.fk_id_factura = fk_id_factura.getId();
        this.fk_id_usuario = fk_id_usuario.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getData_pagamento() {
        return data_pagamento;
    }

    public void setData_pagamento(String data_pagamento) {
        this.data_pagamento = data_pagamento;
    }

    public long getReferencia_numero_fatura() {
        return referencia_numero_fatura;
    }

    public void setReferencia_numero_fatura(long referencia_numero_fatura) {
        this.referencia_numero_fatura = referencia_numero_fatura;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public int getFk_id_metodo_pagamento() {
        return fk_id_metodo_pagamento;
    }

    public void setFk_id_metodo_pagamento(int fk_id_metodo_pagamento) {
        this.fk_id_metodo_pagamento = fk_id_metodo_pagamento;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public int getFk_id_factura() {
        return fk_id_factura;
    }

    public void setFk_id_factura(int fk_id_factura) {
        this.fk_id_factura = fk_id_factura;
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
