package jminvsm.service.desconto;

import java.sql.SQLException;
import javafx.collections.ObservableList;
import jminvsm.dao.desconto.DescontoDAO;
import jminvsm.model.desconto.Desconto;
import jminvsm.service.ServiceException;

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

    public void regista(String nomeDesconto, Double percentagem, String descricao) {
        setOpsSuccess(false);
        
        if (nomeDesconto == null || nomeDesconto.isEmpty()) {
            throw new ServiceException("Designacao do desconto nao pode ser nula!");
        }
        
        if (descricao == null || descricao.isEmpty()) {
            descricao = "Descricao padrao!";
        }
        
        if (percentagem == null) {
            throw new ServiceException("Taxa de Desconto nao informada!");
        }
        
        Desconto d = new Desconto();
        d.setNome(nomeDesconto);
        d.setPercentagem(percentagem);
        d.setDescricao(descricao);
        if (descontoDao.addEntity(d)) {
            setOpsSuccess(true);
        }
    }
    
    public void actualizar(Integer id, String nomeDesconto, Double percentagem, String descricao) {
        setOpsSuccess(false);
        
        if (id == null) {
            throw new ServiceException("Desconto nao selecionado!");
        }
        
        if (nomeDesconto == null || nomeDesconto.isEmpty()) {
            throw new ServiceException("Designacao do desconto nao pode ser nula!");
        }
        
        if (descricao == null || descricao.isEmpty()) {
            descricao = "Descricao padrao!";
        }
        if (percentagem == null) {
            throw new ServiceException("Taxa de Desconto nao informada!");
        }
        
        
        Desconto d = new Desconto();
        d.setNome(nomeDesconto);
        d.setPercentagem(percentagem);
        d.setDescricao(descricao);
        if (descontoDao.updateEntity(d,id)) {
            setOpsSuccess(true);
        }
    }
    
     public void excluir(Integer id) {
        setOpsSuccess(false);
        
        if (id == null) {
            throw new ServiceException("Desconto nao selecionado!");
        }
          
        if (descontoDao.deleteEntity(id)) {
            setOpsSuccess(true);
        }
    }

    public ObservableList<Desconto> listDescontos() {
        return descontoDao.listAllEntities();
    }
}
