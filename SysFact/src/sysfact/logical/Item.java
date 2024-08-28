/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class Item {

    private int id;
    private String tipo;
    private double quantidade;
    private double preco_unitario;
    private double subtotal;
    private String data_criacao;
    private int fk_id_desconto;
    private int fk_id_produto;
    private int fk_id_factura;
    private int fk_id_servico;
    private int fk_id_usuario;
    private boolean active_or_not;

    public Item() {
    }

    public Item(String tipo, double quantidade, double preco_unitario, double subtotal, Desconto fk_id_desconto, Produto fk_id_produto, Factura fk_id_factura, Servico fk_id_servico, Usuario fk_id_usuario) {
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.preco_unitario = preco_unitario;
        this.subtotal = subtotal;
        this.fk_id_desconto = fk_id_desconto.getId();
        this.fk_id_produto = fk_id_produto.getId();
        this.fk_id_factura = fk_id_factura.getId();
        this.fk_id_servico = fk_id_servico.getId();
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

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public double getPreco_unitario() {
        return preco_unitario;
    }

    public void setPreco_unitario(double preco_unitario) {
        this.preco_unitario = preco_unitario;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getData_criacao() {
        return data_criacao;
    }

    public void setData_criacao(String data_criacao) {
        this.data_criacao = data_criacao;
    }

    public int getFk_id_desconto() {
        return fk_id_desconto;
    }

    public void setFk_id_desconto(int fk_id_desconto) {
        this.fk_id_desconto = fk_id_desconto;
    }

    public int getFk_id_produto() {
        return fk_id_produto;
    }

    public void setFk_id_produto(int fk_id_produto) {
        this.fk_id_produto = fk_id_produto;
    }

    public int getFk_id_factura() {
        return fk_id_factura;
    }

    public void setFk_id_factura(int fk_id_factura) {
        this.fk_id_factura = fk_id_factura;
    }

    public int getFk_id_servico() {
        return fk_id_servico;
    }

    public void setFk_id_servico(int fk_id_servico) {
        this.fk_id_servico = fk_id_servico;
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
