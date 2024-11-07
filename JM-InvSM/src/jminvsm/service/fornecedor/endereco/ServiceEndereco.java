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
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

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
            showErroAlert( "Bairro nao pode ser nulo! Informe o bairro");
            return;
        }
        if (cidade == null || cidade.isEmpty()) {
            showErroAlert( "Cidade nao pode ser nula! Informe uma cidade.");
            return;
        }
        if (postalCode == null || postalCode <= 0) {
            showErroAlert( "Codigo postal nao informado. Sera criado um codigo postal padrao!");
            postalCode = 1111;
        }
        if (numero == null || numero == 0) {
            showErroAlert( "Numero do endereco nao pode ser nulo!");
            return;
        }
        if (provincia == null || provincia.isEmpty()) {
            showErroAlert( "Provincia nao pode ser nula!");
            return;
        }
        if (rua == null || rua.isEmpty()) {
            rua = "Dos Mocambicanos";
        }
        if (id_Forn == null || id_Forn == 0) {
            showErroAlert( "Cliente nao selecionado ou definido!");
            return;
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
            showSuccessAlert( "Registo adicionado com sucesso.");
        }
    }

    public void actualizar(Integer id, String avenida, String bairro, String cidade,
            Integer postalCode, Integer numero, String provincia, String rua,
            Integer id_Forn, Usuario user) {
        setOpsSuccess(false);
        if (id == null || id == 0) {
            showErroAlert( "Endereco nao selecionado ou definido!");
            return;
        }
        if (avenida == null || avenida.isEmpty()) {
            avenida = "de Mocambique";
        }
        if (bairro == null || bairro.isEmpty()) {
            showErroAlert( "Bairro nao pode ser nulo! Informe o bairro");
            return;
        }
        if (cidade == null || cidade.isEmpty()) {
            showErroAlert( "Cidade nao pode ser nula! Informe uma cidade.");
            return;
        }
        if (postalCode == null || postalCode == 0) {
            showErroAlert( "Codigo postal nao informado. Sera criado um codigo postal padrao!");
            postalCode = 1111;
        }
        if (numero == null || numero == 0) {
            showErroAlert( "Numero do endereco nao pode ser nulo!");
            return;
        }
        if (provincia == null || provincia.isEmpty()) {
            showErroAlert( "Provincia nao pode ser nula!");
            return;
        }
        if (rua == null || rua.isEmpty()) {
            rua = "Dos Mocambicanos";
        }
        if (id_Forn == null || id_Forn == 0) {
            showErroAlert( "Cliente nao selecionado ou definido!");
            return;
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
            showSuccessAlert("Registo actualizado com sucesso.");
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
            showSuccessAlert( "Registo excluido com sucesso.");
        }
    }
}
