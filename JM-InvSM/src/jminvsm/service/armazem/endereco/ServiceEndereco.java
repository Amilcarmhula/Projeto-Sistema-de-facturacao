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
import jminvsm.service.ServiceException;

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
            Integer id_arm) {
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
        if (id_arm == null || id_arm == 0) {
            throw new ServiceException("Armazem nao selecionado!");
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
        x.setArmazem(a);

        if (enderecoArmazemDao.addEntity(x)) {
            setOpsSuccess(true);
        }
    }

    public void actualizar(String avenida, String bairro, String cidade,
            Integer postalCode, Integer numero, String provincia, String rua,
            Integer id) {
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
        if (id == null || id == 0) {
            throw new ServiceException("Endereco nao selecionado!");
        }

        EnderecoArmazem x = new EnderecoArmazem();
        x.setAvenida_arm(avenida);
        x.setBairro_arm(bairro);
        x.setCidade_arm(cidade);
        x.setCodigoPostal_arm(postalCode);
        x.setNumero_arm(numero);
        x.setProvincia_arm(provincia);
        x.setRua_arm(rua);

        if (enderecoArmazemDao.updateEntityByID(x, id)) {
            setOpsSuccess(true);
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
        }
    }
}
