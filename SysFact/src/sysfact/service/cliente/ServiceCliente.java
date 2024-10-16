/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.cliente;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import sysfact.dao.cliente.ClienteDAO;
import sysfact.model.cliente.Cliente;
import sysfact.util.ValidationUtilities;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceCliente {
    private ClienteDAO clienteDao;

    public ServiceCliente(ClienteDAO clienteDao) {
        this.clienteDao = clienteDao;
    }
    
    public Cliente getClienteByID(Integer id){
        if(id ==0){
            ValidationUtilities.showDialog(FontAwesomeIcon.TIMES, "Erro de validacao!", "Cliente nao definido!");
            return null;
        }
        
        Cliente cliente = clienteDao.getClieneByID(id);
        return cliente;
    }
}
