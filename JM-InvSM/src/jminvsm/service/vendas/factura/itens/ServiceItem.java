/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.vendas.factura.itens;

import java.util.List;
import jminvsm.dao.vendas.itens.ItemDAO;
import jminvsm.model.vendas.factura.itens.Item;
import static jminvsm.util.AlertUtilities.showDialog;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceItem {
    private ItemDAO itemDao;

    public ServiceItem(ItemDAO itemDao) {
        this.itemDao = itemDao;
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
}
