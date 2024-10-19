/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.armazem;

import java.sql.SQLException;
import sysfact.dao.armazem.ArmazemDAO;
import sysfact.model.armazem.Armazem;
import static sysfact.util.AlertUtilities.showErroNotification;
import static sysfact.util.AlertUtilities.showSuccessNotification;

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

    public void registaArmazem(String tipo, String nome, String descricao) {
        setOpsSuccess(false);
        if (tipo == null || tipo.isEmpty()) {
            showErroNotification("Erro", "Tipo nao definido!");
            return;
        }
        if (nome == null || nome.isEmpty()) {
            showErroNotification("Erro", "nome nao pode ser nulo!");
            return;
        }
        if (descricao == null || descricao.isEmpty()) {
            showErroNotification("Erro", "Descricao nula, sera definida uma descricao padrao!");
            descricao = "Descricao de armazem padrao";
        }

        Armazem a = new Armazem();
        a.setTipo(tipo);
        a.setNome_arm(nome);
        a.setDescricao_arm(descricao);
        if (armazemDao.addEntity(a)) {
            setOpsSuccess(true);
            showSuccessNotification("Sucesso", "Armazem adicionado com sucesso!");
        }
    }

    public void actualizaArmazem(Integer id, String tipo, String nome, String descricao) {
        setOpsSuccess(false);
        if (id == null) {
            showErroNotification("Erro", "Armazem nao selecionado!");
            return;
        }
        if (tipo == null || tipo.isEmpty()) {
            showErroNotification("Erro", "Tipo nao definido!");
            return;
        }
        if (nome == null || nome.isEmpty()) {
            showErroNotification("Erro", "nome nao pode ser nulo!");
            return;
        }
        if (descricao == null || descricao.isEmpty()) {
            showErroNotification("Erro", "Descricao nula, sera definida uma descricao padrao!");
            descricao = "Descricao de armazem padrao";
        }

        Armazem a = new Armazem();
        a.setTipo(tipo);
        a.setNome_arm(nome);
        a.setDescricao_arm(descricao);
        if (armazemDao.updateEntityByID(a, id)) {
            setOpsSuccess(true);
            showSuccessNotification("Sucesso", "Armazem actualizado com sucesso!");
        }
    }

    public void excluir(int id) {
        setOpsSuccess(false);
        
        if (armazemDao.deleteEntity(id)) {
            setOpsSuccess(true);
            showSuccessNotification("Sucesso", "Registo excluido com sucesso!");
        }
    }

}
