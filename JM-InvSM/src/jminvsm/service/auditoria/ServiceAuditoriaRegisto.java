package jminvsm.service.auditoria;

import javafx.collections.ObservableList;
import jminvsm.dao.auditoria.AuditoriaRegistoDAO;
import jminvsm.model.auditoria.AuditoriaRegisto;
import jminvsm.service.ServiceException;

public class ServiceAuditoriaRegisto {

    private final AuditoriaRegistoDAO auditoriaDao;

    public ServiceAuditoriaRegisto() {
        this.auditoriaDao = new AuditoriaRegistoDAO();
    }

    public ObservableList<AuditoriaRegisto> pesquisar(String dataInicio, String dataFim,
            String tabela, String operacao, Integer idUsuario, String idRegisto) {
        if (dataInicio != null && dataFim != null
                && !dataInicio.trim().isEmpty() && !dataFim.trim().isEmpty()
                && dataInicio.compareTo(dataFim) > 0) {
            throw new ServiceException("A data inicial nao pode ser maior que a data final!");
        }
        return auditoriaDao.pesquisar(dataInicio, dataFim, tabela, operacao, idUsuario, idRegisto);
    }

    public ObservableList<String> listarTabelasAuditadas() {
        ObservableList<String> tabelas = auditoriaDao.listarTabelasAuditadas();
        tabelas.add(0, "Todos");
        return tabelas;
    }
}
