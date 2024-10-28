/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.armazem.endereco;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.armazem.endereco.EnderecoArmazemDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.model.armazem.endereco.EnderecoArmazem;
import jminvsm.model.usuario.Usuario;
import static jminvsm.util.AlertUtilities.showErroNotification;
import static jminvsm.util.AlertUtilities.showSuccessNotification;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceEndereco {

    private final EnderecoArmazemDAO enderecoArmazemDao;
    private boolean opsSuccess;

    public ServiceEndereco() throws SQLException {
        this.enderecoArmazemDao = new EnderecoArmazemDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registar(String avenida, String bairro, String cidade,
            Integer postalCode, Integer numero, String provincia, String rua,
            Integer id_arm, Usuario user) {
        setOpsSuccess(false);
        if (avenida == null || avenida.isEmpty()) {
            showErroNotification("Erro", "Avenida nao pode ser nulo! sera introduzida uma avenida padrao.");
            avenida = "de Mocambique";
        }
        if (bairro == null || bairro.isEmpty()) {
            showErroNotification("Erro", "Bairro nao pode ser nulo! Informe o bairro");
            return;
        }
        if (cidade == null || cidade.isEmpty()) {
            showErroNotification("Erro", "Cidade nao pode ser nula! Informe uma cidade.");
            return;
        }
        if (postalCode == null || postalCode == 0) {
            showErroNotification("Erro", "Codigo postal nao informado. Sera criado um codigo postal padrao!");
            postalCode = 1111;
        }
        if (numero == null || numero == 0) {
            showErroNotification("Erro", "Numero do endereco nao pode ser nulo!");
            return;
        }
        if (provincia == null || provincia.isEmpty()) {
            showErroNotification("Erro", "Provincia nao pode ser nula!");
            return;
        }
        if (rua == null || rua.isEmpty()) {
            showErroNotification("Erro", "Rua nao pode ser nula. Sere criada uma rua padrao!");
            rua = "Dos Mocambicanos";
        }
        if (id_arm == null || id_arm == 0) {
            showErroNotification("Erro", "Armazem nao selecionado!");
            return;
        }

        EnderecoArmazem x = new EnderecoArmazem();
        Armazem a = new Armazem();
        a.setId(id_arm);
        x.setAvenida_arm(avenida);
        x.setBairro_arm(bairro);
        x.setCidade_arm(cidade);
        x.setCodigoPostal_arm(postalCode);
        x.setNumero_arm(numero);
        x.setProvincia_arm(provincia);
        x.setRua_arm(rua);
        x.setUsuario(user);
        x.setArmazem(a);

        if (enderecoArmazemDao.addEntity(x)) {
            setOpsSuccess(true);
            showSuccessNotification("Sucesso", "Endereco registado com sucesso!");
        }
    }

    public void actualizar(String avenida, String bairro, String cidade,
            Integer postalCode, Integer numero, String provincia, String rua,
            Integer id, Usuario user) {
        setOpsSuccess(false);
        if (avenida == null || avenida.isEmpty()) {
            showErroNotification("Erro", "Avenida nao pode ser nulo! sera introduzida uma avenida padrao.");
            avenida = "de Mocambique";
        }
        if (bairro == null || bairro.isEmpty()) {
            showErroNotification("Erro", "Bairro nao pode ser nulo! Informe o bairro");
            return;
        }
        if (cidade == null || cidade.isEmpty()) {
            showErroNotification("Erro", "Cidade nao pode ser nula! Informe uma cidade.");
            return;
        }
        if (postalCode == null || postalCode == 0) {
            showErroNotification("Erro", "Codigo postal nao informado. Sera criado um codigo postal padrao!");
            postalCode = 1111;
        }
        if (numero == null || numero == 0) {
            showErroNotification("Erro", "Numero do endereco nao pode ser nulo!");
            return;
        }
        if (provincia == null || provincia.isEmpty()) {
            showErroNotification("Erro", "Provincia nao pode ser nula!");
            return;
        }
        if (rua == null || rua.isEmpty()) {
            showErroNotification("Erro", "Rua nao pode ser nula. Sere criada uma rua padrao!");
            rua = "Dos Mocambicanos";
        }
        if (id == null || id == 0) {
            showErroNotification("Erro", "Armazem nao selecionado!");
            return;
        }

        EnderecoArmazem x = new EnderecoArmazem();
        x.setAvenida_arm(avenida);
        x.setBairro_arm(bairro);
        x.setCidade_arm(cidade);
        x.setCodigoPostal_arm(postalCode);
        x.setNumero_arm(numero);
        x.setProvincia_arm(provincia);
        x.setRua_arm(rua);
        x.setUsuario(user);

        if (enderecoArmazemDao.updateEntityByID(x, id)) {
            setOpsSuccess(true);
            showSuccessNotification("Sucesso", "Endereco registado com sucesso!");
        }
    }
    
    public ObservableList<EnderecoArmazem> listaEnderecos() {
        return enderecoArmazemDao.listAllEntities();
    }
    
    public ObservableList<EnderecoArmazem> consultaEnderecosPorArmazem(int idCliente) {
        return enderecoArmazemDao.searchEntityByCLiente(idCliente);
    }
    
    public void excluir(int id) {
        setOpsSuccess(false);
        
        if (enderecoArmazemDao.deleteEntity(id)) {
            setOpsSuccess(true);
            showSuccessNotification("Sucesso", "Registo excluido com sucesso!");
        }
    }
}
