/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.logical;

/**
 *
 * @author JM-Tecnologias
 */
public class ProdutoFornecido {

    private int id;
    private int quantidade;
    private String data_fornedida;
    private double custo;
    private Armazem fk_id_armazem;
    private Produto fk_id_produto;
    private Fornecedor fk_id_fornecedor;
    private boolean active_or_not;
    
    public ProdutoFornecido(){}

    public ProdutoFornecido(int id, int quantidade, String data_fornedida, double custo, Armazem fk_id_armazem, Produto fk_id_produto, Fornecedor fk_id_fornecedor, boolean active_or_not) {
        this.id = id;
        this.quantidade = quantidade;
        this.data_fornedida = data_fornedida;
        this.custo = custo;
        this.fk_id_armazem = fk_id_armazem;
        this.fk_id_produto = fk_id_produto;
        this.fk_id_fornecedor = fk_id_fornecedor;
        this.active_or_not = active_or_not;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getData_fornedida() {
        return data_fornedida;
    }

    public void setData_fornedida(String data_fornedida) {
        this.data_fornedida = data_fornedida;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public Armazem getFk_id_armazem() {
        return fk_id_armazem;
    }

    public void setFk_id_armazem(Armazem fk_id_armazem) {
        this.fk_id_armazem = fk_id_armazem;
    }

    public Produto getFk_id_produto() {
        return fk_id_produto;
    }

    public void setFk_id_produto(Produto fk_id_produto) {
        this.fk_id_produto = fk_id_produto;
    }

    public Fornecedor getFk_id_fornecedor() {
        return fk_id_fornecedor;
    }

    public void setFk_id_fornecedor(Fornecedor fk_id_fornecedor) {
        this.fk_id_fornecedor = fk_id_fornecedor;
    }

    public boolean isActive_or_not() {
        return active_or_not;
    }

    public void setActive_or_not(boolean active_or_not) {
        this.active_or_not = active_or_not;
    }
    
    
}
