/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.usuario;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import sysfact.dao.usuario.UsuarioDAO;
import sysfact.model.usuario.Usuario;
import sysfact.util.MetodosUteis;

/**
 *
 * @author JM-Tecnologias
 */
public class ServiceUsuario {

    private UsuarioDAO usuarioDAO;

    public ServiceUsuario(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }
    
    public Usuario authLogin(String user, String pwd) {
        Usuario usuario = null;
        user = MetodosUteis.validaString(FontAwesomeIcon.EXCLAMATION_TRIANGLE,user);
        pwd = MetodosUteis.validaString(FontAwesomeIcon.EXCLAMATION_TRIANGLE,pwd);

        usuario = usuarioDAO.login(user, pwd);

        return usuario;
    }

}
