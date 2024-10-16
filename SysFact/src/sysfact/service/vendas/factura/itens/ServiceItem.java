/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.vendas.factura.itens;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.util.List;
import sysfact.dao.vendas.itens.ItemDAO;
import sysfact.model.vendas.factura.itens.Item;
import sysfact.util.ValidationUtilities;

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
            ValidationUtilities.showDialog(FontAwesomeIcon.TIMES, "Erro de dados", "Nao existem itens na factura!");
            return;
        }
        itemDao.addEntity(itens);
    }
}
