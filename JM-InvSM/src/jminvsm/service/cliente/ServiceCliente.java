/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.cliente;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.cliente.ClienteDAO;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceCliente {

    private final ClienteDAO clienteDao;
    private boolean opsSuccess;

    public ServiceCliente() throws SQLException {
        this.clienteDao = new ClienteDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registar(String nome, String razaosocial, String tipo, Integer nuit, Usuario user) {
        setOpsSuccess(false);
        if (nome == null || nome.isEmpty()) {
            throw new ServiceException("Nome nao pode ser nulo!");
        }
        if (razaosocial == null || razaosocial.isEmpty()) {
            razaosocial = nome + ", E.I";
        }
        if (tipo == null || tipo.isEmpty()) {
            tipo = "Pessoa Física";
        }
        if (nuit == null || nuit <= 0) {
            nuit = 100000001;
        }
        Cliente cliente = new Cliente();
        cliente.setNome_cli(nome);
        cliente.setRazao_cli(razaosocial);
        cliente.setNuit_cli(nuit);
        cliente.setTipo(tipo);
        cliente.setUsuario(user);
        if (clienteDao.addEntity(cliente)) {
            setOpsSuccess(true);
        }
    }

    public void actualizar(Integer id, String nome, String razaosocial, String tipo, Integer nuit, Usuario user) {
        setOpsSuccess(false);
        if (id == null || id <= 0) {
            throw new ServiceException("Cliente nao selecionado!");
        }
        if (nome == null || nome.isEmpty()) {
            throw new ServiceException("Nome nao pode ser nulo!");
        }
        if (razaosocial == null || razaosocial.isEmpty()) {
            razaosocial = nome + ", E.I";
        }
        if (tipo == null || tipo.isEmpty()) {
            tipo = "Pessoa Física";
        }
        if (nuit == null || nuit <= 0) {
            nuit = 100000001;
        }
        Cliente cliente = new Cliente();
        cliente.setNome_cli(nome);
        cliente.setRazao_cli(razaosocial);
        cliente.setNuit_cli(nuit);
        cliente.setTipo(tipo);
        cliente.setUsuario(user);
        if (clienteDao.updateEntityByID(cliente, id)) {
            setOpsSuccess(true);
        }
    }
    
    public ObservableList<Cliente> listaClientes() {
        return clienteDao.listClientesForSearch();
    }
    public ObservableList<Cliente> listaClienteView() {
        return clienteDao.listAllEntitiesToClienteView();
       
    }
    
    public Cliente getClienteCompletoByID(Integer id) {
        Cliente cliente = clienteDao.getFullClieneByID(id);
        return cliente;
    }

    public Cliente getUtimoCliente() {
        return clienteDao.getLastEntity();
    }

    public Cliente consultaClienteByID(Integer id) {
        Cliente cliente = clienteDao.getEntityByID(id);
        return cliente;
    }

    public int contaClientes() {
        return clienteDao.CountClientes();
    }

    public void excluir(int id) {
        setOpsSuccess(false);
        if (clienteDao.deleteEntity(id)) {
            setOpsSuccess(true);
        }

    }
}
