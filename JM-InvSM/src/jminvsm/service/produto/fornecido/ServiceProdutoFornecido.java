/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.produto.fornecido;

import jminvsm.dao.produto.fornecido.ProdutoFornecidoDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.produto.Produto;
import jminvsm.model.produto.fornecido.ProdutoFornecido;
import jminvsm.model.usuario.Usuario;
import static jminvsm.util.AlertUtilities.showDialog;
import static jminvsm.util.AlertUtilities.showErroNotification;
import static jminvsm.util.AlertUtilities.showSuccessNotification;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceProdutoFornecido {
    private final ProdutoFornecidoDAO produtoFornecidoDao;

    public ServiceProdutoFornecido(ProdutoFornecidoDAO produtoFornecidoDao) {
        this.produtoFornecidoDao = produtoFornecidoDao;
    }
    
    public void registraProdutoFornecido(Integer id_arm, Integer id_pro, Integer id_for,
            Usuario user, Integer qtd,String data_fornecida, Double  custo){
        
        // Validações básicas antes de registrar a categoria
        if (id_arm <= 0) {
            showErroNotification("Erro", "Armazem não definido!");
            return;
        }
        
        if (id_for <= 0) {
            showErroNotification("Erro", "Fornecedor não definido!");
            return;
        }
        if ( qtd <= 0) {
            showErroNotification("Erro", "Quantidade não definida!");
            return;
        }
        if (custo <= 0) {
            showErroNotification("Erro", "Preco não pode ser nulo!");
            return;
        }
        
        if ( data_fornecida.isEmpty()) {
            showErroNotification("Erro", "Data de fornecimento não pode ser nula!");
            return;
        }
        ProdutoFornecido pf = new ProdutoFornecido();
        Armazem a = new Armazem();
        a.setId(id_arm);
        pf.setArmazem(a);
        Produto p = new Produto();
        p.setId(id_pro);
        pf.setProduto(p);
        Fornecedor f = new Fornecedor();
        f.setId(id_for);
        pf.setFornecedor(f);
        pf.setUsuario(user);
        pf.setQuantidade(qtd);
        pf.setData_fornecida(data_fornecida);
        pf.setCusto(custo);
        try{
            produtoFornecidoDao.addEntity(pf);
            showSuccessNotification("Sucesso", "Stock actualizado com sucesso!");
        }catch(Exception e){
            showDialog("Erro", "Erro ao adicionar/actualizar sock!");
        }
        
        
        
    }
    
    

}
