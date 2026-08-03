package jminvsm.service.empresa;

import jminvsm.dao.empresa.EmpresaDAO;
import jminvsm.model.empresa.Empresa;

public class ServiceEmpresa {

    private final EmpresaDAO empresaDao;

    public ServiceEmpresa() {
        this.empresaDao = new EmpresaDAO();
    }

    public Empresa getEmpresaData() {
        return empresaDao.getEmpresaData();
    }
}
