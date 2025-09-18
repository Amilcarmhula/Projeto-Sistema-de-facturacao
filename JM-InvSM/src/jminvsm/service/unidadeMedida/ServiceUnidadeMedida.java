/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.unidadeMedida;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.unidademedida.UnidadeMedidaDAO;
import jminvsm.model.unidade_medida.UnidadeMedida;
import static jminvsm.util.AlertUtilities.showSuccessAlert;
import static jminvsm.util.AlertUtilities.showErroAlert;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceUnidadeMedida {

    private final UnidadeMedidaDAO unidadeDao;
    private boolean opsSuccess;

    public ServiceUnidadeMedida() throws SQLException {
        this.unidadeDao = new UnidadeMedidaDAO();
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void regista(String identificador, String sigla, String descricao) {

        setOpsSuccess(false);

        if (identificador == null || identificador.isEmpty()) {
            showErroAlert("Nome da unidade de medida nao pode ser nulo!");
            return;
        }

        if (sigla == null || sigla.isEmpty()) {
            showErroAlert("Sigla da unidade de medida nao pode ser nulo!");
            return;
        }

        if (descricao == null || descricao.isEmpty()) {
            descricao = "Unidade de medida sem descricao. Esta e uma descriao padrao!";
        }

        UnidadeMedida u = new UnidadeMedida();
        u.setNome(identificador);
        u.setSigla(sigla);
        u.setDescricao(descricao);

        if (unidadeDao.addEntity(u)) {
            setOpsSuccess(true);
            showSuccessAlert("Registo adicionado com sucesso");
        }
    }

    public void actualizar(Integer id, String identificador, String sigla, String descricao) {

        setOpsSuccess(false);

        if (id == null || id <= 0) {
            showErroAlert("Unidade de medida nao selecionada!");
            return;
        }

        if (identificador == null || identificador.isEmpty()) {
            showErroAlert("Nome da unidade de medida nao pode ser nulo!");
            return;
        }

        if (sigla == null || sigla.isEmpty()) {
            showErroAlert("Sigla da unidade de medida nao pode ser nulo!");
            return;
        }

        if (descricao == null || descricao.isEmpty()) {
            descricao = "Unidade de medida sem descricao. Esta e uma descriao padrao!";
        }

        UnidadeMedida u = new UnidadeMedida();
        u.setNome(identificador);
        u.setSigla(sigla);
        u.setDescricao(descricao);

        if (unidadeDao.updateEntityByID(u, id)) {
            setOpsSuccess(true);
            showSuccessAlert("Registo actualizada com sucesso");
        }
    }

    public ObservableList<UnidadeMedida> getUnidades() {
        return unidadeDao.listAllEntities();
    }
    
    public void excluir(int id){
        setOpsSuccess(false);
        if(unidadeDao.deleteEntity(id)){
            setOpsSuccess(true);
            showSuccessAlert("Unidade de medida excluida com sucesso.");
        }
    }
}
