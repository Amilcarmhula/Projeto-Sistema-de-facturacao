/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sysfact.service.usuario;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import sysfact.dao.usuario.UsuarioDAO;
import sysfact.model.usuario.Usuario;
import sysfact.util.LoadAndMoveUtilities;
import sysfact.util.ValidationUtilities;

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
        Usuario usuario;
        user = ValidationUtilities.validaString(FontAwesomeIcon.USER_TIMES,user);
        pwd = ValidationUtilities.validaString(FontAwesomeIcon.USER_TIMES,pwd);

        usuario = usuarioDAO.login(user, pwd);

        return usuario;
    }

}
