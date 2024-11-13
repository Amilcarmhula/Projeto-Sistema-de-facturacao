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
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

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
            showErroAlert( "nome nao pode ser nulo!");
            return;
        }
        if (razaosocial == null || razaosocial.isEmpty()) {
//            showErroAlert( "Razao Social denifida por padrao!");
            razaosocial = nome + ", E.I";
        }
        if (tipo == null || tipo.isEmpty()) {
//            showErroAlert( "Tipo de cliente nao defindo. Tipo de Cliente padrao criado!");
            tipo = "Pessoa Física";
        }
        if (nuit == null || nuit <= 0) {
//            showErroAlert( "NUIT nulo. NUIT padrao criado!");
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
            showSuccessAlert( "Registo adicionado com sucesso.");
        }
    }

    public void actualizar(Integer id, String nome, String razaosocial, String tipo, Integer nuit, Usuario user) {
        setOpsSuccess(false);
        if (id == null || id <= 0) {
            showErroAlert( "Cliente nao selecionado!");
            return;
        }
        if (nome == null || nome.isEmpty()) {
            showErroAlert( "nome nao pode ser nulo!");
            return;
        }
        if (razaosocial == null || razaosocial.isEmpty()) {
//            showErroAlert( "Razao Social denifida por padrao!");
            razaosocial = nome + ", E.I";
        }
        if (tipo == null || tipo.isEmpty()) {
//            showErroAlert( "Tipo de cliente nao defindo. Tipo de Cliente padrao criado!");
            tipo = "Pessoa Física";
        }
        if (nuit == null || nuit <= 0) {
//            showErroAlert( "NUIT nulo. NUIT padrao criado!");
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
            showSuccessAlert( "Registo actualizado com sucesso.");
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
            showSuccessAlert( "Exclusao de registo com sucesso.");
        }

    }
}
