/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.vendas.factura.itens;

import java.sql.SQLException;
import java.util.List;
import javafx.scene.control.TableView;
import jminvsm.dao.vendas.itens.ItemDAO;
import jminvsm.model.vendas.factura.itens.Item;
import static jminvsm.util.AlertUtilities.showDialog;
import static jminvsm.util.AlertUtilities.showErroAlert;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceItem {
    private ItemDAO itemDao;

    public ServiceItem() throws SQLException {
        this.itemDao = new ItemDAO();
    }
    public void registaItens(List<Item> itens){
        if(itens == null || itens.isEmpty()){
            showErroAlert( "Nao pode ser emitida factura sem itens!");
            return;
        }
        
        try {
            itemDao.addEntity(itens);
//            showSuccessAlert( "Itens adicionados a factura com sucesso!");
        } catch (Exception ex) {
            showDialog( "Erro", "Erro ao emitir factura!");
        }
    }
    
    public void registaCotacao(List<Item> itens){
        if(itens == null || itens.isEmpty()){
            showErroAlert( "Nao pode ser emitida factura sem itens!");
            return;
        }
        
        try {
            itemDao.addEntityQuotation(itens);
//            showSuccessAlert( "Itens adicionados a factura com sucesso!");
        } catch (Exception ex) {
            showDialog( "Erro", "Erro ao emitir factura!");
        }
    }
    
    public List<Item>  consultaItensFactura(String numero){
        return itemDao.getItensData(numero);
    }
    
}
