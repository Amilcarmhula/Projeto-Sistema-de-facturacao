/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.cliente.endereco;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.cliente.endereco.EnderecoClienteDAO;
import jminvsm.model.cliente.Cliente;
import jminvsm.model.cliente.endereco.EnderecoCliente;
import jminvsm.model.usuario.Usuario;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceEndereco {

    private final EnderecoClienteDAO enderecoDao;
    private boolean opsSuccess;

    public ServiceEndereco() throws SQLException {
        this.enderecoDao = new EnderecoClienteDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registar(String tipo, String avenida, String bairro, String cidade,
            Integer postalCode, Integer numero, String provincia, String rua,
            Integer id_cli, Usuario user) {
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
        if (id_cli == null || id_cli == 0) {
            showErroAlert( "Cliente nao selecionado ou definido!");
            return;
        }
        if (tipo == null || tipo.isEmpty()) {
            tipo = "Facturação";
        }

        EnderecoCliente endereco = new EnderecoCliente();
        Cliente c = new Cliente();
        c.setId(id_cli);
        endereco.setAvenida_cli(avenida);
        endereco.setBairro_cli(bairro);
        endereco.setCidade_cli(cidade);
        endereco.setCodigoPostal_cli(postalCode);
        endereco.setNumero_cli(numero);
        endereco.setProvincia_cli(provincia);
        endereco.setRua_cli(rua);
        endereco.setTipo_cli(tipo);
        endereco.setUsuario(user);
        endereco.setCliente(c);

        if (enderecoDao.addEntity(endereco)) {
            setOpsSuccess(true);
            showSuccessAlert( "Registo adicionado com sucesso.");
        }
    }

    public void actualizar(Integer id, String tipo, String avenida, String bairro, String cidade,
            Integer postalCode, Integer numero, String provincia, String rua,
            Usuario user) {
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
        if (tipo == null || tipo.isEmpty()) {
            tipo = "Facturação";
        }

        EnderecoCliente endereco = new EnderecoCliente();
        endereco.setAvenida_cli(avenida);
        endereco.setBairro_cli(bairro);
        endereco.setCidade_cli(cidade);
        endereco.setCodigoPostal_cli(postalCode);
        endereco.setNumero_cli(numero);
        endereco.setProvincia_cli(provincia);
        endereco.setRua_cli(rua);
        endereco.setUsuario(user);
        endereco.setTipo_cli(tipo);

        if (enderecoDao.updateEntityByID(endereco, id)) {
            setOpsSuccess(true);
            showSuccessAlert("Registo adicionado com sucesso.");
        }
    }

    public ObservableList<EnderecoCliente> listaEnderecos(){
        return enderecoDao.listAllEntities();
    }
    
    public ObservableList<EnderecoCliente> consultaEnderecosPorCLiente(int idCliente) {
        return enderecoDao.searchEntityByCLiente(idCliente);
    }

    public void excluir(int id) {
        setOpsSuccess(false);
        if (enderecoDao.deleteEntity(id)) {
            setOpsSuccess(true);
            showSuccessAlert( "Registo excluido com sucesso.");
        }
    }
}
