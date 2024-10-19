/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.cliente;

import sysfact.dao.cliente.ClienteDAO;
import sysfact.model.cliente.Cliente;
import static sysfact.util.AlertUtilities.showErroNotification;

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
            showErroNotification("Erro", "Cliente nao definido!");
            return null;
        }
        
        Cliente cliente = clienteDao.getClieneByID(id);
        return cliente;
    }
}
