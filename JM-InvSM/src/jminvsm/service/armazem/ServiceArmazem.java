/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.armazem;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.armazem.ArmazemDAO;
import jminvsm.model.armazem.Armazem;
import jminvsm.service.ServiceException;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceArmazem {

    private final ArmazemDAO armazemDao;
    private boolean opsSuccess;

    public ServiceArmazem() throws SQLException {
        this.armazemDao = new ArmazemDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registar(String tipo, String nome, String descricao) {
        setOpsSuccess(false);
        if (tipo == null || tipo.isEmpty()) {
            throw new ServiceException("Tipo nao definido!");
        }
        if (nome == null || nome.isEmpty()) {
            throw new ServiceException("Nome nao pode ser nulo!");
        }
        if (descricao == null || descricao.isEmpty()) {
            descricao = "Descricao de armazem padrao";
        }

        Armazem a = new Armazem();
        a.setTipo(tipo);
        a.setNome_arm(nome);
        a.setDescricao_arm(descricao);
        if (armazemDao.addEntity(a)) {
            setOpsSuccess(true);
        }
    }

    public void actualizar(Integer id, String tipo, String nome, String descricao) {
        setOpsSuccess(false);
        if (id == null) {
            throw new ServiceException("Armazem nao selecionado!");
        }
        if (tipo == null || tipo.isEmpty()) {
            throw new ServiceException("Tipo nao definido!");
        }
        if (nome == null || nome.isEmpty()) {
            throw new ServiceException("Nome nao pode ser nulo!");
        }
        if (descricao == null || descricao.isEmpty()) {
            descricao = "Descricao de armazem padrao";
        }

        Armazem a = new Armazem();
        a.setTipo(tipo);
        a.setNome_arm(nome);
        a.setDescricao_arm(descricao);
        if (armazemDao.updateEntityByID(a, id)) {
            setOpsSuccess(true);
        }
    }
    public ObservableList<Armazem> listaArmazens(){
        return armazemDao.listAllEntities();
    }
    
    public ObservableList<Armazem> listaTodosArmazens(){
        return armazemDao.listAllArmazens();
    }
    
    public Armazem getUltimoArmazem(){
        return armazemDao.getLastEntity();
    }
    
    public Armazem consultaArmazem(int id){
        return armazemDao.getEntityByID(id);
    }

    public void excluir(int id) {
        setOpsSuccess(false);
        
        if (armazemDao.deleteEntity(id)) {
            setOpsSuccess(true);
        }
    }

}
