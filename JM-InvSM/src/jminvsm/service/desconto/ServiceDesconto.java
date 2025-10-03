package jminvsm.service.desconto;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.desconto.DescontoDAO;
import jminvsm.model.desconto.Desconto;
import static jminvsm.util.AlertUtilities.showErroAlert;
import static jminvsm.util.AlertUtilities.showSuccessAlert;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceDesconto {

    private final DescontoDAO descontoDao;
    private boolean opsSuccess;

    public ServiceDesconto() throws SQLException {
        this.descontoDao = new DescontoDAO();
    }

    public boolean isOpsSuccess() {
        return opsSuccess;
    }

    public void setOpsSuccess(boolean opsSuccess) {
        this.opsSuccess = opsSuccess;
    }

    public void regista(String nomeDesconto, String descricao) {
        setOpsSuccess(false);
        
        if (nomeDesconto == null || nomeDesconto.isEmpty()) {
            showErroAlert( "Designacao do desconto nao pode ser nula!");
            return;
        }
        
        if (descricao == null || descricao.isEmpty()) {
            descricao = "Descricao padrao!";
        }
        
        Desconto d = new Desconto();
        d.setNome(nomeDesconto);
        d.setDescricao(descricao);
        if (descontoDao.addEntity(d)) {
            setOpsSuccess(true);
            showSuccessAlert( "Desconto registado com sucesso!");
        }
    }
    
    public void actualizar(Integer id, String nomeDesconto, String descricao) {
        setOpsSuccess(false);
        
        if (id == null) {
            showErroAlert("Desconto nao selecionado!");
            return;
        }
        
        if (nomeDesconto == null || nomeDesconto.isEmpty()) {
            showErroAlert( "Designacao do desconto nao pode ser nula!");
            return;
        }
        
        if (descricao == null || descricao.isEmpty()) {
            descricao = "Descricao padrao!";
        }
        
        Desconto d = new Desconto();
        d.setNome(nomeDesconto);
        d.setDescricao(descricao);
        if (descontoDao.updateEntity(d,id)) {
            setOpsSuccess(true);
            showSuccessAlert( "Desconto actualizado com sucesso!");
        }
    }
    
     public void excluir(Integer id) {
        setOpsSuccess(false);
        
        if (id == null) {
            showErroAlert("Desconto nao selecionado!");
            return;
        }
          
        if (descontoDao.deleteEntity(id)) {
            setOpsSuccess(true);
        }
    }

    public ObservableList<Desconto> listDescontos() {
        return descontoDao.listAllEntities();
    }
}
