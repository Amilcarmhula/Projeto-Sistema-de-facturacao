package jminvsm.service.empresa;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jminvsm.dao.empresa.util.ContaBancariaDAO;
import jminvsm.model.empresa.conta.ContaBancaria;

public class ServiceContaBancaria {

    private final ContaBancariaDAO contaBancariaDao;

    public ServiceContaBancaria() {
        this.contaBancariaDao = new ContaBancariaDAO();
    }

    public ObservableList<ContaBancaria> listaContasBancarias() {
        return FXCollections.observableArrayList(contaBancariaDao.listAllEntities());
    }
}
