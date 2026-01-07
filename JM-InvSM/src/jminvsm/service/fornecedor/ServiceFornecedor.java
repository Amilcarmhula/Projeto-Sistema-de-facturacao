/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.fornecedor;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.fornecedor.FornecedorDAO;
import jminvsm.model.fornecedor.Fornecedor;
import jminvsm.model.usuario.Usuario;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceFornecedor {

    private final FornecedorDAO fornecedor;
    private boolean opsSuccess;

    public ServiceFornecedor() throws SQLException {
        this.fornecedor = new FornecedorDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void registar(String razaosocial, String tipo, Integer nuit) {
        setOpsSuccess(false);

        if (razaosocial == null || razaosocial.isEmpty()) {
            showErroAlert( "Razao Social Nao pode ser nula!");
            return;
        }
        if (tipo == null || tipo.isEmpty()) {
            tipo = "Pessoa Física";
        }
        if (nuit == null || nuit <= 0) {
            nuit = 100000001;
        }
        Fornecedor f = new Fornecedor();
        f.setRazaosocial_forn(razaosocial);
        f.setNuit_forn(nuit);
        f.setTipo_forn(tipo);
        if (fornecedor.addEntity(f)) {
            setOpsSuccess(true);
            showSuccessAlert( "Registo adicionado com sucesso.");
        }
    }

    public void actualizar(Integer id, String razaosocial, String tipo, Integer nuit) {
        setOpsSuccess(false);
        if (id == null || id <= 0) {
            showErroAlert( "Fornecedor nao selecionado!");
            return;
        }

        if (razaosocial == null || razaosocial.isEmpty()) {
            showErroAlert( "Razao Social Nao pode ser nula!");
            return;
        }
        if (tipo == null || tipo.isEmpty()) {
            tipo = "Pessoa Física";
        }
        if (nuit == null || nuit <= 0) {
            nuit = 100000001;
        }
        Fornecedor f = new Fornecedor();
        f.setRazaosocial_forn(razaosocial);
        f.setNuit_forn(nuit);
        f.setTipo_forn(tipo);
        if (fornecedor.updateEntityByID(f, id)) {
            setOpsSuccess(true);
            showSuccessAlert( "Registo actualizado com sucesso.");
        }
    }
    
    public ObservableList<Fornecedor> getFornecedores() {
        return fornecedor.listAllEntities();
    }
    
    public ObservableList<Fornecedor> listaTodosFornecedores() {
        return fornecedor.listAllFornecedores();
    }
    public Fornecedor ultimoFornecedor() {
        return fornecedor.getLastEntity();
       
    }
    
    public Fornecedor consultaFornecedor(int id) {
        return fornecedor.getEntityByID(id);
        
    }

    public int contaFornecedores() {
        return fornecedor.CountFornecedor();
    }

    public void excluir(int id) {
        setOpsSuccess(false);
        if (fornecedor.deleteEntity(id)) {
            setOpsSuccess(true);
            showSuccessAlert( "Registo excluido com sucesso.");
        }

    }
}
