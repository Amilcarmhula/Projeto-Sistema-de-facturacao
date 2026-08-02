/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jminvsm.service.usuario;

import java.sql.SQLException;
import jminvsm.dao.usuario.UsuarioDAO;
import jminvsm.model.usuario.Usuario;
import jminvsm.service.ServiceException;

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
            throw new ServiceException("Usuario nao pode ser nulo!");
        }
        if (pwd == null || pwd.isEmpty()) {
            throw new ServiceException("Senha nao pode ser nula!");
        }

        try {
            usuario = usuarioDAO.login(user, pwd);
            
        } catch (Exception e) {
            throw new ServiceException("Erro ao autenticar usuario.", e);
        }

        return usuario;
    }

}
