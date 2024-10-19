/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.vendas.factura.itens;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import java.sql.SQLException;
import java.util.List;
import sysfact.dao.vendas.itens.ItemDAO;
import sysfact.model.vendas.factura.itens.Item;
import sysfact.util.AlertUtilities;
import static sysfact.util.AlertUtilities.showDialog;
import static sysfact.util.AlertUtilities.showErroNotification;
import static sysfact.util.AlertUtilities.showSuccessNotification;

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
            showErroNotification("Erro", "Nao pode ser emitida factura sem itens!");
            return;
        }
        
        try {
            itemDao.addEntity(itens);
            showSuccessNotification("Sucesso", "Itens adicionados a factura com sucesso!");
        } catch (Exception e) {
            showDialog(FontAwesomeIcon.TIMES, "Erro", "Erro ao emitir factura!");
        }
    }
}
