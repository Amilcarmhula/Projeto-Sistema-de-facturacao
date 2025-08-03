/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.usuario;

import java.sql.SQLException;
import jminvsm.dao.usuario.UsuarioDAO;
import jminvsm.model.usuario.Usuario;
import jminvsm.util.AlertUtilities;
import static jminvsm.util.AlertUtilities.showDialog;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceUsuario {

    private final UsuarioDAO usuarioDAO;

    public ServiceUsuario() throws SQLException {
        this.usuarioDAO = new UsuarioDAO();
    }

    public Usuario authLogin(String user, String pwd) {
        Usuario usuario = null;
        if (user == null || user.isEmpty()) {
            AlertUtilities.showDialog("Erro", "Usuario nao pode ser nula!");
            return null;
        }
        if (pwd == null || pwd.isEmpty()) {
            AlertUtilities.showDialog("Erro", "Senha nao pode ser nula!");
            return null;
        }

        try {
            usuario = usuarioDAO.login(user, pwd);
            
        } catch (Exception e) {
            showDialog( "Erro", "Detalhes: "+e);
        }

        return usuario;
    }

}
