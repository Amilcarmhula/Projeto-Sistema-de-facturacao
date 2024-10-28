/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.usuario;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import jminvsm.dao.usuario.UsuarioDAO;
import jminvsm.model.usuario.Usuario;
import static jminvsm.util.AlertUtilities.showDialog;
import static jminvsm.util.AlertUtilities.showErroNotification;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceUsuario {

    private final UsuarioDAO usuarioDAO;

    public ServiceUsuario(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario authLogin(String user, String pwd) {
        Usuario usuario = null;
        if (user == null || user.isEmpty()) {
            showErroNotification("Erro", "Usuario nao pode ser nula!");
            return null;
        }
        if (pwd == null || pwd.isEmpty()) {
            showErroNotification("Erro", "Senha nao pode ser nula!");
            return null;
        }

        try {
            usuario = usuarioDAO.login(user, pwd);
            
        } catch (Exception e) {
            showDialog(FontAwesomeIcon.TIMES, "Erro", "Detalhes: "+e);
        }

        return usuario;
    }

}
