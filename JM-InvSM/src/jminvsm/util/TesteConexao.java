/*
 * The MIT License
 *
 * Copyright 2026 JM-Tecnologias.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package jminvsm.util;

/**
 *
 * @author JM-Tecnologias
 */

import jminvsm.dao.usuario.UsuarioDAO;
import jminvsm.model.usuario.Usuario;

public class TesteConexao {

     public static void main(String[] args) {

        UsuarioDAO dao = new UsuarioDAO();

        // usuário e senha existentes no banco
        String usuario = "junior";
        String senha = "senha123";

        Usuario user = dao.login(usuario, senha);

        if (user != null) {

            System.out.println("✅ Login realizado com sucesso!");

            System.out.println("ID: " + user.getId());
            System.out.println("Nome: " + user.getNome());
            System.out.println("Usuário: " + user.getUsuario());
            System.out.println("Email: " + user.getEmail());

            if (user.getNivelAcesso() != null) {
                System.out.println("Nível: "
                        + user.getNivelAcesso().getDescricao());
            }

            if (user.getPermissao() != null) {
                System.out.println("Permissão: "
                        + user.getPermissao().getTipo());
            }

        } else {

            System.out.println("❌ Usuário ou senha inválidos.");

        }
    }
}