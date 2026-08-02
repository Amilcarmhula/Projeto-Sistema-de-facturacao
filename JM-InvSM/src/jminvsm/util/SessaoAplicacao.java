package jminvsm.util;

import jminvsm.model.empresa.Empresa;
import jminvsm.model.usuario.Usuario;

/**
 * Mantem os dados globais da sessao actual da aplicacao.
 */
public final class SessaoAplicacao {

    private static Usuario userData;
    private static Object data;
    private static Empresa empresaData;

    private SessaoAplicacao() {
    }

    public static Empresa getEmpresaData() {
        return empresaData;
    }

    public static void setEmpresaData(Empresa empresaData) {
        SessaoAplicacao.empresaData = empresaData;
    }

    public static Object getData() {
        return data;
    }

    public static void setData(Object data) {
        SessaoAplicacao.data = data;
    }

    public static Usuario getUserData() {
        return userData;
    }

    public static void setUserData(Usuario userData) {
        SessaoAplicacao.userData = userData;
    }
}
