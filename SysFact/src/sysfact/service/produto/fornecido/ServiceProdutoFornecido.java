/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.produto.fornecido;

import javafx.scene.control.Alert;
import sysfact.dao.produto.fornecido.ProdutoFornecidoDAO;
import sysfact.model.armazem.Armazem;
import sysfact.model.fornecedor.Fornecedor;
import sysfact.model.produto.Produto;
import sysfact.model.produto.fornecido.ProdutoFornecido;
import sysfact.model.usuario.Usuario;
import sysfact.util.MenssagensDeDialogo;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceProdutoFornecido {
    private ProdutoFornecidoDAO produtoFornecidoDao;

    public ServiceProdutoFornecido(ProdutoFornecidoDAO produtoFornecidoDao) {
        this.produtoFornecidoDao = produtoFornecidoDao;
    }
    
    public void registraProdutoFornecido(Integer id_arm, Integer id_pro, Integer id_for,
            Usuario user, Integer qtd,String data_fornecida, Double  custo){
        
        // Validações básicas antes de registrar a categoria
        if (id_arm <= 0) {
            showErrorDialog("Erro de validação", "Armazem não definido.");
            return;
        }
        
        if (id_for <= 0) {
            showErrorDialog("Erro de validação", "Fornecedor não definido.");
            return;
        }
        if ( qtd <= 0) {
            showErrorDialog("Erro de validação", "Quantidade não definida.");
            return;
        }
        if (custo <= 0) {
            showErrorDialog("Erro de validação", "Preco não definido.");
            return;
        }
        
        if ( data_fornecida.isEmpty()) {
            showErrorDialog("Erro de validação", "Data de fornecimento não definida.");
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
            MenssagensDeDialogo.showInfo("Info", "Stock actualizado com sucesso.");
        }catch(Exception e){
            MenssagensDeDialogo.showErro("Erro", "Erro ao adicionar/actualizar sock!");
        }
        
        
        
    }
    
    
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
