/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.fornecedor.endereco;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.fornecedor.endereco.EnderecoFornecedorDAO;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.fornecedor.endereco.EnderecoFornecedor;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceEndereco {

    private final EnderecoFornecedorDAO enderecoDao;
    private boolean opsSuccess;

    public ServiceEndereco() throws SQLException {
        this.enderecoDao = new EnderecoFornecedorDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registar(String avenida, String bairro, String cidade,
            Integer postalCode, Integer numero, String provincia, String rua,
            Integer id_Forn, Usuario user) {
        setOpsSuccess(false);
        if (avenida == null || avenida.isEmpty()) {
            avenida = "de Mocambique";
        }
        if (bairro == null || bairro.isEmpty()) {
            throw new ServiceException("Bairro nao pode ser nulo! Informe o bairro");
        }
        if (cidade == null || cidade.isEmpty()) {
            throw new ServiceException("Cidade nao pode ser nula! Informe uma cidade.");
        }
        if (postalCode == null || postalCode <= 0) {
            postalCode = 1111;
        }
        if (numero == null || numero == 0) {
            throw new ServiceException("Numero do endereco nao pode ser nulo!");
        }
        if (provincia == null || provincia.isEmpty()) {
            throw new ServiceException("Provincia nao pode ser nula!");
        }
        if (rua == null || rua.isEmpty()) {
            rua = "Dos Mocambicanos";
        }
        if (id_Forn == null || id_Forn == 0) {
            throw new ServiceException("Fornecedor nao selecionado ou definido!");
        }

        EnderecoFornecedor endereco = new EnderecoFornecedor();
        Fornecedor c = new Fornecedor();
        c.setId(id_Forn);
        endereco.setAvenida_forn(avenida);
        endereco.setBairro_forn(bairro);
        endereco.setCidade_forn(cidade);
        endereco.setCodigoPostal_forn(postalCode);
        endereco.setNumero_forn(numero);
        endereco.setProvincia_forn(provincia);
        endereco.setRua_forn(rua);
        endereco.setUsuario(user);
        endereco.setFornecedor(c);

        if (enderecoDao.addEntity(endereco)) {
            setOpsSuccess(true);
        }
    }

    public void actualizar(Integer id, String avenida, String bairro, String cidade,
            Integer postalCode, Integer numero, String provincia, String rua,
            Integer id_Forn, Usuario user) {
        setOpsSuccess(false);
        if (id == null || id == 0) {
            throw new ServiceException("Endereco nao selecionado ou definido!");
        }
        if (avenida == null || avenida.isEmpty()) {
            avenida = "de Mocambique";
        }
        if (bairro == null || bairro.isEmpty()) {
            throw new ServiceException("Bairro nao pode ser nulo! Informe o bairro");
        }
        if (cidade == null || cidade.isEmpty()) {
            throw new ServiceException("Cidade nao pode ser nula! Informe uma cidade.");
        }
        if (postalCode == null || postalCode == 0) {
            postalCode = 1111;
        }
        if (numero == null || numero == 0) {
            throw new ServiceException("Numero do endereco nao pode ser nulo!");
        }
        if (provincia == null || provincia.isEmpty()) {
            throw new ServiceException("Provincia nao pode ser nula!");
        }
        if (rua == null || rua.isEmpty()) {
            rua = "Dos Mocambicanos";
        }
        if (id_Forn == null || id_Forn == 0) {
            throw new ServiceException("Fornecedor nao selecionado ou definido!");
        }

        EnderecoFornecedor endereco = new EnderecoFornecedor();
        Fornecedor c = new Fornecedor();
        c.setId(id_Forn);
        endereco.setAvenida_forn(avenida);
        endereco.setBairro_forn(bairro);
        endereco.setCidade_forn(cidade);
        endereco.setCodigoPostal_forn(postalCode);
        endereco.setNumero_forn(numero);
        endereco.setProvincia_forn(provincia);
        endereco.setRua_forn(rua);
        endereco.setUsuario(user);
        endereco.setFornecedor(c);

        if (enderecoDao.updateEntityByID(endereco, id)) {
            setOpsSuccess(true);
        }
    }

    public ObservableList<EnderecoFornecedor> getEnderecos(){
        return enderecoDao.listAllEntities();
    }
    
    public ObservableList<EnderecoFornecedor> consultaEnderecosPorFornecedor(int id) {
        return enderecoDao.searchEntityByCLiente(id);
    }

    public void excluir(int id) {
        setOpsSuccess(false);
        if (enderecoDao.deleteEntity(id)) {
            setOpsSuccess(true);
        }
    }
}
